package com.yeb.centre.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yeb.centre.pojo.Department;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zhoubin
 * @since 2022-01-21
 */
public interface DepartmentMapper extends BaseMapper<Department> {

    /**
     * 获取所有子部门
     * @return
     */
    List<Department> getAllDepartment(Integer parentId);

    /**
     * 添加部门
     * @param department
     */
    void addDepartment(Department department);

    /**
     * 删除部门
     * @param department
     */
    void deleteDep(Department department);
}
