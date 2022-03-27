package com.yeb.centre.config.rabbitmq;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.yeb.centre.pojo.MailContstants;
import com.yeb.centre.pojo.MailLog;
import com.yeb.centre.service.IMailLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author dai
 * @create 2022-02-2022/2/9  16-15-01
 */
@Configuration
public class RabbitConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitConfig.class);

    @Autowired
    private CachingConnectionFactory cachingConnectionFactory;
    @Autowired
    private IMailLogService mailLogService;

    @Bean
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(cachingConnectionFactory);
        /**
         * 消息确认回调，确认消息是否到达broker
         * data:消息唯一的标识
         * ack：确认结果
         * cause： 失败原因
         */
        rabbitTemplate.setConfirmCallback((date, ack, cause) -> {
            //获取到id
            String msgId = date.getId();
            //判断是否成功
            if (ack) {
                //消息确认成功
                LOGGER.info("{}--->消息发送成功！", msgId);
                //更新数据库中的状态码
                mailLogService.update(new UpdateWrapper<MailLog>().set("status", 1).
                        eq("msgId", msgId));
            } else {
                LOGGER.error("{}--->消息发送失败！", msgId);
            }
        });

        /**
         * 消 息 失 败 回 调 ， 比 如 router不 到 queue时 回 调
         * msg:消 息 主 题
         * repCode:响 应 码
         * repText:响 应 描 述
         * exchange:交 换 机
         * routingKey:路 由 键
         */
        rabbitTemplate.setReturnCallback((msg, repCode, repText, exchange, routingKey
        ) -> {
            LOGGER.info("{}=====>消 息 发 送 到 queue时 失 败 ", msg.getBody());
        });

        return rabbitTemplate;
    }
}
