<template>
    <el-container class="home-container">
      <!-- 头部区 -->
      <el-header>
        <div>
          <span>电商后台管理系统</span>
        </div>
        <el-button type="info" @click="logout">退出</el-button>
      </el-header>
      <!-- 页面主体区域 -->
      <el-container>
        <!-- 侧边栏 -->
        <el-aside :width="isCollapsed ? '64px' : '200px'">
          <div class="toggle-button" @click="toggleCollapse">
            |||
          </div>
          <!-- 侧边栏菜单区 -->
          <el-menu
            background-color="#08293b"
            text-color="#fff"
            active-text-color="#409EFF"
            :unique-opened="true"
            :collapse="isCollapsed"
            :collapse-transition="false"
            router
            :default-active="this.$router.history.current.path">
              <!-- 一级菜单 -->
            <el-submenu :index="item.id + ''" v-for="item in menuList" :key="item.id">
              <!-- 一级菜单的模板区域 -->
              <template slot="title">
                <!-- 图标 -->
                <i :class="iconsObj[item.id]"></i>
                <!-- 文本 -->
                <span >{{ item.authName }}</span>
              </template>
              <!-- 二级菜单 -->
              <el-menu-item
                :index="'/' + subItem.path"
                v-for="subItem in item.children"
                :key="subItem.id">
                <!-- 图标 -->
                <i class="el-icon-menu"></i>
                <!-- 文本 -->
                <span>{{ subItem.authName }}</span>
              </el-menu-item>
            </el-submenu>
          </el-menu>
        </el-aside>
        <!-- 主体区 -->
        <el-main>
          <router-view/>
        </el-main>
      </el-container>
    </el-container>
</template>

<script>
import { getMenusList } from '../network/menu'
import { logoutReq } from '../network/login'
export default {
  name: 'home',
  data () {
    return {
      menuList: [],
      iconsObj: {
        // user
        125: 'el-icon-user-solid',
        // 权限
        103: 'el-icon-key',
        // 商品
        101: 'el-icon-goods',
        // 订单
        102: 'el-icon-s-order',
        // 数据统计
        145: 'el-icon-s-marketing'
      },
      isCollapsed: false
    }
  },
  created () {
    this.getMenuList()
  },
  methods: {
    logout () {
      logoutReq().then(res => {
        window.sessionStorage.clear()
        this.$router.push('/login')
      })
      // window.sessionStorage.clear()
      // this.$router.push('/login')
    },
    getMenuList () {
      const request = getMenusList()
      request.then(res => {
        this.menuList = res.data
      })
    },
    // 点击按钮切换侧边栏的折叠与展开
    toggleCollapse () {
      this.isCollapsed = !this.isCollapsed
    }
  }
}
</script>
<style scoped>
.home-container {
  height: 100%;
}

.el-header {
  background-color: #368286;
  display: flex;
  justify-content: space-between;
  padding-left: 0;
  align-items: center;
  color: #ffffff;
  font-size: 20px;
}

.el-header div {
  display: flex;
  align-items: center;
}

.el-header div span {
  margin-left: 15px;
}

.el-aside {
  background-color: #08293b;
}

.el-main {
  background-color: #E9EEF3;
}

.el-menu {
  border-right: none;
}

.toggle-button {
  background-color: #4a5064;
  font-size: 10px;
  line-height: 24px;
  color: #ffffff;
  text-align: center;
  letter-spacing: 0.2em;
  cursor: pointer;
}
</style>
