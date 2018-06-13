package com.fly.springboot.repository;

import com.fly.springboot.entity.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author fly
 * @date 2018/5/16 23:17
 * @description     权限仓库
 **/
public interface AuthorityRepository extends JpaRepository<Authority, Long> {

}
