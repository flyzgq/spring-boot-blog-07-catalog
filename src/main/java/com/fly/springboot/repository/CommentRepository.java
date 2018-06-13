package com.fly.springboot.repository;

import com.fly.springboot.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author fly
 * @date 2018/5/24 23:27
 * @description     评论仓库
 **/
public interface CommentRepository extends JpaRepository<Comment, Long> {

}
