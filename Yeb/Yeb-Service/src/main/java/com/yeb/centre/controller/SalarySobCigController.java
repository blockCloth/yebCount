package com.yeb.centre.controller;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.yeb.centre.pojo.Employee;
import com.yeb.centre.pojo.PageRespBean;
import com.yeb.centre.pojo.RespBean;
import com.yeb.centre.pojo.Salary;
import com.yeb.centre.service.IEmployeeService;
import com.yeb.centre.service.ISalaryService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author dai
 * @create 2022-02-2022/2/12  14-47-09
 */
@RestController
@RequestMapping("/salary/sobcfg")
public class SalarySobCigController {
    @Autowired
    private ISalaryService salaryService;
    @Autowired
    private IEmployeeService employeeService;

    @ApiOperation(value = "获取所有工资账套")
    @GetMapping("/salaryes")
    public List<Salary> getAllSalary(){
        return salaryService.list();
    }

    @ApiOperation(value = "查询所有员工工资账套")
    @GetMapping("/")
    public PageRespBean getEmployeeWithSalary(@RequestParam(defaultValue = "1") Integer currentPage,
                                              @RequestParam(defaultValue = "10") Integer size){
        return employeeService.getEmployeeWithSalary(currentPage,size);
    }

    @ApiOperation(value = "修改员工工资账套")
    @PutMapping("/{eid},{sid}")
    public RespBean updateEmpSalary(@PathVariable Integer eid,
                                    @PathVariable Integer sid){
        System.out.println(eid);
        System.out.println(sid);
        if (employeeService.update(new UpdateWrapper<Employee>().set("salaryId",sid)
                .eq("id",eid))){
            return RespBean.success("修改成功！");
        }
        return RespBean.error("修改失败！");
    }
}
