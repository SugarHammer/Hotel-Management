<template>
  <div>
    <el-menu
      background-color="#545c64"
      text-color="#fff"
      active-text-color="#ffd04b"
      style="width: 200px;min-height:1800px"
      :default-active="$route.path"
      router
    >
    <!--<div  v-for="m in user.permissions" :key="m.id">
        <el-menu-item :index="m.path" v-if="m.name !== 'Person' && m.name !== 'Password' ">
          <i :class="m.icon"></i>  {{ m.comment }}
        </el-menu-item>
      </div>
    </el-menu>
  </div>-->
    <div v-for="m in user.permissions" :key="m.id">

      <el-menu-item v-if="m.childrenList.length == 0 " :index="m.path">
        <i :class="m.icon"></i>
        {{ m.comment }}
      </el-menu-item>

      <el-submenu v-else :index="m.id+''">

        <template v-slot:title>
          <i :class="m.icon"></i>
          <span>{{ m.comment }}</span>
        </template>
        <el-menu-item-group>
          <el-menu-item
            v-for="cm in m.childrenList"
            :index="cm.path"
            :key="m.id+'-'+cm.id"
            :route="cm.path"
          >
            <i :class="cm.icon"></i>
              {{ cm.comment }}
          </el-menu-item>
        </el-menu-item-group>
      </el-submenu>
    </div>
    </el-menu>
  </div>
</template>

<script>
import request from "@/utils/request";

export default {
  name: "Aside",
  data() {
    return {
      user: {}
    }
  },
  created() {
    // 请求服务端，确认当前登录用户的 合法信息
    let userStr = sessionStorage.getItem("user") || "{}"
    this.user = JSON.parse(userStr)
  }
}
</script>

<style scoped>
  .title {
    text-align: center;
    color: #fff;
  }
</style>
