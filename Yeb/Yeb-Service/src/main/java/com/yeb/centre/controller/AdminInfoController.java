package com.yeb.centre.controller;

import com.yeb.centre.pojo.Admin;
import com.yeb.centre.pojo.RespBean;
import com.yeb.centre.service.IAdminService;
import com.yeb.centre.util.FastDFSUtils;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.UUID;

/**
 * @author dai
 * @create 2022-02-2022/2/13  13-14-09
 */
@RestController
@RequestMapping("/adminInfo")
public class AdminInfoController {

    @Autowired
    private IAdminService adminService;

    @ApiOperation(value = "更新用户信息")
    @PutMapping("/update")
    public RespBean updateAdminInfo(@RequestBody Admin admin, Authentication authentication){
        if (adminService.updateById(admin)){
            //更新成功设置Authentication对象
            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(
                    admin,null,authentication.getAuthorities()
            ));
            return RespBean.success("更新成功！");
        }
        return RespBean.error("更新失败！");
    }

    @ApiOperation(value = "更新用户密码")
    @PutMapping("/updatePass")
    public RespBean updatePass(@RequestBody Map<String,String> info){
        //获取旧密码
        String oldPass = info.get("oldPass");
        //获取新密码
        String newPass = info.get("newPass");
        //获取用户id
        String adminId = info.get("adminId");
        //更新
        return adminService.updateAdminPass(oldPass,newPass,adminId);
    }

    @ApiOperation(value = "更新用户头像")
    @PutMapping("/updateUserFace")
    public RespBean updateUserFace(@RequestPart("file") MultipartFile file,
                                   Integer id,
                                   @RequestParam(required = false) Authentication authentication){

        String[] fileAbsolutePath = FastDFSUtils.upload(file);
        String url = FastDFSUtils.getTrackerUrl() + fileAbsolutePath[0] + "/" + fileAbsolutePath[1];
        return adminService.updateUserFace(url,id,authentication);
    }
}
