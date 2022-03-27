package com.yeb.centre.config.secutiry.componenet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yeb.centre.pojo.RespBean;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 当未登录或者token失效时访问接口，自定义的返回结果
 *
 * @author dai
 * @create 2022-01-2022/1/17  15-43-39
 */

public class RestfulAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        //设置输出字符集
        response.setCharacterEncoding("UTF-8");
        //设置响应格式
        response.setContentType("application/json");
        //获取输出流
        PrintWriter out = response.getWriter();
        //提示错误信息
        RespBean respBean = RespBean.error("用户登录失败，请重新登录！");
        //设置状态码
        respBean.setCode(403);
        //输出信息
        out.write(new ObjectMapper().writeValueAsString(respBean));
        //刷新，关闭
        out.flush();
        out.close();
    }
}
