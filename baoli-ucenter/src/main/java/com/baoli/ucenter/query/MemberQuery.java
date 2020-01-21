package com.baoli.ucenter.query;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Pattern;

/**
 * @author ys
 * @create 2020-01-19-12:40
 */
@Data
public class MemberQuery {
    @Pattern(regexp = "^1[35678]\\d{9}$", message = "手机号格式不正确")
    private String mobile;
    private String code;
    @Length(max = 30,min = 4,message = "密码长度必须4-30")
    private String password;
}
