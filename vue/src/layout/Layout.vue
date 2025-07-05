<template>
  <div>
    <!--    头部-->
    <Header :user="user"/>
    <!--    主体-->
    <div style="display: flex">
      <!--      侧边栏-->
      <Aside />
      <div style="margin: 10px;flex: 1">
        <!--      面包屑-->
          <Breadcrumb />
        <!--      内容区域-->
        <div style="margin: 10px">
          <router-view @userInfo="refreshUser"/>
          
        </div>
        <el-footer
            style="
            bottom: 0;
            width: 100%;
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
    
  </div>
</template>

<script>
import Header from "@/components/Header";
import Aside from "@/components/Aside";
import Breadcrumb from "@/components/Breadcrumb"
import request from "@/utils/request";

export default {
  name: "Layout",
  components: {
    Header,
    Aside,
    Breadcrumb
  },
  data() {
    return {
      user: {}
    }
  },
  created() {
    this.refreshUser()
  },
  methods: {
    refreshUser() {
      let userJson = sessionStorage.getItem("user");
      if (!userJson) {
        return
      }
      let userId = JSON.parse(userJson).id
      // 从后台取出更新后的最新用户信息
      request.get("/user/" + userId).then(res => {
        this.user = res.data
      })
    }
  }
}
</script>

<style scoped>

</style>
