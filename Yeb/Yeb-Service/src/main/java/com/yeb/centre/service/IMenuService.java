package com.yeb.centre.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yeb.centre.pojo.Menu;
import com.yeb.centre.pojo.Role;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhoubin
 * @since 2022-01-21
 */
public interface IMenuService extends IService<Menu> {

    /**
     * 根据用户id获取菜单
     * @return
     */
    List<Menu> getMenusByAdminId();

    /**
     * 根据角色获取请求URL
     * @return
     */
    List<Menu> getMenusWithRole();

    /**
     * 查询所有菜单
     * @return
     */
    List<Menu> getAllMenus();
}
