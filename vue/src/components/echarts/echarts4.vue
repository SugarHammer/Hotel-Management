<template>
    <div id="box2" :style="{width: '700px', height: '400px'}"></div>
</template>

<script>
import request from "@/utils/request";

  export default {
    name:'echarts4',
    data() {
      return {};
    },
    methods: {
      drawCharts() {
        //初始化ehcharts实例
        let myChart=this.$root.echarts.init(document.getElementById("box2"));
        //指定图表的配置项和数据
        let option = {
            title:{
                text:'酒店下单统计'
            },
              tooltip: {
                trigger: 'axis',
                axisPointer: {
                  type: 'cross',
                  crossStyle: {
                    color: '#999'
                  }
                }
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
              legend: {
                icon: 'rect',//形状  类型包括 circle，rect,line，roundRect，triangle，diamond，pin，arrow，none
                itemWidth: 10,  // 设置宽度
                itemHeight: 4, // 设置高度
                itemGap: 24, // 设置间距
                trigger: 'item',
                textStyle: {
                    //文字样式
                    color: '#c1dafc',
                    fontSize: '12'
                },
                right: '30%'
              },
              xAxis: [
                {
                  type: 'category',
                  data: [],
                  axisPointer: {
                    type: 'shadow'
                  }
                }
              ],
              yAxis: [
                {
                  type: 'value',
                  interval: 1,
                  axisLabel: {
                    formatter: '{value}'
                  }
                }
              ],
              series: [
                {
                  name: '下单人数',
                  type: 'bar',
                  barWidth: '5%',
                  data: []
                }
              ],
              color: ['#b1de6a', '#4ab0ee']
            };


        request.get("/contract/count").then(res => {
          if (res.code === '0') {
            res.data.forEach(item => {
              option.series[0].data.push(item.count);
              option.xAxis[0].data.push(item.name)
            })
            // 绘制图表
            myChart.setOption(option);
          }
      })
    }
    },
    mounted() {
      this.drawCharts();
    },
  };
    
</script>