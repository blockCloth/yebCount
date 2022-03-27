package com.yeb.centre.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yeb.centre.pojo.AdminRole;
import com.yeb.centre.pojo.RespBean;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhoubin
 * @since 2022-01-21
 */
public interface IAdminRoleService extends IService<AdminRole> {

    /**
     * 修改操作员角色信息
     * @param adminId
     * @param rids
     * @return
     */
    RespBean updateAdminRole(Integer adminId, Integer[] rids);
}
