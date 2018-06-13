package com.fly.springboot.service.impl;

import com.fly.springboot.entity.Catalog;
import com.fly.springboot.entity.User;
import com.fly.springboot.repository.CatalogRespository;
import com.fly.springboot.service.interfaces.CatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * @author fly
 * @date 2018/5/28 0:14
 * @description
 **/
@Service
public class CatalogServiceImpl implements CatalogService {

    @Autowired
    private CatalogRespository catalogRespository;

    @Transactional
    @Override
    public Catalog saveCatalog(Catalog catalog) {
        List<Catalog> catalogList = catalogRespository.findByUserAndName(catalog.getUser(), catalog.getName());
        if(catalogList != null && catalogList.size() > 0){
            throw new IllegalArgumentException("该分类已经存在了");
        }
        return catalogRespository.save(catalog);
    }

    @Transactional
    @Override
    public void removeCatalog(Long id) {
        catalogRespository.deleteById(id);
    }

    @Override
    public Catalog getCatalogById(Long id) {
        return catalogRespository.getOne(id);
    }

    @Override
    public List<Catalog> listCatalog(User user) {
        return catalogRespository.findByUser(user);
    }
}
