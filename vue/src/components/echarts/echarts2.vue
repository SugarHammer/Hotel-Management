<!-- echartscom.vue -->
<template>
  <div class="charttest">
    <div :style="{width: '700px', height: '400px'}" :id="echarts" class="echarts" ref="echarts">
    </div>
  </div>
</template>

<script>

import request from "@/utils/request";

  export default {
    name:'echarts2',
    data() {
      return {};
    },
    methods: {
      drawCharts() {
        let myChart = this.$root.echarts.init(document.getElementById(this.echarts));
        let option = {
          title: {
            text: '报修反馈比例统计图',
            subtext: '虚拟数据',
            left: 'left'
          },
          tooltip: {
            trigger: 'item'
          },
          legend: {
            trigger: 'item',
            orient: 'vertical',
            x:'right',      //可设定图例在左、右、居中
            y:'center',     //可设定图例在上、下、居中
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
              name: '维修情况',
              type: 'pie',
              radius: ['50%', '70%'],
              avoidLabelOverlap: false,
              label: {
                normal: {
                  show: false,
                  position: 'center'
                },
                emphasis: {
                  show: true,
                  textStyle: {
                    fontSize: '30',
                    fontWeight: 'bold'
                  }
                }
              },
              labelLine: {
                normal: {
                  show: false
                }
              },
              data: []
            }
          ]
        };
        request.get("/engineer/count").then(res => {
        if (res.code === '0') {
          res.data.forEach(item => {
            option.series[0].data.push({name: item.state, value: item.count})
          })
          // 绘制图表
          myChart.setOption(option);
        }
      })
      }
    },
    computed: {
      echarts() {
        return 'echarts' + Math.random() * 100000;
      }
    },
    mounted() {
      this.drawCharts();
    },
  };
</script>

<style></style>