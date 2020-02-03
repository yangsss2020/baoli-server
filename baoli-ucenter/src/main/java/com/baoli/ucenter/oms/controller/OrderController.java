package com.baoli.ucenter.oms.controller;


import com.baoli.common.constans.ResultCodeEnum;
import com.baoli.common.vo.R;
import com.baoli.oms.entity.Order;
import com.baoli.ucenter.interceptor.LoginInterceptor;
import com.baoli.ucenter.oms.service.OrderService;
import com.baoli.ucenter.query.OrderQuery;
import com.baoli.ums.entity.Member;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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

    @PostMapping("create")
    @ApiOperation("创建订单")
    public R createOrder(@RequestBody OrderQuery orderQuery){
        Member member = LoginInterceptor.getMember();
        orderQuery.setMemberId(member.getId());
        Order order=this.orderService.createOrder(orderQuery);
        return R.ok().data(order);
    }

    @PostMapping("getorderlist")
    @ApiOperation("获取订单列表")
    public R getOrderList(@RequestBody Map<String,Object> map){
        Member member = LoginInterceptor.getMember();
        Page<Order> orderPage= this.orderService.getOrderList(map,member.getId());
        if(CollectionUtils.isEmpty(orderPage.getRecords())){
            return R.setResult(ResultCodeEnum.NOT_FOUND);
        }else {
            return R.ok().data(R.pageResult(orderPage)).code(NumberUtils.toInt(map.get("status").toString()));
        }
    }
}

