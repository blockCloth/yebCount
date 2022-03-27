package com.yeb.mail;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * @author dai
 * @create 2022-02-2022/2/9  15-04-40
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class MailApplication {
    public static void main(String[] args) {
        SpringApplication.run(MailApplication.class,args);
    }
}
