package com.baoli.sms.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author ys
 * @since 2020-02-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sms_payment")
@ApiModel(value="Payment对象", description="")
public class Payment implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "支付码")
    private String code;

    @ApiModelProperty(value = "是否线上支付:0 否 1是")
    @TableField(value = "is_online")
    private Boolean online;

    @ApiModelProperty(value = "备忘")
    private String memo;

    @ApiModelProperty(value = "支付名称")
    private String name;

    @ApiModelProperty(value = "是否支持:0 否 1是")
    private Boolean status;

    @ApiModelProperty(value = "排序")
    private Integer sort;


}
