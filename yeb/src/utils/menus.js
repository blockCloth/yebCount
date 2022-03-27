import {getRequest} from "@/utils/app";

//导出初始化菜单
export const initMenu = (router,store) =>{
    //判断状态是否成功
    if (store.state.routes.length > 0){
        return;
    }
    //发送请求
    getRequest("/system/sys/menu").then(data =>{
        //判断数据是否为空
        if (data){
            //格式化route
            let fmtRoutes = formatRoutes(data);
            //添加到route中
            router.addRoutes(fmtRoutes);
            //将数据存入到vuex中
            store.commit('initRoutes',fmtRoutes)
        }
    })
}

export const formatRoutes = (data) => {
    //定义form数据
    let fmRoutes = [];
    //循环数据
    data.forEach(data => {
        //定义菜单属性
        let{
            path,
            component,
            name,
            meta,
            iconCls,
            children
        } = data;
        //再次判断子类是否属于数组
        if (children && children instanceof Array){
            children = formatRoutes(children);
        }
        let fmRouter = {
            path: path,
            name: name,
            meta: meta,
            iconCls: iconCls,
            children: children,
            component(resolve) {
                //获取到路由路径
                if (component.startsWith('Home')){
                    require(['../views/' + component + '.vue'], resolve);
                }else {
                    require(['../views/' + component.substr(0,3).toLowerCase() + '/' + component + '.vue'], resolve);
                }
            }
        }
        fmRoutes.push(fmRouter);
    })
    return fmRoutes;
}
