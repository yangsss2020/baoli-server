package com.baoli.manage.pms.service.impl;

import com.baoli.pms.entity.Category;
import com.baoli.manage.pms.mapper.CategoryMapper;
import com.baoli.manage.pms.service.CategoryService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public List<Category> findAll() {
        QueryWrapper<Category> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(Category::getParentId, 0);
        List<Category> list = this.categoryMapper.selectList(wrapper);
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
        return list;
    }

    /**
     * 根据id删除当前及子分类
     *
     * @param id
     * @param parentId
     */
    @Override
    @Transactional
    public void deleteAll(Long id, Long parentId) {
        if (parentId == 0) {
            List<Category> categories = this.categoryMapper.selectList(new LambdaQueryWrapper<Category>().eq(Category::getParentId, id));
            categories.forEach(item -> {
                List<Category> categories1 = this.categoryMapper.selectList(new LambdaQueryWrapper<Category>().eq(Category::getParentId,
                        item.getId()));
                categories1.forEach(val -> {
                    this.categoryMapper.deleteById(val.getId());
                });
                this.categoryMapper.deleteById(item.getId());
            });
            this.categoryMapper.deleteById(id);
        } else {
            this.categoryMapper.delete(new LambdaQueryWrapper<Category>().eq(Category::getParentId, id));
            this.categoryMapper.deleteById(id);
        }

    }
}
