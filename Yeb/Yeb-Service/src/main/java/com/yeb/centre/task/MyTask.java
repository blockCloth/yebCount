package com.yeb.centre.task;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.yeb.centre.pojo.Employee;
import com.yeb.centre.pojo.MailContstants;
import com.yeb.centre.pojo.MailLog;
import com.yeb.centre.service.IEmployeeService;
import com.yeb.centre.service.IMailLogService;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author dai
 * @create 2022-02-2022/2/10  13-41-00
 * 定时发送邮件
 */
@Component
public class MyTask {
    @Autowired
    private IMailLogService mailLogService;
    @Autowired
    private IEmployeeService employeeService;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Scheduled(cron = "0/10 * * * * ?")
    public void myTask(){
        //消息状态小于0且重试时间小于当前时间的
        List<MailLog> mailLogList = mailLogService.list(new QueryWrapper<MailLog>().eq("status", 0)
                .lt("tryTime", LocalDateTime.now()));
        //循环判断是否需要重新发送
        mailLogList.forEach(mailLog -> {
            //判断重试次数是否大于3
            if (mailLog.getCount() >= 3){
                //将状态修改为2，不能重新发送
                mailLogService.update(new UpdateWrapper<MailLog>().set("status",2)
                        .eq("msgId",mailLog.getMsgId()));
                return;
            }
            //消息能够重发，将消息的重发次数，发送时间，修改时间都修改
            mailLogService.update(new UpdateWrapper<MailLog>().set("count",mailLog.getCount()+1)
                    .set("updateTime",LocalDateTime.now())
                    .set("tryTime",LocalDateTime.now().plusMinutes(MailContstants.MSG_TIMEOUT))
                    .eq("msgId",mailLog.getMsgId()));
            //获取用户
            Employee employee = employeeService.getAllEmployee(mailLog.getEid()).get(0);
            //重新发送消息
            rabbitTemplate.convertAndSend(MailContstants.EXCHANGE_NAME,MailContstants.ROUTING_KEY
                    ,employee,new CorrelationData(mailLog.getMsgId()));
        });
    }
}
