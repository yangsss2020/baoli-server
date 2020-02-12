package com.baoli.pms.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 首页轮播图
 * </p>
 *
 * @author ys
 * @since 2020-01-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("pms_carousel")
@ApiModel(value = "Carousel对象", description = "首页轮播图")
public class Carousel implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("link_value")
    private String linkValue;

    @TableField("link_type")
    private Integer linkType;

    @ApiModelProperty(value = "图片地址")
    private String image;

    @ApiModelProperty(value = "逻辑删除:")
    @TableLogic
    private Boolean deleted;

    @ApiModelProperty(value = "是否可用:0->不可用,1->可用")
    private Boolean enable;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;


}
