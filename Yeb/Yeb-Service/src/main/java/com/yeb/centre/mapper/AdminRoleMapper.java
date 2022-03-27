package com.yeb.centre.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yeb.centre.pojo.AdminRole;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zhoubin
 * @since 2022-01-21
 */
public interface AdminRoleMapper extends BaseMapper<AdminRole> {

    /**
     * 修改操作员角色信息
     * @param adminId
     * @param rids
     */
    Integer addAdminRole(@Param("adminId") Integer adminId, @Param("rids") Integer[] rids);
}
