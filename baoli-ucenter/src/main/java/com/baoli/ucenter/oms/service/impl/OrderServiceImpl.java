package com.baoli.ucenter.oms.service.impl;

import com.baoli.common.exception.BaoliException;
import com.baoli.common.vo.R;
import com.baoli.oms.entity.Cart;
import com.baoli.oms.entity.Order;
import com.baoli.oms.entity.OrderItem;
import com.baoli.ucenter.client.SkuClient;
import com.baoli.ucenter.oms.mapper.OrderMapper;
import com.baoli.ucenter.oms.service.CartService;
import com.baoli.ucenter.oms.service.OrderItemService;
import com.baoli.ucenter.oms.service.OrderService;
import com.baoli.ucenter.query.OrderQuery;
import com.baoli.ucenter.ums.service.AddressService;
import com.baoli.ucenter.ums.service.MemberService;
import com.baoli.ums.entity.Address;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

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
    @Autowired
    private SkuClient skuClient;
    @Autowired
    private CartService cartService;
    @Autowired
    private MemberService memberService;
    @Autowired
    private AddressService addressService;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private OrderItemService orderItemService;
    @Autowired
    private OrderMapper orderMapper;

    @Override
    @Transactional
    public Order createOrder(OrderQuery orderQuery) {
        Order order = new Order();
        String[] idsArr = orderQuery.getIds().split(",");
        List<Long> list = new ArrayList<>();
        for (String s : idsArr) {
            list.add(NumberUtils.toLong(s));
        }
        Collection<Cart> carts = this.cartService.listByIds(list);
        BigDecimal amount = BigDecimal.ZERO;
        for (Cart cart : carts) {
            // 判断库存
//            SkuStock stock = this.skuClient.findSkuStockBySkuId(cart.getSkuId());
//            if (stock.getStock() < cart.getQuantity()) {
//                throw new BaoliException("库存不足,请重新下单");
//            }
            R r = this.skuClient.stockDecrement(cart.getSkuId(), cart.getQuantity());
            if (!r.getStatus()) {
                throw new BaoliException("库存不足,请重新下单");
            }
            this.cartService.removeById(cart.getId());
            BigDecimal num = new BigDecimal(cart.getQuantity());
            amount = amount.add(num.multiply(cart.getPrice()));
        }
        Address address = this.addressService.getById(orderQuery.getAddressId());
        order.setOrderId(null);
        order.setMemberId(orderQuery.getMemberId());
        order.setTotalPrice(amount);
        order.setReceiverArea(address.getAreaName());
        order.setReceiverDetailAddress(address.getAddress());
        order.setReceiverName(address.getName());
        order.setReceiverPhone(address.getPhone());
        order.setNote(orderQuery.getNote());
        order.setStatus(1);
        order.setPayType(0);
        order.setOrderType(0);
        this.save(order);
        carts.forEach(cart -> {
            OrderItem orderItem = objectMapper.convertValue(cart, OrderItem.class);
            orderItem.setOrderId(order.getOrderId());
            orderItem.setId(null);
            this.orderItemService.save(orderItem);
        });
        return order;
    }

    @Override
    public Page<Order> getOrderList(Map<String, Object> map, String memberId) {
        long page =NumberUtils.toLong( map.get("page").toString());
        long limit = NumberUtils.toLong(map.get("limit").toString());
        int status = NumberUtils.toInt(map.get("status").toString());
        Order order = new Order();
        order.setMemberId(memberId);
        if(status !=0){
            order.setStatus(status);
        }
        Page<Order> orderPage = new Page<>(page,limit);
        this.page(orderPage,new LambdaQueryWrapper<>(order));
        List<Order> orderList = orderPage.getRecords();
        orderList.forEach(item->{
            List<OrderItem> list = this.orderItemService.list(new LambdaQueryWrapper<>(new OrderItem().setOrderId(item.getOrderId())));
            item.setItems(list);
        });
        return orderPage;
    }
}
