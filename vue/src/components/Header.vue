<template>
  <div style=" background-color: #545c64;height: 50px; line-height: 50px; border-bottom: 1px solid #545c64; display: flex">
    <div style="width: 200px;padding-left: 30px; font-weight: bold; color: #ffd04b">智能酒店管理系统</div>
    <div style="flex: 1"></div>
    <div style="width: 180px">
      <el-row>
      <div style="width: 130px; padding-left: 30px;color: #ffd04b">
            {{ user.nickname }}    
      </div>
      <el-dropdown>
        <span class="el-dropdown-link">
          <el-avatar :size="30" :src="user.avatar" style="position: relative; top: 10px"></el-avatar>
          <i class="el-icon-arrow-down el-icon--right"></i>
        </span>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item @click="$router.push('/login')">退出系统</el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
      </el-row>
    </div>
  </div>
</template>

<script>
export default {
  name: "Header",
  props: ['user'],
  data() {
    return {

    }
  },
  created() {
  }
}
</script>

<style scoped>

</style>
