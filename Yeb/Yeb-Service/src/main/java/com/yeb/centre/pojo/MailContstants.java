package com.yeb.centre.pojo;

/**
 * @author dai
 * @create 2022-02-2022/2/9  20-16-11
 */
public class MailContstants {
    //消息投递中
    public static final Integer DELIVERING = 0;
    //投递成功
    public static final Integer SUCCESS = 1;
    //投递失败
    public static final Integer FAILURE = 2;
    //最大重试次数
    public static final Integer MAX_RETRY_COUNT = 3;
    //投递超时时间
    public static final Integer MSG_TIMEOUT = 1;
    //定义交换机名称
    public static final String EXCHANGE_NAME = "mail.exchange";
    //定义队列名称
    public static final String QUEUE_NAME = "mail.queue";
    //定义routing-key
    public static final String ROUTING_KEY = "mail.routing_key";
}
