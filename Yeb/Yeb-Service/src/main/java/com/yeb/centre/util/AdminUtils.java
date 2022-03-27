package com.yeb.centre.util;

import com.yeb.centre.pojo.Admin;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.core.context.SecurityContextHolder;
import springfox.documentation.spi.service.contexts.SecurityContext;

/**
 * @author dai
 * @create 2022-02-2022/2/3  15-10-28
 */
public class AdminUtils {
    //获取当前登录的用户信息
    public static Admin getCurrentAdmin(){
        return (Admin) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
