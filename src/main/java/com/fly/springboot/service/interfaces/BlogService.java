package com.fly.springboot.service.interfaces;

import com.fly.springboot.entity.Blog;
import com.fly.springboot.entity.Catalog;
import com.fly.springboot.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author fly
 * @date 2018/5/21 0:22
 * @description
 **/
public interface BlogService {

    /**
     * 保存博客
     * @param blog
     * @return
     */
    Blog saveBlog(Blog blog);

    /**
     * 删除博客
     * @param id
     */
    void removeBlog(Long id);

    /**
     *根据id获取博客
     * @param id
     * @return
     */
    Blog getBlogById(Long id);

    /**
     * 根据用户进行博客名称分页查询（最新）
     * @param user
     * @param title
     * @param pageable
     * @return
     */
    Page<Blog> listBlogsByTitleBVote(User user, String title, Pageable pageable);

    /**
     * 根据用户进行博客名称分页查询（最热）
     * @param user
     * @param title
     * @param pageable
     * @return
     */
    Page<Blog> listBlogsByTitleBVoteAndSort(User user, String title, Pageable pageable);


    /**
     * 阅读量递增
     * @param id
     */
    void readingIncrease(Long id);

    /**
     * 发表评论
     * @param blogId
     * @param commentContent
     * @return
     */
    Blog createComment(Long blogId, String commentContent);

    /**
     * 删除评论
     * @param blogId
     * @param commentId
     */
    void removeComment(Long blogId, Long commentId);

    /**
     * 点赞
     * @param blogId
     * @return
     */
    Blog createVote(Long blogId);

    /**
     * 取消点赞
     * @param blogId
     * @param voteId
     */
    void removeVote(Long blogId, Long voteId);

    /**
     * 根据分类查询博客列表
     * @param catalog
     * @param pageable
     * @return
     */
    Page<Blog> listBlogByCatalog(Catalog catalog, Pageable pageable);
}
