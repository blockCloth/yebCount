package com.yeb.centre.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yeb.centre.pojo.MenuRole;
import com.yeb.centre.pojo.RespBean;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhoubin
 * @since 2022-01-21
 */
public interface IMenuRoleService extends IService<MenuRole> {

    /**
     * 更新角色菜单信息
     * @param rid
     * @param mids
     * @return
     */
    RespBean updateMenuRole(Integer rid, Integer[] mids);
}
