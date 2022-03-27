package com.yeb.mail.controller;

import com.rabbitmq.client.Channel;
import com.yeb.centre.pojo.Employee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.Date;

/**
 * @author dai
 * @create 2022-02-2022/2/9  16-24-02
 * 接受消息
 */
@Component
public class MyRecevier {
    //创建日志打印对象
    private static final Logger LOGGER = LoggerFactory.getLogger(MyRecevier.class);
    //创建消息发送模板
    @Autowired
    private JavaMailSender javaMailSender;
    //创建消息配置
    @Autowired
    private MailProperties mailProperties;
    //创建模板引擎
    @Autowired
    private TemplateEngine templateEngine;
    //redis模板引擎
    @Autowired
    private RedisTemplate redisTemplate;
    //判断消息是否被消费过

    @RabbitListener(queues = "mail.queue")
    public void handler(Message message, Channel channel){
        //从消息中获取到员工信息
        Employee employee = (Employee) message.getPayload();
        //获取到消息头
        MessageHeaders headers = message.getHeaders();
        //获取到消息序号
        long tag = (long) headers.get(AmqpHeaders.DELIVERY_TAG);
        //获取到msgId
        String msgId = (String) headers.get("spring_returned_message_correlation");
        //创建redis的string结构存储
        HashOperations hashOperations = redisTemplate.opsForHash();
        try {
            //判断改消息是否被消费过
            if (hashOperations.entries("mail_log").containsKey(msgId)){
                //消息已经处理过
                LOGGER.error("消息已被处理---->{}",msgId);
                channel.basicAck(tag,false);
                return;
            }
            //创建消息
            MimeMessage msg = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(msg);
            //发件人
            helper.setFrom(mailProperties.getUsername());
            //收件人
            helper.setTo(employee.getEmail());
            //主题
            helper.setSubject("入职欢迎邮件");
            //发送日期
            helper.setSentDate(new Date());
            //邮件内容
            Context context = new Context();
            context.setVariable("name",employee.getName());
            context.setVariable("posName",employee.getPosition().getName());
            context.setVariable("joblevelName",employee.getJoblevel().getName());
            context.setVariable("departmentName",employee.getDepartment().getName());
            //发送邮件
            String mail = templateEngine.process("mail", context);
            helper.setText(mail,true);
            javaMailSender.send(msg);
            //将处理好的消息id存入到redis中
            hashOperations.put("mail_log",msgId,"OK");
            channel.basicAck(tag,false);
        } catch (Exception e) {
            /**
             * 1、目标值
             * 2、是否多人处理
             * 3、是否回退
             */
            try {
                channel.basicNack(tag,false,true);
            } catch (IOException ioException) {
                LOGGER.error("邮件发送失败：{}",e.getMessage());
            }
            LOGGER.error("邮件发送失败：{}",e.getMessage());
        }

    }
}
