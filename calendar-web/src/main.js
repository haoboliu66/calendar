import Vue from 'vue'
import App from './App'
import router from './router'
import store from './store/store'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'//element-ui css
import locale from 'element-ui/lib/locale/lang/en'
Vue.use(ElementUI, { locale })  // use English as language


import axios from 'axios'
axios.defaults.baseURL = 'http://localhost:9999'
axios.defaults.headers['Content-Type'] = 'application/x-www-form-urlencoded;charset=UTF-8'
axios.defaults.withCredentials = true
axios.defaults.headers.common['token'] = store.getters.getToken

Vue.prototype.$axios = axios


Vue.config.productionTip = false

import {post,get} from './utils/http'

Vue.prototype.$post = post;
Vue.prototype.$get = get;




router.beforeEach((to, from, next) => {
    /* router different title */
    if (to.meta.title) {
        document.title = to.meta.title;
    }
    next();
})



/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  store,
  components: { App },
  template: '<App/>'
})


axios.interceptors.request.use(config => {
//判断是否存在token，如果存在将每个页面header都添加token
  console.log("interceptors!!")
  console.log(store.getters.getToken)
  if(store.getters.getToken){
    config.headers.common['token'] = store.getters.getToken;
    console.log("token here:");
    console.log(store.getters.getToken);
  }
  return config;
}, error => {
  return Promise.reject(error);
});


// axios.interceptors.response.use(
//   response => {
//
//     return response;
//   },
//   error => {
//
//     if (error.response) {
//       switch (error.response.status) {
//         case 401:
//           // remove token
//           this.$store.commit('del_token');
//           router.replace({
//             path: '/login',
//             query: {redirect: router.currentRoute.fullPath}//登录成功后跳入浏览的当前页面
//           })
//       }
//     }
//     return Promise.reject(error.response)
//   });
