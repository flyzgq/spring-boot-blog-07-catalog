package com.fly.springboot.controller;

import com.fly.springboot.entity.Authority;
import com.fly.springboot.entity.User;
import com.fly.springboot.service.interfaces.AuthorityService;
import com.fly.springboot.service.interfaces.UserService;
import com.fly.springboot.utils.SecurityUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 * @author fly
 * @date 2018/5/13 16:22
 * @description 主页控制器
 **/
@Controller
public class MainController {

    public static final Long ROLE_USER_AUTHORITY_ID = 2L;
    /**
     * 日志记录
     */
    public static final Logger LOGGER = LoggerFactory.getLogger(MainController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private AuthorityService authorityService;


    @GetMapping("/")
    public String root(){
        return "redirect:/index";
    }


    @GetMapping("/index")
    public String index(){
        return "index";
    }

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping("/login-error")
    public String loginError(Model model){
        model.addAttribute("loginError", true);
        model.addAttribute("errorMsg", "登录失败，用户名或密码错误！");
        return "login";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }
    @PostMapping("/register")
    public String registerUser(User user){
        List<Authority> authorities = new ArrayList<>();
        authorities.add(authorityService.getAuthrityById(ROLE_USER_AUTHORITY_ID));
        user.setAuthorities(authorities);
        user.setEncodingPassword(user.getPassword());
        userService.registerUser(user);
        return "redirect:/login";
    }
}
