package com.baoli.manage.pms.service.impl;

import com.baoli.manage.vo.CategoryVo;
import com.baoli.pms.entity.Category;
import com.baoli.manage.pms.mapper.CategoryMapper;
import com.baoli.manage.pms.service.CategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 商品分类 服务实现类
 * </p>
 *
 * @author ys
 * @since 2020-01-08
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;
    @Override
    public List<CategoryVo> findAll() {
//        this.categoryMapper
        return null;
    }
}
