package com.fly.springboot.service.impl;

import com.fly.springboot.entity.Vote;
import com.fly.springboot.repository.VoteRepository;
import com.fly.springboot.service.interfaces.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author fly
 * @date 2018/5/27 20:58
 * @description     点赞实现
 **/
@Service
public class VoteServiceImpl implements VoteService {

    @Autowired
    private VoteRepository voteRepository;

    @Override
    public Vote getVoteById(Long id) {
        return voteRepository.getOne(id);
    }

    @Override
    public void removeVote(Long id) {
        voteRepository.deleteById(id);
    }
}
