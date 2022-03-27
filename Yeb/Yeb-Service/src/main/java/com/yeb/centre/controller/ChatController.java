package com.yeb.centre.controller;

import com.yeb.centre.pojo.Admin;
import com.yeb.centre.service.IAdminService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author dai
 * @create 2022-02-2022/2/12  19-01-17
 */
@RestController
@RequestMapping("/chat")
public class ChatController {
    @Autowired
    private IAdminService adminService;

    @ApiOperation(value = "获取所有操作员")
    @GetMapping("/admin")
    private List<Admin> getAllAdmin(String wordKyes){
        return adminService.getAllAdmin(wordKyes);
    }
}
