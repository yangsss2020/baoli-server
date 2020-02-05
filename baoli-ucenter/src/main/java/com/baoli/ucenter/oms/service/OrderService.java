package com.baoli.ucenter.oms.service;

import com.baoli.oms.entity.Order;
import com.baoli.ucenter.query.OrderQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 订单表 服务类
 * </p>
 *
 * @author ys
 * @since 2020-01-08
 */
public interface OrderService extends IService<Order> {

    Order createOrder(OrderQuery orderQuery);

    Page<Order> getOrderList(Map<String, Object> map, String memberId);

    Order findById(String order_id);

    Map<String,Integer> getOrderStatusNum(Map<String, Object> map, String id);

    void closeBackOrder(String orderId);
}
