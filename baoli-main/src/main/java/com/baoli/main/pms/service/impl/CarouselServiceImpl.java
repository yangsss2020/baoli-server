package com.baoli.main.pms.service.impl;

import com.baoli.main.pms.mapper.CarouselMapper;
import com.baoli.main.pms.service.CarouselService;
import com.baoli.pms.entity.Carousel;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 首页轮播图 服务实现类
 * </p>
 *
 * @author ys
 * @since 2020-01-12
 */
@Service
public class CarouselServiceImpl extends ServiceImpl<CarouselMapper, Carousel> implements CarouselService {
    @Autowired
    private CarouselMapper carouselMapper;

    @Override
    public List<Carousel> findAll() {
        List<Carousel> carouselList;
        carouselList = this.carouselMapper.selectList(new LambdaQueryWrapper<>(new Carousel().setEnable(true)));

        return carouselList;
    }
}
