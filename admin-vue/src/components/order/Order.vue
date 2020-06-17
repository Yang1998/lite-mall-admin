<template>
    <div>
      <!-- 面包屑导航区域 -->
      <el-breadcrumb separator-class="el-icon-arrow-right">
        <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
        <el-breadcrumb-item>订单管理</el-breadcrumb-item>
        <el-breadcrumb-item>订单分类</el-breadcrumb-item>
      </el-breadcrumb>

      <!-- 卡片视图区域 -->
      <el-card>
        <el-row>
          <el-col :span="8">
            <el-input placeholder="请输入内容">
              <el-button slot="append" icon="el-icon-search"></el-button>
            </el-input>
          </el-col>
        </el-row>
        <!-- 订单列表数据 -->
        <el-table :data="orderList" border stripe>
          <el-table-column type="index"></el-table-column>
          <el-table-column label="订单编号" prop="order_number"></el-table-column>
          <el-table-column label="订单价格" prop="order_price"></el-table-column>
          <el-table-column label="是否付款" prop="pay_status">
            <template slot-scope="scope">
              <el-tag type="success" v-if="scope.row.pay_status === '1'">已付款</el-tag>
              <el-tag type="danger" v-else>未付款</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="是否发货" prop="is_send"></el-table-column>
          <el-table-column label="下单时间">
            <template slot-scope="scope">
              {{ scope.row.create_time | dateFormat }}
            </template>
          </el-table-column>
          <el-table-column label="操作">
            <template slot-scope="scope">
              <el-button type="primary" icon="el-icon-edit" size="mini" @click="showBox(scope.row)"></el-button>
              <el-button type="success" icon="el-icon-location" size="mini" @click="showProgressBox(scope.row)"></el-button>
            </template>
          </el-table-column>
        </el-table>
        <!-- 分页 -->
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="queryInfo.pageNum"
          :page-sizes="[5, 10, 15, 20]"
          :page-size="queryInfo.pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total">
        </el-pagination>
      </el-card>
      <!-- 修改地址的对话框 -->
      <el-dialog
        title="修改地址"
        :visible.sync="addressDialogVisible"
        width="50%" @close="editAddressDialogClosed">
        <el-form :model="editAddressForm"
                 :rules="editAddressRules"
                 ref="editAddressFormRef"
                 label-width="100px">
          <el-form-item label="省市区/县" prop="address">
            <el-cascader
              :options="cityData"
              v-model="editAddressForm.address">
            </el-cascader>
          </el-form-item>
          <el-form-item label="详细地址" prop="detailAddress">
            <el-input v-model="editAddressForm.detailAddress"></el-input>
          </el-form-item>
        </el-form>
        <span slot="footer" class="dialog-footer">
    <el-button @click="editAddressDialogClosed = false">取 消</el-button>
    <el-button type="primary" @click="editAddressDialogClosed = false">确 定</el-button>
        </span>
      </el-dialog>
      <!-- 物流进度的对话框 -->
      <el-dialog
        title="修改地址"
        :visible.sync="progressDialogVisible"
        width="50%" @close="progressDialogClosed">
        <el-timeline>
          <el-timeline-item
            v-for="(activity, index) in progressInfo"
            :key="index"
            :timestamp="activity.time">
            {{ activity.context }}
          </el-timeline-item>
        </el-timeline>
      </el-dialog>
    </div>
</template>

<script>
import { getOrderList } from '../../network/order'
import cityData from '../../js/cityData'
import { getKuaidi } from '../../network/kuaidi'
export default {
  name: 'Order',
  created () {
    this.queryOrderList()
  },
  data () {
    return {
      queryInfo: {
        query: '',
        pageSize: 5,
        pageNum: 1
      },
      total: 0,
      orderList: [],
      addressDialogVisible: false,
      editAddressForm: {
        address: [],
        detailAddress: ''
      },
      editAddressRules: {
        address: [
          { required: true, message: '请选择省市区县', trigger: 'blur' }
        ],
        detailAddress: [
          { required: true, message: '请填写详细地址', trigger: 'blur' }
        ]
      },
      cityData: cityData,
      progressDialogVisible: false,
      progressInfo: []
    }
  },
  methods: {
    queryOrderList () {
      getOrderList(this.queryInfo).then(res => {
        if (res.code !== 200) {
          this.$message.error(res.message)
        }
        this.orderList = res.data.orders
        this.total = res.data.total
      })
    },
    handleSizeChange (newSize) {
      this.queryInfo.pageSize = newSize
      this.queryOrderList()
    },
    handleCurrentChange (newNum) {
      this.queryInfo.pageNum = newNum
      this.queryOrderList()
    },
    showBox (row) {
      this.addressDialogVisible = true
    },
    editAddressDialogClosed () {
      this.$refs.editAddressFormRef.resetFields()
    },
    showProgressBox (row) {
      getKuaidi(1106975712662).then(res => {
        if (res.code !== 200) {
          this.$message.error(res.message)
        } else {
          this.progressInfo = res.data
        }
      })
      this.progressDialogVisible = true
    },
    progressDialogClosed () {}
  }
}
</script>

<style scoped>
.el-cascader {
  width: 100%;
}
</style>
