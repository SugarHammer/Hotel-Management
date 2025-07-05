<template>
  <div style="width: 100%; height: 100vh; overflow: hidden">
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
    <div style="width: 500px; height:420px; margin: 100px auto; background: white;">
      <div style="font-size: 30px; text-align: center; padding: 30px 0">欢迎注册</div>
      <center>
      <el-form ref="form" :model="form" size="normal" :rules="rules">
        <el-form-item prop="username">
          <el-input prefix-icon="el-icon-user-solid" placeholder="请输入账号" v-model="form.username"></el-input>
        </el-form-item>
        <el-form-item prop="telephone">
          <el-input prefix-icon="el-icon-phone" placeholder="请输入手机号" v-model="form.telephone"></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input prefix-icon="el-icon-lock" placeholder="请输入密码" v-model="form.password" show-password></el-input>
        </el-form-item>
        <el-form-item prop="confirm">
          <el-input prefix-icon="el-icon-lock" placeholder="请再输入一次密码" v-model="form.confirm" show-password></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="register">注 册</el-button>
        </el-form-item>
      </el-form>
      </center>
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

export default {
  name: "Register",
  data() {
    return {
      form: {},
      rules: {
        username: [
          {required: true, message: '请输入用户名', trigger: 'blur'},
        ],
        telephone: [
          {required: true, message: '请输入正确手机号', trigger: 'blur', pattern: /^1([358][0-9]|4[579]|66|7[0135678]|9[89])[0-9]{8}$/},
        ], 
        password: [
          {required: true, message: '密码要求由数字、大写字母和小写字母组成，且长度要在8-16位之间。', trigger: 'blur',  pattern: /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[^]{8,16}$/},
        ],
        confirm: [
          {required: true, message: '请确认密码', trigger: 'blur'},
        ],
      }
    }
  },
  methods: {
    register() {

      if (this.form.password !== this.form.confirm) {
        this.$message({
          type: "error",
          message: '2次密码输入不一致！'
        })
        return
      }

      this.$refs['form'].validate((valid) => {
        if (valid) {
          request.post("/user/register", this.form).then(res => {
            if (res.code === '0') {
              this.$message({
                type: "success",
                message: "注册成功"
              })
              this.$router.push("/login")  //登录成功之后进行页面的跳转，跳转到主页
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
.building{
  background:url("../assets/bgbg.jpg");
  width:100%;
  height:100%;
  position:fixed;
  background-size:100% 100%;
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
