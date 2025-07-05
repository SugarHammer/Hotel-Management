<template>
<div style="padding: 10px">

<!--   系统管理员界面-->
  <!--    功能区域-->
    <div style="margin: 10px 0" v-if="userform.roles[0] === 1">
      <el-divider content-position="left">功能区域</el-divider>
      <el-button type="primary" @click="add">新增</el-button>
      <el-upload
        action="http://localhost:9090/apartment/import"
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
    <div style="margin: 10px 0" v-if="userform.roles[0] === 1">
      <el-divider content-position="left">查询条件</el-divider>
      <span>酒店名：</span><el-input v-model="search" placeholder="请输入酒店名" style="width: 30%" clearable></el-input>
      <el-button type="primary" style="margin-left: 5px" @click="findall">查询<i class="el-icon-search el-icon--right"></i></el-button>
    </div>

    <el-divider content-position="left" v-if="userform.roles[0] === 1">酒店列表</el-divider>
    <el-table
    :data="tableData"
    border
    stripe
    style="width: 100%"
    v-if="userform.roles[0] === 1">
    <el-table-column
      fixed
      prop="id"
      label="ID"
      width="100"
      sortable>
    </el-table-column>
    <el-table-column
      prop="name"
      label="名称"
      width="200">
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
      prop="address"
      label="地址"
      width="200">
    </el-table-column>
    <el-table-column
      prop="adminName"
      label="管理员"
      width="100">
    </el-table-column>
   <el-table-column label="操作" width="400">
        <template #default="scope">
          <el-button size="mini" type="primary" plain @click="handleEdit(scope.row)">编辑</el-button>
          <el-popconfirm title="确定删除吗？" @confirm="handleDelete(scope.row.id)">
            <template #reference>
              <el-button size="mini" type="danger">删除</el-button>
            </template>
          </el-popconfirm>
        </template>
      </el-table-column>
  </el-table>

  <div style="margin: 10px 0" v-if="userform.roles[0] === 1">
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

  <!--   酒店管理员界面-->
  <el-card v-if="userform.roles[0] === 4">
    <el-form ref="apData" :model="apData" label-width="120px">
      <el-form-item label="酒店信息管理">
      </el-form-item>
      <el-form-item label="酒店名：  ">
        {{apData.name}}
      </el-form-item>
      <el-form-item label="酒店地址：  ">
        {{this.apData.address}}
      </el-form-item>
      <el-form-item label="酒店图片：  ">
        <el-image
              style="width: 200px; height: 200px"
              :src="apData.pic"
              :preview-src-list="[apData.pic]">
          </el-image>
      </el-form-item>
      <el-form-item label="酒店管理员：  ">
        {{apData.adminName}}
      </el-form-item>
    </el-form>
    <div style="text-align: center">
      <el-button size="mini" type="primary" plain @click="handleEdit(apData)">编辑</el-button>
    </div>
  </el-card>

  <el-dialog title="酒店信息编辑" v-model="dialogVisible" width="30%">
      <el-form :model="form" label-width="120px">
      <el-form-item label="坐标">
        {{this.coordinates}}
      </el-form-item>
      <el-form-item label="酒店名">
        <el-input v-model="form.name" style="width: 80%"></el-input>
      </el-form-item>
      <el-form-item label="酒店地址" v-if="userform.roles[0] === 1">
        <el-input v-model="form.address" style="width: 80%"></el-input>
      </el-form-item>
      <el-form-item label="酒店图片">
        <el-upload ref="upload" :action="filesUploadUrl" :on-success="filesUploadSuccess">
          <el-button type="primary">点击上传</el-button>
        </el-upload>
      </el-form-item>
      <el-form-item label="酒店管理员" v-if="userform.roles[0] === 1">
        <el-select v-model="form.adminId" placeholder="请选择" style="width: 80%">
          <el-option v-for="item in this.admins" :key="item" :label="item.truename" :value="item.id"></el-option>
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
    name: 'Apartment',
    components: {},
    data() {
      return {
        loading: true,
        userform: {},
        form: {
          coordinates:'',
        },
        coordinates: '',
        search: '',
        currentPage: 1,
        pageSize: 5,
        total: 0,
        tableData: [],
        apData: {},
        admins: [],
        filesUploadUrl: "http://" + window.server.filesUploadUrl + ":9090/files/upload",
        dialogVisible : false   
      }
    },
    created() {
      let str = sessionStorage.getItem("user") || "{}"
      this.userform = JSON.parse(str)
      var geolocation = new BMap.Geolocation();
      let _this = this;
      geolocation.getCurrentPosition(function(r,a){
        if(this.getStatus() == BMAP_STATUS_SUCCESS){
          _this.coordinates=r.point.lng+','+r.point.lat
          console.log(_this.coordinates)
        }
        else {
          alert('定位失败'+this.getStatus());
        }
      });
      console.log(this.coordinates)
      this.findall()
    },
    methods: {
      handleEdit(row) {
        this.form = JSON.parse(JSON.stringify(row))
        this.dialogVisible = true
      },
      handleDelete(id) {
        request.delete("/apartment/" + id).then(res => {
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
        request.get("/apartment/all", {
            params: {
              pageNum: this.currentPage,
              pageSize: this.pageSize,
              search: this.search,
              adminId: this.userform.id                                                                                                                                                                                                                                                                                                                   
            }
          }).then(res => {
            this.loading = false
            this.tableData = res.data.records
            this.apData = this.tableData[0]
            this.total = res.data.total
          })
          if(this.userform.roles[0] === 1){
            request.get("/user/admin").then(res => {
              this.admins = res.data
            })
          }
      },
      add() {
        this.dialogVisible = true
        this.form = {coordinates:'',}
      },
      save() {
        this.form.coordinates = this.coordinates
        console.log(this.form)
        if (this.form.id) {  // 更新
          request.put("/apartment", this.form).then(res => {
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
          request.post("/apartment", this.form).then(res => {
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
        location.href = "http://" + window.server.filesUploadUrl + ":9090/apartment/export";
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