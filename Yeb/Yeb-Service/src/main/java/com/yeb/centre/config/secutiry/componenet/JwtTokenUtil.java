package com.yeb.centre.config.secutiry.componenet;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author dai
 * @create 2022-01-2022/1/16  16-29-36
 */
@Component
public class JwtTokenUtil {
    //定义用户名
    public static final String CLATM_KEY_USERANEM = "sub";
    //用户创建时间
    public static final String CLATM_KEY_CREATE = "create";
    //加密使用的密钥
    @Value("${jwt.secret}")
    private String secret;
    //用户超时时间
    @Value("${jwt.expiration}")
    private Long expiration;

    /**
     * 根据用户信息生成Token
     * @return
     */
    public String generationToken(UserDetails userDetails){
        //创建集合
        Map<String,Object> claims = new HashMap<>();
        //创建用户
        claims.put(CLATM_KEY_USERANEM,userDetails.getUsername());
        //设置创建时间
        claims.put(CLATM_KEY_CREATE,new Date());

        return generateToken(claims);
    }

    /**
     * 从Token中获取用户名
     * @return
     */
    public String getUsernameFromToken(String token){
        String username;
        try {
            Claims claims = getClaimsFromToken(token);
            username = claims.getSubject();
        } catch (Exception e) {
            username = null;
            e.printStackTrace();
        }
        return username;
    }

    /**
     * 判断Token是否有效
     * @param token
     * @return
     */
    public boolean validateToken(String token,UserDetails userDetails){
        //先获取用户名
        String username = getUsernameFromToken(token);
        //在判断用户名是否一致
        return username.equals(userDetails.getUsername()) && !isExpirationToken(token);
    }

    /**
     * 判断token是否可以刷新
     * @param token
     * @return
     */
    public boolean isTokenPefresh(String token){
        return !isExpirationToken(token);
    }

    /**
     * 刷新Token
     * @param token
     * @return
     */
    public String pefreshToken(String token){
        Claims claims = getClaimsFromToken(token);
        claims.put(CLATM_KEY_CREATE,new Date());

        return generateToken(claims);
    }
    /**
     * 根据负载生成JWT Token
     * @param claims
     * @return
     */
    private String generateToken(Map<String,Object> claims){
        return Jwts.builder()
                //设置条件
                .setClaims(claims)
                //设置超时时间
                .setExpiration(generateExpirationData())
                //设置通信协议
                .signWith(SignatureAlgorithm.HS512,secret)
                .compact();
    }

    /**
     * 从Token中获取JWT的负载
     * @param token
     * @return
     */
    private Claims getClaimsFromToken(String token) {
        Claims claims = null;
        try {
            claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return claims;
    }

    /**
     * 生成Token过期时间
     * @return
     */
    private Date generateExpirationData() {
        return new Date(System.currentTimeMillis() + expiration * 1000);
    }

    /**
     * 从Token中获取过期时间
     * @param token
     * @return
     */
    private Date getExpirationTokenDate(String token){
        Claims claimsDate = getClaimsFromToken(token);
        return claimsDate.getExpiration();
    }

    /**
     * 判断token是否过期
     * @param token
     * @return
     */
    private boolean isExpirationToken(String token){
        Date claimsFromDate = getExpirationTokenDate(token);
        return claimsFromDate.before(new Date());
    }
}
