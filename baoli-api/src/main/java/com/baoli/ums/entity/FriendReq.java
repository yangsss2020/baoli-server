package com.baoli.ums.entity;

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
 * 
 * </p>
 *
 * @author ys
 * @since 2020-02-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("ums_friend_req")
@ApiModel(value="FriendReq对象", description="")
public class FriendReq implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    @ApiModelProperty(value = "请求好友id")
    private String fromUserId;

    @ApiModelProperty(value = "被请求好友id")
    private String toUserId;

    @ApiModelProperty(value = "发送时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "消息内容")
    private String message;

    @ApiModelProperty(value = "是否已处理")
    private Boolean status;


}
