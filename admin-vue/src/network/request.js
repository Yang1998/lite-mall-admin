import axios from 'axios'

export function request (config) {
  const instance = axios.create({
    baseURL: 'http://localhost',
    timeout: 5000
  })
  // axios的拦截器
  // 1. 请求拦截的作用
  instance.interceptors.request.use(config => {
    if (config.url.indexOf('/login') !== -1) {
      return config
    } else {
      config.headers.Authorization = window.sessionStorage.getItem('token')
      return config
    }
  }, error => {
    console.log(error)
  })
  // 2.响应拦截
  instance.interceptors.response.use(res => {
    return res.data
  }, error => {
    console.log(error)
  })
  // 发送真正的网络请求
  return instance(config)
}
