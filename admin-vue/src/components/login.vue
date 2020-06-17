<template>
  <div class="login-container">
    <div class="login-box">
      <div class="avatar-box">
        <img src="../assets/cat.jpg" alt=""/>
      </div>
      <!-- 可以通过ref的值拿到form的实例对象 -->
      <el-form label-width="0px" class="login-form"
               :model="loginForm"
               :rules="loginRules"
               ref="loginFormRef">
        <!-- 用户名 -->
        <el-form-item prop="username">
          <el-input
            prefix-icon="el-icon-user"
            v-model="loginForm.username"
            placeholder="用户名"/>
        </el-form-item>
        <!-- 密码 -->
        <el-form-item prop="password">
          <el-input
            prefix-icon="el-icon-lock"
            v-model="loginForm.password"
            type="password"
            placeholder="密码"/>
        </el-form-item>
        <!-- 按钮 -->
        <el-form-item class="btns">
          <el-button type="primary" @click="login">登录</el-button>
          <el-button type="info" @click="resetLoginForm">重置</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
import { loginReq } from '../network/login'
import { JSEncrypt } from 'jsencrypt'
import keys from '../js/rsaKey'
import _ from 'loadsh'

export default {
  name: 'login',
  data () {
    return {
      // 登录表单的数据绑定对象
      loginForm: {
        username: '',
        password: ''
      },
      loginRules: {
        username: [
          { required: true, message: '请输入姓名', trigger: 'blur' },
          { min: 2, max: 7, message: '姓名长度在 2 到 7 个字符', trigger: ['blur', 'change'] }
        ],
        password: [
          { required: true, message: '密码不可为空', trigger: 'blur' },
          { min: 6, max: 15, message: '密码长度在 6 到 15 个字符', trigger: ['blur', 'change'] }
        ]
      }
    }
  },
  methods: {
    //  点击重置按钮，重置表单
    resetLoginForm () {
      this.$refs.loginFormRef.resetFields()
    },
    login () {
      this.$refs.loginFormRef.validate(valid => {
        if (!valid) {
          return
        }
        const form = _.clone(this.loginForm)
        form.password = this.encryptedData(keys.RSPublicKey, form.password)
        // console.log(form.password)
        // console.log(this.decryptData(keys.RSPrivateKey, form.password))
        const res = loginReq(form)
        res.then(res => {
          if (res.code === 200) {
            this.$message.success(res.message)
          } else {
            this.$message.error(res.message)
          }
          // 1. 保存token到sessionStorage
          window.sessionStorage.setItem('token', res.data.token)
          // 2. 通过编程式导航跳转到后台主页， -> /home
          this.$router.push('/home')
        })
      })
    },
    encryptedData (publicKey, data) {
      // 新建JSEncrypt对象
      const encryptor = new JSEncrypt()
      // 设置公钥
      encryptor.setPublicKey(publicKey)
      // 加密数据
      return encryptor.encrypt(data)
    },
    // 解密, 用不到
    decryptData (privateKey, data) {
      // 新建JSEncrypt对象
      const decrypt = new JSEncrypt()
      // 设置私钥
      decrypt.setPrivateKey(privateKey)
      // 解密数据
      return decrypt.decrypt(data)
    }
  }
}
</script>

<style scoped>
.login-container {
  height: 100%;
  /*background-color: #2b4b6b;*/
  background: #6190E8;  /* fallback for old browsers */
  background: -webkit-linear-gradient(to right, #A7BFE8, #6190E8);  /* Chrome 10-25, Safari 5.1-6 */
  background: linear-gradient(to right, #A7BFE8, #6190E8); /* W3C, IE 10+/ Edge, Firefox 16+, Chrome 26+, Opera 12+, Safari 7+ */
}

.login-box {
  width: 450px;
  height: 300px;
  background-color: rgba(0, 0, 0, 0.38);
  /*background: #de6161;  !* fallback for old browsers *!*/
  /*background: -webkit-linear-gradient(to right, #2657eb, #de6161);  !* Chrome 10-25, Safari 5.1-6 *!*/
  /*background: linear-gradient(to right, #2657eb, #de6161); !* W3C, IE 10+/ Edge, Firefox 16+, Chrome 26+, Opera 12+, Safari 7+ *!*/
  border-radius: 10px;
  position: absolute;
  left: 50%;
  top: 50%;
  transform: translate(-50%, -50%);
}

.login-box .avatar-box {
  height: 130px;
  width: 130px;
  border: 1px solid #eeeeee;
  border-radius: 50%;
  padding: 10px;
  box-shadow: 0 0 10px #dddddd;
  position: absolute;
  left: 50%;
  transform: translate(-50%, -50%);
  background-color: #ffffff;
}

.login-box .avatar-box img {
  width: 100%;
  height: 100%;
  border-radius: 50%;
  background-color: #eeeeee;
}

.btns {
  display: flex;
  justify-content: flex-end;
}

.login-form {
  position: absolute;
  bottom: 0;
  width: 100%;
  padding: 0 20px;
  box-sizing: border-box;
}
</style>
