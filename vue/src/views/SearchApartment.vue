<template>
  <div style="margin-top: 10px; margin-bottom: 80px">
    <el-card>
      <div style="padding: 20px; color: #888">
        <div>
          <el-input type="input" style="width: 80%" placeholder="请输入酒店名" clearable v-model="search"></el-input>
          <el-button type="primary" style="margin-left: 20px" @click="findall">查找</el-button>
        </div>
      </div>

      <div style="display: flex; padding: 20px" v-for="item in apartments" :key="item">
        <div style="text-align: center; flex: 1">
          <el-image :src="item.pic" style="width: 250px; height: 150px"></el-image>
        </div>
        <div style="padding: 0 10px; flex: 5">
          <div><b style="font-size: 20px">{{ item.name }}</b></div>
          <div style="padding: 10px 0; color: #888">
            酒店地址：{{ item.address}}
          </div>
          <div style="padding: 10px 0; color: #888">
            管理员：{{ item.adminName}}
          </div>
          <div style="color: #888">
            <span>联系电话：{{ item.telephone}}</span>
            <el-button type="text" style="margin-left: 20px" @click="LockAp(item.id)">查看详细信息</el-button>
          </div>
        </div>
      </div>

      <el-dialog title="所剩房间信息" v-model="dialogFormVisible" width="30%">
        <el-empty  v-if="roomData.length === 0" description="什么都没找到"></el-empty>
        <div style="display: flex; padding: 20px" v-for="item in roomData" :key="item">
        <div style="text-align: center; flex: 1">
          <el-image :src="item.pic" style="width: 250px; height: 150px"></el-image>
        </div>
        <div style="padding: 0 10px; flex: 5">
          <div><b style="font-size: 14px">房间号：{{ item.name }}</b></div>
          <div style="padding: 10px 0; color: #888">
            所属酒店：{{ item.apartmentName}}
          </div>
          <div style="background-color: #eee; padding: 10px" v-if="item.parentMessage">{{ item.username }}：{{ item.parentMessage.content }}</div>
          <div style="color: #888; font-size: 12px">
            <span>价格：{{ item.price}} 元/天</span>
          </div>
          <div style="padding: 10px 0px;color: #888; font-size: 12px">
            <span>面积：96平方米</span>
          </div>
          <div style="padding: 30px 0px;color: red; font-size: 15px">
            <span>请联系管理员订房(0352-1243)</span>
          </div>
        </div>
      </div>
        <template #footer>
          <el-button @click="cancel">退出</el-button>
        </template>
      </el-dialog>
    </el-card>


  </div>

  <div style="margin: 10px 0">
      <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="currentPage"
          :page-sizes="[5, 10, 20]"
          :page-size="pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total">
      </el-pagination>
    </div>
</template>

<script>
import request from "@/utils/request";

export default {
  name: "SearchApartment",
  data() {
    return {
      user: {},
      search:'',
      apartments: [],
      roomData: [],
      dialogFormVisible: false,
      isCollapse: false,
      currentPage: 1,
      pageSize: 5,
      total: 0,
    }
  },
  created() {
    this.user = sessionStorage.getItem("user") ? JSON.parse(sessionStorage.getItem("user")) : {};
    this.findall();
  },
  methods: {
    findall(){
        this.loading = true
        request.get("/apartment/all", {
            params: {
              pageNum: this.currentPage,
              pageSize: this.pageSize,
              search: this.search,
              adminId: 1                                                                                                                                                                                                                                                                                                                   
            }
          }).then(res => {
            this.apartments = res.data.records
            this.total = res.data.total
          })
    },
    findallroom(id){
        this.loading = true
            request.get("/room/all", {
                params: {
                search: this.search,
                apartmentId: id,
                hasPeople: '否',
                adminId: 1                                                                                                                                                                                                                                                                        
                }
            }).then(res => {
                this.roomData = res.data.records
                this.total = res.data.total
            })
    },
    LockAp(id){
        this.dialogFormVisible = true;
        this.findallroom(id);
    },
    cancel() {
      this.dialogFormVisible = false;
    },
    handleSizeChange(pageSize) {   // 改变当前每页的个数触发
        this.pageSize = pageSize
        this.findall();
      },
      handleCurrentChange(pageNum) {  // 改变当前页码触发
        this.currentPage = pageNum
        this.findall();
      }
  }
}
</script>
