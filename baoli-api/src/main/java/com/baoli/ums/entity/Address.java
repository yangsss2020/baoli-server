package com.baoli.ums.entity;

import com.baomidou.mybatisplus.annotation.IdType;
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
 * @since 2020-01-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("ums_address")
@ApiModel(value="Address对象", description="")
public class Address implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String memberId;

    @ApiModelProperty(value = "收货姓名")
    private String name;

    @ApiModelProperty(value = "收货手机号")
    private String phone;

    @ApiModelProperty(value = "地区地址")
    private String areaName;

    @ApiModelProperty(value = "详细地址")
    private String address;

    private Integer areaId;

    @ApiModelProperty(value = "是否默认")
    private Boolean dft;


}
