<template>
  <div style="padding: 10px">
    <el-card>
      <div id="myChart">
      <el-button v-if="form.roles[0] === 6" @click="drawer = true" type="primary" style="margin-left: 16px;">
        点我进行实名认证
      </el-button></div>
      <el-form ref="form" :model="form" label-width="120px">
        <el-form-item style="text-align: center" label-width="0">
          <el-upload
              class="avatar-uploader"
              action="http://localhost:9090/files/upload"
              :show-file-list="false"
              :on-success="handleAvatarSuccess"
          >
            <img v-if="form.avatar" :src="form.avatar" class="avatar">
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
          </el-upload>
        </el-form-item>
        <el-form-item label="个人中心">
        </el-form-item>
        <el-form-item label="用户名">
          {{form.username}}
        </el-form-item>
        <el-form-item label="昵称">
          <el-input v-model="form.nickname"></el-input>
        </el-form-item>
        <el-form-item label="年龄">
          <el-input v-model="form.age"></el-input>
        </el-form-item>
        <el-form-item label="电话号码">
          <el-input v-model="form.telephone"></el-input>
        </el-form-item>
        <el-form-item label="性别">
          <el-input v-model="form.sex"></el-input>
        </el-form-item>
      </el-form>
      <div style="text-align: center">
        <el-button type="primary" @click="update">保存</el-button>
      </div>
    </el-card> 
  <div style="width: 100%;height:10px"></div>
    <el-card >
      <el-form ref="form" :model="form" label-width="120px" :rules="rules">
          <el-form-item label="修改密码">
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
          <el-button type="primary" @click="changePass">保存</el-button>
        </div>
    </el-card>
  </div>

 <el-drawer
  title="实名认证中心"
  :before-close="handleClose"
  v-model="drawer"
  direction="rtl"
  size="60%"
  custom-class="demo-drawer"
  ref="drawer"
  >
        <el-form ref="form" :model="Idform" label-width="120px" :rules="idrules">
          <el-form-item label="实名认证中心">
          </el-form-item>
          <el-form-item label="输入身份证号">
            <el-input v-model="Idform.idcode"></el-input>
          </el-form-item>
          <el-form-item label="输入真实姓名">
            <el-input v-model="Idform.truename"></el-input>
          </el-form-item>
        </el-form>
        <div style="text-align: center">
          <el-button type="primary" style="width: 80%; margin: 10px" @click="saveId">实名认证</el-button>
        </div>
      
  </el-drawer>

</template>

<script>
import request from "@/utils/request";

export default {
  name: 'Home',
  components: {},
  data() {
    return {
     drawer : false,
     form: {
       cpassword: '',
       newPass: '',
       confirmPass: ''
     },
     Idform: {
       truename: '',
       idcode: '',
       id: ''
     },
     rules: {
        cpassword: [
          {required: true, message: '请输入原密码', trigger: 'blur'},
        ],
        newPass: [
          {required: true, message: '密码要求由数字、大写字母和小写字母组成，且长度要在8-16位之间。', trigger: 'blur',  pattern: /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[^]{8,16}$/},
        ],
        confirmPass: [
          {required: true, message: '请输入确认新密码', trigger: 'blur'},
        ],
        telephone: [
          {required: true, message: '请输入正确手机号', trigger: 'blur', pattern: /^1([358][0-9]|4[579]|66|7[0135678]|9[89])[0-9]{8}$/},
        ], 
      },
      idrules: {
        idcode: [
          {required: true, message: '请输入身份证号', trigger: 'blur'},
        ],
        truename: [
          {required: true, message: '请输入真实姓名', trigger: 'blur'},
        ],
      },
    }
  },
  created() {
    let str = sessionStorage.getItem("user") || "{}"
    this.form = JSON.parse(str)
  },
  methods: {
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
              this.$message.success('修改个人成功，请重新登录！')
              this.$router.push("/login")
            } else {
              this.$message.error(res.msg)
            }
          })
        }
      })
    },
    handleAvatarSuccess(res) {
      this.form.avatar = res.data
      this.$message.success("上传成功")
      this.update()
    },
    update() {
      request.put("/user", this.form).then(res => {
        console.log(res)
        if (res.code === '0') {
          this.$message({
            type: "success",
            message: "更新成功"
          })
          sessionStorage.setItem("user", JSON.stringify(this.form))
          // 触发Layout更新用户信息
          this.$emit("userInfo")
        } else {
          this.$message({
            type: "error",
            message: res.msg
          })
        }
      })
    },
    saveId(){
      this.Idform.id = this.form.id;
      request.post("/user/id",this.Idform).then(res => {
            if (res.code === '0') {
              this.$message({
                type: "success",
                message: "实名认证成功,请重新登录"
              })
              this.$router.push("/login")
            } else {
              this.$message({
                type: "error",
                message: res.msg
              })
            }
        })
    }
  }
}
</script>

<style>
.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}
.avatar-uploader .el-upload:hover {
  border-color: #409EFF;
}
.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 120px;
  height: 120px;
  line-height: 120px;
  text-align: center;
}
.avatar {
  width: 178px;
  height: 178px;
  display: block;
}
</style>