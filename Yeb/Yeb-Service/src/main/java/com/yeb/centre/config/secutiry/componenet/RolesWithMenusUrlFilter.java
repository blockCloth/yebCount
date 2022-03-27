package com.yeb.centre.config.secutiry.componenet;

import com.yeb.centre.mapper.MenuMapper;
import com.yeb.centre.pojo.Menu;
import com.yeb.centre.pojo.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import java.util.Collection;
import java.util.List;

/**
 * @author dai
 * @create 2022-01-2022/1/27  15-23-58
 */
@Component
public class RolesWithMenusUrlFilter implements FilterInvocationSecurityMetadataSource {
    @Autowired
    private MenuMapper menuMapper;

    AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        //获取到请求URL
        String requestUrl = ((FilterInvocation) object).getRequestUrl();
        //获取到角色列表
        List<Menu> menusWithRole = menuMapper.getMenusWithRole();
        //循环判断请求url跟菜单是否一致
        for (Menu menu : menusWithRole) {
            if (antPathMatcher.match(menu.getUrl(),requestUrl)){
                //如果正确，则放行
                String[] strings = menu.getRoles().stream().map(Role::getName).toArray(String[]::new);
                return SecurityConfig.createList(strings);
            }
        }
        //请求不正确，给予默认访问权限
        return SecurityConfig.createList("ROLE_LOGIN");
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return false;
    }
}
