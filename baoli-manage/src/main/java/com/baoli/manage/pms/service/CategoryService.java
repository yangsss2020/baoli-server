package com.baoli.manage.pms.service;

import com.baoli.manage.vo.CategoryVo;
import com.baoli.pms.entity.Category;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 商品分类 服务类
 * </p>
 *
 * @author ys
 * @since 2020-01-08
 */
public interface CategoryService extends IService<Category> {

    List<CategoryVo> findAll();
}
