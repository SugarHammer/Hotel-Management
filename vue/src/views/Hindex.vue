<template>
  <div class="dashboard-container">
    <div class="dashboard-editor-container">
      <el-row style="background:#fff;padding:16px 16px 0;margin-bottom:32px;">
        <el-carousel :interval="4000" type="card" height="540px" style="width: 100%; margin: 10px">
          <el-carousel-item v-for="item in 6" :key="item">
          </el-carousel-item>
        </el-carousel>
      </el-row>
      <el-row style="background:#fff;padding:16px 16px 0;margin-bottom:32px;">
        <el-divider>  热  销  房  间  </el-divider>
      </el-row>
      <el-row style="background:#fff;padding:16px 16px 0;margin-bottom:32px;">
        <el-col :span="6" v-for="(o, index) in randomData" :key="o" :offset="index > 0 ? 0 : 0"
          style="background:#fff;padding:25px 25px 0;margin-bottom:50px;">
              <el-card :body-style="{ padding: '10px' }">
                <el-image
                  style="width: 200px; height: 150px"
                  :src="o.pic"
                  :preview-src-list="[o.pic]">
                </el-image>
                <div style="padding: 14px;">
                    <span>房间名：{{ o.name }}</span>
                    <br>
                    <span>房间价格：{{ o.price }} 元/天</span>
                    <br>
                    <span>所属酒店：{{ o.apartmentName }}</span>
                    <br>
                    <el-button size="mini" type="text" plain @click="Look(o)">查看相关信息</el-button>
                  </div>
              </el-card>
            </el-col>
      </el-row>
      <el-row style="background:#fff;padding:16px 16px 0;margin-bottom:32px;">
        <el-divider>  热  销  公  寓  </el-divider>
      </el-row>
      <el-row style="background:#fff;padding:16px 16px 0;margin-bottom:32px;">
        <el-col :span="6" v-for="(o, index) in randomApData" :key="o" :offset="index > 0 ? 0 : 0"
          style="background:#fff;padding:25px 25px 0;margin-bottom:50px;">
              <el-card :body-style="{ padding: '10px' }">
                <el-image
                  style="width: 200px; height: 150px"
                  :src="o.pic"
                  :preview-src-list="[o.pic]">
                </el-image>
                <div style="padding: 14px;">
                    <span>酒店名：{{ o.name }}</span>
                    <br>
                    <span>酒店地址：{{ o.address }}</span>
                    <br>
                    <span>管理员电话：{{ o.telephone }}</span>
                    <br>
                    <el-button size="mini" type="text" plain @click="LookAp(o)">查看相关信息</el-button>
                  </div>
              </el-card>
            </el-col>
      </el-row>
    </div>
  </div>

  <el-dialog title="房间信息" v-model="dialogVisible" width="30%">
      <el-form :model="roomform" label-width="120px">
        <el-form-item label="房间名">
          {{roomform.name}}
        </el-form-item>
        <el-form-item label="图片">
          <el-image
            style="width: 300px; height: 200px"
            :src="roomform.pic"
            :preview-src-list="[roomform.pic]">
          </el-image>
        </el-form-item>
        <el-form-item label="房间价格">
          {{roomform.price}}
        </el-form-item>
        <el-form-item label="所属酒店">
          {{roomform.apartmentName}}
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">退 出</el-button>
        </span>
      </template>
    </el-dialog>

    <el-dialog title="酒店信息" v-model="dialogVisibleAp" width="30%">
      <el-form :model="roomform" label-width="120px">
        <el-form-item label="酒店名">
          {{apartmentform.name}}
        </el-form-item>
        <el-form-item label="图片">
          <el-image
            style="width: 300px; height: 200px"
            :src="apartmentform.pic"
            :preview-src-list="[apartmentform.pic]">
          </el-image>
        </el-form-item>
        <el-form-item label="酒店地址">
          {{apartmentform.address}}
        </el-form-item>
        <el-form-item label="管理员">
          {{apartmentform.adminName}}
        </el-form-item>
        <el-form-item label="订购热线">
          {{apartmentform.telephone}}
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisibleAp = false">退 出</el-button>
        </span>
      </template>
    </el-dialog>

</template>

<script>
import request from "@/utils/request";

export default {
  name:'hindex',
  components: {},
  created(){
    this.findRoomRandom();
    this.findApartmentRandom();
  },
  data() {
    return {
      randomData:[],
      randomApData:[],
      roomform:'',
      apartmentform:'',
      dialogVisible: false,
      dialogVisibleAp: false,
    };
  },
  methods:{
    findRoomRandom(){
      request.get("/room/random").then(res => {
        this.loading = false
        this.randomData = res.data
      })
    },
    findApartmentRandom(){
      request.get("/apartment/random").then(res => {
        this.loading = false
        this.randomApData = res.data
        console.log(this.randomApData)
      })
    },
    Look(o){
      this.roomform = JSON.parse(JSON.stringify(o))
      this.dialogVisible = true
    },
    LookAp(o){
      this.apartmentform = JSON.parse(JSON.stringify(o))
      this.dialogVisibleAp = true
    }
  }
}
</script>

<style>

  .el-carousel__item:nth-child(2n) {
    background:url("../assets/bgbg.jpg");
    background-size: 100%;
  }
  
  .el-carousel__item:nth-child(2n+1) {
    background:url("../assets/bgbg.jpg");
    background-size: 100%;
  }
  .time {
    font-size: 13px;
    color: #999;
  }
  
  .bottom {
    margin-top: 13px;
    line-height: 12px;
  }

  .button {
    padding: 0;
    float: right;
  }

  .image {
    width: 100%;
    display: block;
  }

  .clearfix:before,
  .clearfix:after {
      display: table;
      content: "";
  }
  
  .clearfix:after {
      clear: both
  }
</style>
