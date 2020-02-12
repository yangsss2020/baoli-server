package com.baoli.ums.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author ys
 * @since 2020-01-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("ums_member")
@ApiModel(value="Member对象", description="")
public class Member implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    @ApiModelProperty(value = "分享人id")
    private String superiorId;

    @ApiModelProperty(value = "等级id")
    private Long memberLevelId;

    @ApiModelProperty(value = "用户名称")
    private String username;

    @ApiModelProperty(value = "用户昵称")
    @Length(min = 2,max = 20,message = "昵称格式错误")
    private String nickname;

    @ApiModelProperty(value = "手机号")
    @Pattern(regexp = "^1[35678]\\d{9}$", message = "手机号格式不正确")
    private String phone;

    @ApiModelProperty(value = "账号状态:0->禁用,1->启用")
    private Boolean status;

    @ApiModelProperty(value = "头像")
    private String avatar;

    private String openId;

    @ApiModelProperty(value = "性别:0->未知,1->男,2->女")
    private Integer gender;

    private Date birthday;
    @ApiModelProperty(value = "所在城市")
    private String city;

    @ApiModelProperty(value = "余额")
    private BigDecimal balance;

    @ApiModelProperty(value = "宝励豆")
    private BigDecimal bean;

    @ApiModelProperty(value = "密码盐")
    @JsonIgnore
    private String salt;
    @ApiModelProperty(value = "密码")
    @JsonIgnore
    @Length(max = 30,min = 4,message = "密码长度必须4-30")
    public String password;

}
