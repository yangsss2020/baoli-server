package com.baoli.ums.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author ys
 * @since 2020-02-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("ums_chat_record")
@ApiModel(value="ChatRecord对象", description="")
public class ChatRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    @ApiModelProperty(value = "用户id")
    private String userId;

    @ApiModelProperty(value = "朋友id")
    private String friendId;

    @ApiModelProperty(value = "是否已读")
    private Boolean hasRead;

    @ApiModelProperty(value = "逻辑删除")
    private Boolean delete;

    @ApiModelProperty(value = "文本消息")
    private String textMsg;

    @ApiModelProperty(value = "图片")
    private String imageMsg;

    @ApiModelProperty(value = "消息类型:0 用户消息 1->系统消息")
    private Integer type;

    @ApiModelProperty(value = "消息内容消息:0->文本消息,1->图片消息,2->语音消息")
    private Integer msgType;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;


}
