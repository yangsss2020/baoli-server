package com.baoli.oms.entity;

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
 * 购物车列表
 * </p>
 *
 * @author ys
 * @since 2020-01-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("oms_cart")
@ApiModel(value="Cart对象", description="购物车列表")
public class Cart implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long skuId;

    private Long spuId;

    private String memberId;

    @ApiModelProperty(value = "购买数量")
    private Integer quantity;

    @ApiModelProperty(value = "价格")
    private BigDecimal price;

    @ApiModelProperty(value = "销售属性")
    private String skuParam;

    @ApiModelProperty(value = "sku图片")
    private String skuIamges;

    @ApiModelProperty(value = "商品名称")
    private String skuName;

    @ApiModelProperty(value = "会员昵称")
    private String memberNickname;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    @ApiModelProperty(value = "是否选中")
    private Boolean selected;


}
