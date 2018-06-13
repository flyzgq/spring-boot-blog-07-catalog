package com.fly.springboot.service.interfaces;

import com.fly.springboot.entity.Authority;

/**
 * @author fly
 * @date 2018/5/16 23:20
 * @description
 **/
public interface AuthorityService {

    /**
     * 根据id获取权限
     * @param id
     * @return
     */
   Authority getAuthrityById(Long id);
}
