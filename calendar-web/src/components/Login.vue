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
            <el-button type="primary" icon="el-icon-check" @click="doLogin()">Submit</el-button>
          </el-form-item>

          <!--     gmail logo   -->
          <div>
            <el-button v-google-signin-button="clientId" class="google-signin-button">
              <el-image class="gmail_img" :src="img_src" :fit="fit" :alt="alt"></el-image>
              Sign in with Google
            </el-button>
          </div>

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

  // import GoogleLogin from 'vue-google-login';

  import GoogleSignInButton from 'vue-google-signin-button-directive'
  import jsonwebtoken from 'jsonwebtoken'
  import gmail_url from "../assets/gmail.jpg"

  //Client ID: 995665339175-kvr7eb3slh81dbr9vlkh9rshu4p2el12.apps.googleusercontent.com
  //Client secret: TmKrwOhd_s_VztFz3Age4Rs1
  export default {
    directives: {
      GoogleSignInButton
    },
    name: "login",
    data() {
      return {
        clientId: "995665339175-kvr7eb3slh81dbr9vlkh9rshu4p2el12.apps.googleusercontent.com",
        img_src: gmail_url,
        fit: 'cover',
        alt: "Sign in with Google",

        userForm: {
          username: "",
          password: ""
        }
      };
    },
    mounted() {
      //check session
      let token = store.getters.getToken;
      if (token) {
        console.log("has logged in");
      }
    },
    created() {

    },
    methods: {
      /**
       * Gmail sign-in
       */
      OnGoogleAuthSuccess(idToken) {
        let token = jsonwebtoken.decode(idToken);

        let verified = token["email_verified"];
        if (!verified) {
          this.$message.error({message: "Google Verification Failed", duration: 2000});
        }

        let clientId = token["aud"];
        let email = token["email"];
        let jti = token["jti"];

        let params = {clientId: clientId, email: email, jti: jti};

        axios
          .post(api.OAuth, JSON.stringify(params), {
            headers: {
              'content-type': 'application/json'
            }
          })
          .then(res => {
            let data = res.data;
            if (data.code === 200) {
              // jump to event page if code == 200
              this.$store.commit('set_token', data.token);

              if (store.getters.getToken) {
                this.$message.success({message: "login success", duration: 2000})
                this.$router.push({path: "/event", query: data.result});
              } else {
                this.$router.replace('/login');
              }
            }
          })

      },
      OnGoogleAuthFail(error) {
        console.log(error)
      },
      /**
       * login method
       */
      doLogin() {
        if (!this.userForm.username) {
          this.$message.error(
            {
              message: "Please enter username！",
              duration: 2000
            });
          return;
        } else if (!this.userForm.password) {
          this.$message.error(
            {
              message: "Please enter password! ",
              duration: 2000
            }
          );
          return;
        } else {
          //validate username and password;

          const pwd = md5(this.userForm.password);
          let params = 'username=' + this.userForm.username + '&password=' + pwd;

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
                  this.$router.push({path: "/event", query: data.result});

                } else {
                  this.$router.replace('/login');
                }

              } else {
                this.$message.error(
                  {
                    message: "incorrect username or password！",
                    duration: 2000
                  }
                );
              }
            });

        }
      },
      clearForm() {
        this.userForm.username = "",
          this.userForm.password = ""
      }
    }
  };
</script>


<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>

  .el-button {
    width: 80%;
    margin-left: 5px;
  }

  .gmail_img {
    margin-left: -20px;
    margin-top: -12px;
    width: 38px;
    height: 38px;
    float: left;
  }

  .google-signin-button {
    margin-left: 40px;
    alignment: center;
    align-self: center;
    color: white;
    background-color: #409EFF;
    height: 40px;
    width: 232px;
    /*font-size: 16px;*/
    /*border-radius: 10px;*/
    /*padding: 10px 20px 25px 20px;*/
    box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2), 0 6px 20px 0 rgba(0, 0, 0, 0.19);
  }


  .login {
    width: 100%;
    height: 740px;
    background: url("../assets/images/ucd-bg.jpg") no-repeat;
    background-size: cover;
    overflow: hidden;
  }

  /*login form part*/
  .login-wrap {
    /*background: url("../assets/images/ucd-bg.jpg") no-repeat;*/
    background-size: cover;
    width: 400px;
    height: 600px;
    margin: 215px auto;
    overflow: hidden;
    padding-top: 10px;
    line-height: 40px;
  }

  #password {
    margin-bottom: 5px;
  }

  h3 {
    color: white;
    font-size: 24px;
  }

  hr {
    background-color: #444;
    margin: 20px auto;
  }

  a {
    text-decoration: none;
    color: white;
    font-size: 15px;
  }

  a:hover {
    color: coral;
  }


</style>
