package com.baoli.manage.pms.controller;


import com.baoli.common.vo.R;
import com.baoli.manage.pms.service.CategoryService;
import com.baoli.manage.vo.CategoryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 商品分类 前端控制器
 * </p>
 *
 * @author ys
 * @since 2020-01-08
 */
@RestController
@RequestMapping("/pms/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public R findAll() {
        List<CategoryVo> list = this.categoryService.findAll();
        return R.ok().data("list", list);
    }
}

