package com.yeb.centre.controller;

import com.yeb.centre.pojo.Admin;
import com.yeb.centre.pojo.AdminLoginParam;
import com.yeb.centre.pojo.RespBean;
import com.yeb.centre.service.IAdminService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.spi.service.contexts.SecurityContext;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

/**
 * @author dai
 * @create 2022-01-2022/1/17  13-16-07
 */
@RestController
@Api(value = "LoginController")
public class LoginController {
    //创建用户service
    @Autowired
    private IAdminService adminService;

    @ApiOperation(value = "登录之后返回token")
    @PostMapping("/login")
    public RespBean login(@RequestBody AdminLoginParam adminLoginParam, HttpServletRequest request){
        return adminService.login(adminLoginParam,request);
    }

    @ApiOperation(value = "用户退出")
    @GetMapping("/logout")
    public RespBean logout(){
        return RespBean.success("用户已退出！");
    }

    @ApiOperation(value = "根据用户名查询详细信息")
    @GetMapping("/queryAdmin")
    public Admin getAdminByUsername(Principal principal){
        if (principal != null){
            Admin admin = adminService.getAdminByUsername(principal.getName());
            //设置角色
            admin.setRoles(adminService.getRoles(admin.getId()));
            //设置密码
            admin.setPassword("******");
            return admin;
        }
        throw new UsernameNotFoundException("请输入正确的用户名！");
    }

}
