<template>
<div style="padding: 10px">
<!--    功能区域-->
    <div style="margin: 10px 0">
      <el-divider content-position="left">功能区域</el-divider>
      <el-button type="primary" @click="add">新增</el-button>
      <el-upload
        action="http://localhost:9090/room/import"
        :on-success="handleUploadSuccess"
        :show-file-list=false
        :limit="1"
        accept='.xlsx'
        style="display: inline-block; margin: 0 10px"
      >
        <el-button type="primary">导入</el-button>
      </el-upload>
      <el-button type="primary" @click="exportUser">导出</el-button>
    </div>

    <!--    搜索区域-->
    <div style="margin: 10px 0">
      <el-divider content-position="left">查询条件</el-divider>
      <el-row>
      <el-col :span="10">
        <span>房间名称：</span>
      <el-input v-model="search" placeholder="请输入房间号" style="width: 50%" clearable></el-input>
      </el-col>
      <el-col :span="10">
        <span>所属酒店：</span>
      <el-select v-model="searchApartmentId" placeholder="请选择所属酒店" style="width: 50%" clearable>
        <el-option v-for="item in this.apartments" :key="item.id" :label="item.name" :value="item.id"></el-option>
      </el-select>
      </el-col>
      <el-button type="primary" style="margin-left: 5px" @click="findall">查询<i class="el-icon-search el-icon--right"></i></el-button>
      </el-row>
      
    </div>

    <el-divider content-position="left">房间列表</el-divider>
    <el-tabs v-model="searchHasPeople" type="card" @tab-click="findall">
      <el-tab-pane label="已入住" name="是"></el-tab-pane>
      <el-tab-pane label="未入住" name="否"></el-tab-pane>
    </el-tabs>
    <el-table
      :data="tableData"
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
        prop="name"
        label="房间名称"
        width="100">
      </el-table-column>
      <el-table-column
        prop="hasPeople"
        label="是否有人入住"
        width="100">
      </el-table-column>
      <el-table-column
        label="酒店图片">
          <template #default="scope">
            <el-image
                style="width: 250px; height: 150px"
                :src="scope.row.pic"
                :preview-src-list="[scope.row.pic]">
            </el-image>
          </template>
      </el-table-column>
      <el-table-column
        prop="price"
        label="房间价格"
        width="100">
      </el-table-column>
      <el-table-column
        prop="apartmentName"
        label="所属酒店"
        width="150">
      </el-table-column>
      <el-table-column label="操作" width="400">
        <template #default="scope">
            <el-button size="mini" type="primary" plain @click="handlePass(scope.row)">清扫房间</el-button>
            <el-button size="mini" type="primary" plain @click="handleEdit(scope.row)">编辑</el-button>
            <el-popconfirm title="确定删除吗？" @confirm="handleDelete(scope.row.id)">
              <template #reference>
                <el-button size="mini" type="danger">删除</el-button>
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

    <el-dialog title="房间信息编辑" v-model="dialogVisible" width="30%">
      <el-form :model="form" label-width="120px">
      <el-form-item label="房间名">
        <el-input v-model="form.name" style="width: 80%"></el-input>
      </el-form-item>
      <el-form-item label="图片">
        <el-upload ref="upload" :action="filesUploadUrl" :on-success="filesUploadSuccess">
          <el-button type="primary">点击上传</el-button>
        </el-upload>
      </el-form-item>
      <el-form-item label="房间价格">
        <el-input v-model="form.price" style="width: 80%"></el-input>
      </el-form-item>
      <el-form-item label="所属酒店" v-if="userform.roles[0] === 1">
        <el-select v-model="form.apartmentId" placeholder="请选择" style="width: 80%">
          <el-option v-for="item in this.apartments" :key="item.id" :label="item.name" :value="item.id"></el-option>
        </el-select>
      </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取 消</el-button>
          <el-button type="primary" @click="save">确 定</el-button>
        </span>
      </template>
    </el-dialog>


  </div>
</template>



<script>
import request from "@/utils/request";

  export default {
    name: 'Room',
    components: {},
    data() {
      return {
        formPass: {
          cpassword: '',
          newPass: '123',
          confirmPass: '123',
          roomId: ''
        },
        loading: true,
        userform: {},
        form: {},
        searchHasPeople: '',
        search: '',
        searchApartmentId: '',
        currentPage: 1,
        pageSize: 5,
        total: 0,
        tableData: [],
        apartments: [],
        filesUploadUrl: "http://" + window.server.filesUploadUrl + ":9090/files/upload",
        dialogVisible : false   
      }
    },
    created() {
      let str = sessionStorage.getItem("user") || "{}"
      this.userform = JSON.parse(str)
      this.findall()
    },
    methods: {
      handlePass(row) {
        this.formPass.cpassword = row.password
        this.formPass.roomId = row.id
        console.log("mima =",row.password)
        request.put("/room/pass", this.formPass).then(res => {
            if (res.code === '0') {
              this.$message.success('清扫完成')
            } else {
              this.$message.error(res.msg)
            }
          })
      },
      handleEdit(row) {
        this.form = JSON.parse(JSON.stringify(row))
        this.dialogVisible = true
      },
      handleDelete(id) {
        request.delete("/room/" + id).then(res => {
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
      findall(){
        this.loading = true
          request.get("/room/all", {
            params: {
              pageNum: this.currentPage,
              pageSize: this.pageSize,
              search: this.search,
              adminId: this.userform.id,
              apartmentId: this.searchApartmentId,
              hasPeople: this.searchHasPeople                                                                                                                                                                                                                                                                          
            }
          }).then(res => {
            this.loading = false
            this.tableData = res.data.records
            this.total = res.data.total
          })
          if(this.userform.roles[0] === 1){
            request.get("/apartment/all",{params: {adminId: this.userform.id}}).then(res => {
              this.apartments = res.data.records
              
            })
          }
      },
      add() {
        this.dialogVisible = true
        this.form = {}
      },
      save() {
        if (this.form.id) {  // 更新
          request.put("/room", this.form).then(res => {
            if (res.code === '0') {
              this.$message({
                type: "success",
                message: "更新成功"
              })
            } else {
              this.$message({
                type: "error",
                message: res.msg
              })
            }
            this.findall() // 刷新表格的数据
            this.dialogVisible = false  // 关闭弹窗
          })
        } else {  // 新增
          request.post("/room", this.form).then(res => {
            if (res.code === '0') {
              this.$message({
                type: "success",
                message: "新增成功"
              })
            } else {
              this.$message({
                type: "error",
                message: res.msg
              })
            }
          this.findall() // 刷新表格的数据
          this.dialogVisible = false  // 关闭弹窗
        })
        }
      },
      exportUser() {
        location.href = "http://" + window.server.filesUploadUrl + ":9090/room/export";
      },
      filesUploadSuccess(res) {
        this.form.pic = res.data
      },
      handleSizeChange(pageSize) {   // 改变当前每页的个数触发
        this.pageSize = pageSize
        this.findall();
      },
      handleCurrentChange(pageNum) {  // 改变当前页码触发
        this.currentPage = pageNum
        this.findall();
      }
    },
  }
</script>