package com.baoli.ucenter.oms.controller;


import com.baoli.common.vo.R;
import com.baoli.ucenter.interceptor.LoginInterceptor;
import com.baoli.ucenter.oms.service.OrderService;
import com.baoli.oms.entity.Order;
import com.baoli.ucenter.query.OrderQuery;
import com.baoli.ums.entity.Member;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
}

