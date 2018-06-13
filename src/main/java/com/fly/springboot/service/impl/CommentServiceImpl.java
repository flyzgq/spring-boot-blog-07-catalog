package com.fly.springboot.service.impl;

import com.fly.springboot.entity.Comment;
import com.fly.springboot.repository.CommentRepository;
import com.fly.springboot.service.interfaces.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * @author fly
 * @date 2018/5/24 23:33
 * @description     评论实现类
 **/
@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Override
    public Comment getCommentById(Long id) {
        return commentRepository.getOne(id);
    }

    @Transactional
    @Override
    public void removeComment(Long id) {
        commentRepository.deleteById(id);
    }
}
