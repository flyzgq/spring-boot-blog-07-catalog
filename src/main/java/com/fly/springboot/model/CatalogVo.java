package com.fly.springboot.model;

import com.fly.springboot.entity.Catalog;

import java.io.Serializable;

/**
 * @author fly
 * @date 2018/5/28 0:01
 * @description
 **/
public class CatalogVo implements Serializable {

    private static final long serialVersionUID = -8369346336347930358L;

    private String username;

    private Catalog catalog;

    public CatalogVo() {

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Catalog getCatalog() {
        return catalog;
    }

    public void setCatalog(Catalog catalog) {
        this.catalog = catalog;
    }
}
