package com.fly.springboot.controller;

import com.fly.springboot.entity.Catalog;
import com.fly.springboot.entity.User;
import com.fly.springboot.model.CatalogVo;
import com.fly.springboot.model.ResponseVo;
import com.fly.springboot.service.interfaces.CatalogService;
import com.fly.springboot.service.interfaces.UserService;
import com.fly.springboot.utils.ConstraintViolationExceptionHandler;
import com.fly.springboot.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import java.util.List;

/**
 * @author fly
 * @date 2018/5/28 0:27
 * @description
 **/
@Controller
@RequestMapping("/catalogs")
public class CatalogController {

    @Autowired
    private UserService userService;

    @Autowired
    private CatalogService catalogService;
    /**
     * 获取分类列表
     * @param username
     * @param model
     * @return
     */
    @GetMapping
    public String listCommets(@RequestParam(value = "username", required = true) String username, Model model){
        User user = userService.getUserByUsername(username);
        List<Catalog> catalogs = catalogService.listCatalog(user);
        boolean owner = SecurityUtils.isOwner(username);
        model.addAttribute("isCatalogsOwner", owner);
        model.addAttribute("catalogs", catalogs);
        return "/userspace/u :: #catalogRepleace";
    }

    /**
     * 创建分类
     * @param catalogVo
     * @return
     */
    @PostMapping
    @PreAuthorize("authentication.name.equals(#catalogVo.username)")
    public ResponseEntity<ResponseVo> create(@RequestBody CatalogVo catalogVo){

        String username = catalogVo.getUsername();
        Catalog catalog = catalogVo.getCatalog();
        User user = userService.getUserByUsername(username);

        try {
            catalog.setUser(user);
            catalogService.saveCatalog(catalog);
        } catch (ConstraintViolationException e) {
            return ResponseEntity.ok().body(new ResponseVo(false,
                    ConstraintViolationExceptionHandler.getMessage(e)));
        }catch (Exception e) {
            return ResponseEntity.ok().body(new ResponseVo(false, e.getMessage()));
        }
        return ResponseEntity.ok().body(new ResponseVo(true, "创建分类成功", null));
    }

    /**
     * 删除分类
     * @param username
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("authentication.name.equals(#username)")
    public ResponseEntity<ResponseVo> delete(String username, @PathVariable("id") Long id){

        try {
            catalogService.removeCatalog(id);
        } catch (ConstraintViolationException e) {
            return ResponseEntity.ok().body(new ResponseVo(false,
                    ConstraintViolationExceptionHandler.getMessage(e)));
        }catch (Exception e) {
            return ResponseEntity.ok().body(new ResponseVo(false, e.getMessage()));
        }
        return ResponseEntity.ok().body(new ResponseVo(true, "删除分类成功", null));
    }


    /**
     * 获取分类编辑界面
     * @param model
     * @return
     */
    @GetMapping("/edit")
    public String getCatalogEdit(Model model){
        model.addAttribute("catalog", new Catalog(null, null));
        return "/userspace/catalogedit";
    }

    @GetMapping("/edit/{id}")
    public String getCatalogById(@PathVariable("id") Long id, Model model){
        Catalog catalog = catalogService.getCatalogById(id);
        model.addAttribute("catalog", catalog);
        return "userspace/catalogedit";
    }
}
