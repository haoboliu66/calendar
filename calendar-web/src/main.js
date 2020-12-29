import Vue from 'vue'
import App from './App'
import router from './router'
import store from './store/store'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'//element-ui css
import locale from 'element-ui/lib/locale/lang/en'
Vue.use(ElementUI, { locale })  // use English as language

import axios from 'axios'
// axios.defaults.baseURL = 'http://localhost:9999'
axios.defaults.baseURL = 'https://cloud-calendar.herokuapp.com'
axios.defaults.headers['Content-Type'] = 'application/x-www-form-urlencoded;charset=UTF-8'
axios.defaults.withCredentials = true

axios.defaults.headers.common['token'] = store.getters.getToken

Vue.prototype.$axios = axios

Vue.config.productionTip = false

import {post,get} from './utils/http'
import api from "./api";

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

//add token to header if token exists
  if(store.getters.getToken){
    config.headers.common['token'] = store.getters.getToken;
  }
  return config;
}, error => {
  console.log("error? ");
  return Promise.reject(error);
});


// request retry
axios.defaults.timeout = 15000;
axios.defaults.retry = 4;
axios.defaults.retryDelay = 1000;
axios.interceptors.response.use(undefined, function axiosRetryInterceptor(err) {
  let config = err.config;
  // If config does not exist or the retry option is not set, reject
  if(!config || !config.retry) return Promise.reject(err);

  // Set the variable for keeping track of the retry count
  config.__retryCount = config.__retryCount || 0;

  // Check if we've maxed out the total number of retries
  if(config.__retryCount >= config.retry) {
    // Reject with the error
    return Promise.reject(err);
  }

  // Increase the retry count
  config.__retryCount += 1;

  // Create new promise to handle exponential backoff
  let backoff = new Promise(function (resolve) {
    setTimeout(function () {
      resolve();
    }, config.retryDelay || 1);
  });
  // Return the promise in which recalls axios to retry the request
  return backoff.then(function() {
    return axios(config);
  });
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
//             query: {redirect: router.currentRoute.fullPath}
//           })
//       }
//     }
//     return Promise.reject(error.response)
//   });
