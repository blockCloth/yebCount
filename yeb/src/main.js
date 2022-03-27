import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import 'font-awesome/css/font-awesome.min.css'

//引入ElementUI
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
//导入所有请求
import {postRequest} from "@/utils/app";
import {putRequest} from "@/utils/app";
import {getRequest} from "@/utils/app";
import {deleteRequest} from "@/utils/app";
import {initMenu} from "@/utils/menus";
import {downloadRequest} from "@/utils/download";

Vue.config.productionTip = false
// 安装ElementUI
Vue.use(ElementUI,{size:'small'})
//注册请求
Vue.prototype.postRequest = postRequest;
Vue.prototype.putRequest = putRequest;
Vue.prototype.getRequest = getRequest;
Vue.prototype.deleteRequest = deleteRequest;
Vue.prototype.downloadRequest = downloadRequest;

router.beforeEach(((to, from, next) => {
    //判断访问的是否为登录路径
    if (to.path != '/'){
      initMenu(router,store);
      //判断用户是否登录
      if (!window.sessionStorage.getItem('user')){
        //登录则发送请求
        return getRequest("/queryAdmin").then(resp => {
            //存入用户信息
            window.sessionStorage.setItem("user",JSON.stringify(resp));
            store.commit('INIT_ADMIN',resp);
            next();
        })
      }
      next();
    }else {
        //判断是否登录主页或者跳转别处路径
        if (to.path == '/'){
            next();
        }else {
            next("/?redirect" + to.path);
        }
    }

}))

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
