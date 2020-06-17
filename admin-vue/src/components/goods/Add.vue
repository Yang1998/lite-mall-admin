<template>
    <div>
      <!-- 面包屑导航 -->
      <el-breadcrumb separator-class="el-icon-arrow-right">
        <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
        <el-breadcrumb-item>商品管理</el-breadcrumb-item>
        <el-breadcrumb-item>添加商品</el-breadcrumb-item>
      </el-breadcrumb>
      <!-- 卡片视图 -->
      <el-card>
        <el-alert
          title="添加商品信息"
          type="info"
          :closable="false">
        </el-alert>
        <!-- 步骤条区域 -->
        <el-steps :space="200" :active="activeIndex - 0" finish-status="success" align-center>
          <el-step title="基本信息"></el-step>
          <el-step title="商品参数"></el-step>
          <el-step title="商品属性"></el-step>
          <el-step title="商品图片"></el-step>
          <el-step title="商品内容"></el-step>
          <el-step title="完成"></el-step>
        </el-steps>

        <el-form :model="addForm" :rules="addFormRules"
                 ref="addFormRef" label-width="100px" label-position="top">

        <!-- tab栏 -->
          <el-tabs v-model="activeIndex" :tab-position="'left'"
                   :before-leave="beforeTabLeave" @tab-click="tabClicked">
            <el-tab-pane label="基本信息" name="0">
              <el-form-item label="商品名称" prop="goods_name">
                <el-input v-model="addForm.goods_name"></el-input>
              </el-form-item>

              <el-form-item label="商品价格" prop="goods_price">
                <el-input v-model.number="addForm.goods_price" type="number"></el-input>
              </el-form-item>

              <el-form-item label="商品重量" prop="goods_weight">
                <el-input v-model.number="addForm.goods_weight" type="number"></el-input>
              </el-form-item>

              <el-form-item label="商品数量" prop="goods_number">
                <el-input v-model.number="addForm.goods_number" type="number"></el-input>
              </el-form-item>

              <el-form-item label="商品分类" prop="goods_cat">
                <el-cascader
                  v-model="addForm.goods_cat"
                  :options="cateList"
                  :props="cateProps"
                  @change="handleChange"></el-cascader>
              </el-form-item>
            </el-tab-pane>

            <el-tab-pane label="商品参数" name="1">
              <el-form-item v-for="item in manyTableData" :key="item.attr_id" :label="item.attr_name">
                <el-checkbox-group v-model="item.attr_vals">
                  <el-checkbox v-for="(val, index) in item.attr_vals" :key="index" :label="val" border/>
                </el-checkbox-group>
              </el-form-item>
            </el-tab-pane>

            <el-tab-pane label="商品属性" name="2">
              <el-form-item v-for="item in onlyTableData" :key="item.attr_id" :label="item.attr_name">
                <el-input v-model="item.attr_vals"></el-input>
              </el-form-item>
            </el-tab-pane>

            <el-tab-pane label="商品图片" name="3">
              <el-upload
                :action="baseUrl + '/upload'"
                :on-preview="handlePreview"
                :on-remove="handleRemove"
                list-type="picture"
                :headers="headerObject"
                :on-success="handleUploadSuccess">
                <el-button size="small" type="primary">点击上传</el-button>
              </el-upload>
            </el-tab-pane>

            <el-tab-pane label="商品内容" name="4">
              <!-- 富文本编辑器 -->
              <quill-editor v-model="addForm.goods_introduce"></quill-editor>
              <el-button type="primary" class="btnAdd" @click="add">添加商品</el-button>
            </el-tab-pane>
          </el-tabs>
        </el-form>
      </el-card>

      <!-- 图片预览对话框 -->
      <el-dialog
        title="图片预览"
        :visible.sync="previewDialogVisible"
        width="50%">
        <img :src="previewPath" class="previewImg"/>
      </el-dialog>
    </div>
</template>

<script>
import { getAllCates } from '../../network/category'
import { getParamsList } from '../../network/Params'
import _ from 'lodash'
import { addGoods } from '../../network/goods'

export default {
  name: 'Add',
  created () {
    this.getCateList()
  },
  data () {
    return {
      activeIndex: '0',
      addForm: {
        goods_name: '',
        goods_price: 0,
        goods_weight: 0,
        goods_number: 0,
        // 商品所属的分类
        goods_cat: [],
        // 图片的数组
        pics: [],
        // 商品的详情描述
        goods_introduce: '',
        attrs: []
      },
      cateList: [],
      addFormRules: {
        goods_name: [
          { required: true, message: '请输入商品名称', trigger: 'blur' }
        ],
        goods_price: [
          { required: true, message: '请输入商品价格', trigger: 'blur' },
          { type: 'number', message: '商品价格必须为数值', trigger: 'blur' }
        ],
        goods_weight: [
          { required: true, message: '请输入商品重量', trigger: 'blur' },
          { type: 'number', message: '商品重量必须为数值', trigger: 'blur' }
        ],
        goods_number: [
          { required: true, message: '请输入商品数量', trigger: 'blur' },
          { type: 'number', message: '商品数量必须为数值', trigger: 'blur' }
        ],
        goods_cat: [
          { required: true, message: '请选择商品分类', trigger: 'blur' }
        ]
      },
      cateProps: {
        expandTrigger: 'hover',
        label: 'cat_name',
        value: 'cat_id',
        children: 'children'
      },
      // 动态参数列表数组
      manyTableData: [],
      // 静态属性列表数组
      onlyTableData: [],
      baseUrl: 'http://localhost',
      headerObject: {
        Authorization: window.sessionStorage.getItem('token')
      },
      previewPath: '',
      previewDialogVisible: false
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
    // 级联选择器选中项选中会触发这个函数
    handleChange () {
      // console.log(this.addForm.goods_cat)
      if (!this.isValid) {
        this.addForm.goods_cat = []
      }
    },
    beforeTabLeave (activeName, oldActiveName) {
      if (oldActiveName === '0' && !this.isValid) {
        this.$message.error('请先选择商品分类')
        return false
      }
    },
    tabClicked () {
      if (this.activeIndex === '1') {
        // 访问的动态参数页面
        getParamsList(this.cateId, { sel: 'many' }).then(res => {
          if (res.code !== 200) {
            this.$message.error(res.message)
          } else {
            this.manyTableData = res.data
            res.data.forEach(item => {
              item.attr_vals = item.attr_vals.length === 0 ? []
                : item.attr_vals.split(',')
            })
          }
        })
      } else if (this.activeIndex === '2') {
        // 访问的静态属性页面
        getParamsList(this.cateId, { sel: 'only' }).then(res => {
          if (res.code !== 200) {
            this.$message.error(res.message)
          } else {
            this.onlyTableData = res.data
          }
        })
      }
    },
    // 处理图片预览
    handlePreview (file) {
      this.previewPath = file.response.data.url
      this.previewDialogVisible = true
    },
    // 处理图片移除操作
    handleRemove (file) {
      const filePath = file.response.data.tmp_path
      const index = this.addForm.pics.findIndex(x => x.pic === filePath)
      this.addForm.pics.splice(index, 1)
    },
    handleUploadSuccess (response) {
      const picInfo = { pic: response.data.tmp_path }
      this.addForm.pics.push(picInfo)
    },
    // 添加商品
    add () {
      this.$refs.addFormRef.validate(valid => {
        if (!valid) {
          return this.$message.error('商品信息检验不通过')
        }
        const form = _.clone(this.addForm)
        form.goods_cat = form.goods_cat.join(',')
        // 处理动态参数
        this.manyTableData.forEach(item => {
          const newInfo = {
            attr_id: item.attr_id,
            attr_value: item.attr_vals.join(',')
          }
          this.addForm.attrs.push(newInfo)
        })
        // 处理静态属性
        this.onlyTableData.forEach(item => {
          const newInfo = {
            attr_id: item.attr_id,
            attr_value: item.attr_vals
          }
          this.addForm.attrs.push(newInfo)
        })
        form.attrs = this.addForm.attrs
        // 发起请求，添加商品
        addGoods(form).then(res => {
          if (res.code !== 201) {
            this.$message.error(res.message)
          } else {
            this.$message.success(res.message)
            // 添加成功跳转到商品列表
            this.$router.push('/goods')
          }
        })
      })
    }
  },
  computed: {
    isValid () {
      return this.addForm.goods_cat.length === 3
    },
    cateId () {
      if (this.isValid) {
        return this.addForm.goods_cat[2]
      }
      return null
    }
  }
}
</script>

<style scoped>
.el-checkbox {
  margin: 0 5px 0 0 !important;
}

.previewImg {
  width: 100%;
}

.btnAdd {
  margin-top: 15px;
}
</style>
