package com.fly.springboot.controller;

import com.fly.springboot.entity.Authority;
import com.fly.springboot.entity.User;
import com.fly.springboot.model.ResponseVo;
import com.fly.springboot.service.interfaces.AuthorityService;
import com.fly.springboot.service.interfaces.UserService;
import com.fly.springboot.utils.ConstraintViolationExceptionHandler;
import com.fly.springboot.utils.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;


/**
 * @author fly
 * @date 2018/5/13 16:22
 * @description
 **/
@Controller
@RequestMapping("/users")
public class UserController {

    /**
     * 日志记录
     */
    public static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private AuthorityService authorityService;
    @GetMapping
    public ModelAndView list(
             @RequestParam(value = "async", required = false) boolean async,
             @RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex,
             @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
             @RequestParam(value = "name", required = false, defaultValue = "") String name,
             Model model){
        Pageable pageable = PageRequest.of(pageIndex, pageSize);
        Page<User> page = userService.listUsersByNameLike(name, pageable);
        List<User> users = page.getContent();
        model.addAttribute("page", page);
        model.addAttribute("userlist", users);
        return new ModelAndView(async==true?"users/list :: #mainContainerRepleace":"users/list", "userModel", model);
    }

    /**
     * 获取修改用户的界面，及数据
     * @param id
     * @param model
     * @return
     */
    @GetMapping("edit/{id}")
    public ModelAndView modifyForm(@PathVariable("id") Long id, Model model){
        User user = userService.getUserById(id);
        model.addAttribute("user", user);
        return new ModelAndView("users/edit", "userModel", model);
    }

    /**
     * 获取 form 表单页面
     * @param model
     * @return
     */
    @GetMapping("/add")
    public ModelAndView createForm(Model model) {
        model.addAttribute("user", new User(null, null, null, null));
        return new ModelAndView("users/add", "userModel", model);
    }

    /**
     * 删除用户
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseVo> delete(@PathVariable("id") Long id){
        try {
            userService.removeUser(id);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return ResponseEntity.ok().body(new ResponseVo(false, e.getMessage()));
        }
        return ResponseEntity.ok().body(new ResponseVo(true, "处理成功"));
    }

    /**
     * 新增用户
     * @param user
     * @param authorityId
     * @return
     */
    @PostMapping
    public ResponseEntity<ResponseVo> saveOrUpdate(User user, Long authorityId){
        List<Authority> authorities = new ArrayList<>();
        authorities.add(authorityService.getAuthrityById(authorityId));
        user.setAuthorities(authorities);
        if(user.getId() == null){
            user.setEncodingPassword(user.getPassword());
        }else{
            //判断密码是否修改
            String rawPassword = userService.getUserById(user.getId()).getPassword();
            Boolean isMatch = SecurityUtils.isPasswordChange(rawPassword, user.getPassword());
            if(!isMatch){
                //没有改密码
                user.setEncodingPassword(user.getPassword());
            }
        }
        try {
            userService.saveOrUpdateUser(user);
        } catch (ConstraintViolationException e) {
            LOGGER.error(e.getMessage(), e);
            return ResponseEntity.ok().body(new ResponseVo(false, ConstraintViolationExceptionHandler.getMessage(e)));
        }
        return ResponseEntity.ok().body(new ResponseVo(true, "处理成功", user));
    }
}
