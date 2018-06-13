package com.fly.springboot.service.interfaces;

import com.fly.springboot.entity.Comment;

/**
 * @author fly
 * @date 2018/5/24 23:29
 * @description
 **/
public interface CommentService {

    /**
     * 根据id获取comment
     * @param id
     * @return
     */
    Comment getCommentById(Long id);

    /**
     * 删除评论
     * @param id
     */
    void removeComment(Long id);

}
