package com.baoli.ucenter.sms.controller;


import com.baoli.common.constans.ResultCodeEnum;
import com.baoli.common.vo.R;
import com.baoli.rms.entity.RecordTrading;
import com.baoli.sms.entity.Payment;
import com.baoli.ucenter.interceptor.LoginInterceptor;
import com.baoli.ucenter.query.PaymentQuery;
import com.baoli.ucenter.rms.service.RecordTradingService;
import com.baoli.ucenter.sms.service.PaymentService;
import com.baoli.ucenter.vo.PaymentVo;
import com.baoli.ums.entity.Member;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author ys
 * @since 2020-02-02
 */
@RestController
@RequestMapping("/sms/payment")
@Api(tags = "支付接口")
public class PaymentController {
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private RecordTradingService recordTradingService;
    @PostMapping("getlist")
    public R getList(){
        List<Payment> list = this.paymentService.list(new LambdaQueryWrapper<>(new Payment().setStatus(true)));
        if(CollectionUtils.isEmpty(list)){
            return R.setResult(ResultCodeEnum.NOT_FOUND);
        }else {
            return R.ok().data(list);
        }
    }

    @PostMapping("checkpay")
    @ApiOperation("支付检查")
    public R checkpay(@RequestBody PaymentQuery paymentQuery){
        Member member = LoginInterceptor.getMember();
        PaymentVo paymentVo= this.paymentService.checkPay(paymentQuery,member.getId());
        return R.ok().data(paymentVo);
    }
    @PostMapping("getinfo")
    @ApiModelProperty("根据id获取交易记录")
    public R getRecord(@RequestBody Map<String,Long> map){
        RecordTrading record = this.recordTradingService.getById(map.get("orderId"));
        if(record==null){
            return R.setResult(ResultCodeEnum.NOT_FOUND);
        }else {
            return R.ok().data(record);
        }
    }
}

