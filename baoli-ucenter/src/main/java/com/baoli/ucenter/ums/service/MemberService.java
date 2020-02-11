package com.baoli.ucenter.ums.service;

import com.baoli.ucenter.query.MemberPasswordQuery;
import com.baoli.ucenter.query.MemberQuery;
import com.baoli.ums.entity.Member;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ys
 * @since 2020-01-08
 */
public interface MemberService extends IService<Member> {

    void sendVerifyCode(String phone);

    String register(MemberQuery memberQuery);

    String login(MemberQuery memberQuery);

    Member getMember(String token);

    void changePassword(MemberPasswordQuery memberPassword);
}
