<template>

  <div id="app">
    <keep-alive>
      <router-view/>
    </keep-alive>

  </div>
</template>


<script>
export default {
  name: 'App',
  // mounted() {
  //    // 创建cnzz统计js
  //   const script = document.createElement('script')
  //   script.src = `https://s13.cnzz.com/z_stat.php?id=1271767266&web_id=1271767266`
  //   script.language = 'JavaScript'
  //   document.body.appendChild(script)
  // },
  watch: {
    '$route': {
      handler(to, from) {
        setTimeout(() => { //避免首次获取不到window._czc
          if (window._czc) {
            let location = window.location;
            let contentUrl = location.pathname + location.hash;
            let refererUrl = '/';
            // 用于发送某个URL的PV统计请求，
            window._czc.push(['_trackPageview', contentUrl, refererUrl])
          }
        }, 300)
      },
      immediate: true  // 首次进入页面即执行
    }
  }
}
</script>

<style>
*{
    margin: 0;
    padding: 0;
}
body {
    height: 100%;
    font: 14px/18px "Microsoft Yahei", Tahoma, Helvetica, Arial, Verdana, "\5b8b\4f53", sans-serif;
    color: #51555C;
}
a {
    color: #369/*#424242*/;
    text-decoration: none;
}
@media only screen and (min-width: 320px) and (max-width: 768px) {
    .el-dialog{
      width: 90%;
    }
}
</style>
