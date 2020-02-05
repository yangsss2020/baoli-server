package com.baoli.ucenter.oms.mapper;

import com.baoli.oms.entity.Order;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 订单表 Mapper 接口
 * </p>
 *
 * @author ys
 * @since 2020-01-08
 */
public interface OrderMapper extends BaseMapper<Order> {

    void closeBackOrder(String orderId);
}
