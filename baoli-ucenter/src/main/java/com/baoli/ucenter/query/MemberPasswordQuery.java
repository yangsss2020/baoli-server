package com.baoli.ucenter.query;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * @author ys
 * @create 2020-02-05-13:17
 */
@Data
public class MemberPasswordQuery {
    private String memberId;
    @Length(max = 30,min = 4,message = "密码长度必须4-30")
    private String password;
    @Length(max = 30,min = 4,message = "密码长度必须4-30")
    private String newPassword;
    @Length(max = 30,min = 4,message = "密码长度必须4-30")
    private String rePassword;
}
