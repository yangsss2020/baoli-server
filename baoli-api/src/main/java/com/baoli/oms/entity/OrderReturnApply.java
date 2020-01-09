package com.baoli.oms.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 退货订单
 * </p>
 *
 * @author ys
 * @since 2020-01-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("oms_order_return_apply")
@ApiModel(value="OrderReturnApply对象", description="退货订单")
public class OrderReturnApply implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "订单id")
    private String orderId;

    @ApiModelProperty(value = "退货商品id")
    private Long skuId;

    @ApiModelProperty(value = "会员id")
    private String memberId;

    @ApiModelProperty(value = "退款金额")
    private BigDecimal returnAmount;

    @ApiModelProperty(value = "退货姓名")
    private String returnName;

    @ApiModelProperty(value = "退货号码")
    private String returnPhone;

    @ApiModelProperty(value = "申请状态:0->待处理, 1->退货中,2->已完成,3->已拒绝")
    private Boolean status;

    @ApiModelProperty(value = "商品图片")
    private String skuImages;

    @ApiModelProperty(value = "商品名称")
    private String skuName;

    @ApiModelProperty(value = "退货数量")
    private Integer skuCount;

    @ApiModelProperty(value = "商品单价")
    private BigDecimal skuPrice;

    @ApiModelProperty(value = "退货原因")
    private String reason;


}
