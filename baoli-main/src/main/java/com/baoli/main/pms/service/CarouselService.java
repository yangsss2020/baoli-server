package com.baoli.main.pms.service;

import com.baoli.pms.entity.Carousel;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 首页轮播图 服务类
 * </p>
 *
 * @author ys
 * @since 2020-01-12
 */
public interface CarouselService extends IService<Carousel> {
    List<Carousel> findAll();
}
