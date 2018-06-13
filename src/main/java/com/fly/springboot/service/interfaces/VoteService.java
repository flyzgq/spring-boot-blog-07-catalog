package com.fly.springboot.service.interfaces;

import com.fly.springboot.entity.Vote;


/**
 * @author fly
 * @date 2018/5/27 20:55
 * @description
 **/
public interface VoteService {

    /**
     * 根据id获取vote
     * @param id
     * @return
     */
    Vote getVoteById(Long id);


    /**
     * 删除vote
     * @param id
     */
    void removeVote(Long id);
}
