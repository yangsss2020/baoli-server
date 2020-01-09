package com.baoli.manage.pms.controller;


import com.baoli.pms.entity.Spu;
import com.baoli.manage.pms.service.SpuService;
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
        return this.spuService.getById(3);
//        List<Spu> list = this.spuService.list(null);
    }
}

