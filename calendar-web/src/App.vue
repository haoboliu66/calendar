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
  watch: {
    '$route': {
      handler(to, from) {
        setTimeout(() => {
          if (window._czc) {
            let location = window.location;
            let contentUrl = location.pathname + location.hash;
            let refererUrl = '/';

            window._czc.push(['_trackPageview', contentUrl, refererUrl])
          }
        }, 300)
      },
      immediate: true
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
