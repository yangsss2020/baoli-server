package com.baoli.ucenter.sms.service.impl;

import com.baoli.common.constans.PaymentType;
import com.baoli.common.constans.UCenterContant;
import com.baoli.common.exception.BaoliException;
import com.baoli.oms.entity.Order;
import com.baoli.rms.entity.RecordTrading;
import com.baoli.sms.entity.Payment;
import com.baoli.ucenter.oms.mapper.OrderMapper;
import com.baoli.ucenter.query.PayQuery;
import com.baoli.ucenter.query.PaymentQuery;
import com.baoli.ucenter.rms.service.RecordTradingService;
import com.baoli.ucenter.sms.mapper.PaymentMapper;
import com.baoli.ucenter.sms.service.PaymentService;
import com.baoli.ucenter.ums.mapper.MemberMapper;
import com.baoli.ucenter.vo.PaymentVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author ys
 * @since 2020-02-02
 */
@Service
public class PaymentServiceImpl extends ServiceImpl<PaymentMapper, Payment> implements PaymentService {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private RedissonClient redissonClient;
    @Autowired
    private MemberMapper memberMapper;
    @Autowired
    private RecordTradingService recordTradingService;

    /**
     * 支付检查
     * @param paymentQuery
     * @param memberId
     * @return
     */
    @Override
    public PaymentVo checkPay(PaymentQuery paymentQuery, String memberId) {
        String idsStr = paymentQuery.getIds();
        if (StringUtils.isBlank(idsStr)) {
            return null;
        }
        String[] ids = idsStr.split(",");
        List<Map<String, Object>> list = new ArrayList<>();
        BigDecimal money = BigDecimal.ZERO;
        for (String id : ids) {
            Map<String, Object> map = new HashMap<>();

            QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("order_id", id).eq("member_id", memberId).eq("status", 1);
            Order orderItem = this.orderMapper.selectOne(queryWrapper);
            if (orderItem == null) {
                throw new BaoliException("存在无效订单");
            }
            map.put("source_id", orderItem.getOrderId());
            map.put("money", orderItem.getTotalPrice());
            money = money.add(orderItem.getTotalPrice());
            list.add(map);
        }
        PaymentVo paymentVo = new PaymentVo();
        paymentVo.setMoney(money);
        paymentVo.setRel(list);
        String token = UUID.randomUUID().toString().replace("-", "");
        RBucket<Object> bucket = redissonClient.getBucket(UCenterContant.PAYMENT_TOKEN + memberId);
        bucket.set(token, 5, TimeUnit.MINUTES);
        paymentVo.setToken(token);
        return paymentVo;
    }

    @Override
    @Transactional
    public RecordTrading pay(PayQuery payQuery) {
        RBucket<Object> bucket = this.redissonClient.getBucket(UCenterContant.PAYMENT_TOKEN + payQuery.getMemberId());
        Object o = bucket.get();
        if (o == null) {
            throw new BaoliException("请勿重新支付,或支付信息过期2");
        }
        if (!StringUtils.equals(o.toString(), payQuery.getToken())) {
            throw new BaoliException("请勿重新支付,或支付信息过期1");
        }
        bucket.delete();
        String idsStr = payQuery.getIds();
        if (StringUtils.isBlank(idsStr)) {
            throw new BaoliException("订单有误");
        }
        String[] ids = idsStr.split(",");
        RecordTrading record = null;
        switch (payQuery.getPaymentType()) {
            case PaymentType.GOODS_PAY:
                //商品支付
                record = this.goodsPay(payQuery, ids, payQuery.getMemberId());
                break;
            case PaymentType.RECHARGE_PAY:
                System.out.println("充值");
                break;
            case PaymentType.CODE_PAY:
                System.out.println("支付码支付");
                break;
            default:
                throw new BaoliException("不支持的支付方式");
        }
        return record;
        //生成交易记录
    }

    /**
     * 生成交易记录
     * @param payQuery
     * @param ids
     * @param memberId
     * @return
     */
    private RecordTrading goodsPay(PayQuery payQuery, String[] ids, String memberId) {
        RecordTrading record = new RecordTrading();
        for (String id : ids) {
            Order order = this.orderMapper.selectById(id);
            int result = this.memberMapper.pay(memberId, order.getTotalPrice());
            if (result == 1) {
                record.setMemberId(payQuery.getMemberId());
                order.setStatus(2);
                //生成交易记录
                record.setMoney(order.getTotalPrice());
                record.setType(1);
                record.setPayType(1);
                record.setOrderSn(id);
                this.recordTradingService.save(record);
                this.orderMapper.updateById(order);
            } else {
                throw new BaoliException("余额不足");
            }
        }
        return record;
    }
}
