<template>
  <div style="padding: 10px">
    <!--    搜索区域-->
    <div style="margin: 10px 0">
      <el-divider content-position="left">查询条件</el-divider>
      <span>报修内容：</span><el-input v-model="search" placeholder="请输入报修内容" style="width: 30%" clearable></el-input>
      <el-button type="primary" style="margin-left: 5px" @click="findall">查询<i class="el-icon-search el-icon--right"></i></el-button>
      <el-button @click="drawer = true" type="primary" style="margin-left: 16px;">
        用户报修
      </el-button>
    </div>

    <el-divider content-position="left">报修列表</el-divider>
    <el-tabs v-model="searchState" type="card" @tab-click="findall">
      <el-tab-pane label="未分配维修员" name="0"></el-tab-pane>
      <el-tab-pane label="维修未完成" name="1"></el-tab-pane>
      <el-tab-pane label="维修已完成" name="2"></el-tab-pane>
    </el-tabs>
    <el-table
    :data="contract"
    border
    stripe
    style="width: 100%">
    <el-table-column
      fixed
      prop="id"
      label="ID"
      width="100"
      sortable>
    </el-table-column>
    <el-table-column
      prop="userName"
      label="报修人">
    </el-table-column>
    <el-table-column
      prop="text"
      label="报修内容">
    </el-table-column>
    <el-table-column
      prop="createTime"
      label="报修时间"
      width="150">
    </el-table-column>
    <el-table-column
      prop="finishTime"
      label="结束时间"
      width="150">
    </el-table-column>
    <el-table-column
      prop="adminName"
      label="管理员">
    </el-table-column>
    <el-table-column
      prop="apartmentName"
      label="酒店名">
    </el-table-column>
    <el-table-column
      prop="roomName"
      label="房间号">
    </el-table-column>
    <el-table-column
      prop="engineerName"
      label="维修员">
    </el-table-column>
    <el-table-column label="操作" width="240">
        <template #default="scope">
          <el-button v-if="scope.row.state === 0 && (userform.roles[0] === 1 || userform.roles[0] === 4)" size="mini" type="primary" plain @click="handleEdit(scope.row)">分配维修员</el-button>
          <el-popconfirm title="确定结束维修吗？"  v-if="scope.row.state != 2 && (userform.roles[0] === 1 || userform.roles[0] === 3)" @confirm="overEdit(scope.row)">
            <template #reference>
              <el-button size="mini" type="primary" plain>结束维修</el-button>
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
  title="用户报修中心"
  :before-close="handleClose"
  v-model="drawer"
  direction="rtl"
  size="60%"
  custom-class="demo-drawer"
  ref="drawer"
  >
      <el-card>
        <el-form ref="form" :model="form" label-width="120px">
          <el-form-item label="用户报修中心">
          </el-form-item>
          <el-form-item label="报修原因填写">
            <el-input
                type="textarea"
                :autosize="{ minRows: 2, maxRows: 4}"
                placeholder="请输入内容"
                v-model="form.text"></el-input>
          </el-form-item>
        </el-form>
        <div style="text-align: center">
          <el-button type="primary" style="width: 80%; margin: 10px" @click="saveEngineers">报  修</el-button>
        </div>
      </el-card>
  </el-drawer>

  <el-dialog title="报修" v-model="dialogVisible" width="30%">
      <el-form :model="engineerform" label-width="120px">
        <el-form-item label="选择维修人员">
            <el-select v-model="engineerform.engineerId" 
              placeholder="试试搜索：用户名"
              :options="options"
              filterable
              style="width: 90%; margin: 10px">
              <el-option v-for="item in this.engineers" :key="item.id" :label="item.nickname" :value="item.id"></el-option>
            </el-select>
          </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取 消</el-button>
          <el-button type="primary" @click="updateEdit">确 定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import request from "@/utils/request";

  export default {
    name: 'Engineer',
    components: {},
    data() {
      return {
        dialogVisible: false,
        loading: true,
        drawer: false,
        search: '',
        searchState: '0',
        form: {
          roomId: '',
          userId: '',
          text: '',
          engineerId: ''
        },

        userform: {},
        engineerform: {
          id: '',
          engineerId: '',
          state: '',
        },
        room:[],
        engineers: [],
        contract: [],
      }
    },
    watch: {
      'form.roomId':{
        handler(val){
          this.findRoom(val)
        },
      },
      deep: true,
      immediate: true
    },
    created() {
      let str = sessionStorage.getItem("user") || "{}"
      this.userform = JSON.parse(str)
      this.findall();
      this.findallengineer();
    },
    methods: {
      findall(){
        this.loading = true
          request.get("/engineer/all", {
            params: {
              pageNum: this.currentPage,
              pageSize: this.pageSize,
              search: this.search,
              state: this.searchState,
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
      findallengineer(){
        request.get("/engineer/allEngineer").then(res => {
             this.engineers = res.data
          })
      },
      handleEdit(row) {
        this.engineerform.id = row.id
        this.engineerform = JSON.parse(JSON.stringify(row))
        this.dialogVisible = true
      },
      updateEdit(){
        this.engineerform.state = '1'
        request.put("/engineer/update", this.engineerform).then(res => {
          if (res.code === '0') {
            this.$message.success("更新维修状态成功")
          }
          
        })
        dialogVisible = false
        this.findall()
      },
      overEdit(row){
        this.engineerform.id = row.id
        this.engineerform.state = '2'
        request.put("/engineer/update", this.engineerform).then(res => {
          if (res.code === '0') {
            this.$message.success("更新维修状态成功")
          }
        })
        this.findall()
      },
      onInput() {
        this.$forceUpdate();
      },
      saveEngineers(){
        this.form.userId = this.userform.id
        request.post("/engineer/add",this.form).then(res => {
            console.log(res)
            if (res.code === '0') {
              this.$message({
                type: "success",
                message: "报修成功"
              })
            } else {
              this.$message({
                type: "error",
                message: res.msg
              })
            }
        })
        this.drawer = false
        this.findall()
      }
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
