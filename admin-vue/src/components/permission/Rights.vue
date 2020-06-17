<template>
  <div>
    <!-- 面包屑导航区域 -->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>权限管理</el-breadcrumb-item>
      <el-breadcrumb-item>权限列表</el-breadcrumb-item>
    </el-breadcrumb>
    <!-- 卡片页面 -->
    <el-card>
      <el-table :data="rightsList" border stripe>
        <!-- 索引列 -->
        <el-table-column type="index"/>
        <el-table-column label="权限名称" prop="authName"/>
        <el-table-column label="路径" prop="path"/>
        <el-table-column label="权限等级" prop="level">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.level === '0'">一级</el-tag>
            <el-tag type="success" v-if="scope.row.level === '1'">二级</el-tag>
            <el-tag type="warning" v-if="scope.row.level === '2'">三级</el-tag>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script>
import { getAllRights } from '../../network/rights'
export default {
  name: 'permission',
  data () {
    return {
      rightsList: []
    }
  },
  created () {
    this.getRightsList()
  },
  methods: {
    // 获取权限列表
    getRightsList () {
      // 写死， 只获取列表数据
      getAllRights('list').then(res => {
        if (res.code !== 200) {
          this.$message.error(res.message)
        } else {
          this.rightsList = res.data
        }
      })
    }
  }
}
</script>

<style scoped>

</style>
