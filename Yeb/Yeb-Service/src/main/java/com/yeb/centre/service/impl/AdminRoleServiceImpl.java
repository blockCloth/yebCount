package com.yeb.centre.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yeb.centre.mapper.AdminRoleMapper;
import com.yeb.centre.pojo.Admin;
import com.yeb.centre.pojo.AdminRole;
import com.yeb.centre.pojo.RespBean;
import com.yeb.centre.service.IAdminRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zhoubin
 * @since 2022-01-21
 */
@Service
public class AdminRoleServiceImpl extends ServiceImpl<AdminRoleMapper, AdminRole> implements IAdminRoleService {
    @Autowired
    private AdminRoleMapper adminRoleMapper;

    /**
     * 修改操作员角色信息
     * @param adminId
     * @param rids
     * @return
     */
    @Override
    @Transactional
    public RespBean updateAdminRole(Integer adminId, Integer[] rids) {
        //先将操作员关联角色表的信息清空
        adminRoleMapper.delete(new QueryWrapper<AdminRole>().eq("adminId",adminId));
        //再次将记录插入进去
        Integer result = adminRoleMapper.addAdminRole(adminId, rids);
        //判断是否插入成功
        if (rids.length == result){
            return RespBean.success("修改成功！");
        }
        return RespBean.error("修改失败！");
    }
}
