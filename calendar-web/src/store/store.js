import Vue from 'vue'
import Vuex from 'vuex'

// plug in Vuex
Vue.use(Vuex)

export default new Vuex.Store({
  state:{
    token:'',
    events: ''
  },
  mutations:{
    set_token(state, token) {
      state.token = token
      localStorage.setItem("token", token);
    },
    del_token(state) {
      state.token = ''
      localStorage.removeItem('token')
    },
    setEvents(state,data){
      state.events = data
      sessionStorage.setItem("events", JSON.stringify(data))
    },

  },
  getters:{
    getEvents(state){
      state.events = sessionStorage.getItem("events");
      return state.events;
    },
    getToken(state){
      if (!state.token) {
        state.token = localStorage.getItem('token')
      }
      return state.token;
    }

  }

})
