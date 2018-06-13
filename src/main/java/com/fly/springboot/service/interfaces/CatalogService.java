package com.fly.springboot.service.interfaces;

import com.fly.springboot.entity.Catalog;
import com.fly.springboot.entity.User;

import java.util.List;

/**
 * @author fly
 * @date 2018/5/28 0:07
 * @description
 **/
public interface CatalogService {

    /**
     * 保存catalog
     * @param catalog
     * @return
     */
    Catalog saveCatalog(Catalog catalog);

    /**
     * 删除catalog
     * @param id
     */
    void removeCatalog(Long id);

    /**
     * 根据id获取catalog
     * @param id
     * @return
     */
    Catalog getCatalogById(Long id);

    /**
     * 根据用户获取catalog列表
     * @param user
     * @return
     */
    List<Catalog> listCatalog(User user);
}
