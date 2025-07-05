<template>
  <div class="example-container">
    <el-breadcrumb separator="">
        <el-breadcrumb-item
          v-for="item in breadList"
          :key="item.name"
          :to="{ path: item.path }"
        >
        <el-button size="mini">
            {{item.name}}
            <el-button type="text" size="mini"  @click.stop="deleteBread(item)">&nbsp;x</el-button>
        </el-button>
        </el-breadcrumb-item>
      </el-breadcrumb>
     </div>
</template>

<script>
  export default {
    data() {
      return {
        breadList: [], // 路由集合
      };
    },
    watch: {
      $route() {
        this.getBreadcrumb();
      }
    },
    methods: {
      isHome(route) {
        return route.name === 'Hindex';
      },
      getBreadcrumb() {
        let matched = this.$route.matched;
        matched.forEach(m => {
            if(m.name != 'Layout' ){
                this.breadList.push(m)
            }
        });
        this.breadList =  Array.from(new Set(this.breadList))
      },
      deleteBread(item){
        this.breadList.forEach(function(bread, index, arr) {
            if(bread.name === item.name) {
                arr.splice(index, 1);
            }
        });
      },
    },
    created() {
      this.getBreadcrumb();
    }
  }
</script>

<style>
.app-breadcrumb.el-breadcrumb {
  display: inline-block;
  font-size: 14px;
  line-height: 50px;
  margin-left: 8px;
}
</style>
