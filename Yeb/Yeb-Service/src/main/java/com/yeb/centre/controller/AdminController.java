package com.yeb.centre.controller;


import com.yeb.centre.pojo.Admin;
import com.yeb.centre.pojo.RespBean;
import com.yeb.centre.pojo.Role;
import com.yeb.centre.service.IAdminRoleService;
import com.yeb.centre.service.IAdminService;
import com.yeb.centre.service.IRoleService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zhoubin
 * @since 2022-01-21
 * 操作员控制器
 */
@RestController
@RequestMapping("/system/admin")
public class AdminController {
    @Autowired
    private IAdminService adminService;
    @Autowired
    private IRoleService roleService;
    @Autowired
    private IAdminRoleService adminRoleService;

    @ApiOperation(value = "获取所有操作员")
    @GetMapping("/")
    public List<Admin> getAllAdmin(String workName){
        return adminService.getAllAdmin(workName);
    }

    @ApiOperation(value = "修改操作员")
    @PutMapping("/")
    public RespBean updateAdmin(@RequestBody Admin admin){
        if (adminService.updateById(admin)){
            return RespBean.success("修改成功！");
        }
        return RespBean.error("修改成功！");
    }

    @ApiOperation(value = "删除操作员")
    @DeleteMapping("/{id}")
    public RespBean deleteAdmin(@PathVariable Integer id){
        if (adminService.removeById(id)){
            return RespBean.success("删除成功！");
        }
        return RespBean.error("删除失败！");
    }

    @ApiOperation(value = "查询所有角色")
    @GetMapping("/role")
    public List<Role> getAllRoles(){
        return roleService.list();
    }

    @ApiOperation(value = "修改操作员角色信息")
    @PutMapping("/role/{adminId}")
    public RespBean updateAdminRole(@PathVariable Integer adminId,Integer[] rids){
        return adminRoleService.updateAdminRole(adminId,rids);
    }
}
