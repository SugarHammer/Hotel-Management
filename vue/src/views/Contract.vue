<template>
  <div style="padding: 10px">
    <!--    搜索区域-->
    <div style="margin: 10px 0">
      <el-divider content-position="left">查询条件</el-divider>
      <span>下单客户用户名：</span><el-input v-model="search" placeholder="请输入下单客户用户名" style="width: 30%" clearable></el-input>
      <el-button type="primary" style="margin-left: 5px" @click="findall">查询<i class="el-icon-search el-icon--right"></i></el-button>
      <el-button @click="drawer = true" type="primary" style="margin-left: 16px;">
        用户下单
      </el-button>
    </div>

    <el-divider content-position="left">合约列表</el-divider>
    <el-table
    :data="contract"
    border
    stripe
    style="width: 100%">
    <el-table-column
      fixed
      prop="conId"
      label="ID"
      width="100"
      sortable>
    </el-table-column>
    <el-table-column
      prop="userName"
      label="下单用户"
      width="100">
    </el-table-column>
    <el-table-column
      prop="createTime"
      label="下单时间"
      width="200">
    </el-table-column>
    <el-table-column
      prop="terminTime"
      label="解约时间"
      width="200">
    </el-table-column>
    <el-table-column
      prop="adminName"
      label="下单方"
      width="100">
    </el-table-column>
    <el-table-column
      prop="apartmentName"
      label="下单酒店"
      width="200">
    </el-table-column>
    <el-table-column
      prop="roomName"
      label="下单房间">
    </el-table-column>
    <el-table-column label="操作" width="400">
        <template #default="scope">
          <el-popconfirm title="确定解约吗？" @confirm="handleDelete(scope.row.conId)">
            <template #reference>
              <el-button size="mini" type="danger">解约</el-button>
            </template>
          </el-popconfirm>
        </template>
      </el-table-column>
  </el-table>

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

  <el-drawer
  title="用户下单中心"
  :before-close="handleClose"
  v-model="drawer"
  direction="rtl"
  size="60%"
  custom-class="demo-drawer"
  ref="drawer"
  >
      <el-card>
        <el-form ref="form" :model="form" label-width="120px" :rules="rules">
          <el-form-item label="下单中心">
          </el-form-item>
          <el-form-item label="选择下单用户">
            <el-select v-model="form.userId" 
              placeholder="试试搜索：用户名"
              :options="options"
              filterable
              style="width: 90%; margin: 10px">
              <el-option v-for="item in this.users" :key="item.id" :label="item.truename" :value="item.id"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="选择房间">
            <el-select v-model="form.roomId" 
              placeholder="试试搜索：房间号"
              :options="options"
              filterable
              style="width: 90%; margin: 10px">
              <el-option v-for="item in this.rooms" :key="item.id" :label="item.name" :value="item.id"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="选择租约时间:">
            <el-date-picker style="width: 90%; margin: 10px"
              v-model="form.time"
              type="datetimerange"
              value-format="YYYY-MM-DD HH:mm:ss"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              align="right">
            </el-date-picker>
          </el-form-item>
        </el-form>
        <div style="text-align: center">
          <el-button type="primary" style="width: 80%; margin: 10px" @click="saveContract">签   约</el-button>
        </div>
      </el-card>
    <div style="width: 100%;height:10px"></div>
      <el-card>
        <el-image
          style="width: 100px; height: 100px"
          :src="this.room.pic"
          :preview-src-list="[this.room.pic]">
          >
        </el-image>
          <div style="padding: 14px;">
            <span>房间名：{{ this.room.name }}</span>
            <br>
            <span>房间价格：{{ this.room.price }} 元/天</span>
            <br>
            <span>所属酒店：{{ this.room.apartmentName }}</span>
            <br>
          </div>
      </el-card>
      <div style="width: 100%;height:10px"></div>
      <el-card>
        <el-image
          style="width: 60px; height: 60px; border-radius: 50%"
          :src="this.user.avatar"
          :preview-src-list="[this.user.avatar]">
          >
        </el-image>
          <div style="padding: 14px;">
            <span>下单人姓名：{{ this.user.truename }}</span>
            <br>
            <span>下单人身份证号：{{ this.user.idcode }}</span>
            <br>
            <span>下单人电话：{{ this.user.telephone }}</span>
            <br>
          </div>
      </el-card>
  </el-drawer>
  </div>
</template>

<script>
import request from "@/utils/request";

  export default {
    name: 'Contract',
    components: {},
    data() {
      return {
        loading: true,
        drawer: false,
        form: {
          roomId: '',
          userId: '',
          createTime: '',
          terminTime: '',
          apartmentId: ''
        },
        search: '',
        userform: {},
        user:{},
        room:{},
        users: [],
        rooms: [],
        contract: [],
        rules: {
          userId: [
            {required: true, message: '请选择入住人', trigger: 'blur'},
          ],
          roomId: [
            {required: true, message: '请选择房间', trigger: 'blur'},
          ],
          time: [
            {required: true, message: '请选择租约时长', trigger: 'blur'},
          ], 
        },
      }
    },
    watch: {
      'form.roomId':{
        handler(val){
          this.findRoom(val)
        },
      },
      'form.userId':{
        handler(val){
          this.findUser(val)
        },
      },
      deep: true,
      immediate: true
    },
    created() {
      let str = sessionStorage.getItem("user") || "{}"
      this.userform = JSON.parse(str)
      this.findall();
      this.findalluser();
      this.findallroom();
    },
    methods: {
      findall(){
        this.loading = true
          request.get("/contract/all", {
            params: {
              pageNum: this.currentPage,
              pageSize: this.pageSize,
              search: this.search,
              adminId: this.userform.id                                                                                                                                                                                                                                                                                                    
            }
          }).then(res => {
            this.loading = false
            this.contract = res.data.records
            this.total = res.data.total
          })
      },
      findRoom(id){
        console.log(id)
        request.get("/room/"+id).then(res => {
             this.room = res.data
          })
      },
      findUser(id){
        console.log(id)
        request.get("/user/"+id).then(res => {
             this.user = res.data
          })
      },
      findalluser(){
        request.get("/contract/alluser").then(res => {
             this.users = res.data
          })
      },
      findallroom(){
        request.get("/contract/allroom/"+this.userform.id).then(res => {
             this.rooms = res.data
          })
      },
      saveContract(){
        this.form.createTime = this.form.time[0]
        this.form.terminTime = this.form.time[1]
        this.form.adminId = this.userform.id
        request.post("/contract/add",this.form).then(res => {
            if (res.code === '0') {
              this.$message({
                type: "success",
                message: "下单成功"
              })
              window.open(res.data)
            } else {
              this.$message({
                type: "error",
                message: res.msg
              })
            }
        })
      },
      handleDelete(id) {
        request.delete("/contract/" + id).then(res => {
          if (res.code === '0') {
            this.$message({
              type: "success",
              message: "删除成功"
            })
          } else {
            this.$message({
              type: "error",
              message: res.msg
            })
          }
          this.findall()  // 删除之后重新加载表格的数据
        })
      },
    }
  }
</script>

<style>
  .el-carousel__item:nth-child(2n) {
    background-color: #99a9bf;
  }
  
  .el-carousel__item:nth-child(2n+1) {
    background-color: #d3dce6;
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
