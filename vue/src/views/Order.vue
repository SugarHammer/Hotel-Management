<template>
  <div style="padding: 10px">

    <!--    搜索区域-->
    <div style="margin: 10px 0">
      <el-divider content-position="left">查询条件</el-divider>
      <span>订单编号：</span><el-input v-model="search" placeholder="请输入订单编号" style="width: 30%" clearable></el-input>
      <el-button type="primary" style="margin-left: 5px" @click="load">查询<i class="el-icon-search el-icon--right"></i></el-button>
    </div>

    <el-divider content-position="left">订单列表</el-divider>
    <el-tabs v-model="searchState" type="card" @tab-click="load">
      <el-tab-pane label="未支付" name="0"></el-tab-pane>
      <el-tab-pane label="已支付" name="1"></el-tab-pane>
    </el-tabs>
    <el-table
        v-loading="loading"
        :data="tableData"
        border
        stripe
        style="width: 100%"
    >
      <el-table-column
          width="70"
          prop="id"
          label="ID"
          sortable
      >
      </el-table-column>
      <el-table-column
          width="150"
          prop="name"
          label="订单名称">
      </el-table-column>
      <el-table-column
          width="160"
          prop="orderNo"
          label="订单编号">
      </el-table-column>
      <el-table-column
          prop="totalPrice"
          label="总价">
      </el-table-column>
      <el-table-column
          prop="payPrice"
          label="实付款">
      </el-table-column>
      <el-table-column
          width="150"
          prop="createTime"
          label="创建时间">
      </el-table-column>
      <el-table-column
          width="150"
          prop="payTime"
          label="付款时间">
      </el-table-column>
      <el-table-column
          prop="username"
          label="购买人">
      </el-table-column>
      <el-table-column label="支付状态">
        <template #default="scope">
          <span v-if="scope.row.state === 0" style="color: orange">未支付</span>
          <span v-if="scope.row.state === 1" style="color: green">已支付</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="240">
        <template #default="scope">
          <el-button v-if="scope.row.state === 0 && (user.id === scope.row.userId)" size="mini" type="primary" plain @click="handleBuy(scope.row)">支付订单</el-button>
          <el-popconfirm title="确定删除吗？" v-if="user.roles[0] === 1" @confirm="handleDelete(scope.row.id)">
            <template #reference>
              <el-button size="mini" type="danger" >删除</el-button>
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
  </div>
</template>

<script>


import request from "@/utils/request";

export default {
  name: 'Order',
  components: {

  },
  data() {
    return {
      user: {},
      loading: true,
      form: {},
      dialogVisible: false,
      search: '',
      searchState: '0',
      currentPage: 1,
      pageSize: 10,
      total: 0,
      order: {},
      tableData: [],
    }
  },
  created() {
    this.user = sessionStorage.getItem("user") ? JSON.parse(sessionStorage.getItem("user")) : {};
    this.load()
  },
  methods: {
    load() {
      this.loading = true
      request.get("/order", {
        params: {
          pageNum: this.currentPage,
          pageSize: this.pageSize,
          search: this.search,
          state: this.searchState,
          userId: this.user.id
        }
      }).then(res => {
        this.loading = false
        this.tableData = res.data.records
        this.total = res.data.total
      })
    },
    handleBuy(row) {
      this.order = JSON.parse(JSON.stringify(row))
      console.log(this.order)
      request.post("/order/buy",this.order).then(res => {
        // 请求成功跳转沙箱支付的页面
        window.open(res.data)
      })
    },
    add() {
      this.dialogVisible = true
      this.form = {}
      if (this.$refs['upload']) {
        this.$refs['upload'].clearFiles()  // 清除历史文件列表
      }
    },
    save() {
      if (this.form.id) {  // 更新
        request.put("/order", this.form).then(res => {
          console.log(res)
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
          this.load() // 刷新表格的数据
          this.dialogVisible = false  // 关闭弹窗
        })
      }  else {  // 新增
        request.post("/order", this.form).then(res => {
          console.log(res)
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

          this.load() // 刷新表格的数据
          this.dialogVisible = false  // 关闭弹窗
        })
      }
    },
    handleEdit(row) {
      this.form = JSON.parse(JSON.stringify(row))
      this.dialogVisible = true
      this.$nextTick(() => {
        if (this.$refs['upload']) {
          this.$refs['upload'].clearFiles()  // 清除历史文件列表
        }
      })

    },
    handleDelete(id) {
      console.log(id)
      request.delete("/order/" + id).then(res => {
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
        this.load()  // 删除之后重新加载表格的数据
      })
    },
    handleSizeChange(pageSize) {   // 改变当前每页的个数触发
      this.pageSize = pageSize
      this.load()
    },
    handleCurrentChange(pageNum) {  // 改变当前页码触发
      this.currentPage = pageNum
      this.load()
    }
  }
}
</script>
