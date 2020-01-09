package com.baoli.ucenter.oms.service.impl;

import com.baoli.oms.entity.Order;
import com.baoli.ucenter.oms.mapper.OrderMapper;
import com.baoli.ucenter.oms.service.OrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 订单表 服务实现类
 * </p>
 *
 * @author ys
 * @since 2020-01-08
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

}
