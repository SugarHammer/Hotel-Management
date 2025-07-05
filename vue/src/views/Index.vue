<template>
<el-container>
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

</el-container>
</template>

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
</style>