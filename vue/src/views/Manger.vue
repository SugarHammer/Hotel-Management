<template>
  <div style="padding: 10px">
    <!-- <el-row style="background:#fff;padding:16px 16px 0;margin-bottom:32px;">
        <echarts-4 />
      </el-row>
      <el-row :gutter="32">
        <el-col :xs="24" :sm="24" :lg="8">
          <div class="chart-wrapper">
            <echarts-1 />
          </div>
        </el-col>
        <el-col :xs="24" :sm="24" :lg="8">
          <div class="chart-wrapper">
            <echarts-2 />
          </div>
        </el-col>
        <el-col :xs="24" :sm="24" :lg="8">
          <div class="chart-wrapper">
            <echarts-3 />
          </div>
        </el-col>
      </el-row> -->
    <el-row>
      <el-col :span="12">
        <el-card style="padding: 10px">
          <echarts-1 />
        </el-card>

      </el-col>
      <el-col :span="12">
        <el-card  style="padding: 10px">
          <echarts-2 />
        </el-card>
      </el-col>
    </el-row>
    <el-row>
      <el-col :span="12">
        <el-card  style="padding: 10px">
          <echarts-3 />
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card style="padding: 10px">
          <echarts-4 />
        </el-card>
      </el-col>
    </el-row>
    
  </div>
</template>

<script>
import echarts1 from '../components/echarts/echarts1.vue';
import echarts2 from '../components/echarts/echarts2.vue';
import echarts3 from '../components/echarts/echarts3.vue';
import echarts4 from '../components/echarts/echarts4.vue';

export default {
  components: { echarts2,echarts1,echarts3,echarts4 },
  name: "Manger",
  data() {
    return {}
  },
  mounted() {
  },
  methods: {
   
  }
}
</script>

<style scoped>
.chart-wrapper {
      background: #fff;
      padding: 16px 16px 0;
      margin-bottom: 32px;
    }
</style>
