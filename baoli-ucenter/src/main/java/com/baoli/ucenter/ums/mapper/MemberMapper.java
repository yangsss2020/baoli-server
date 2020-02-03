package com.baoli.ucenter.ums.mapper;

import com.baoli.ums.entity.Member;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author ys
 * @since 2020-01-08
 */
public interface MemberMapper extends BaseMapper<Member> {

    int pay(@Param("memberId") String memberId,@Param("price") BigDecimal totalPrice);
}
