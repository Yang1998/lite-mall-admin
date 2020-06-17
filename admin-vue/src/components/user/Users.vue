<template>
  <div>
    <!-- 面包屑导航区域 -->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>活动管理</el-breadcrumb-item>
      <el-breadcrumb-item>活动列表</el-breadcrumb-item>
    </el-breadcrumb>
    <el-card>
      <div style="margin-top: 15px;">
        <!-- 搜索与添加区域 -->
        <el-row :gutter="20">
          <el-col :span="8">
            <el-input placeholder="请输入内容"
                      class="input-with-select"
                      v-model="queryInfo.query"
                      clearable @clear="getUserList">
              <el-button slot="append"
                         icon="el-icon-search"
                         @click="getUserList"/>
            </el-input>
          </el-col>
          <el-col :span="4">
            <el-button type="primary" @click="addDialogVisible = true">添加用户</el-button>
          </el-col>
        </el-row>
      </div>
      <!-- 用户列表区域 -->
      <el-table :data="userList" border stripe width="180px">
        <el-table-column label="#"    type="index"></el-table-column>
        <el-table-column label="姓名" prop="username"></el-table-column>
        <el-table-column label="邮箱" prop="email"></el-table-column>
        <el-table-column label="电话" prop="mobile"></el-table-column>
        <el-table-column label="角色" prop="roleName"></el-table-column>
        <el-table-column label="状态">
          <template slot-scope="scope">
            <el-switch v-model="scope.row.state" @change="userStateChanged(scope.row)"/>
          </template>
        </el-table-column>
        <el-table-column label="操作">
          <template slot-scope="scope">
<!--            {{ scope.row.state }}{{ total }}-->
            <!-- 修改按钮 -->
            <el-tooltip effect="dark" content="修改" placement="top" :enterable="false">
              <el-button type="primary"
                         icon="el-icon-edit"
                         circle size="mini"
                         @click="showEditDialog(scope.row.id)"/>
            </el-tooltip>
            <!-- 删除按钮 -->
            <el-tooltip effect="dark" content="删除" placement="top" :enterable="false">
              <el-button type="danger"
                         icon="el-icon-delete"
                         circle size="mini"
                         @click="removeUserById(scope.row.id)"/>
            </el-tooltip>
            <!-- 分配角色按钮 -->
            <el-tooltip effect="dark" content="分配角色" placement="top" :enterable="false">
              <el-button
                type="warning"
                icon="el-icon-setting"
                circle size="mini" @click="setRole"/>
            </el-tooltip>
          </template>
        </el-table-column>
      </el-table>
      <!-- 分页区域 -->
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="queryInfo.pageNum"
        :page-sizes="[1, 2, 5, 10]"
        :page-size="queryInfo.pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total">
      </el-pagination>
    </el-card>
    <el-dialog
      title="添加用户"
      :visible.sync="addDialogVisible"
      width="50%" @close="addDialogClosed">
      <!-- 对话框内容主题区 -->
      <el-form :model="addForm" :rules="addFormRules" ref="addFormRef" label-width="70px">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="addForm.username"></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="addForm.password" type="password"></el-input>
        </el-form-item>
        <el-form-item label="确认密码" prop="reconfirmPassword">
          <el-input v-model="addForm.reconfirmPassword" type="password"></el-input>
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="addForm.email"></el-input>
        </el-form-item>
        <el-form-item label="手机" prop="phone">
          <el-input v-model="addForm.phone"></el-input>
        </el-form-item>
      </el-form>
      <!-- 底部区域 -->
      <span slot="footer" class="dialog-footer">
    <el-button @click="addDialogVisible = false">取 消</el-button>
    <el-button type="primary" @click="addUser">确 定</el-button>
  </span>
    </el-dialog>
    <el-dialog
      title="修改用户"
      :visible.sync="editDialogVisible"
      width="50%" @close="editDialogClosed">
      <!-- 对话框内容主题区 -->
      <el-form :model="editForm" :rules="editFormRules" ref="editFormRef" label-width="70px">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="editForm.username" disabled/>
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="editForm.email"/>
        </el-form-item>
        <el-form-item label="手机" prop="mobile">
          <el-input v-model="editForm.mobile"/>
        </el-form-item>
      </el-form>
      <!-- 底部区域 -->
      <span slot="footer" class="dialog-footer">
    <el-button @click="addDialogVisible = false">取 消</el-button>
    <el-button type="primary" @click="editUserInfo">确 定</el-button>
      </span>
    </el-dialog>
    <!-- 分配角色对话框 -->
    <el-dialog
      title="提示"
      :visible.sync="roleDialogVisible"
      width="30%" @close="setRoleDiologClosed">
      <div>
        <p>当前的用户： {{ userInfo.userName }}</p>
        <p>当前的角色： {{ userInfo.roleName }}</p>
        <p>分配新角色：
          <el-select v-model="selectedRoleId"
                     placeholder="请选择角色">
            <el-option
              v-for="item in rolesList"
               :key="item.id"
              :label="item.roleName"
              :value="item.id"/>
          </el-select>
        </p>
      </div>
      <span slot="footer" class="dialog-footer">
    <el-button @click="roleDialogVisible = false">取 消</el-button>
    <el-button type="primary" @click="saveRoleInfo">确 定</el-button>
  </span>
    </el-dialog>
  </div>
</template>

<script>
import {
  userListReq, changeUserState, createUser,
  getUserInfoById, editUserInfo, deleteUser, setUserRoleId
} from '../../network/User'
import { getRoleList } from '../../network/role'
export default {
  name: 'Users',
  created () {
    this.getUserList()
  },
  data () {
    return {
      // 获取用户列表的参数
      queryInfo: {
        query: '',
        pageNum: 1,
        pageSize: 1
      },
      userList: [],
      total: 0,
      // 控制添加用户对话框的显示与隐藏, 默认隐藏
      addDialogVisible: false,
      // 添加用户对象
      addForm: {
        username: '',
        password: '',
        reconfirmPassword: '',
        email: '',
        phone: ''
      },
      // 添加用户表单的验证规则对象
      addFormRules: {
        username: [
          { require: true, message: '请输入用户名', trigger: 'blur' },
          { min: 2, max: 7, message: '用户名的长度在2-7个字符之间', trigger: 'blur' }
        ],
        password: [
          { require: true, message: '密码不能为空', trigger: 'blur' },
          { min: 6, max: 15, message: '密码长度在6-15个字符之间', trigger: 'blur' }
        ],
        reconfirmPassword: [
          { require: true, message: '密码不能为空', trigger: 'blur' },
          { min: 6, max: 15, message: '密码长度在6-15个字符之间', trigger: 'blur' }
        ],
        email: [
          { require: true, message: '邮箱不能为空', trigger: 'blur' },
          { type: 'email', message: '请输入合法的邮箱地址', trigger: ['blur', 'change'] }
        ],
        phone: [
          { require: true, message: '手机号不能为空', trigger: 'blur' },
          { pattern: /^1[3456789]\d{9}$/, message: '请输入正确的手机号地址', trigger: ['blur', 'change'] }
        ]
      },
      editDialogVisible: false,
      editForm: {},
      editFormRules: {
        mobile: [
          { require: true, message: '手机号不能为空', trigger: 'blur' },
          { pattern: /^1[3456789]\d{9}$/, message: '请输入正确的手机号地址', trigger: ['blur', 'change'] }
        ],
        email: [
          { require: true, message: '邮箱不能为空', trigger: 'blur' },
          { type: 'email', message: '请输入合法的邮箱地址', trigger: ['blur', 'change'] }
        ]
      },
      roleDialogVisible: false,
      userInfo: {},
      // 所有角色的数据列表
      rolesList: {},
      // 已选中的角色id值
      selectedRoleId: ''
    }
  },
  methods: {
    getUserList () {
      userListReq(this.queryInfo).then(res => {
        if (res.code !== 200) {
          this.$message.error(res.message)
        } else {
          this.userList = res.data.users
          this.total = res.data.totalPage
        }
      })
    },
    // 监听pageSize改变的事件
    handleSizeChange (val) {
      this.queryInfo.pageSize = val
      this.getUserList()
    },
    // 监听页码值改变的事件
    handleCurrentChange (newPage) {
      this.queryInfo.pageNum = newPage
      this.getUserList()
    },
    // 监听switch开关转台的变化
    userStateChanged (userInfo) {
      changeUserState(userInfo).then(res => {
        if (res.code !== 200) {
          userInfo.state = !userInfo.state
          return this.$message.error(res.message)
        } else {
          this.$message.success(res.message)
        }
      })
    },
    // 监听添加用户对话框关闭事件
    addDialogClosed () {
      this.$refs.addFormRef.resetFields()
    },
    // 点击按钮添加用户
    addUser () {
      this.$refs.addFormRef.validate(valid => {
        const pwd = this.addForm.password
        const confirmPwd = this.addForm.reconfirmPassword
        if (!valid || pwd !== confirmPwd) {
          this.$message.error('验证未通过或者确认密码有误，请重试！')
        } else {
          // 发网络请求
          createUser(this.addForm).then(res => {
            if (res.code !== 201) {
              this.$message.error(res.message)
            } else {
              this.$message.success(res.message)
              // 隐藏添加用户的对话框
              this.addDialogVisible = false
              // 重新获取用户的列表
              this.getUserList()
            }
          })
        }
      })
    },
    showEditDialog (id) {
      getUserInfoById(id).then(res => {
        if (res.code !== 200) {
          this.$message.error(res.message)
        } else {
          this.editForm = res.data
        }
      })
      this.editDialogVisible = true
    },
    // 监听修改对话框的关闭
    editDialogClosed () {
      this.$refs.editFormRef.resetFields()
    },
    //
    editUserInfo () {
      this.$refs.editFormRef.validate(valid => {
        if (!valid) {
          this.$message.error('验证未通过')
        } else {
          editUserInfo(this.editForm).then(res => {
            if (res.code !== 200) {
              this.$message.error(res.message)
            } else {
              // 关闭对话框
              this.editDialogVisible = false
              // 刷新用户列表
              this.getUserList()
              // 提示修改成功
              this.$message.success(res.message)
            }
          })
        }
      })
    },
    removeUserById (id) {
      this.$confirm('此操作将永久删除该用户, 是否继续?', '删除用户', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        // 发请求
        deleteUser(id).then(res => {
          if (res.code !== 200) {
            this.$message.error(res.message)
          } else {
            this.$message({
              type: 'success',
              message: '删除成功!'
            })
            this.getUserList()
          }
        })
      }).catch(() => {
        this.$message({
          type: 'error',
          message: '已取消删除'
        })
      })
    },
    setRole (userInfo) {
      this.userInfo = userInfo
      // 在展示对话框前获取所有的角色列表
      getRoleList().then(res => {
        if (res.code !== 200) {
          this.$message.error(res.message)
        } else {
          this.rolesList = res.data
        }
      })
      this.roleDialogVisible = true
    },
    // 点击按钮分配角色
    saveRoleInfo () {
      if (!this.selectedRoleId) {
        return this.$message.error('请选择要分配的角色')
      }
      setUserRoleId(this.userInfo.id, { rid: this.selectedRoleId }).then(res => {
        if (res.code !== 200) {
          this.$message.error(res.message)
        } else {
          this.$message.success(res.message)
        }
        this.getUserList()
        this.roleDialogVisible = false
      })
    },
    setRoleDiologClosed () {
      this.selectedRoleId = ''
    }
  }
}
</script>

<style scoped>

</style>
