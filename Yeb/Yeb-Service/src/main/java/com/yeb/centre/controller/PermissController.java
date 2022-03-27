package com.yeb.centre.controller;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yeb.centre.mapper.MenuRoleMapper;
import com.yeb.centre.pojo.Menu;
import com.yeb.centre.pojo.MenuRole;
import com.yeb.centre.pojo.RespBean;
import com.yeb.centre.pojo.Role;
import com.yeb.centre.service.IMenuRoleService;
import com.yeb.centre.service.IMenuService;
import com.yeb.centre.service.IRoleService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author dai
 * @create 2022-01-2022/1/28  15-23-11
 */
@RestController
@RequestMapping("/system/basic/permiss")
public class PermissController {
    @Autowired
    private IRoleService roleService;
    @Autowired
    private IMenuService menuService;
    @Autowired
    private IMenuRoleService menuRoleService;

    @ApiOperation(value = "查询角色信息")
    @GetMapping("/")
    public List<Role> getAllRole(){
        return roleService.list();
    }

    @ApiOperation(value = "新增角色信息")
    @PostMapping("/")
    public RespBean insertRole(@RequestBody Role role){
        //先判断角色名是否为ROLE_开头
        if (!role.getName().startsWith("ROLE_")){
            //如果不是则添加上
            role.setName("ROLE_" + role.getName());
        }

        if (roleService.save(role)) {
            return RespBean.success("新增成功！");
        }
        return RespBean.error("新增失败！");
    }

    @ApiOperation(value = "修改角色信息")
    @PutMapping("/")
    public RespBean updateRole(@RequestBody Role role){
        //先判断角色名是否为ROLE_开头
        if (!role.getName().startsWith("ROLE_")){
            //如果不是则添加上
            role.setName("ROLE_" + role.getName());
        }

        if (roleService.updateById(role)) {
            return RespBean.success("修改成功！");
        }
        return RespBean.error("修改失败！");
    }

    @ApiOperation(value = "删除角色信息")
    @DeleteMapping("/role/{rid}")
    public RespBean deleteRole(@PathVariable Integer rid){
        if (roleService.removeById(rid)) {
            return RespBean.success("删除成功！");
        }
        return RespBean.error("删除失败！");
    }

    @ApiOperation(value = "批量删除角色信息")
    @DeleteMapping("/role")
    public RespBean batchDeleteRole(Integer[] rids){
        if (roleService.removeByIds(Arrays.asList(rids))) {
            return RespBean.success("删除成功！");
        }
        return RespBean.error("删除失败！");
    }

    @ApiOperation(value = "查询所有菜单")
    @GetMapping("/menus")
    public List<Menu> getAllMenus(){
        return menuService.getAllMenus();
    }

    @ApiOperation(value = "根据用户id查询菜单id")
    @GetMapping("/mid/{rid}")
    public List<Integer> getMenusByRid(@PathVariable Integer rid){
        return menuRoleService.list(new QueryWrapper<MenuRole>().eq("rid",rid)).stream().map(MenuRole::getMid)
                .collect(Collectors.toList());
    }

    @ApiOperation(value = "更新角色菜单信息")
    @PutMapping("/role/{rid}")
    public RespBean updateMenuRole(@PathVariable Integer rid,Integer[] mids){
        return menuRoleService.updateMenuRole(rid,mids);
    }
}
