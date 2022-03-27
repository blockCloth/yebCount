package com.yeb.centre.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yeb.centre.mapper.MenuRoleMapper;
import com.yeb.centre.pojo.MenuRole;
import com.yeb.centre.pojo.RespBean;
import com.yeb.centre.service.IMenuRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zhoubin
 * @since 2022-01-21
 */
@Service
public class MenuRoleServiceImpl extends ServiceImpl<MenuRoleMapper, MenuRole> implements IMenuRoleService {

    @Autowired
    private MenuRoleMapper menuRoleMapper;
    /**
     * 更新角色菜单信息
     * @param rid
     * @param mids
     * @return
     */
    @Override
    public RespBean updateMenuRole(Integer rid, Integer[] mids) {
        //根据用户id将菜单id清空删除完毕
        menuRoleMapper.delete(new QueryWrapper<MenuRole>().eq("rid",rid));
        //再次判断用户是否只需要删除菜单
        if (mids.length == 0 || mids == null){
            return RespBean.success("更新成功！");
        }
        //更新菜单信息
        Integer record = menuRoleMapper.insertRecord(rid, mids);
        if (record == mids.length){
            return RespBean.success("更新成功！");
        }
        return RespBean.error("更新失败！");
    }
}
