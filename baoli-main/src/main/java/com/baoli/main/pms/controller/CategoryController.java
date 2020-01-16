package com.baoli.main.pms.controller;


import com.baoli.common.constans.ResultCodeEnum;
import com.baoli.common.vo.R;
import com.baoli.main.pms.service.CategoryService;
import com.baoli.pms.entity.Category;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
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
@Api(tags = "分类")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @PostMapping("getall")
    @ApiOperation("所有分类")
    public R findAll() {
        List<Category> categories = this.categoryService.findAll();
        if(CollectionUtils.isEmpty(categories)){
           return R.setResult(ResultCodeEnum.NOT_FOUND);
        }
        return R.ok().data(categories);
    }
}

