package com.fly.springboot.service.impl;

import com.fly.springboot.entity.Authority;
import com.fly.springboot.repository.AuthorityRepository;
import com.fly.springboot.service.interfaces.AuthorityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author fly
 * @date 2018/5/16 23:22
 * @description
 **/
@Service
public class AuthorityServiceImpl implements AuthorityService {

    @Autowired
    private AuthorityRepository authorityRepository;

    @Override
    public Authority getAuthrityById(Long id) {
        return authorityRepository.getOne(id);
    }
}
