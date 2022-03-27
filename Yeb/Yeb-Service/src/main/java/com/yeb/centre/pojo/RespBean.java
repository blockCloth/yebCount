package com.yeb.centre.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author dai
 * @create 2022-01-2022/1/17  12-46-42
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RespBean {
    //登录状态码
    private long code;
    //登录信息
    private String message;
    //返回对象
    private Object object;

    /**
     * 返回登录成功信息
     *
     * @param message
     * @return
     */
    public static RespBean success(String message) {
        return new RespBean(200, message, null);
    }

    /**
     * 返回登录成功信息
     *
     * @param message
     * @param object
     * @return
     */
    public static RespBean success(String message, Object object) {
        return new RespBean(200, message, object);
    }

    /**
     * 返回登录失败信息
     *
     * @param message
     * @return
     */
    public static RespBean error(String message) {
        return new RespBean(500, message, null);
    }

    /**
     * 返回登录失败信息
     *
     * @param message
     * @param object
     * @return
     */
    public static RespBean error(String message, Object object) {
        return new RespBean(500, message, object);
    }
}
