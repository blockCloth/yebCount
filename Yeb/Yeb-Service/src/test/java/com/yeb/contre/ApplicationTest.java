package com.yeb.contre;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yeb.centre.YebApplication;
import com.yeb.centre.mapper.AdminMapper;
import com.yeb.centre.mapper.MenuMapper;
import com.yeb.centre.pojo.*;
import com.yeb.centre.service.*;
import com.yeb.centre.util.DateTimeUtil;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

/**
 * @author dai
 * @create 2022-01-2022/1/25  15-54-49
 */
@SpringBootTest(classes = YebApplication.class)
public class ApplicationTest {
    @Autowired
    private MenuMapper menuMapper;
    @Autowired
    private IPositionService positionService;
    @Autowired
    private IAdminService adminService;
    @Autowired
    private IDepartmentService departmentService;
    @Autowired
    private AdminMapper adminMapper;
    @Autowired
    private IEmployeeService employeeService;
    @Autowired
    private INationService nationService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 根据用户id获取菜单
     */
    @Test
    public void getMenusByAdminId(){
        System.out.println(menuMapper.getMenusByAdminId(1));
    }

    /**
     * 获取所有角色
     */
    @Test
    public void getRoles(){
        List<Role> roles = adminService.getRoles(1);
        System.out.println(roles);
    }

    /**
     * 获取所有职位
     */
    @Test
    public void getPosition(){
        System.out.println(positionService.list());
    }

    /**
     * 获取到
     */
    @Test
    public void getAllDepartment(){
        List<Department> allDepartment = departmentService.getAllDepartment();
        System.out.println(allDepartment);
    }

    /**
     * 获取所有操作员
     */
    @Test
    public void getAllAdmin(){
        List<Admin> allAdmin = adminMapper.getAllAdmin(1, "淑");
        System.out.println(allAdmin);
    }

    /**
     * 测试localdate
     */
    @Test
    public void getEmployee(){
        //创建时间数据
        LocalDate parse = LocalDate.parse("2021-11-15", DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        System.out.println(parse.toString());

        String[] strings = new String[]{"2002-1-15","2003-1-14"};

        LocalDate[] localDates = DateTimeUtil.parseForDate(strings);
    }

    /**
     * 获取所有员工
     */
    @Test
    public void getAllEmployee(){
        List<Employee> employee = employeeService.getAllEmployee(1);
        System.out.println(employee);
    }

    /**
     * 获取到民族id
     */
    @Test
    public void getNationId(){
        //Map<String, Object> map = nationService.getMap(new QueryWrapper<Nation>().eq("name", "汉族"));
        //System.out.println(nationService.getMap(new QueryWrapper<Nation>().eq("name", "汉族")).get("id"));

        Integer id = nationService.getOne(new QueryWrapper<Nation>().eq("name", "汉族")).getId();
        System.out.println(id);
    }

    /**
     * 测试redis的hash表
     */
    @Test
    public void setHash(){
        HashOperations hashOperations = redisTemplate.opsForHash();
        hashOperations.put("mail_log","123456","OK");
        //hashOperations.put("aaa","mail","OK");
        //System.out.println(hashOperations.entries("mail_log").containsKey("123456"));
    }

    /**
     * 测试密码解密
     */
    @Test
    public void mathPass(){
        System.out.println(passwordEncoder.encode("$10$nXjlSzYOPXinfidRj05.9.O05jaxOVpGb81meLPwKUcmPnuzRAcMS"));
    }
}
