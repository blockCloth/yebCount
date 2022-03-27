package com.yeb.centre.test;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Locale;

/**
 * @author dai
 * @create 2022-02-2022/2/20  19-31-20
 */
public class Test {
    public static void main(String[] args) {
        /*String msg = "abcdefg";
        System.out.println(msg.substring(0,3).toUpperCase(Locale.ROOT));*/
        String msg = "11e22a533823377786318b416228ac9c.jpg";
        System.out.println(msg.substring(msg.indexOf(".")));
    }
}
