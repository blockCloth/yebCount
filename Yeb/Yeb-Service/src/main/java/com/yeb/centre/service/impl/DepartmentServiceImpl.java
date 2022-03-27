package com.yeb.centre.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yeb.centre.mapper.DepartmentMapper;
import com.yeb.centre.pojo.Department;
import com.yeb.centre.pojo.RespBean;
import com.yeb.centre.service.IDepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zhoubin
 * @since 2022-01-21
 */
@Service
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, Department> implements IDepartmentService {
    @Autowired
    private DepartmentMapper departmentMapper;

    /**
     * 获取所有子部门
     * @return
     */
    @Override
    public List<Department> getAllDepartment() {
        return departmentMapper.getAllDepartment(-1);
    }

    /**
     * 添加部门
     * @param department
     * @return
     */
    @Override
    public RespBean addDepartment(Department department) {
        //设置enable为true
        department.setEnabled(true);
        //调用存储过程，并返回结果集
        departmentMapper.addDepartment(department);
        //判断是否添加成功
        if (department.getResult() == 1){
            return RespBean.success("添加成功！",department);
        }
        return RespBean.error("添加失败！");
    }

    /**
     * 删除部门
     * @param id
     * @return
     */
    @Override
    public RespBean deleteDep(Integer id) {
        //创建部门对象，并设置id值
        Department department = new Department();
        department.setId(id);
        //调用存储对象
        departmentMapper.deleteDep(department);
        //判断是否删除成功
        switch (department.getResult()){
            case 1:
                return RespBean.success("删除成功！");
            case -1:
                return RespBean.error("该部门下还有员工，删除失败！");
            case -2:
                return RespBean.error("该部门下还有子部门，删除失败！");
            default:
                return RespBean.error("删除失败！");
        }
    }
}
