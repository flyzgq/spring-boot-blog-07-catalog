package com.fly.springboot.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;

/**
 * @author fly
 * @date 2018/5/18 21:41
 * @description
 **/
public class SecurityUtils {

    private static PasswordEncoder passwordEncoder;


    static {
        passwordEncoder = new BCryptPasswordEncoder();
    }
    /**
     * 参数加密
     * @param param
     * @return
     */
    public static String encode(String param){
        return passwordEncoder.encode(param);
    }

    /**
     * 判断参数加密后是否一致
     * @param rawPassword       验证的明文参数
     * @param encodedPassword   已经加密过的参数
     * @return
     */
    public static Boolean isMatch(String rawPassword, String encodedPassword){
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    /**
     * 检验密码是否修改
     * @param rawPassword   已经加密的密码
     * @param passWord      前台返回的密码
     * @return
     */
    public static boolean isPasswordChange(String rawPassword, String passWord){
        return isMatch(rawPassword, encode(passWord));
    }

    /**
     * 判断操作用户是否是博客的所有者
     * @param username      用户名
     * @return
     */
    public static boolean isOwner(String username) {
        boolean isOwner = false;
        User user = getPrincipalUser();
        if (!StringUtils.isEmpty(user) && username.equals(user.getUsername())) {
            isOwner = true;
        }
        return isOwner;
    }

    /**
     * 获取Principal用户
     * @return
     */
    public static User getPrincipalUser(){
       User user = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!StringUtils.isEmpty(authentication)){
            boolean authenticated = authentication.isAuthenticated();
            if(authenticated) {
                boolean anonymousUser = authentication.getPrincipal().toString().equals("anonymousUser");
                if(!anonymousUser){
                     user = (User) authentication.getPrincipal();
                }
            }
        }
        return user;
    }

    /**
     * 从认证信息中获取用户信息
     * @return
     */
    public static User getUser(){
         return  (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
