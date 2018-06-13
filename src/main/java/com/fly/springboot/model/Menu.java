package com.fly.springboot.model;

import java.io.Serializable;

/**
 * @author fly
 * @date 2018/5/14 0:25
 * @description
 **/
public class Menu implements Serializable {

    private static final long serialVersionUID = 79849097967069953L;
    private String name;
    private String url;

    public Menu(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
