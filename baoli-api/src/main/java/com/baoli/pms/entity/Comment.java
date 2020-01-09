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
 * 评论列表
 * </p>
 *
 * @author ys
 * @since 2020-01-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("pms_comment")
@ApiModel(value="Comment对象", description="评论列表")
public class Comment implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long spuId;

    @ApiModelProperty(value = "会员id")
    private String memberId;

    @ApiModelProperty(value = "评论内容")
    private String commit;

    @ApiModelProperty(value = "父级id")
    private Long parentId;

    @ApiModelProperty(value = "逻辑删除:")
    @TableLogic
    private Boolean deleted;

    @ApiModelProperty(value = "评论ip地址")
    private String memberIp;

    @ApiModelProperty(value = "评论星级:0->5")
    private Integer star;

    @ApiModelProperty(value = "评论图片")
    private String images;

    @ApiModelProperty(value = "用户头像")
    private String avatar;

    @ApiModelProperty(value = "是否是夫级:0->是 1->否")
    private Boolean parent;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;


}
