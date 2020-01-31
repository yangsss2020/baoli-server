package com.baoli.main.pms.controller;


import com.baoli.common.constans.ResultCodeEnum;
import com.baoli.common.vo.R;
import com.baoli.main.pms.service.CarouselService;
import com.baoli.pms.entity.Carousel;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 首页轮播图 前端控制器
 * </p>
 *
 * @author ys
 * @since 2020-01-12
 */
@RestController
@RequestMapping("/pms/carousel")
@Api(tags = "首页轮播图")
//@CrossOrigin
public class CarouselController {
    @Autowired
    private CarouselService carouselService;

    @GetMapping
    @ApiOperation("获取轮播图")
    public R findAll() {
        Carousel carousel = new Carousel();
        carousel.setEnable(true);
        List<Carousel> list = this.carouselService.list(new LambdaQueryWrapper<>(carousel));
        if (CollectionUtils.isEmpty(list)) {
            return R.setResult(ResultCodeEnum.NOT_FOUND);
        }
        return R.ok().data(list);
    }
}

