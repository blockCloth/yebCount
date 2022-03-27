package com.yeb.centre.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author dai
 * @create 2022-01-2022/1/27  20-33-55
 */
@RestController
public class HelloTestController {

    @ApiOperation(value = "测试")
    @GetMapping("/hello")
    public String hello(){
        return "hello";
    }

    @ApiOperation(value = "admin访问基本资料")
    @GetMapping("/employee/basic/hello")
    public String hello1(){
        return "/employee/basic/**";
    }

    @ApiOperation(value = "admin访问高级资料")
    @GetMapping("/employee/advanced/hello")
    public String hello2(){
        return "/employee/advanced/**";
    }
}
