package com.yeb.centre.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yeb.centre.mapper.EmployeeMapper;
import com.yeb.centre.mapper.MailLogMapper;
import com.yeb.centre.pojo.*;
import com.yeb.centre.service.IEmployeeService;
import com.yeb.centre.util.DateTimeUtil;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zhoubin
 * @since 2022-01-21
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements IEmployeeService {
    @Autowired
    private EmployeeMapper employeeMapper;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private MailLogMapper mailLogMapper;

    /**
     * 获取所有员工（分页）
     * @param currentPage 当前页数
     * @param size 总条数
     * @param employee 查询员工信息
     * @param beginDateSource 开始与结束时间
     * @return
     */
    @Override
    public PageRespBean getEmployee(Integer currentPage, Integer size, Employee employee, LocalDate[] beginDateScope) {
/*        LocalDate[] localDates = null;
        //判断String数组是否为空
        if (beginDateSource != null){
            //将String数组转换为localdate数组
            localDates = DateTimeUtil.parseForDate(beginDateSource);
        }*/
        //创建分页对象
        Page<Employee> page = new Page<>(currentPage,size);
        //查询数据
        IPage<Employee> employeeIPage = employeeMapper.getEmployee(page, employee, beginDateScope);
        //返回结果
        System.out.println(employeeIPage.getTotal());
        PageRespBean pageRespBean = new PageRespBean(employeeIPage.getTotal(),employeeIPage.getRecords());
        return pageRespBean;
    }

    /**
     * 获取最大工号ID
     * @return
     */
    @Override
    public RespBean maxWorkId() {
        List<Map<String, Object>> maps = employeeMapper.selectMaps(new QueryWrapper<Employee>().select("max(workId)"));
        //将ID加1，得到最大ID值
        String format = String.format("%08d", Integer.parseInt(maps.get(0).get("max(workId)").toString()) + 1);
        return RespBean.success(null,format);
    }

    /**
     * 添加员工
     * @param employee
     * @return
     */
    @Override
    public RespBean addEmployee(Employee employee) {
        //计算合同年限，保留两位小数点
        //获取合同开始日期
        LocalDate beginDate = employee.getBeginContract();
        //获取合同结束日期
        LocalDate endContract = employee.getEndContract();
        //获取合同天数
        long days = beginDate.until(endContract, ChronoUnit.DAYS);
        //设置double格式
        DecimalFormat decimalFormat = new DecimalFormat("##.00");
        //设置合同年限
        employee.setContractTerm(Double.parseDouble(decimalFormat.format(days / 365.00)));
        //添加数据
        if (employeeMapper.insert(employee) > 0){
            Employee emp = employeeMapper.getAllEmployee(employee.getId()).get(0);
            //生成消息的唯一下标
            String msgId = UUID.randomUUID().toString();
            //消息投递的回调部分
            MailLog mailLog = new MailLog();
            mailLog.setMsgId(msgId);
            mailLog.setEid(emp.getId());
            mailLog.setCount(0);
            mailLog.setStatus(0);
            mailLog.setExchange(MailContstants.EXCHANGE_NAME);
            mailLog.setRouteKey(MailContstants.ROUTING_KEY);
            mailLog.setCreateTime(LocalDateTime.now());
            mailLog.setUpdateTime(LocalDateTime.now());
            mailLog.setTryTime(LocalDateTime.now().plusMinutes(MailContstants.MSG_TIMEOUT));
            mailLogMapper.insert(mailLog);
            //添加成功则，将员工消息添加到消息队列之中
            rabbitTemplate.convertAndSend(MailContstants.EXCHANGE_NAME,MailContstants.ROUTING_KEY,
                    emp,new CorrelationData(msgId));
            return RespBean.success("添加成功！");
        }
        return RespBean.error("添加失败！");
    }

    /**
     * 更新员工信息
     * @param employee
     * @return
     */
    @Override
    public RespBean updateEmployee(Employee employee) {
        //计算合同年限，保留两位小数点
        //获取合同开始日期
        LocalDate beginDate = employee.getBeginContract();
        //获取合同结束日期
        LocalDate endContract = employee.getEndContract();
        //获取合同天数
        long days = beginDate.until(endContract, ChronoUnit.DAYS);
        //设置double格式
        DecimalFormat decimalFormat = new DecimalFormat("##.00");
        //设置合同年限
        employee.setContractTerm(Double.parseDouble(decimalFormat.format(days / 365.00)));
        //添加数据
        if (employeeMapper.updateById(employee) > 0){
            return RespBean.success("添加成功！");
        }
        return RespBean.error("添加失败！");
    }

    /**
     * 查询出所有员工信息
     * @return
     */
    @Override
    public List<Employee> getAllEmployee(Integer eid) {
        return employeeMapper.getAllEmployee(eid);
    }

    /**
     * Integer
     * @param currentPage
     * @param size
     * @return
     */
    @Override
    public PageRespBean getEmployeeWithSalary(Integer currentPage, Integer size) {
        //开启分页
        Page<Employee> page = new Page(currentPage,size);
        //查询结果
        IPage<Employee> iPage = employeeMapper.getEmployeeWithSalary(page);
        //返回结果
        PageRespBean pageRespBean = new PageRespBean(iPage.getTotal(),iPage.getRecords());
        return pageRespBean;
    }
}
