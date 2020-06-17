import Vue from 'vue'
import VueRouter from 'vue-router'

Vue.use(VueRouter)

const routes = [
  {
    path: '',
    redirect: '/login'
  },
  {
    path: '/login',
    component: () => import('../components/login')
  },
  {
    path: '/home',
    component: () => import('../components/home'),
    redirect: '/welcome',
    children: [
      {
        path: '/welcome',
        component: () => import('../components/welcome')
      },
      {
        path: '/users',
        component: () => import('../components/user/Users')
      },
      {
        path: '/goods',
        component: () => import('../components/goods/Goods')
      },
      {
        path: '/rights',
        component: () => import('../components/permission/Rights')
      },
      {
        path: '/roles',
        component: () => import('../components/permission/Role')
      },
      {
        path: '/categories',
        component: () => import('../components/goods/Cate')
      },
      {
        path: '/params',
        component: () => import('../components/goods/Params')
      },
      // {
      //   path: '/list',
      //   component: () => import('../components/goods/List')
      // },
      {
        path: '/goods/add',
        component: () => import('../components/goods/Add')
      },
      {
        path: '/orders',
        component: () => import('../components/order/Order')
      },
      {
        path: '/reports',
        component: () => import('../components/report/Report')
      }
    ]
  }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

// 挂载路由导航守卫
router.beforeEach((to, from, next) => {
  // to    将要访问的路径
  // from  从哪个路径跳转而来
  // next  放行函数， next() -> 放行 or next('/home') -> 强制跳转
  if (to.path === '/login') {
    next()
  } else {
    // token
    const token = window.sessionStorage.getItem('token')
    if (!token) {
      next('/login')
    } else {
      next()
    }
  }
})

export default router
