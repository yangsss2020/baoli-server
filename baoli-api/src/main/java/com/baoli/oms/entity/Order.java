package com.baoli.oms.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 订单表
 * </p>
 *
 * @author ys
 * @since 2020-01-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("oms_order")
@ApiModel(value = "Order对象", description = "订单表")
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "order_id", type = IdType.ID_WORKER_STR)
    private String orderId;

    private String memberId;
    @ApiModelProperty(value = "订单总价格")
    private BigDecimal totalPrice;

    @ApiModelProperty(value = "支付方式:0->未支付,1->余额,2->宝励豆,3->微信支付,4->支付宝,5->其他")
    private Integer payType;

    @ApiModelProperty(value = "订单状态:0->待付款,1->代发货,2->已发货,3->已完成,4->待评价,5->已关闭,6->无效订单")
    private Integer status;

    @ApiModelProperty(value = "订单类型:0->正常订单,1->秒杀订单")
    private Integer orderType;

    @ApiModelProperty(value = "快递公司")
    private String deliveryCompany;

    @ApiModelProperty(value = "快递单号")
    private String deliverySn;

    @ApiModelProperty(value = "收货人")
    private String receiverName;

    @ApiModelProperty(value = "收货号码")
    private String receiverPhone;
    @ApiModelProperty(value = "收货区域")
    private String receiverArea;

    @ApiModelProperty(value = "详细地址")
    private String receiverDetailAddress;

    @ApiModelProperty(value = "备注")
    private String note;

    @ApiModelProperty(value = "逻辑删除")
    @TableLogic
    private Boolean deleted;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    @ApiModelProperty(value = "支付时间")
    private Date paymentTime;

    @ApiModelProperty(value = "发货时间")
    private Date deliveryTime;

    @ApiModelProperty(value = "确认收货时间")
    private Date receiveTime;

    @ApiModelProperty(value = "评论时间")
    private Date commentTime;
    @TableField(exist = false)
    private List<OrderItem> items = new ArrayList<>();

}
