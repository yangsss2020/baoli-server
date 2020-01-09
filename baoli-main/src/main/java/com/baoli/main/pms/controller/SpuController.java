package com.baoli.main.pms.controller;


import com.baoli.common.exception.BaoliException;
import com.baoli.main.pms.service.SpuService;
import com.baoli.pms.entity.Spu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 商品spu 前端控制器
 * </p>
 *
 * @author ys
 * @since 2020-01-08
 */
@RestController
@RequestMapping("/pms/spu")
public class SpuController {
    @Autowired
    private SpuService spuService;

    @GetMapping
    public Spu test() {
        if (true) {
            throw new BaoliException("错误测试", 20004);
        }
        return this.spuService.getById(1);
//        List<Spu> list = this.spuService.list(null);
    }
}

