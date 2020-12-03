<template>
  <div class="login clearfix">
    <div class="login-wrap">
      <el-row type="flex" justify="center">
        <el-form ref="loginForm" :model="userForm" status-icon label-width="80px">
          <h3>Register</h3>
          <hr>
          <el-form-item prop="username" label="username">
            <el-input v-model="userForm.username" @blur="checkUsername" placeholder="Please enter username"></el-input>
          </el-form-item>
          <el-form-item prop="email" label="email">
            <el-input v-model="userForm.email" placeholder="Please enter email"></el-input>
          </el-form-item>
          <el-form-item prop="password" label="password">
            <el-input v-model="userForm.password" show-password placeholder="Please enter password"></el-input>
          </el-form-item>
          <el-form-item prop="confirm" label="confirm">
            <el-input v-model="userForm.confirm" show-password placeholder="Please confirm password"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" icon @click="doRegister()">Register</el-button>
          </el-form-item>
        </el-form>
      </el-row>
    </div>
  </div>
</template>

<script>
  import api from '../api';
  import md5 from 'md5';
  import axios from "axios";
  import QS from 'qs';

  export default {
    name: "login",
    data() {
      return {
        userForm: {
          username: "",
          email: "",
          password: "",
          confirm: ""
        },
      };
    },
    created() {
    },
    methods: {
      checkUsername() {
        let username = this.userForm.username;
        if (username === "") {
          return;
        }
        let params = {username: username};
        axios
          .post(api.ValidateUsername, QS.stringify(params))
          .then(res => {
            if (res.data.code !== 200)
              this.$message.error("Username already exists！");
          });

      },
      doRegister() {
        if (!this.userForm.username) {
          this.$message.error("username cannot be empty！");
          return;
        }
        if (!this.userForm.email) {
          this.$message.error("email cannot be empty！");
          return;

        }
        if (this.userForm.email != null) {
          let reg = /^(([a-zA-Z0-9_\-\.]+)@([a-zA-Z0-9_\-\.]+)\.([a-zA-Z]{2,5}){1,25})$/;
          if (!reg.test(this.userForm.email)) {
            this.$message.error("invalid email！");
            return;
          }
          if (!this.userForm.password) {
            this.$message.error("password cannot be empty！");
            return;
          }

          if (this.userForm.password != this.userForm.confirm) {
            this.$message.error("password not consistent");
            return;
          }

          // this.$router.push({ path: "/" }); //for vue test
          this.userForm.password = md5(this.userForm.password);
          axios
            .post(api.Register, QS.stringify(this.userForm))
            .then(res => {
              if (res.data.code === 200) {
                this.$message.success("register success")
                this.clearForm();
                this.$router.push({path: "/"});
              } else {
                this.$message.error("Username already exists！");
              }
            });

        }
      },
      clearForm() {
        this.userForm.username = "",
          this.userForm.email = "",
          this.userForm.password = "",
          this.userForm.confirm = ""
      }
    }
  };
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
  .login {
    width: 100%;
    height: 740px;
    /*background: url("../assets/images/login-bg.jpg") no-repeat;*/
    background-size: cover;
    overflow: hidden;
  }

  .login-wrap {
    /*background: url("../assets/images/login-bg.jpg") no-repeat;*/
    background-size: cover;
    width: 400px;
    height: 650px;
    margin: 215px auto;
    overflow: hidden;
    padding-top: 10px;
    line-height: 20px;
  }

  h3 {
    color: #0babeab8;
    font-size: 24px;
  }

  hr {
    background-color: #444;
    margin: 20px auto;
  }

  .el-button {
    width: 80%;
    margin-left: 5px;
  }
</style>
