package com.baoli.pms.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * spu详情
 * </p>
 *
 * @author ys
 * @since 2020-01-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("pms_spu_detail")
@ApiModel(value="SpuDetail对象", description="spu详情")
public class SpuDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "spu_id", type = IdType.ID_WORKER_STR)
    private Long spuId;

    @ApiModelProperty(value = "商品详情")
    private String description;

    @ApiModelProperty(value = "销售属性")
    private String saleParam;

    @ApiModelProperty(value = "基本属性")
    private String baseParam;

    @ApiModelProperty(value = "售后服务")
    private String afterService;

    @ApiModelProperty(value = "商品轮播图")
    private String bannerImages;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;


}
