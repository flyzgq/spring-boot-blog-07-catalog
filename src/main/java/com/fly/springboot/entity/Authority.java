package com.fly.springboot.entity;

import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

/**
 * @author fly
 * @date 2018/5/16 22:08
 * @description 权限实体
 **/
@Entity
public class Authority implements GrantedAuthority {

    private static final long serialVersionUID = -6791934865986739979L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /*非空*/
    @Column(nullable = false)
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getAuthority() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
