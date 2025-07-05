<template>
    <div id="box" :style="{width: '700px', height: '400px'}"></div>
</template>

<script>
import request from "@/utils/request";
  export default {
    name:'echarts3',
    data() {
      return {};
    },
    methods: {
      drawCharts() {
        //初始化ehcharts实例
        var myChart=this.$root.echarts.init(document.getElementById("box"));
        //指定图表的配置项和数据
        var option={
            //标题
            title:{
            text:'酒店收入统计'
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
            //工具箱
            //保存图片
            toolbox: {
                show: true,
                feature: {
                    mark: {show: true},
                    dataView: {show: true, readOnly: false},
                    restore: {show: true},
                    saveAsImage: {show: true}
                }
            },
            //图例-每一条数据的名字叫销量
            legend:{
                data:['销量'],
            },
            //x轴
            xAxis:{
                data:[]
            },
            //y轴没有显式设置，根据值自动生成y轴
            yAxis:{axisLabel: {
                    formatter: '{value}'
                  }},
            //数据-data是最终要显示的数据
            series:[{
                name:'收入',
                type:'line',
                data:[],
            }]
            };
        request.get("/order/count").then(res => {
          if (res.code === '0') {
            res.data.forEach(item => {
              option.series[0].data.push(item.count);
              option.xAxis.data.push(item.name)
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
