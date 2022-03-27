package com.yeb.centre.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yeb.centre.pojo.Menu;
import com.yeb.centre.pojo.Role;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zhoubin
 * @since 2022-01-21
 */
public interface MenuMapper extends BaseMapper<Menu> {

    /**
     * 根据用户id获取菜单
     * @return
     * @param id
     */
    List<Menu> getMenusByAdminId(Integer id);

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
