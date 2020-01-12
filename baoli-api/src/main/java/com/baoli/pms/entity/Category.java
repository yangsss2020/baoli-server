package com.baoli.pms.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 商品分类
 * </p>
 *
 * @author ys
 * @since 2020-01-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("pms_category")
@ApiModel(value = "Category对象", description = "商品分类")
public class Category implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "分类名称")
    private String name;

    @ApiModelProperty(value = "上级分类id")
    private Long parentId;

    @ApiModelProperty(value = "是否是父级:0->否 ,1->是")
    private Boolean parent;

    @ApiModelProperty(value = "是否是父级:0->否 ,1->是")
    private Boolean enable;

    @ApiModelProperty(value = "是否上架:0->否 ,1->是")
    @TableLogic
    private Boolean deleted;

    @ApiModelProperty(value = "逻辑删除")
    private String images;
    @TableField(exist = false)
    private List<Category> children;


}
