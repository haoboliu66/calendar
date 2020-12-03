<template>
  <div class="login" clearfix>
    <div class="login-wrap">
      <el-row type="flex" justify="center">
<!--         :rules="rules"-->
        <el-form ref="loginForm" :model="userForm" status-icon label-width="80px">
          <h3>Login</h3>
          <hr>
<!--       username   -->
          <el-form-item prop="username" label="username">
            <el-input v-model="userForm.username" placeholder="Please enter username" prefix-icon></el-input>
          </el-form-item>
<!--       password   -->
          <el-form-item id="password" prop="password" label="password">
            <el-input v-model="userForm.password" show-password placeholder="Please enter password"></el-input>
          </el-form-item>
<!--       forgot password and register  -->
          <router-link to="/">Forgot Password</router-link>
          <router-link style="float: right" to="/register">Register</router-link>

<!--       submit button   -->
          <el-form-item>
            <el-button type="primary" icon="el-icon-upload" @click="doLogin()">Submit</el-button>
          </el-form-item>
        </el-form>

      </el-row>
    </div>
  </div>
</template>

<script>
  import api from '../api';
  import md5 from 'md5'
  import axios from "axios";
  import store from '../store/store'

  export default {
    name: "login",
    data() {
      return {
        userForm: {
          username: "",
          password: ""
        }
      };
    },
    mounted() {
      //check session
      let token = store.getters.getToken;
      if(token){
        console.log("has logged in");
      }
    },
    created() {
    },
    methods: {
      // login method
      doLogin() {
        if (!this.userForm.username) {
          this.$message.error("Please enter username！");
          return;
        } else if (!this.userForm.password) {
          this.$message.error("Please enter password! ");
          return;
        } else {
          //validate username and password;

          const pwd = md5(this.userForm.password);
          let params = 'username=' + this.userForm.username + '&password=' +pwd;

          axios
            .post(api.Login, params)
            .then(res => {
              let data = res.data;
              if (data.code === 200) {
                // jump to event page if code == 200
                this.$store.commit('set_token', data.token);
                if (store.getters.getToken) {
                  this.$message.success("login success")
                  this.clearForm();

                  this.$router.push({ path: "/event" , query:data.result});

                } else {
                  this.$router.replace('/login');
                }

              } else {
                this.$message.error("incorrect username or password！");
              }
            });

        }
      },
      clearForm(){
        this.userForm.username = "",
          this.userForm.password = ""
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
    height: 300px;
    margin: 215px auto;
    overflow: hidden;
    padding-top: 10px;
    line-height: 40px;
  }
  #password {
    margin-bottom: 5px;
  }
  h3 {
    color: #0babeab8;
    font-size: 24px;
  }
  hr {
    background-color: #444;
    margin: 20px auto;
  }
  a {
    text-decoration: none;
    color: #aaa;
    font-size: 15px;
  }
  a:hover {
    color: coral;
  }
  .el-button {
    width: 80%;
    margin-left: 5px;
  }

</style>
