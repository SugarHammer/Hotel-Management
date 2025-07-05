<template>
  
    <div class="homepage-hero-module">
      <div class="building">
        <el-row>
        <el-col :span="10">
            <div class="bg-purple-dark"  style="font-size: 50px">
                欢迎来到智能酒店管理系统
            </div>
        </el-col>
        <el-menu
        class="bg-purple-dark"
        mode="horizontal" 
        text-color="#000000" 
        active-text-color="#0000ff" 
        style="width: 1000px;padding-left: 200px">
            <el-menu-item index="1" style="width: 250px;color: #ffffff;font-size: 20px"
            route="/login"
            @click="$router.push('/login')">登录</el-menu-item>
            <el-menu-item index="2" style="width: 250px;color: #ffffff;font-size: 20px"
            @click="$router.push('/register')">注册</el-menu-item>
            <el-menu-item index="3" style="width: 250px;color: #ffffff;font-size: 20px"
            @click="$router.push('/')">返回首页</el-menu-item>
        </el-menu>
        </el-row>
      <div :style="fixStyle" class="filter">
        <div style="width: 500px; height:360px; margin: 100px auto; background: white;">
          <div style="font-size: 30px; text-align: center; padding: 30px 0; color: #333">欢迎登录</div>
          <center>
          <el-form ref="form" :model="form" size="normal" :rules="rules">
            <el-form-item prop="username">
              <el-input prefix-icon="el-icon-user-solid" v-model="form.username" placeholder="请输入账号"></el-input>
            </el-form-item>
            <el-form-item prop="password">
              <el-input prefix-icon="el-icon-lock" v-model="form.password" show-password placeholder="请输入密码"></el-input>
            </el-form-item>
            <el-form-item>
              <el-row>
                <el-col :span="10" style="padding-left: 50px">
                <el-input prefix-icon="el-icon-key" v-model="form.validCode" style="width: 200px;" placeholder="请输入验证码"></el-input>
                </el-col>
                <el-col :span="10" style="padding-left: 50px">
                <ValidCode @input="createValidCode" /> 
                </el-col>
              </el-row>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="login">登 录</el-button>
            </el-form-item>
          </el-form>
          </center>
        </div>
      </div>
      <el-footer
        style="
        position: absolute;
        bottom: 0;
        width: 100%;
        background-color: #B3C0D1;
        color: #333;
        text-align: center;
        line-height: 100px;
        ">
        <div style="margin: -20px;">
            Copyright © 2025.XXX All rights reserved
        </div>
      </el-footer>
    </div>
  </div>

</template>

<script>
import request from "@/utils/request";
import ValidCode from "@/components/ValidCode";
import {activeRouter} from "@/utils/permission";
import router from "@/router";

export default {
  name: "Login",
  components: {
    ValidCode,
  },
  data() {
    return {
      vedioCanPlay: false,
      fixStyle: '',
      form: {role: 1},
      rules: {
        username: [
          {required: true, message: '请输入用户名', trigger: 'blur'},
        ],
        password: [
          {required: true, message: '请输入密码', trigger: 'blur'},
        ],
      },
      validCode: ''
      // 加背景图片
      // bg: {
      //   backgroundImage: "url(" + require("@/assets/bg.jpg") + ")",
      //   backgroundRepeat: "no-repeat",
      //   backgroundSize: "100% 100%"
      // }
    }
  },
  mounted() {
    sessionStorage.removeItem("user")

    window.onresize = () => {
      const windowWidth = document.body.clientWidth
      const windowHeight = document.body.clientHeight
      const windowAspectRatio = windowHeight / windowWidth
      let videoWidth
      let videoHeight
      if (windowAspectRatio < 0.5625) {
        videoWidth = windowWidth
        videoHeight = videoWidth * 0.5625
        this.fixStyle = {
          height: windowWidth * 0.5625 + 'px',
          width: windowWidth + 'px',
          'margin-bottom': (windowHeight - videoHeight) / 2 + 'px',
          'margin-left': 'initial'
        }
      } else {
        videoHeight = windowHeight
        videoWidth = videoHeight / 0.5625
        this.fixStyle = {
          height: windowHeight + 'px',
          width: windowHeight / 0.5625 + 'px',
          'margin-left': (windowWidth - videoWidth) / 2 + 'px',
          'margin-bottom': 'initial'
        }
      }
    }
    window.onresize()
  },
  watch: {
    '$route' (to, from) {
        this.$router.go(0);
    }
  },
  methods: {
    canplay() {
      this.vedioCanPlay = true
    },
    // 接收验证码组件提交的 4位验证码
    createValidCode(data) {
      this.validCode = data
    },
    login () {
      this.$refs['form'].validate((valid) => {
        if (valid) {
          if (!this.form.validCode) {
            this.$message.error("请填写验证码")
            return
          }
          if(this.form.validCode.toLowerCase() !== this.validCode.toLowerCase()) {
            this.$message.error("验证码错误")
            return
          }
          request.post("/user/login", this.form).then(res => {
            if (res.code === '0') {
              sessionStorage.setItem("user", JSON.stringify(res.data))  // 缓存用户信息
              // 登录成功的时候更新当前路由
              activeRouter()
              this.$message({
                type: "success",
                message: "登录成功"
              })
              this.$router.push("/hindex")  //登录成功之后进行页面的跳转，跳转到主页
            } else {
              this.$message({
                type: "error",
                message: res.msg
              })
            }
          })
        }
      })
    }
  }
}
</script>

<style scoped>
.homepage-hero-module,

.building{
  background:url("../assets/bgbg.jpg");
  width:100%;
  height:100%;
  position:fixed;
  background-size:100% 100%;
}

.fillWidth {
  width: 100%;
}

.bg-purple-dark {
    background: rgba(0, 0, 0, 0);
    text-align: center;
    padding: 60px 0; 
    color: white;
  }

.el-input {
  width: 400px;
}

.el-button{
  width: 400px;
}

.el-form-item ::v-deep .el-form-item__error{
  padding-left: 50px;
}

</style>
