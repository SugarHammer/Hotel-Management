<template>
    <div id="myChart" :style="{width: '700px', height: '400px'}"></div>
</template>

<script>
import request from "@/utils/request";

export default {
  data() {
    return {}
  },
  mounted() {
    this.drawLine();
  },
  methods: {
    drawLine() {
      // 基于准备好的dom，初始化echarts实例
      let myChart = this.$root.echarts.init(document.getElementById('myChart'))
      let option = {
        title: {
          text: '各地区用户比例统计图',
          subtext: '虚拟数据',
          left: 'left'
        },
        tooltip: {
          trigger: 'item'
        },
        legend: {
          trigger: 'item',
          orient: 'vertical',
          x:'right',  
          y:'center', 
          padding:[0,50,0,0],
        },
        toolbox: {
          show: true,
          feature: {
            mark: {show: true},
            dataView: {show: true, readOnly: false},
            restore: {show: true},
            saveAsImage: {show: true}
          }
        },
        series: [
          {
            name: '用户比例',
            type: 'pie',
            radius: [50, 150],
            center: ['50%', '50%'],
            roseType: 'area',
            itemStyle: {
              borderRadius: 4
            },
            data: []
          }
        ]
      }
      request.get("/user/count").then(res => {
        if (res.code === '0') {
          res.data.forEach(item => {
            option.series[0].data.push({name: item.address, value: item.count})
          })
          // 绘制图表
          myChart.setOption(option);
        }
      })

    }
  }
}
</script>

<style scoped>

</style>
