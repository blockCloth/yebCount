//import axios from "axios";
let proxyObj = {};

proxyObj['/'] = {
  //webscoket
  ws: false,
  //目标地址
  target: 'http://localhost:8081' ,
  //发送请求头中host会设置成target
  changeOrigin: true,
  //不重写请求地址
  pathRewrite: {
    '^/': '/'
  }
}

//实现跨域代理
module.exports = {
  devServer:{
    host: 'localhost',
    port: '8080',
    proxy: proxyObj
  }
}

/*const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true
})*/

