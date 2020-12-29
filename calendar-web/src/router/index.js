import Vue from 'vue'
import Router from 'vue-router'
import Login from '../components/Login'
import Register from "../components/Register"
import Event from "../components/Event";

Vue.use(Router)

const router = new Router({
  routes: [

    {
      path: '',
      redirect:'/login'
    },

    {
      path: '/event',
      name: 'event',
      component: Event,
      meta: {
        title: 'Event'
      }
    },
    {
      path: '/login',
      name: 'Login',
      component: Login,
      meta: {
        title: 'Login'
      }
    },
    {
      path: '/register',
      name: 'Register',
      component: Register,
      meta: {
        title: 'Register'
      }
    },


  ],
  // history mode instead of hash
  // mode: 'history'
})


router.beforeEach((to, from, next) => {
  if(to.path === '/event'){
    next();
    return;
  }

  if(to.path === '/register'){
    next();
    return;
  }

  if (to.path === '/login') {
    next();
  } else {
    let token = sessionStorage.getItem('token');

    if (token === null || token === '') {
      next('/login');
    } else {
      next();
    }
  }
});

export default router
