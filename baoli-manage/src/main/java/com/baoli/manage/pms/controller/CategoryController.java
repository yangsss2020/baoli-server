package com.baoli.manage.pms.controller;


import com.baoli.common.vo.R;
import com.baoli.manage.pms.service.CategoryService;
import com.baoli.pms.entity.Category;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
@Api(tags = "分类管理")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping
    @ApiOperation("查询所有分类")
    public R findAll() {
        List<Category> list = this.categoryService.findAll();
        return R.ok().data(list);
    }


    @PostMapping
    @ApiOperation("新增分类")
    public R save(@RequestBody Category category) {
        if (category.getParentId() == null) {
            category.setParent(true);
            category.setParentId(null);
        }
        this.categoryService.save(category);
        return R.ok().message("新增成功");
    }

    @ApiOperation("删除分类")
    @DeleteMapping("{id}")
    public R delete(@PathVariable Long id, @RequestParam("parent") Boolean parent,
                    @RequestParam(value = "parentId", required = false) Long parentId) {
        if (parent) {
            this.categoryService.deleteAll(id, parentId);
        } else {
            this.categoryService.removeById(id);
        }
        return R.ok().message("删除成功");
    }
}

