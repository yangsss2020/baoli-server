package com.baoli.rms.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


/**
 * <p>
 * 交易记录表
 * </p>
 *
 * @author ys
 * @since 2020-02-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("rms_record_trading")
@ApiModel(value = "RecordTrading对象", description = "交易记录表")
public  class RecordTrading implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "会员id")
    private String memberId;

    @ApiModelProperty(value = "订单编号")
    private String orderSn;

    @ApiModelProperty(value = "会员账号")
    private String username;

    @ApiModelProperty(value = "交易金额")
    private BigDecimal money;

    @ApiModelProperty(value = "交易类型: 1 订单,2 转账 3 充值")
    private Integer type;

    @ApiModelProperty(value = "支付方式:0->未支付,1->余额,2->宝励豆,3->微信支付,4->支付宝,5->其他")
    private Integer payType;

    @ApiModelProperty(value = "备注")
    private String note;

    @ApiModelProperty(value = "交易说明")
    private String explan;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "交易时间")
    private Date time;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
}
