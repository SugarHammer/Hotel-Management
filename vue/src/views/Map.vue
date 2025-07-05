<template>
  <div>
    <div id="container" style="width: 100%;height:1000px"></div>
  </div>
</template>

<script>
import mapIcon from "../assets/markers.png";
import request from "@/utils/request";
export default {
  name: "Map",
  data() {
    return {
    }
  },
  mounted() {

  // 百度地图API功能
  var map = new BMap.Map('container'); // 创建Map实例
  var mapList = [];
  // 创建地图实例  
  var point = new BMap.Point(116.404, 39.915);
  // 创建点坐标  
  map.centerAndZoom(point, 12);
  var marker = new BMap.Marker(point);
  // map.addOverlay(marker);
  map.enableScrollWheelZoom(true); // 开启鼠标滚轮缩放
  function markerFun(points, infoWindows, icon) {
    var markers = new BMap.Marker(points, { icon: icon });
    map.addOverlay(markers);
    markers.addEventListener('click', function (event) {
      map.openInfoWindow(infoWindows, points);//参数：窗口、点  根据点击的点出现对应的窗口
    })
  }

  var geolocation = new BMap.Geolocation();
    geolocation.getCurrentPosition(function(r){
      if(this.getStatus() == BMAP_STATUS_SUCCESS){
        var mk = new BMap.Marker(r.point);
        map.addOverlay(mk);
        map.panTo(r.point);
        alert('您的位置：'+r.point.lng+','+r.point.lat);
      }
      else {
        alert('定位失败'+this.getStatus());
      }
    });
  request.get("/apartment/allList").then(res => {
    mapList = res.data
    for (var i = 0; i < mapList.length; i++) {

      var x = Number(mapList[i].coordinates.split(',')[0])
      var y = Number(mapList[i].coordinates.split(',')[1])
      // 根据不同的状态设置不同的标记
      if (mapList[i].status != 0) {
        var icon = new BMap.Icon( mapIcon, new BMap.Size(20, 25), {
          anchor: new BMap.Size(10, 30)
        })
      } else {
        var icon = new BMap.Icon( mapIcon, new BMap.Size(20, 25), {
          anchor: new BMap.Size(10, 30)
        })
      }
      var html = '<span>电话：</span>' + mapList[i].telephone + '<br>' + '<span>地址:</span>' + mapList[i].address + '<br>' + '<span>名称：<span>' + mapList[i].name
      var points = new BMap.Point(x, y);//创建坐标点
      var opts = {
          width: 300,
          height: 100,
          title: '管理员姓名:' + mapList[i].adminName
        }
        var infoWindows = new BMap.InfoWindow(html, opts);
        markerFun(points, infoWindows, icon);
      }
  });
  // mapList = [{
  //   coordinates: '121.396802,31.212527',
  //   siloNumber: '1',
  //   adminName: 'admin',
  //   address: 'xxx1',
  //   status: '0',
  //   },{
  //     coordinates: '121.417643,31.193748',
  //     siloNumber: '2',
  //     adminName: 'admin2',
  //     address: 'xxx2',
  //     status: '0',
  //   },{
  //     coordinates: '121.389113,31.195168',
  //     siloNumber: '3',
  //     adminName: 'admin3',
  //     address: 'xxx3',
  //     status: '0',
  //   },];
  },
}
</script>
<style scoped>
    html,body,.XSDFXPage{
        width: 50%;
        height: 50%;
        overflow: hidden;
        margin: 0px auto;
        font-family: "微软雅黑";
    }
</style>