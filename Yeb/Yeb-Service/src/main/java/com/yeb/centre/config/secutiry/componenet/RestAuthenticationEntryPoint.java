package com.yeb.centre.config.secutiry.componenet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yeb.centre.pojo.RespBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author dai
 * @create 2022-01-2022/1/17  15-44-19
 */
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        //设置输出字符集
        response.setCharacterEncoding("UTF-8");
        //设置响应格式
        response.setContentType("application/json");
        //获取输出流
        PrintWriter out = response.getWriter();
        //提示错误信息
        RespBean respBean = RespBean.error("用户权限不足，请联系管理员！");
        //设置状态码
        respBean.setCode(401);
        //输出信息
        out.write(new ObjectMapper().writeValueAsString(respBean));
        //刷新，关闭
        out.flush();
        out.close();
    }
}
