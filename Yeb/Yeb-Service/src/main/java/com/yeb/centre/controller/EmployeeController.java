package com.yeb.centre.controller;


import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yeb.centre.pojo.*;
import com.yeb.centre.service.*;
import com.yeb.centre.util.DateTimeUtil;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author zhoubin
 * @since 2022-01-21
 */
@RestController
@RequestMapping("/system/basic/employee")
@Slf4j
public class EmployeeController {

    @Autowired
    private IEmployeeService employeeService;
    @Autowired
    private IPoliticsStatusService politicsStatusService;
    @Autowired
    private INationService nationService;
    @Autowired
    private IDepartmentService departmentService;
    @Autowired
    private IJoblevelService joblevelService;
    @Autowired
    private IPositionService positionService;


    @ApiOperation(value = "获取所有员工（分页）")
    @GetMapping("/")
    public PageRespBean getEmployee(
            @RequestParam(defaultValue = "1") Integer currentPage,
            @RequestParam(defaultValue = "10") Integer size,
            Employee employee,
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate[] beginDateScope) {
        return employeeService.getEmployee(currentPage, size, employee, beginDateScope);
    }

    @ApiOperation(value = "获取所有政治面貌")
    @GetMapping("/politicsStatus")
    public List<PoliticsStatus> getAllPoliticsStatus() {
        return politicsStatusService.list();
    }

    @ApiOperation(value = "获取所有民族")
    @GetMapping("/nation")
    public List<Nation> getAllNation() {
        return nationService.list();
    }

    @ApiOperation(value = "获取所有部门")
    @GetMapping("/department")
    public List<Department> getAllDepartment() {
        return departmentService.getAllDepartment();
    }

    @ApiOperation(value = "获取所有职称")
    @GetMapping("/joblevel")
    public List<Joblevel> getAllJoblevel() {
        return joblevelService.list();
    }

    @ApiOperation(value = "获取所有职位")
    @GetMapping("/position")
    public List<Position> getAllPosition() {
        return positionService.list();
    }

    @ApiOperation(value = "获取最大工号")
    @GetMapping("/maxWorkId")
    public RespBean maxWorkId() {
        return employeeService.maxWorkId();
    }

    @ApiOperation(value = "添加员工")
    @PostMapping("/")
    public RespBean addEmployee(@RequestBody Employee employee) {
        return employeeService.addEmployee(employee);
    }

    @ApiOperation(value = "更新员工信息")
    @PutMapping("/")
    public RespBean updateEmployee(@RequestBody Employee employee) {
        return employeeService.updateEmployee(employee);
    }

    @ApiOperation(value = "删除员工")
    @DeleteMapping("/{id}")
    public RespBean deleteEmployee(@PathVariable Integer id) {
        if (employeeService.removeById(id)) {
            return RespBean.success("删除成功！");
        }
        return RespBean.error("删除失败！");
    }

    @ApiOperation(value = "导出员工信息")
    @GetMapping(value = "/export", produces = "application/octet-stream")
    public void export(HttpServletResponse response) {
        //查询出所有员工的信息
        List<Employee> allEmployee = employeeService.getAllEmployee(null);
        //将查询出来的员工信息导出到Excel表
        /**
         * 创建Excel文件信息
         * 1、表名
         * 2、stream名
         * 3、导出版本格式
         */
        ExportParams exportParams = new ExportParams("员工表", "员工表", ExcelType.XSSF);
        //导出表
        Workbook sheets = ExcelExportUtil.exportExcel(exportParams, Employee.class, allEmployee);
        ServletOutputStream out = null;
        try {
            //设置响应格式
            response.setHeader("content-type", "application/octet-stream");
            //防止中文乱码
            response.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode("员工表.xls", "UTF-8"));
            out = response.getOutputStream();
            sheets.write(out);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @ApiOperation(value = "导入员工信息", consumes = "multipart/form-data")
    @PostMapping(value = "/import", headers = "content-type=multipart/form-data")
    public RespBean importEmployee(@RequestPart("file") MultipartFile file) {
        //获取开始时间
        long beginTime = new Date().getTime();

        ImportParams importParams = new ImportParams();
        //去除第一行标题行
        importParams.setTitleRows(1);
        try {
            //导入数据
            List<Employee> employeeList = ExcelImportUtil.importExcel(file.getInputStream(), Employee.class, importParams);

            employeeList.forEach(employee -> {
                //根据字段名获取到实体类id值,并且设置id值
                //设置民族id
                employee.setNationId(nationService.getOne(new QueryWrapper<Nation>()
                        .eq("name", employee.getNation().getName())).getId());
                //设置政治面貌id
                employee.setPoliticId(politicsStatusService.getOne(new QueryWrapper<PoliticsStatus>()
                        .eq("name", employee.getPoliticsStatus().getName())).getId());
                //设置部门id
                employee.setDepartmentId(departmentService.getOne(new QueryWrapper<Department>()
                        .eq("name", employee.getDepartment().getName())).getId());
                //设置职称id
                employee.setJobLevelId(joblevelService.getOne(new QueryWrapper<Joblevel>()
                        .eq("name", employee.getJoblevel().getName())).getId());
                //设置职位id
                employee.setPosId(positionService.getOne(new QueryWrapper<Position>()
                        .eq("name", employee.getPosition().getName())).getId());
            });
            //插入新数据
            if (employeeService.saveBatch(employeeList)) {
                //获取结束时间
                long endTime = new Date().getTime();
                System.out.println(endTime - beginTime);
                return RespBean.success("导入成功！");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return RespBean.error("导入失败！");
    }
}
