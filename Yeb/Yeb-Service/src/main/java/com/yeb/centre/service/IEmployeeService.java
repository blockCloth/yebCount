package com.yeb.centre.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yeb.centre.pojo.Employee;
import com.yeb.centre.pojo.PageRespBean;
import com.yeb.centre.pojo.RespBean;

import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhoubin
 * @since 2022-01-21
 */
public interface IEmployeeService extends IService<Employee> {

    /**
     * 获取所有员工（分页）
     * @param currentPage 当前页数
     * @param size 总条数
     * @param employee 查询员工信息
     * @param beginDateSource 开始与结束时间
     * @return
     */
    PageRespBean getEmployee(Integer currentPage, Integer size, Employee employee, LocalDate[] beginDateSource);

    /**
     * 获取最大工号ID
     * @return
     */
    RespBean maxWorkId();

    /**
     * 添加员工
     * @param employee
     * @return
     */
    RespBean addEmployee(Employee employee);

    /**
     * 更新员工信息
     * @param employee
     * @return
     */
    RespBean updateEmployee(Employee employee);

    /**
     * 查询出所有员工信息
     */
    List<Employee> getAllEmployee(Integer eId);

    /**
     * 查询所有员工工资账套
     * @param current
     * @param page
     * @return
     */
    PageRespBean getEmployeeWithSalary(Integer currentPage, Integer size);
}
