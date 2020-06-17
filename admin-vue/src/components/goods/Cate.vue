<template>
    <div>
      <!-- 面包屑导航区域 -->
      <el-breadcrumb separator-class="el-icon-arrow-right">
        <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
        <el-breadcrumb-item>商品管理</el-breadcrumb-item>
        <el-breadcrumb-item>商品分类</el-breadcrumb-item>
      </el-breadcrumb>
      <!-- 卡片视图区域 -->
      <el-card>
        <el-row>
          <el-col>
            <el-button type="primary" @click="showAddCateDialog">添加分类</el-button>
          </el-col>
        </el-row>
        <!-- 表格区域 -->
        <tree-table :data="cateList"
                    :columns="columns"
                    :selection-type="false"
                    :expand-type="false"
                    show-index
                    index-text="#"
                    border
                    :show-row-hover="false"
                    v-loading="loading"
                    element-loading-text="拼命加载中"
                    element-loading-spinner="el-icon-loading">
          <!-- 是否有效 -->
          <template slot="isDeleted" slot-scope="scope">
            <i class="el-icon-success"
               v-if="!scope.row.cat_deleted"
               style="color: lightgreen"/>
            <i class="el-icon-error"
               v-if="scope.row.cat_deleted"
               style="color: red"/>
          </template>
          <!-- 排序 -->
          <template v-slot:order="scope">
            <el-tag :type="orderTypeMap[scope.row.cat_level]"
                    size="mini">
              {{ orderMap[scope.row.cat_level] }}
            </el-tag>
          </template>
          <!-- 操作 -->
          <template v-slot:opt="scope">
            <el-button type="primary"
                       icon="el-icon-edit"
                       size="mini"
                       @click="editCate(scope.row.cat_id)">编辑</el-button>
            <el-button type="danger"
                       icon="el-icon-delete"
                       size="mini"
                       @click="deleteCate(scope.row.cat_id)">删除</el-button>
          </template>
        </tree-table>
        <!-- 分页区域 -->
        <el-pagination @size-change="handleSizeChange"
                       @current-change="handleCurrentChange"
                       :current-page="queryInfo.pageNum"
                       :page-sizes="[3, 5, 10, 15]"
                       :page-size="queryInfo.pageSize"
                       layout="total, sizes, prev, pager, next, jumper"
                       :total="total" class="treeTable"/>
      </el-card>
      <el-dialog
        title="添加分类"
        :visible.sync="addCateDialogVisible"
        width="50%"
        @close="addCateDialogClose">
        <!-- 添加分类的表单 -->
        <el-form
          :model="addCateForm"
          :rules="cateFormRules" ref="addCateFormRef"
          label-width="100px"
          class="demo-ruleForm">
          <el-form-item label="分类名称：" prop="cat_name">
            <el-input v-model="addCateForm.cat_name"></el-input>
          </el-form-item>
          <el-form-item label="父级分类：">
            <el-cascader
              v-model="selectedKeys"
              :options="parentCateList"
              @change="parentCateChanged"
              :props="cascaderProps"
              clearable/>
          </el-form-item>
        </el-form>
        <span slot="footer" class="dialog-footer">
    <el-button @click="addCateDialogVisible = false">取 消</el-button>
    <el-button type="primary" @click="addCate">确 定</el-button>
  </span>
      </el-dialog>
      <!-- 修改分类 -->
      <el-dialog
        title="修改分类"
        :visible.sync="editCategoryDialogVisible"
        width="50%">
        <!-- 对话框内容主题区 -->
        <el-form :model="editCateForm" :rules="cateFormRules" ref="editCateFormRef" label-width="70px">
          <el-form-item label="分类名" prop="cat_name">
            <el-input v-model="editCateForm.cat_name"></el-input>
          </el-form-item>
          <el-form-item label="所属分类">
            <el-input v-model="cat_pname" readonly></el-input>
          </el-form-item>
        </el-form>
        <!-- 底部区域 -->
        <span slot="footer" class="dialog-footer">
    <el-button @click="editRoleDialogVisible = false">取 消</el-button>
    <el-button type="primary" @click="doUpdateCate">确 定</el-button>
        </span>
      </el-dialog>
    </div>
</template>

<script>
import { addCategory, deleteCategory, getAllCates, getCategoryById, updateCategory } from '../../network/category'

export default {
  name: 'Cate',
  created () {
    this.loading = true
    this.getCateList()
    this.loading = false
  },
  data () {
    return {
      // 商品分类的数据列表, 默认为空
      cateList: [],
      // 查询条件
      queryInfo: {
        type: 3,
        pageNum: 1,
        pageSize: 5
      },
      total: 0,
      // 为table指定列
      columns: [
        {
          label: '分类名称',
          prop: 'cat_name'
        },
        {
          label: '是否有效',
          // 表示将当前列定义为模板列
          type: 'template',
          template: 'isDeleted'
        },
        {
          label: '排序',
          type: 'template',
          template: 'order'
        },
        {
          label: '操作',
          type: 'template',
          template: 'opt'
        }
      ],
      orderMap: {
        0: '一级',
        1: '二级',
        2: '三级'
      },
      orderTypeMap: {
        0: 'primary',
        1: 'success',
        2: 'warning'
      },
      // 控制添加分类对话框的显示与隐藏
      addCateDialogVisible: false,
      // 表格的加载
      loading: true,
      // 添加分类表单的数据对象
      addCateForm: {
        // 分类名称
        cat_name: '',
        // 父级分类id
        cat_pid: 0,
        // 分类层级, ，默认添加一级分类
        cat_level: 0
      },
      // 添加分类表单的规则
      cateFormRules: {
        cat_name: [
          { required: true, message: '分类名称不能为空', trigger: 'blur' }
        ]
      },
      parentCateList: [],
      // 指定级联选择器的数据对象
      cascaderProps: {
        expandTrigger: 'hover',
        value: 'cat_id',
        label: 'cat_name',
        children: 'children',
        checkStrictly: true
      },
      // 选中的父级分类的id数组
      selectedKeys: [],
      editCategoryDialogVisible: false,
      editCateForm: {},
      cat_pname: ''
    }
  },
  methods: {
    // 获取商品分类数据
    getCateList () {
      getAllCates(this.queryInfo).then(res => {
        if (res.code !== 200) {
          this.$message.error(res.message)
        } else {
          // 把数据列表, 赋值给cateList
          this.cateList = res.data.result
          // 为总数据条数赋值
          this.total = res.data.total
        }
      })
    },
    // 监听pageSize改变的事件
    handleSizeChange (newSize) {
      this.queryInfo.pageSize = newSize
      this.getCateList()
    },
    // 监听pageNum的改变
    handleCurrentChange (newPage) {
      this.queryInfo.pageNum = newPage
      this.getCateList()
    },
    // 添加分类按显示对话框
    showAddCateDialog () {
      // 先获取父级分类数据
      this.getParentCateList()
      this.addCateDialogVisible = true
    },
    // 获取父级的分类列表
    getParentCateList () {
      getAllCates({ type: 2 }).then(res => {
        if (res.code !== 200) {
          this.$message.error(res.message)
        } else {
          this.parentCateList = res.data.result
        }
      })
    },
    // 选择项发生变化触发这个函数
    parentCateChanged () {
      // 如果selectedKeys大小大于0， 说明选中
      // 反之， 说明未选中
      if (this.selectedKeys.length > 0) {
        this.addCateForm.cat_pid =
          this.selectedKeys[this.selectedKeys.length - 1]
        this.addCateForm.cat_level = this.selectedKeys.length
      } else {
        this.addCateForm.cat_pid = 0
        this.addCateForm.cat_level = 0
      }
    },
    addCate () {
      this.$refs.addCateFormRef.validate(valid => {
        if (!valid) {
        } else {
          addCategory(this.addCateForm).then(res => {
            if (res.code !== 201) {
              this.$message.error(res.message)
            } else {
              this.$message.success(res.message)
              this.getCateList()
              this.addCateDialogVisible = false
            }
          })
        }
      })
    },
    // 添加分类对话框关闭处理函数, 重置表单数据
    addCateDialogClose () {
      this.$refs.addCateFormRef.resetFields()
      this.selectedKeys = []
      this.addCateForm.cat_level = 0
      this.addCateForm.cat_pid = 0
    },
    editCate (id) {
      this.editCategoryDialogVisible = true
      getCategoryById(id).then(res => {
        if (res.code !== 200) {
          this.$message.error(res.message)
        } else {
          this.editCateForm = res.data
          console.log(this.editCateForm)
          if (this.editCateForm.cat_pid === 0) {
            this.cat_pname = ''
          } else {
            getCategoryById(this.editCateForm.cat_pid).then(res => {
              if (res.code !== 200) {
                this.$message.error(res.message)
              } else {
                this.cat_pname = res.data.cat_name
              }
            })
          }
        }
      })
    },
    deleteCate (id) {
      this.$confirm('此操作将永久删除该分类, 是否继续?', '删除分类', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        // 发请求
        deleteCategory(id).then(res => {
          if (res.code !== 204) {
            this.$message.error(res.message)
          } else {
            this.getCateList()
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
    doUpdateCate () {
      this.$refs.editCateFormRef.validate(valid => {
        if (!valid) {
          this.$message.error('商品分类校验未通过')
        } else {
          updateCategory(this.editCateForm.cat_id,
            { cat_name: this.editCateForm.cat_name }).then(res => {
            if (res.code !== 200) {
              this.$message.error(res.message)
            } else {
              this.getCateList()
              this.editCategoryDialogVisible = false
              this.$message.success(res.message)
            }
          })
        }
      })
    }
  }
}
</script>

<style scoped>
.treeTable {
  margin-top: 15px;
}
.el-cascader {
  width: 100%;
}
.zk-table {
  margin-top: 15px;
}
</style>
