<template>
    <div>
      <!-- 面包屑导航区域 -->
      <el-breadcrumb separator-class="el-icon-arrow-right">
        <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
        <el-breadcrumb-item>商品管理</el-breadcrumb-item>
        <el-breadcrumb-item>参数列表</el-breadcrumb-item>
      </el-breadcrumb>
      <el-card>
        <!-- 头部的警告区 -->
        <el-alert title="注意：只允许为第三级分类设置相关参数!" type="warning" :closable="false" show-icon/>
        <!-- 选择商品分类区域 -->
        <el-row class="cat_opt">
          <el-col>
            <span>选择商品分类</span>
            <!-- 选择商品的级联选择框 -->
            <el-cascader
              v-model="selectedCateIds"
              :options="cateList"
              :props="cateProps"
              @change="handleChange"></el-cascader>
          </el-col>
        </el-row>
        <!-- tab页签区 -->
        <el-tabs v-model="activeName" @tab-click="handleTabClick">
          <!-- 添加动态参数的面板 -->
          <el-tab-pane label="动态参数" name="many">
            <el-button
              type="primary"
              size="mini"
              :disabled="isBtnDisabled"
              @click="addParamDialogVisible = true">
              添加参数
            </el-button>
            <!-- 动态参数表格 -->
            <el-table :data="manyTableData" border stripe>
              <!-- 展开行 -->
              <el-table-column type="expand">
                <template slot-scope="scope">
                  <el-tag
                    v-for="(attr, index) in scope.row.attr_vals"
                    :key="index"
                    closable
                    @close="handleClose(index, scope.row)">
                    {{ attr }}
                  </el-tag>
                  <el-input
                    class="input-new-tag"
                    v-if="scope.row.inputVisible"
                    v-model="scope.row.inputValue"
                    ref="saveTagInput"
                    size="small"
                    @keyup.enter.native="handleInputConfirm(scope.row)"
                    @blur="handleInputConfirm(scope.row)">
                  </el-input>
                  <el-button v-else size="small" class="input-new-tag" @click="showInput(scope.row)">+ New Tag</el-button>
                </template>
              </el-table-column>
              <!-- 索引列 -->
              <el-table-column type="index"/>
              <el-table-column label="参数名称" prop="attr_name"/>
              <el-table-column label="操作">
                <template slot-scope="scope">
                  <el-button
                    size="mini"
                    type="primary"
                    icon="el-icon-edit" @click="showEditDialog(scope.row.attr_id)">编辑</el-button>
                  <el-button
                    size="mini"
                    type="danger"
                    icon="el-icon-delete"
                    @click="removeParams(scope.row.attr_id)">删除</el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-tab-pane>
          <!-- 添加静态属性的面板 -->
          <el-tab-pane label="静态属性" name="only">
            <el-button
              type="primary"
              size="mini"
              :disabled="isBtnDisabled"
              @click="addParamDialogVisible = true">
              添加属性
            </el-button>
            <el-table :data="onlyTableData" border stripe>
              <!-- 展开行 -->
              <el-table-column type="expand">
                <template slot-scope="scope">
                  <el-tag
                    v-for="(attr, index) in scope.row.attr_vals"
                    :key="index"
                    closable
                    @close="handleClose(index, scope.row)">
                    {{ attr }}
                  </el-tag>
                  <el-input
                    class="input-new-tag"
                    v-if="scope.row.inputVisible"
                    v-model="scope.row.inputValue"
                    ref="saveTagInput"
                    size="small"
                    @keyup.enter.native="handleInputConfirm(scope.row)"
                    @blur="handleInputConfirm(scope.row)">
                  </el-input>
                  <el-button v-else size="small" class="input-new-tag" @click="showInput(scope.row)">+ New Tag</el-button>
                </template>
              </el-table-column>
              <!-- 索引列 -->
              <el-table-column type="index"/>
              <el-table-column label="属性名称" prop="attr_name"/>
              <el-table-column label="操作">
                <template slot-scope="scope">
                  <el-button
                    size="mini"
                    type="primary"
                    icon="el-icon-edit" @click="showEditDialog(scope.row.attr_id)">编辑</el-button>
                  <el-button
                    size="mini"
                    type="danger"
                    icon="el-icon-delete" @click="removeParams(scope.row.attr_id)">删除</el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-tab-pane>
        </el-tabs>
      </el-card>
      <!-- 添加参数的对话框 -->
      <el-dialog
        :title="'添加' + titleText"
        :visible.sync="addParamDialogVisible"
        width="50%" @close="addParamDialogClose">
        <!-- 添加参数的对话框 -->
        <el-form :model="addForm" :rules="addFormRules" ref="addFormRef" label-width="100px">
          <el-form-item :label="titleText" prop="attr_name">
            <el-input v-model="addForm.attr_name"></el-input>
          </el-form-item>
        </el-form>
        <span slot="footer" class="dialog-footer">
    <el-button @click="addParamDialogVisible = false">取 消</el-button>
    <el-button type="primary" @click="addParams">确 定</el-button>
  </span>
      </el-dialog>
      <!-- 修改参数的对话框 -->
      <el-dialog
        :title="'修改' + titleText"
        :visible.sync="editParamDialogVisible"
        width="50%" @close="editParamDialogClose">
        <!-- 添加参数的对话框 -->
        <el-form :model="editForm" :rules="editFormRules" ref="editFormRef" label-width="100px">
          <el-form-item :label="titleText" prop="attr_name">
            <el-input v-model="editForm.attr_name"></el-input>
          </el-form-item>
        </el-form>
        <span slot="footer" class="dialog-footer">
    <el-button @click="editParamDialogVisible = false">取 消</el-button>
    <el-button type="primary" @click="editParams">确 定</el-button>
  </span>
      </el-dialog>
    </div>
</template>

<script>
import { getAllCates } from '../../network/category'
import { addParams, deleteParams, getParamsInfo, getParamsList, updateParamsInfo } from '../../network/Params'

export default {
  name: 'Params',
  created () {
    this.getCateList()
  },
  data () {
    return {
      cateList: [],
      // 级联选择框的配置对象
      cateProps: {
        expandTrigger: 'hover',
        value: 'cat_id',
        label: 'cat_name',
        children: 'children'
      },
      // 级联选择框双向绑定的id数组
      selectedCateIds: [],
      // 被激活的页签名称
      activeName: 'many',
      manyTableData: [],
      onlyTableData: [],
      addParamDialogVisible: false,
      addForm: {
        attr_name: ''
      },
      addFormRules: {
        attr_name: [
          { required: true, message: '请输入参数名称', trigger: 'blur' }
        ]
      },
      editParamDialogVisible: false,
      editForm: {},
      editFormRules: {
        attr_name: [
          { required: true, message: '请输入参数名称', trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    getCateList () {
      getAllCates({ type: 3 }).then(res => {
        if (res.code !== 200) {
          this.$message.error(res.message)
        } else {
          this.cateList = res.data.result
        }
      })
    },
    // 级联选择框变化会触发这个事件
    handleChange () {
      this.getParamsData()
      if (this.selectedCateIds.length !== 3) {
        this.selectedCateIds = []
        this.manyTableData = []
        this.onlyTableData = []
      }
    },
    getParamsData () {
      // 证明选中的不是三级分类
      if (this.selectedCateIds.length !== 3) {
        this.selectedCateIds = []
      } else {
        getParamsList(this.cateId, { sel: this.activeName }).then(res => {
          if (res.code !== 200) {
            this.$message.error(res.message)
          } else {
            res.data.forEach(attr => {
              attr.attr_vals = attr.attr_vals ? attr.attr_vals.split(',') : []
              // 为每个item添加一个文本值 控制文本框的显示与隐藏
              attr.inputVisible = false
              // 文本框中输入的内容
              attr.inputValue = ''
            })
            if (this.activeName === 'many') {
              this.manyTableData = res.data
            } else {
              this.onlyTableData = res.data
            }
          }
        })
      }
    },
    // tab页签点击事件的处理函数
    handleTabClick () {
      this.getParamsData()
    },
    removeParams (attrId) {
      this.$confirm('此操作将永久删除该参数, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteParams(this.cateId, attrId).then(res => {
          if (res.code !== 204) {
            this.$message.error(res.message)
          } else {
            this.$message({
              type: 'success',
              message: res.message
            })
          }
          this.getParamsData()
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        })
      })
    },
    // 监听对话框的关闭事件
    addParamDialogClose () {
      this.$refs.addFormRef.resetFields()
    },
    // 点击按钮 添加参数
    addParams () {
      this.$refs.addFormRef.validate(valid => {
        if (!valid) {
          return
        }
        addParams(this.cateId, {
          attr_name: this.addForm.attr_name,
          attr_sel: this.activeName
        }).then(res => {
          if (res.code !== 201) {
            this.$message.error(res.message)
          } else {
            this.$message.success(res.message)
          }
          this.addParamDialogVisible = false
          this.getParamsData()
        })
      })
    },
    // 点击按钮 展示修改对话框
    showEditDialog (attrId) {
      this.editParamDialogVisible = true
      getParamsInfo(this.cateId, attrId, { attr_sel: this.activeName }).then(res => {
        if (res.code !== 200) {
          this.$message.error(res.message)
        } else {
          this.editForm = res.data
        }
      })
    },
    editParamDialogClose () {
      this.$refs.editFormRef.resetFields()
    },
    editParams () {
      this.$refs.editFormRef.validate(valid => {
        if (!valid) {
          return null
        } else {
          updateParamsInfo(this.cateId, this.editForm.attr_id, {
            attr_name: this.editForm.attr_name,
            attr_sel: this.activeName
          }).then(res => {
            if (res.code !== 200) {
              this.$message.error(res.message)
            } else {
              this.$message.success(res.message)
            }
            this.getParamsData()
            this.editParamDialogVisible = false
          })
        }
      })
    },
    // 文本框失去焦点 或者按下 enter 键都会触发
    handleInputConfirm (row) {
      if (row.inputValue.trim().length === 0) {
        return
      } else {
        // TODO
        row.attr_vals.push(row.inputValue.trim())
        this.saveAttrVals(row)
      }
      row.inputValue = ''
      row.inputVisible = false
    },
    showInput (row) {
      row.inputVisible = true
      // $nextTick 当页面上的元素被重新渲染后 才会执行回调函数中的代码
      this.$nextTick(_ => {
        this.$refs.saveTagInput.$refs.input.focus()
      })
    },
    // 删除对应的参数可选项
    handleClose (index, row) {
      row.attr_vals.splice(index, 1)
      this.saveAttrVals(row)
    },
    // 将对attr_vals的操作更新到数据库
    saveAttrVals (row) {
      updateParamsInfo(this.cateId, row.attr_id, {
        attr_name: row.attr_name,
        attr_sel: row.attr_sel,
        attr_vals: row.attr_vals.join(',')
      }).then(res => {
        if (res.code !== 200) {
          this.$message.error(res.message)
        } else {
          this.$message.success(res.message)
          // row.attr_vals.pop()
        }
      })
    }
  },
  computed: {
    isBtnDisabled () {
      return this.selectedCateIds.length !== 3
    },
    cateId () {
      if (this.selectedCateIds.length === 3) {
        return this.selectedCateIds[2]
      }
      return null
    },
    titleText () {
      return this.activeName === 'many' ? '动态参数' : '静态属性'
    }
  }
}
</script>

<style scoped>
.cat_opt {
  margin: 15px 0;
}

.el-tag {
  margin: 10px;
}

.input-new-tag {
  width: 120px;
}
</style>
