package com.baoli.ucenter.sms.service;

import com.baoli.rms.entity.RecordTrading;
import com.baoli.sms.entity.Payment;
import com.baoli.ucenter.query.PayQuery;
import com.baoli.ucenter.query.PaymentQuery;
import com.baoli.ucenter.vo.PaymentVo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ys
 * @since 2020-02-02
 */
public interface PaymentService extends IService<Payment> {

    PaymentVo checkPay(PaymentQuery paymentQuery,String memberId);

    RecordTrading pay(PayQuery payQuery);
}
