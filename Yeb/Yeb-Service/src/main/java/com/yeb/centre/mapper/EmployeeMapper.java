package com.yeb.centre.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yeb.centre.pojo.Employee;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zhoubin
 * @since 2022-01-21
 */
public interface EmployeeMapper extends BaseMapper<Employee> {

    /**
     * 获取所有员工（分页）
     * @param page
     * @param employee
     * @param beginDateSource
     */
    IPage<Employee> getEmployee(Page<Employee> page, @Param("employee") Employee employee, @Param("beginDateSource") LocalDate[] beginDateSource);

    /**
     * 查询出所有员工信息
     * @return
     * @param eid
     */
    List<Employee> getAllEmployee(@Param("id") Integer id);

    /**
     * 查询所有员工工资账套
     * @return
     */
    IPage<Employee> getEmployeeWithSalary(Page page);
}
