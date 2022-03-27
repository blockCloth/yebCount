package com.yeb.centre.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yeb.centre.pojo.Admin;
import com.yeb.centre.pojo.AdminLoginParam;
import com.yeb.centre.pojo.RespBean;
import com.yeb.centre.pojo.Role;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhoubin
 * @since 2022-01-21
 */
public interface IAdminService extends IService<Admin> {

    /**
     * 用户登录生成token
     * @param adminLoginParam
     * @param request
     * @return
     */
    RespBean login(AdminLoginParam adminLoginParam, HttpServletRequest request);

    /**
     * 根据用户名查询用户详细信息
     * @param username
     * @return
     */
    Admin getAdminByUsername(String username);

    /**
     * 根据用户id获取角色列表
     * @param adminId
     * @return
     */
    List<Role> getRoles(Integer adminId);

    /**
     * 获取所有操作员
     * @param workName
     * @return
     */
    List<Admin> getAllAdmin(String workName);

    /**
     * 修改用户密码
     * @param oldPass
     * @param newPass
     * @param adminId
     * @return
     */
    RespBean updateAdminPass(String oldPass, String newPass, String adminId);

    /**
     * 更新用户头像
     * @param url
     * @param id
     * @param authentication
     * @return
     */
    RespBean updateUserFace(String url, Integer id, Authentication authentication);
}
