import axios from "axios";
import {Message} from "element-ui";
import router from "../router";

//设置请求拦截器
axios.interceptors.request.use(config => {
    //获取到tokenStr
    if (window.sessionStorage.getItem('tokenStr')){
        config.headers['Authorization'] = window.sessionStorage.getItem('tokenStr');
    }
    return config;
},error => {
    console.log(error)
})

//判断是否登录成功,设置响应拦截器
axios.interceptors.response.use(success =>{
    //先判断接口是否调用成功
    if (success.status && success.status == 200){
        //再次判断后端接口返回状态码
        if (success.data.code >= 400){
            //提示访问错误
            Message.error({message:success.data.message});
            return;
        }
        //再次判断是否有消息返回
        if (success.data.message){
            Message.success({message:success.data.message});
        }
    }
    //返回对象
    return success.data;
},error => {
    console.log(error.response.data.code)
    //访问错误，先判断是不是服务器宕机
    if (error.response.data.code == 404 || error.response.data.code == 504){
        Message.error({message:'服务器被吃掉了o(╯□╰)o'});
        return;
    }else if (error.response.data.code == 401){
        Message.error({message:'用户名错误或者账号被禁用'})
        //路由到登录界面
        router.replace("/");
    }else if (error.response.data.code == 403){
        Message.error({message:'权限不足，请联系管理员！'})
    }else {
        if (error.response.data.message){
            Message.error({message:error.response.data.message});
        }else {
            Message.error(({message:'未知错误，请联系管理员！'}))
        }
    }
    return;
})

let base = '';

//传送json格式的post请求
export const postRequest = (url, params) => {
    return axios({
        method: 'post',
        url: `${base}${url}`,
        data: params
    })
}

//传送json格式的put请求
export const putRequest = (url, params) => {
    return axios({
        method: 'put',
        url: `${base}${url}`,
        data: params
    })
}

//传送json格式的get请求
export const getRequest = (url, params) => {
    return axios({
        method: 'get',
        url: `${base}${url}`,
        data: params
    })
}

//传送json格式的delete请求
export const deleteRequest = (url, params) => {
    return axios({
        method: 'delete',
        url: `${base}${url}`,
        data: params
    })
}
