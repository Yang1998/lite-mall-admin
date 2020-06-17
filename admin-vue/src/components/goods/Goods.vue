<template>
  <div>
    <!-- 面包屑导航 -->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>商品管理</el-breadcrumb-item>
      <el-breadcrumb-item>商品列表</el-breadcrumb-item>
    </el-breadcrumb>
    <!-- 卡片视图区域 -->
    <el-card>
      <el-row :gutter="20">
        <el-col :span="8">
          <el-input placeholder="请输入内容" v-model="queryInfo.query" clearable>
            <el-button slot="append" icon="el-icon-search" @click="getGoodsList"></el-button>
          </el-input>
        </el-col>
        <el-col :span="4">
          <el-button type="primary" @click="goAddPage">添加商品</el-button>
        </el-col>
      </el-row>
      <!-- table表格区域 -->
      <el-table :data="goodsList" border stripe>
        <el-table-column type="index"/>
        <el-table-column label="商品名称" prop="goods_name"></el-table-column>
        <el-table-column label="商品价格(元)" prop="goods_price" width="110px"></el-table-column>
        <el-table-column label="商品重量" prop="goods_weight" width="80px"></el-table-column>
        <el-table-column label="创建时间" prop="add_time" width="160px">
          <template slot-scope="scope">
            {{ scope.row.add_time | dateFormat }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="190px">
          <template slot-scope="scope">
            <el-button type="primary" icon="el-icon-edit" size="mini" @click="editGoods(scope.row)">编辑</el-button>
            <el-button type="danger" icon="el-icon-delete" size="mini" @click="removeById(scope.row.goods_id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="queryInfo.pageNum"
        :page-sizes="[5, 10, 15, 20]"
        :page-size="queryInfo.pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total" background>
      </el-pagination>
    </el-card>
  </div>
</template>

<script>
import { deleteGoods, getAllGoods } from '../../network/goods'

export default {
  name: 'List',
  created () {
    this.getGoodsList()
  },
  data () {
    return {
      queryInfo: {
        query: '',
        pageNum: 1,
        pageSize: 10
      },
      goodsList: [],
      total: 0
    }
  },
  methods: {
    getGoodsList () {
      getAllGoods(this.queryInfo).then(res => {
        if (res.code !== 200) {
          this.$message.error(res.message)
        } else {
          this.goodsList = res.data.goods
          this.total = res.data.total
        }
      })
    },
    handleSizeChange (newSize) {
      this.queryInfo.pageSize = newSize
      this.getGoodsList()
    },
    handleCurrentChange (newPage) {
      this.queryInfo.pageNum = newPage
      this.getGoodsList()
    },
    removeById (id) {
      this.$confirm('此操作将永久删除该商品, 是否继续?', '删除商品', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteGoods(id).then(res => {
          if (res.code !== 204) {
            this.$message.error(res.message)
          } else {
            this.$message.success(res.message)
            this.getGoodsList()
          }
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        })
      })
    },
    goAddPage () {
      this.$router.push('/goods/add')
    }
  }
}
</script>

<style scoped>

</style>
