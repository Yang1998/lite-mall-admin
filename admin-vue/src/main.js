import Vue from 'vue'
import App from './App.vue'
import router from './router'
import './assets/css/global.css'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import TreeTable from 'vue-table-with-tree-grid'
// 导入富文本编辑器
import VueQuillEditor from 'vue-quill-editor'
// require styles 导入富文本编辑器的位置
import 'quill/dist/quill.core.css'
import 'quill/dist/quill.snow.css'
import 'quill/dist/quill.bubble.css'

Vue.use(ElementUI)

Vue.use(VueQuillEditor)

Vue.component('tree-table', TreeTable)

Vue.config.productionTip = false

Vue.filter('dateFormat', function (originVal) {
  const time = new Date(originVal * 1000)
  const year = time.getFullYear()
  const month = (time.getMonth() + 1 + '').padStart(2, '0')
  const day = (time.getDate() + '').padStart(2, '0')
  const hour = (time.getHours() + '').padStart(2, '0')
  const minute = (time.getMinutes() + '').padStart(2, '0')
  const second = (time.getSeconds() + '').padStart(2, '0')
  return `${year}-${month}-${day} ${hour}:${minute}:${second}`
})

new Vue({
  router,
  render: h => h(App)
}).$mount('#app')
