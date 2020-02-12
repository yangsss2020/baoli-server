package com.baoli.sms.entity;

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
 * 首页导航
 * </p>
 *
 * @author ys
 * @since 2020-01-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sms_navbar")
@ApiModel(value="Navbar对象", description="首页导航")
public class Navbar implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "标题")
    private String text;

    private String linkValue;

    @ApiModelProperty(value = "类型:0->普通,1->路由,2->商品详情,3->文章详情,4->商品列表,5->文章列表")
    private String linkType;

    private String image;

    @ApiModelProperty(value = "是否启用:0->否,1->是")
    private Boolean enable;

    @ApiModelProperty(value = "逻辑删除")
    @TableLogic
    private Boolean deleted;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;


}
