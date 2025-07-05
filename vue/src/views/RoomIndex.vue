<template>
  <div style="padding: 10px">
    <el-card>
      <div id="myChart"></div>
      <el-form ref="roomform" :model="roomform" label-width="120px">
        <el-form-item style="text-align: center" label-width="0">
            <el-image
                style="width: 250px; height: 150px"
                :src="roomform.pic"
                :preview-src-list="[roomform.pic]">
            </el-image>
        </el-form-item>
        <el-form-item label="房间信息">
        </el-form-item>
        <el-form-item label="房间名">
          {{roomform.name}}
        </el-form-item>
        <el-form-item label="管理员">
          {{contractform.adminName}}
        </el-form-item>
        <el-form-item label="所在酒店名">
          {{roomform.apartmentName}}
        </el-form-item>
      </el-form>
      <el-form ref="contractform" :model="contractform" label-width="120px">
        <el-form-item label="租约到期日">
            {{contractform.terminTime}}
        </el-form-item>
        <el-form-item label="租约起始日">
            {{contractform.createTime}}
        </el-form-item>
        <el-form-item>
            <el-button type="primary" @click="exportContract">合约模板导出</el-button>
        </el-form-item>
      </el-form>
    </el-card> 
    
  <div style="width: 100%;height:10px"></div>
    <el-card >
      <el-form ref="form" :model="form" label-width="120px" :rules="rules">
          <el-form-item label="修改房间密码">
          </el-form-item>
          <el-form-item label="请输入原密码" prop="cpassword">
            <el-input v-model="form.cpassword" show-password></el-input> 
          </el-form-item>
          <el-form-item label="请输入新密码" prop="newPass">
            <el-input v-model="form.newPass" show-password></el-input>
          </el-form-item>
          <el-form-item label="确认新密码" prop="confirmPass">
            <el-input v-model="form.confirmPass" show-password></el-input>
          </el-form-item>
        </el-form>
        <div style="text-align: center">
          <el-button type="primary" @click="changePass">保   存</el-button>
        </div>
    </el-card>
  </div>
</template>

<script>
import request from "@/utils/request";

export default {
  name: 'RoomIndex',
  components: {},
  data() {
    return {
     roomform: {},
     contractform: {},
     form: {
       cpassword: '',
       newPass: '',
       confirmPass: ''
     },
     userform: {},
     rules: {
        cpassword: [
          {required: true, message: '请输入原密码', trigger: 'blur'},
        ],
        newPass: [
          {required: true, message: '请输入新密码', trigger: 'blur'},
        ],
        confirmPass: [
          {required: true, message: '请输入确认新密码', trigger: 'blur'},
        ],
      },
    }
  },
  created() {
    let str = sessionStorage.getItem("user") || "{}"
    this.userform = JSON.parse(str)
    this.findRoom()
  },
  methods: {
    findRoom(){
        request.post("/contract/findselfhaspic", this.userform
          ).then(res => {
            this.contractform = res.data[0]
            this.roomform = res.data[1]
          })
    },
    changePass() {
      this.$refs['form'].validate((valid) => {
        if (valid) {
          if (!this.form.newPass === this.form.confirmPass) {
            this.$message.error('2次输入新密码必须一致')
            return
          }
          let user = JSON.parse(sessionStorage.getItem("user"))
          this.form.userId = user.id
          request.put("/user/pass", this.form).then(res => {
            if (res.code === '0') {
              this.$message.success('修改成功')
            } else {
              this.$message.error(res.msg)
            }
          })
        }
      })
    },
    exportContract() {
        location.href = "http://" + window.server.filesUploadUrl + ":9090/contract/demo/export/"+this.contractform.conId;
      },
  }
}
</script>

<style>
.avatar {
  width: 178px;
  height: 178px;
  display: block;
}
</style>