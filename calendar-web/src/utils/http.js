import axios from 'axios';
import { Message } from 'element-ui';
import router from '../router';

axios.defaults.timeout = 5000;
axios.defaults.baseURL ='';
axios.defaults.headers.post['Content-Type'] = 'application/x-www-eventForm-urlencoded';

axios.defaults.withCredentials = true;//让ajax携带cookie

//http request 拦截器
// axios.interceptors.request.use(
//   config => {
//     let token = sessionStorage.getItem('token')
//     if (token) {  // 判断是否存在token，如果存在的话，则每个http header都加上token
//       config.headers = {
//         'X-token': token
//       }
//     }

//     return config
//   },
//   err => {
//     return Promise.reject(err)
//   }
// )


/**
 * 封装get方法
 * @param url
 * @param data
 * @returns {Promise}
 */

export function get(url, params={}){
  return new Promise((resolve, reject) => {
    axios.get(url,{
      params:params
    })
    .then(response => {
      resolve(response.data);
    })
    .catch(err => {
      reject(err)
    })
  })
}


/**
 * 封装post请求
 * @param url
 * @param data
 * @returns {Promise}
 */

 export function post(url, data = {}){
   return new Promise((resolve, reject) => {
      axios.post(url, data)
      .then(response => {
        resolve(response.data);
      }, err => {
        reject(err)
      })
   })
 }

 /**
 * 封装patch请求
 * @param url
 * @param data
 * @returns {Promise}
 */

export function patch(url, data = {}){
  return new Promise((resolve, reject) => {
    axios.patch(url, data)
     .then(response => {
       resolve(response.data);
     }, err => {
       reject(err)
     })
  })
}

 /**
 * 封装put请求
 * @param url
 * @param data
 * @returns {Promise}
 */

export function put(url, data = {}){
  return new Promise((resolve, reject) => {
    axios.put(url,data)
     .then(response => {
       resolve(response.data);
     }, err => {
       reject(err)
     })
  })
}
