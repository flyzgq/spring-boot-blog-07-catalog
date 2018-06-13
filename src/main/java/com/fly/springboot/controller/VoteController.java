package com.fly.springboot.controller;

import com.fly.springboot.entity.User;
import com.fly.springboot.model.ResponseVo;
import com.fly.springboot.service.interfaces.BlogService;
import com.fly.springboot.service.interfaces.VoteService;
import com.fly.springboot.utils.ConstraintViolationExceptionHandler;
import com.fly.springboot.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.ConstraintViolationException;

/**
 * @author fly
 * @date 2018/5/27 21:40
 * @description
 **/
@Controller
@RequestMapping("/votes")
public class VoteController {

    @Autowired
    private BlogService blogService;

    @Autowired
    private VoteService voteService;

    /**
     * 发表点赞
     * @param blogId
     * @return
     */
    @PostMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_USER')")
    public ResponseEntity<ResponseVo> createVote(Long blogId){
        try {
            blogService.createVote(blogId);
        } catch (ConstraintViolationException e) {
            return ResponseEntity.ok().body(new ResponseVo(false, ConstraintViolationExceptionHandler.getMessage(e)));
        }catch (Exception e) {
            return ResponseEntity.ok().body(new ResponseVo(false, e.getMessage()));
        }
        return ResponseEntity.ok().body(new ResponseVo(true, "点赞成功", null));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_USER')")
    public ResponseEntity<ResponseVo> delete(@PathVariable("id") Long id, Long blogId) {
        User user = voteService.getVoteById(id).getUser();
        boolean owner = SecurityUtils.isOwner(user.getUsername());
        if (!owner) {
            return ResponseEntity.ok().body(new ResponseVo(false, "没有操作权限"));
        }
        try {
            blogService.removeVote(blogId, id);
            voteService.removeVote(id);
        } catch (ConstraintViolationException e) {
            return ResponseEntity.ok().body(new ResponseVo(false, ConstraintViolationExceptionHandler.getMessage(e)));
        } catch (Exception e) {
            return ResponseEntity.ok().body(new ResponseVo(false, e.getMessage()));
        }

        return ResponseEntity.ok().body(new ResponseVo(true, "取消点赞成功", null));
    }

}
