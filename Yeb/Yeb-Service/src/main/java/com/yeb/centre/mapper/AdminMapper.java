package com.yeb.centre.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yeb.centre.pojo.Admin;
import com.yeb.centre.pojo.Role;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zhoubin
 * @since 2022-01-21
 */
public interface AdminMapper extends BaseMapper<Admin> {

    /**
     * 根据用户id获取角色列表
     * @param adminId
     * @return
     */
    List<Role> getRoles(Integer adminId);

    /**
     * 获取所有操作员
     * @param adminId
     * @param workName
     * @return
     */
    List<Admin> getAllAdmin(@Param("id") Integer id, @Param("workName") String workName);
}
