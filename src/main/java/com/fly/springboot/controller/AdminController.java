package com.fly.springboot.controller;

import com.fly.springboot.model.Menu;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author fly
 * @date 2018/5/13 16:22
 * @description     管理员控制器
 **/
@Controller
@RequestMapping("/admins")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")  // 指定角色权限才能操作方法
public class AdminController {
    /**
     * 记录日志
     */
    public static final Logger LOGGER = LoggerFactory.getLogger(AdminController.class);

    /**
     * 获取后台管理主界面
     * @param model
     * @return
     */
    @GetMapping
    public ModelAndView listUser(Model model){
        List<Menu> list = new ArrayList<>();
        list.add(new Menu("用户管理", "/users"));
        list.add(new Menu("角色管理", "/roles"));
        list.add(new Menu("博客管理", "/blogs"));
        list.add(new Menu("评论管理", "/commits"));
        model.addAttribute("menulist", list);
        return new ModelAndView("admins/index","menuList", model);
    }

}
