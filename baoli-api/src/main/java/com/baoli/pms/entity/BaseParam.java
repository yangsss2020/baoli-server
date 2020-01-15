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
 * 商品基本属性:搜索属性
 * </p>
 *
 * @author ys
 * @since 2020-01-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("pms_base_param")
@ApiModel(value="BaseParam对象", description="商品基本属性:搜索属性")
public class BaseParam implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "所属分类")
    private Long cid;

    @ApiModelProperty(value = "属性名称")
    private String name;

    @TableField("`numeric`")
    @ApiModelProperty(value = "数值类型参数:0->非数字,1->数字类型")
    private Boolean numeric;

    @ApiModelProperty(value = "数值类型单位")
    private String unit;

    @ApiModelProperty(value = "是否搜索")
    private Boolean searching;

    @ApiModelProperty(value = "数值类型分段")
    private String segments;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;


}
