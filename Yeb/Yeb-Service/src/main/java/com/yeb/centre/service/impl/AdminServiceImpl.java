package com.yeb.centre.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yeb.centre.config.secutiry.componenet.JwtTokenUtil;
import com.yeb.centre.mapper.AdminMapper;
import com.yeb.centre.pojo.Admin;
import com.yeb.centre.pojo.AdminLoginParam;
import com.yeb.centre.pojo.RespBean;
import com.yeb.centre.pojo.Role;
import com.yeb.centre.service.IAdminService;
import com.yeb.centre.util.AdminUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zhoubin
 * @since 2022-01-21
 */
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements IAdminService {
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    //获取token头部信息
    @Value(("${jwt.tokenHead}"))
    private String tokenHead;
    @Autowired
    private AdminMapper adminMapper;

    @Override
    public RespBean login(AdminLoginParam adminLoginParam, HttpServletRequest request) {
        //获取验证码
        String captcha = (String) request.getSession().getAttribute("captcha");

        //判断验证码是否正确
        if (StringUtils.isEmpty(captcha) || !captcha.equalsIgnoreCase(adminLoginParam.getCode())) {
            return RespBean.error("验证码错误,请重新输入！");
        }

        //获取用户名
        UserDetails userDetails = userDetailsService.loadUserByUsername(adminLoginParam.getUsername());
        //判断用户面是否正确
        if (userDetails == null || !passwordEncoder.matches(adminLoginParam.getPassword(),userDetails.getPassword())) {
            return RespBean.error("用户名或者密码错误！");
        }
        //判断账号是否被禁用
        if (!userDetails.isEnabled()) {
            return RespBean.error("账号被禁用！");
        }

        //创建认证对象
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
        //将认证对象存储到全局作用域当中
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        //生成token
        Map<String,String> tokenMap = new HashMap<>();
        //存放token
        tokenMap.put("token",jwtTokenUtil.generationToken(userDetails));
        //存放到头部信息
        tokenMap.put("tokenHead",tokenHead);
        return RespBean.success("登录成功！",tokenMap);
    }

    @Override
    public Admin getAdminByUsername(String username) {
        Admin admin = adminMapper.selectOne(new QueryWrapper<Admin>().eq("username", username));
        if (admin != null) {
            //admin.setPassword("******");
            //设置角色信息
            admin.setRoles(getRoles(admin.getId()));
            return admin;
        }else {
           throw new UsernameNotFoundException("请输入正确的用户名！");
        }
    }

    /**
     * 根据用户id获取角色列表
     * @param adminId
     * @return
     */
    @Override
    public List<Role> getRoles(Integer adminId) {
        return adminMapper.getRoles(adminId);
    }

    /**
     * 获取所有操作员
     * @param workName
     * @return
     */
    @Override
    public List<Admin> getAllAdmin(String workName) {
        //获取当前用户的id
        Integer adminId = AdminUtils.getCurrentAdmin().getId();
        return adminMapper.getAllAdmin(adminId,workName);
    }

    /**
     * 修改用户密码
     * @param oldPass
     * @param newPass
     * @param adminId
     * @return
     */
    @Override
    public RespBean updateAdminPass(String oldPass, String newPass, String adminId) {
        //查询当前用户信息
        Admin admin = adminMapper.selectById(adminId);
        //创建密码解析器
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        //判断旧密码是否跟新密码一致
        if (passwordEncoder.matches(oldPass,admin.getPassword())){
            //将用户密码设置为新密码
            admin.setPassword(passwordEncoder.encode(newPass));
            //修改信息
            if (adminMapper.updateById(admin) > 0){
                return RespBean.success("修改成功！");
            }
            return RespBean.error("修改失败！");
        }
        return RespBean.error("旧密码不正确，请重新输入！");
    }

    /**
     * 更新用户头像
     * @param url
     * @param id
     * @param authentication
     * @return
     */
    @Override
    public RespBean updateUserFace(String url, Integer id, Authentication authentication) {
        //获取admin
        Admin admin = adminMapper.selectById(id);
        //设置url
        admin.setUserFace(url);
        //判断是否修改成功
        if (adminMapper.updateById(admin) > 0){
            //更新全局对象
            Admin principal = (Admin) authentication.getPrincipal();
            principal.setUserFace(url);
            //设置SecurityContextHolder
            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(
                    authentication.getCredentials(),authentication.getAuthorities()
            ));
            return RespBean.success("更新成功！");
        }
        return RespBean.error("更新失败！");
    }
}
