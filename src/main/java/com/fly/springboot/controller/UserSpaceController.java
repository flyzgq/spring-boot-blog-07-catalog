package com.fly.springboot.controller;

import com.fly.springboot.entity.Blog;
import com.fly.springboot.entity.Catalog;
import com.fly.springboot.entity.User;
import com.fly.springboot.entity.Vote;
import com.fly.springboot.model.ResponseVo;
import com.fly.springboot.service.interfaces.BlogService;
import com.fly.springboot.service.interfaces.CatalogService;
import com.fly.springboot.service.interfaces.UserService;
import com.fly.springboot.utils.ConstraintViolationExceptionHandler;
import com.fly.springboot.utils.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import java.util.List;


/**
 * @author fly
 * @date 2018/5/13 16:22
 * @description     用户主页接口
 **/
@Controller
@RequestMapping("/u")
public class UserSpaceController {
    /**
     * 日志记录
     */
    public static final Logger LOGGER = LoggerFactory.getLogger(UserSpaceController.class);

    @Autowired
    private UserService userService;

    @Autowired
    @Qualifier("userServiceImpl")
    private UserDetailsService userDetailsService;

    @Autowired
    private BlogService blogService;

    @Autowired
    private CatalogService catalogService;
    @Value("${file.server.url}")
    private String fileServerUrl;

    /**
     * 用户的主页
     * @param username
     * @param model
     * @return
     */
    @GetMapping("/{username}")
    public String userSpace(@PathVariable("username") String username, Model model){
        User user = userService.getUserByUsername(username);
        model.addAttribute("user", user);
        LOGGER.info("username" + username);
        return "redirect:/u/" + username + "/blogs";
    }

    /**
     * 获取个人设置界面
     * @param username
     * @param model
     * @return
     */
    @GetMapping("/{username}/profile")
    @PreAuthorize("authentication.name.equals(#username)")
    public String profile(@PathVariable("username") String username, Model model){
        User user = userService.getUserByUsername(username);
        model.addAttribute("user", user);
        model.addAttribute("fileServerUrl", fileServerUrl);
        return "/userspace/profile";
    }

    /**
     * 保存个人信息
     * @param username
     * @param user
     * @return
     */
    @PostMapping("/{username}/profile")
    @PreAuthorize("authentication.name.equals(#username)")
    public String saveProfile(@PathVariable("username") String username, User user){
        User originalUser = userService.getUserById(user.getId());
        originalUser.setEmail(user.getEmail());
        originalUser.setName(user.getName());
        Boolean isMatch = SecurityUtils.isPasswordChange(originalUser.getPassword(), user.getPassword());
        //判读密码是否加密
        if(!isMatch){
            originalUser.setEncodingPassword(user.getPassword());
        }
        userService.saveOrUpdateUser(originalUser);
        return "redirect:/u/"+username+"/profile";
    }

    /**
     * 获取编辑头像的界面
     * @param username
     * @param model
     * @return
     */
    @GetMapping("/{username}/avatar")
    @PreAuthorize("authentication.name.equals(#username)")
    public String avatar(@PathVariable("username") String username, Model model){
        User user = userService.getUserByUsername(username);
        model.addAttribute("user", user);
        return "/userspace/avatar";
    }

    /**
     * 保存头像
     * @param username
     * @param user
     * @return
     */
    @PostMapping("/{username}/avatar")
    @PreAuthorize("authentication.name.equals(#username)")
    public ResponseEntity<ResponseVo> saveAvatar(@PathVariable("username") String username, @RequestBody User user){
        String avatarUrl = user.getAvatar();
        User orgUser = userService.getUserById(user.getId());
        orgUser.setAvatar(avatarUrl);
        userService.saveOrUpdateUser(orgUser);
        return ResponseEntity.ok().body(new ResponseVo(true, "处理成功", avatarUrl));
    }


    /**
     * 获取博客列表
     * @param username
     * @param order
     * @param catalogId
     * @param keyword
     * @param async
     * @param pageIndex
     * @param pageSize
     * @param model
     * @return
     */
    @GetMapping("/{username}/blogs")
    public String listBlogsByOrder(@PathVariable("username") String username,
                                   @RequestParam(value = "order", required = false, defaultValue = "new") String order,
                                   @RequestParam(value = "catalog", required = false) Long catalogId,
                                   @RequestParam(value = "keyword", required = false) String keyword,
                                   @RequestParam(value = "async", required = false) boolean async,
                                   @RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex,
                                   @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
                                   Model model){
        User user = userService.getUserByUsername(username);
        Page<Blog> page = null;
        Pageable pageable = null;
        if(!StringUtils.isEmpty(catalogId) && catalogId >0){
            //后续实现
            Catalog catalog = catalogService.getCatalogById(catalogId);
            pageable = PageRequest.of(pageIndex, pageSize);
            page = blogService.listBlogByCatalog(catalog, pageable);
            order = "";

        }else if("hot".equals(order)){
            //最热
            Sort sort = new Sort(Sort.Direction.DESC, "readSize", "commentSize", "voteSize");
            pageable = PageRequest.of(pageIndex, pageSize, sort);
            page = blogService.listBlogsByTitleBVoteAndSort(user, keyword, pageable);
        }else if("new".equals(order)){
            //最新
            pageable = PageRequest.of(pageIndex, pageSize);
            page = blogService.listBlogsByTitleBVote(user, keyword, pageable);
        }

        assert page != null;
        List<Blog> blogs = page.getContent();
        model.addAttribute("user", user);
        model.addAttribute("order", order);
        model.addAttribute("catalogId", catalogId);
        model.addAttribute("keyword", keyword);
        model.addAttribute("page", page);
        model.addAttribute("blogList", blogs);
        return async == true ? "/userspace/u :: #mainContainerRepleace":"/userspace/u";
    }

    /**
     * 获取博客的展示界面
     * @param username
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/{username}/blogs/{id}")
    public String listBlogsByOrder(@PathVariable("username")String username, @PathVariable("id") Long id, Model model){
        Blog blog = blogService.getBlogById(id);
        /*每次访问该页面, 阅读量递增一次*/
        blogService.readingIncrease(id);
        boolean isOwner = SecurityUtils.isOwner(username);
        /*博客所有者才可以编辑博客，判断是否可以编辑博客*/

        List<Vote> votes = blog.getVotes();
        Vote currentVote = null;
        org.springframework.security.core.userdetails.User principalUser = SecurityUtils.getPrincipalUser();
        if(!StringUtils.isEmpty(principalUser)){
            for (Vote vote:votes) {
                if(vote.getUser().getUsername().equals(principalUser.getUsername())){
                    currentVote = vote;
                    break;
                }
            }
        }
        model.addAttribute("isBlogOwner", isOwner);
        model.addAttribute("blogModel", blog);
        model.addAttribute("currentVote", currentVote);
        return "/userspace/blog";
    }

    /**
     * 获取新增博客的界面
     * @param model
     * @return
     */
    @GetMapping("/{username}/blogs/edit")
    public String createBlog(@PathVariable("username") String username, Model model){
        User user = userService.getUserByUsername(username);
        List<Catalog> catalogs = catalogService.listCatalog(user);
        model.addAttribute("blog", new Blog(null, null, null));
        model.addAttribute("fileServerUrl", fileServerUrl);
        model.addAttribute("catalogs", catalogs);
        return "/userspace/blogedit";
    }
    /**
     * 获取博客的编辑界面
     * @param username
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/{username}/blogs/edit/{id}")
    public String editBlog(@PathVariable("username") String username, @PathVariable("id") Long id, Model model){
        User user = userService.getUserByUsername(username);
        List<Catalog> catalogs = catalogService.listCatalog(user);
        Blog blog = blogService.getBlogById(id);
        model.addAttribute("blog", blog);
        model.addAttribute("fileServerUrl", fileServerUrl);
        model.addAttribute("catalogs", catalogs);
        return "/userspace/blogedit";
    }

    /**
     * 保存博客
     * @param username
     * @param blog
     * @return
     */
    @PostMapping("/{username}/blogs/edit")
    @PreAuthorize("authentication.name.equals(#username)")
    public ResponseEntity<ResponseVo> saveBlog(@PathVariable("username") String username, @RequestBody Blog blog){

        if(blog.getCatalog().getId() == null){
            return ResponseEntity.ok().body(new ResponseVo(false, "未选择分类"));
        }
        try {
            if(!StringUtils.isEmpty(blog.getId())){
                Blog orgBlog = blogService.getBlogById(blog.getId());
                orgBlog.setTitle(blog.getTitle());
                orgBlog.setContent(blog.getContent());
                orgBlog.setSummary(blog.getSummary());
                blogService.saveBlog(orgBlog);
            }else{
                User user = userService.getUserByUsername(username);
                blog.setUser(user);
                blogService.saveBlog(blog);
            }
        }catch (ConstraintViolationException e){
            return ResponseEntity.ok().body(new ResponseVo(false,
                    ConstraintViolationExceptionHandler.getMessage(e)));
        }
        String redirectUrl = "/u/" + username + "/blogs/" + blog.getId();
        return ResponseEntity.ok().body(new ResponseVo(true, "处理成功", redirectUrl));
    }

    /**
     * 删除博客
     * @param username
     * @param id
     * @return
     */
    @DeleteMapping("/{username}/blogs/{id}")
    @PreAuthorize("authentication.name.equals(#username)")
    public ResponseEntity<ResponseVo> deleteBlog(@PathVariable("username") String username, @PathVariable("id") Long id){
        try {
            blogService.removeBlog(id);
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.ok().body(new ResponseVo(false, e.getMessage()));
        }
        String redirectUrl = "/u/" + username + "/blogs";
        return ResponseEntity.ok().body(new ResponseVo(true, "处理成功", redirectUrl));
    }
}
