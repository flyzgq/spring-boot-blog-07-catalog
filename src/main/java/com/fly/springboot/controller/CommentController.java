package com.fly.springboot.controller;

import com.fly.springboot.entity.Blog;
import com.fly.springboot.entity.Comment;
import com.fly.springboot.entity.User;
import com.fly.springboot.model.ResponseVo;
import com.fly.springboot.service.interfaces.BlogService;
import com.fly.springboot.service.interfaces.CommentService;
import com.fly.springboot.utils.ConstraintViolationExceptionHandler;
import com.fly.springboot.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author fly
 * @date 2018/5/25 0:12
 * @description
 **/
@Controller
@RequestMapping("/comments")
public class CommentController {

    @Autowired
    private BlogService blogService;

    @Autowired
    private CommentService commentService;

    /**
     * 获取品论列表
     * @param id
     * @param model
     * @return
     */
    @GetMapping
    public String listCommentd(@RequestParam(value = "blogId", required = true) Long id, Model model){
        Blog blog = blogService.getBlogById(id);
        List<Comment> comments = blog.getComments();
        //按照评论时间排序
        Collections.sort(comments, new Comparator<Comment>() {
            @Override
            public int compare(Comment o1, Comment o2) {
                return o2.getCreateTime().compareTo(o1.getCreateTime());
            }
        });
        String commentOwner = null;
        org.springframework.security.core.userdetails.User user = SecurityUtils.getPrincipalUser();
        if(!StringUtils.isEmpty(user)){
            commentOwner = user.getUsername();
        }
        model.addAttribute("commentOwner", commentOwner);
        model.addAttribute("comments", comments);
        return "/userspace/blog :: #mainContainerReplace";
    }

    /**
     * 发表评论
     * @param id
     * @param commentContent
     * @return
     */
    @PostMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<ResponseVo> createComment(@RequestParam("blogId") Long id, String commentContent){
        try {
            blogService.createComment(id, commentContent);
        } catch (ConstraintViolationException e) {
            return ResponseEntity.ok().body(new ResponseVo(false, ConstraintViolationExceptionHandler.getMessage(e)));
        }catch (Exception e) {
            return ResponseEntity.ok().body(new ResponseVo(false, e.getMessage()));
        }

        return ResponseEntity.ok().body(new ResponseVo(true, "处理成功"));
    }

    /**
     * 删除评论
     * @param id
     * @param blogId
     * @return
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<ResponseVo> delete(@PathVariable("id") Long id, Long blogId){
        User user = commentService.getCommentById(id).getUser();
        boolean isOwner = false;
        if(SecurityUtils.isOwner(user.getUsername())){
            isOwner = true;
        }
        if(!isOwner){
            return ResponseEntity.ok().body(new ResponseVo(false, "没有权限信息"));
        }

        try {
            blogService.removeComment(blogId, id);
            commentService.removeComment(id);
        } catch (ConstraintViolationException e) {
            return ResponseEntity.ok().body(new ResponseVo(false, ConstraintViolationExceptionHandler.getMessage(e)));
        }catch (Exception e) {
            return ResponseEntity.ok().body(new ResponseVo(false, e.getMessage()));
        }

        return ResponseEntity.ok().body(new ResponseVo(true, "处理成功"));
    }
}
