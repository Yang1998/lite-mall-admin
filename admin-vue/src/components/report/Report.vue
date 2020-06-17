<template>
    <div>
      <!-- 面包屑导航 -->
      <el-breadcrumb separator-class="el-icon-arrow-right">
        <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
        <el-breadcrumb-item>数据统计</el-breadcrumb-item>
        <el-breadcrumb-item>数据报表</el-breadcrumb-item>
      </el-breadcrumb>
      <!-- 卡片视图区域 -->
      <el-card>
        <!-- Echarts -->
        <div id="main" class="echartMain"></div>
      </el-card>
    </div>
</template>

<script>
import echarts from 'echarts'
import { getReportData } from '../../network/report'
import _ from 'loadsh'
export default {
  name: 'Report',
  created () {},
  mounted () {
    const myChart = echarts.init(document.getElementById('main'))

    getReportData(1).then(res => {
      if (res.code !== 200) {
        this.$message.error(res.message)
      } else {
        const merge = _.merge(res.data, this.options)
        merge.series.forEach(item => {
          item.areaStyle = { normal: {} }
        })
        myChart.setOption(merge)
      }
    })
  },
  data () {
    return {
      options: {
        title: {
          text: '⽤户来源'
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'cross',
            label: {
              backgroundColor: '#E9EEF3'
            }
          }
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: [
          {
            boundaryGap: false
          }
        ],
        yAxis: [
          {
            type: 'value'
          }
        ]
      }
    }
  },
  methods: {}
}
</script>

<style scoped>
.echartMain {
  width: 800px;
  height: 400px;
}
</style>
