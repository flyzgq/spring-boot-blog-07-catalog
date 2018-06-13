package com.fly.springboot.repository;

import com.fly.springboot.entity.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author fly
 * @date 2018/5/27 20:54
 * @description     点赞仓库
 **/
public interface VoteRepository extends JpaRepository<Vote, Long> {
}
