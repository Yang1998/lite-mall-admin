<template>
    <div>
      <!-- 面包屑导航区域 -->
      <el-breadcrumb separator-class="el-icon-arrow-right">
        <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
        <el-breadcrumb-item>权限管理</el-breadcrumb-item>
        <el-breadcrumb-item>角色列表</el-breadcrumb-item>
      </el-breadcrumb>
      <!-- 卡片视图 -->
      <el-card>
        <!-- 添加角色按钮区域 -->
        <el-row>
          <el-col>
            <el-button type="primary" @click="addRoleDialogVisible = true">添加角色</el-button>
          </el-col>
        </el-row>
        <!-- 角色列表区域 -->
        <el-table :data="roleList" border stripe>
          <!-- 展开列 -->
          <el-table-column type="expand">
            <template slot-scope="scope">
              <el-row
                v-for="(item1, index1) in scope.row.children"
                :key="item1.id"
                :class="['bdbottom', index1 === 0 ? 'bdtop' : '', 'vcenter']">
                <!-- 渲染一级权限 -->
                <el-col :span="5">
                  <el-tag
                    closable
                    @close="removeRightById(scope.row, item1.id)">{{ item1.authName }}</el-tag>
                    <i class="el-icon-caret-right"/>
                </el-col>
                <!-- 渲染二级三级权限 -->
                <el-col :span="19">
                  <el-row
                    v-for="(item2, index2) in item1.children"
                    :key="item2.id"
                    :class="[index2 === 0 ? '' : 'bdtop', 'vcenter']">
                    <!-- 二级权限 -->
                    <el-col :span="6">
                      <el-tag
                        closable
                        @close="removeRightById(scope.row, item2.id)">{{ item2.authName }}</el-tag>
                      <i class="el-icon-caret-right"/>
                    </el-col>
                    <!-- 三级权限 -->
                    <el-col :span="18">
                      <el-tag
                        v-for="(item3) in item2.children"
                        :key="item3.id"
                        type="warning"
                        closable
                        @close="removeRightById(scope.row, item3.id)">
                        {{ item3.authName }}
                      </el-tag>
                    </el-col>
                  </el-row>
                </el-col>
              </el-row>
            </template>
          </el-table-column>
          <!-- 索引列 -->
          <el-table-column type="index"/>
          <el-table-column label="角色名称" prop="roleName"/>
          <el-table-column label="角色描述" prop="roleDesc"/>
          <el-table-column label="操作" width="300px">
            <template slot-scope="scope">
              <el-button size="mini"
                         type="primary"
                         icon="el-icon-edit"
                         @click="getRole(scope.row.id)">编辑</el-button>
              <el-button size="mini"
                         type="danger"
                         icon="el-icon-delete"
                         @click="deleteRole(scope.row.id)">删除</el-button>
              <el-button size="mini"
                         type="warning"
                         icon="el-icon-setting"
                         @click="distributeRole(scope.row)">分配权限</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-card>
      <el-dialog
        title="添加角色"
        :visible.sync="addRoleDialogVisible"
        width="50%" @close="addRoleDialogClosed">
        <!-- 对话框内容主题区 -->
        <el-form :model="addRoleForm" :rules="roleFormRules" ref="addRoleFormRef" label-width="70px">
          <el-form-item label="角色名称" prop="roleName">
            <el-input v-model="addRoleForm.roleName"></el-input>
          </el-form-item>
          <el-form-item label="角色描述" prop="roleDesc">
            <el-input v-model="addRoleForm.roleDesc"></el-input>
          </el-form-item>
        </el-form>
        <!-- 底部区域 -->
        <span slot="footer" class="dialog-footer">
    <el-button @click="addRoleDialogVisible = false">取 消</el-button>
    <el-button type="primary" @click="createRole">确 定</el-button>
        </span>
      </el-dialog>
      <!-- 修改角色 -->
      <el-dialog
        title="修改角色"
        :visible.sync="editRoleDialogVisible"
        width="50%">
        <!-- 对话框内容主题区 -->
        <el-form :model="editRoleForm" :rules="roleFormRules" ref="editRoleFormRef" label-width="70px">
          <el-form-item label="角色名称" prop="roleName">
            <el-input v-model="editRoleForm.roleName"></el-input>
          </el-form-item>
          <el-form-item label="角色描述" prop="roleDesc">
            <el-input v-model="editRoleForm.roleDesc"></el-input>
          </el-form-item>
        </el-form>
        <!-- 底部区域 -->
        <span slot="footer" class="dialog-footer">
    <el-button @click="editRoleDialogVisible = false">取 消</el-button>
    <el-button type="primary" @click="editRole">确 定</el-button>
        </span>
      </el-dialog>
      <el-dialog
        title="分配权限"
        :visible.sync="setRightDialogVisible"
        width="50%"
        @close="setRightDialogClosed">
        <!-- 对话框内容主题区 -->
        <!-- 树形控件 -->
        <el-tree
          :data="rightsList"
          :props="treeProps"
          show-checkbox node-key="id"
          default-expand-all
          :default-checked-keys="defKeys" ref="treeRef"/>
        <!-- 底部区域 -->
        <span slot="footer" class="dialog-footer">
    <el-button @click="setRightDialogVisible = false">取 消</el-button>
    <el-button type="primary" @click="allotRights(id)">确 定</el-button>
        </span>
      </el-dialog>
    </div>
</template>

<script>
import {
  getRoleList, postRole, deleteRoleById, getRoleById,
  updateRoleById, removeRight, allocateRight
} from '../../network/role'
import { getAllRights } from '../../network/rights'
export default {
  name: 'Role',
  created () {
    this.getRolesList()
  },
  data () {
    return {
      roleList: [],
      addRoleDialogVisible: false,
      addRoleForm: {
        roleName: '',
        roleDesc: ''
      },
      roleFormRules: {
        roleName: [
          { require: true, message: '角色名不能为空', trigger: 'blur' }
        ],
        roleDesc: [
          { require: true, message: '角色描述不能为空', trigger: 'blur' }
        ]
      },
      editRoleForm: {},
      editRoleDialogVisible: false,
      setRightDialogVisible: false,
      rightsList: [],
      // 树形控件属性绑定的对象
      treeProps: {
        label: 'authName',
        children: 'children'
      },
      // 默认选中的id值
      defKeys: [],
      roleId: -1
    }
  },
  methods: {
    getRolesList () {
      getRoleList().then(res => {
        if (res.code !== 200) {
          this.$message.error(res.message)
        } else {
          this.roleList = res.data
        }
      })
    },
    createRole () {
      this.$refs.addRoleFormRef.validate(valid => {
        if (!valid) {
          this.$message.error('角色校验未通过')
        } else {
          postRole(this.addRoleForm).then(res => {
            if (res.code !== 201) {
              this.$message.error(res.message)
            } else {
              this.$message.success(res.message)
              this.addRoleDialogVisible = false
              this.getRolesList()
            }
          })
        }
      })
    },
    editRole () {
      this.$refs.editRoleFormRef.validate(valid => {
        if (!valid) {
          this.$message.error('角色校验未通过')
        } else {
          updateRoleById(this.editRoleForm).then(res => {
            if (res.code !== 200) {
              this.$message.error(res.message)
            } else {
              this.editRoleDialogVisible = false
              this.getRolesList()
              this.$message.success(res.message)
            }
          })
        }
      })
    },
    deleteRole (id) {
      this.$confirm('此操作将永久删除该角色, 是否继续?', '删除角色', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        // 发请求
        deleteRoleById(id).then(res => {
          if (res.code !== 204) {
            this.$message.error(res.message)
          } else {
            this.getRolesList()
            this.$message.success(res.message)
          }
        })
      }).catch(() => {
        this.$message({
          type: 'error',
          message: '已取消删除'
        })
      })
    },
    distributeRole (role) {
      this.roleId = role.id
      getAllRights('tree').then(res => {
        if (res.code !== 200) {
          this.$message.error(res.message)
        } else {
          this.rightsList = res.data
          this.getLeafKeys(role, this.defKeys)
          this.setRightDialogVisible = true
        }
      })
    },
    addRoleDialogClosed () {
      this.$refs.addRoleFormRef.resetFields()
    },
    getRole (id) {
      this.editRoleDialogVisible = true
      getRoleById(id).then(res => {
        if (res.code !== 200) {
          this.$message.error(res.message)
        } else {
          this.editRoleForm = res.data
        }
      })
    },
    removeRightById (role, id) {
      this.$confirm('此操作将永久删除该权限, 是否继续?', '删除权限', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        removeRight(role, id).then(res => {
          if (res.code !== 200) {
            this.$message.error(res.message)
          } else {
            role.children = res.data
          }
        })
      }).catch(() => {
        this.$message({
          type: 'error',
          message: '已取消删除'
        })
      })
    },
    // 通过迭代的形式，获得角色下所有的三级权限id
    getLeafKeys (node, arr) {
      if (node.children) {
        node.children.forEach(item1 => {
          if (item1.children) {
            item1.children.forEach(item2 => {
              if (item2.children) {
                item2.children.forEach(item3 => {
                  arr.push(item3.id)
                })
              }
            })
          }
        })
      }
    },
    setRightDialogClosed () {
      this.defKeys = []
    },
    // 点击为角色分配权限
    allotRights (id) {
      const keys = [
        ...this.$refs.treeRef.getCheckedKeys(),
        ...this.$refs.treeRef.getHalfCheckedKeys()
      ]
      const idStr = keys.join(',')
      allocateRight(this.roleId, idStr).then(res => {
        if (res.code !== 200) {
          this.$message.error(res.message)
        } else {
          this.$message.success(res.message)
        }
        this.getRolesList()
        this.setRightDialogVisible = false
      })
    }
  }
}
</script>

<style scoped>
.el-tag {
  margin: 7px;
}

.bdtop {
  border-top: 1px solid #eeeeee;
}

.bdbottom {
  border-bottom: 1px solid #eeeeee;
}

.vcenter {
  display: flex;
  align-items: center;
}
</style>
