package com.baoli.main.pms.service.impl;

import com.baoli.pms.entity.Category;
import com.baoli.main.pms.mapper.CategoryMapper;
import com.baoli.main.pms.service.CategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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

}
