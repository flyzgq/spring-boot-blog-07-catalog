package com.fly.springboot.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author fly
 * @date 2018/5/13 16:22
 * @description Blog控制器
 **/
@Controller
@RequestMapping("/blogs")
public class BlogController {

    /**
     * 日志记录
     */
    public static final Logger LOGGER = LoggerFactory.getLogger(BlogController.class);
    /**
     * 获取博客列表
     * @param order     排序规则
     * @param keyword   关键字
     * @return
     */
    @GetMapping
    public String listBlogs(@RequestParam(value = "order", required = false, defaultValue = "new") String order,
                            @RequestParam(value = "keyword", required = false) String keyword){
        LOGGER.info("order" + order + "keyword" + keyword);
        return "redirect:/index?order=" + order + "&keyword=" + keyword;
    }

}
