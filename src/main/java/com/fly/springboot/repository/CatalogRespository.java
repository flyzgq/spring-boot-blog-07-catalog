package com.fly.springboot.repository;

import com.fly.springboot.entity.Catalog;
import com.fly.springboot.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author fly
 * @date 2018/5/28 0:03
 * @description
 **/
public interface CatalogRespository extends JpaRepository<Catalog, Long> {

    /**
     * 根据用户查询
     * @param user
     * @return
     */
    List<Catalog> findByUser(User user);

    /**
     * 根据用户和分类名称查询
     * @param user
     * @param name
     * @return
     */
    List<Catalog> findByUserAndName(User user, String name);
}
