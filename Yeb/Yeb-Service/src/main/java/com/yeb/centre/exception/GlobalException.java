package com.yeb.centre.exception;

import com.yeb.centre.pojo.RespBean;
import com.yeb.centre.service.IAdminService;
import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 定义全局异常捕捉类
 * @author dai
 * @create 2022-01-2022/1/28  14-59-17
 */
@RestControllerAdvice
@Log4j2
public class GlobalException {
    @Autowired
    private IAdminService adminService;

    /**
     * 捕获SQL异常
     * @param sqlException
     * @return
     */
    @ExceptionHandler(SQLException.class)
    public RespBean sqlException(SQLException sqlException){
        if (sqlException instanceof SQLIntegrityConstraintViolationException){
            log.error(sqlException.getMessage());
            return RespBean.error("数据异常，请勿再次访问！");
        }
        log.error(sqlException.getMessage());
        return RespBean.error("数据异常，请勿访问！");
    }

    /**
     * 捕获username异常
     */
    @ExceptionHandler(UsernameNotFoundException.class)
    public RespBean usernameNotFoundException(UsernameNotFoundException usernameException){
        //提示信息错误异常
       return RespBean.error(usernameException.getMessage());
    }

    /**
     * 捕获权限不足异常
     * @param accessDeniedException
     * @return
     */
    @ExceptionHandler(AccessDeniedException.class)
    public RespBean accessDeniedException(AccessDeniedException accessDeniedException){
        //提示权限不足
        return RespBean.error(accessDeniedException.getMessage());
    }
}
