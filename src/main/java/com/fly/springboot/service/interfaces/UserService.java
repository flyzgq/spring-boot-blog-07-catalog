package com.fly.springboot.service.interfaces;

import com.fly.springboot.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author fly
 * @date 2018/5/13 21:28
 * @description     用户服务接口
 **/
public interface UserService {

    /**
     * 新增 编辑 保存用户
     * @param user
     * @return
     */
    User saveOrUpdateUser(User user);

    /**
     * 注册用户
     * @param user
     * @return
     */
    User registerUser(User user);

    /**
     * 根据账号获取用户
     * @param username
     * @return
     */
    User getUserByUsername(String username);

    /**
     * 删除用户
     * @param id
     */
    void removeUser(Long id);

    /**
     * 根据用户id获取用户
     * @param id
     * @return
     */
    User getUserById(Long id);

    /**
     * 根据用户名进行分页模糊查询
     * @param name
     * @param pageable
     * @return
     */
    Page<User> listUsersByNameLike(String name, Pageable pageable);
}
