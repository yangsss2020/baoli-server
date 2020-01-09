package com.baoli.manage.oms.controller;


import com.baoli.manage.oms.service.OrderService;
import com.baoli.oms.entity.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 订单表 前端控制器
 * </p>
 *
 * @author ys
 * @since 2020-01-08
 */
@RestController
@RequestMapping("/oms/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @GetMapping
    public Order findAll() {
        return orderService.getById(1);
    }
}

