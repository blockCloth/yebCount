package com.yeb.centre.config.secutiry.componenet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author dai
 * @create 2022-01-2022/1/17  15-24-49
 */
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    //Jwt存储的请求头
    @Value("${jwt.tokenHeader}")
    private String tokenHeader;
    //Jwt存储的token的开头
    @Value("${jwt.tokenHead}")
    private String tokenHead;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private UserDetailsService userDetailsService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //获取存储的请求头
        String authHeader = request.getHeader(this.tokenHeader);
        //判断token是否存在,判断token的开头是否为这个
        if (authHeader != null && authHeader.startsWith(this.tokenHead)){
            //获取token,将开头截取出来
            String authToken = authHeader.substring(tokenHead.length());
            //获取用户名
            String username = jwtTokenUtil.getUsernameFromToken(authToken);
            /**
             * 判断用户是否登录
             *  1、判断通过token获取到的用户名是否为空
             *  2、用户在登录成功的时候会更新Security全局作用域对象
             */
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null){
                //未登录就先登录
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                //再次验证token是否有效
                if (jwtTokenUtil.validateToken(authToken,userDetails)){
                    //有效的话，就再次更新Security
                    UsernamePasswordAuthenticationToken authenticationToken
                            = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    //再次设置全局作用域
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
        }
        filterChain.doFilter(request,response);
    }
}
