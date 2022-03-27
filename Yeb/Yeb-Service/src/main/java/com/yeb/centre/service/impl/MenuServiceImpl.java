package com.yeb.centre.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yeb.centre.mapper.MenuMapper;
import com.yeb.centre.pojo.Admin;
import com.yeb.centre.pojo.Menu;
import com.yeb.centre.service.IMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zhoubin
 * @since 2022-01-21
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements IMenuService {
    @Autowired
    private MenuMapper menuMapper;
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 根据用户id获取菜单
     * @return
     */
    @Override
    public List<Menu> getMenusByAdminId() {
        //获取用户id
        Admin admin = (Admin) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        //判断是否将消息存入到了redis中
        ValueOperations valueOperations = redisTemplate.opsForValue();

        List<Menu> menus = (List<Menu>) valueOperations.get("menu_" + admin.getId());
        //判断是否为空
        if (CollectionUtils.isEmpty(menus)){
            //如果为空，就进行数据库查询
            menus = menuMapper.getMenusByAdminId(admin.getId());
            //将数据存入到redis中
            valueOperations.set("menu_" + admin.getId(),menus);
        }

        //将用户id填写进去
        return menus;
    }

    /**
     * 根据角色获取请求URL
     * @return
     */
    @Override
    public List<Menu> getMenusWithRole() {
        return menuMapper.getMenusWithRole();
    }

    /**
     * 查询所有菜单
     * @return
     */
    @Override
    public List<Menu> getAllMenus() {
        return menuMapper.getAllMenus();
    }
}
