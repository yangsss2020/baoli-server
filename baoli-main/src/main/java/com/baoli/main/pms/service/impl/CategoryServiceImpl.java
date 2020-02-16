package com.baoli.main.pms.service.impl;

import com.baoli.common.constans.MainCacheConstant;
import com.baoli.main.pms.mapper.CategoryMapper;
import com.baoli.main.pms.service.CategoryService;
import com.baoli.pms.entity.Category;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RList;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

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
@Slf4j
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private RedissonClient redissonClient;

    /**
     * 获取所有分类
     * @return
     */
    @Override
    public List<Category> findAll() {
        RList<Category> category = redissonClient.getList(MainCacheConstant.CATEGORY_ALL);
        List<Category> categories = category.readAll();
        List<Category> list;
        if (CollectionUtils.isEmpty(categories)) {
            QueryWrapper<Category> wrapper = new QueryWrapper<>();
            wrapper.lambda().eq(Category::getParentId, 0).eq(Category::getEnable, true);
            list = this.categoryMapper.selectList(wrapper);
            list.forEach(item -> {
                List<Category> list1 = this.categoryMapper.selectList(new LambdaQueryWrapper<Category>().eq(Category::getParentId,
                        item.getId()));
                item.setChildren(list1);
                list1.forEach(item2 -> {
                    List<Category> list2 = this.categoryMapper.selectList(new LambdaQueryWrapper<Category>().eq(Category::getParentId,
                            item2.getId()));
                    item2.setChildren(list2);
                });
            });
            category.addAll(list);
        } else {
            log.info("获取到了缓存数据.........");
            list=categories;
        }
        return list;
    }
}
