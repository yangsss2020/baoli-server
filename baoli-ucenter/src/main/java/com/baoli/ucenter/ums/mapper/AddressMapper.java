package com.baoli.ucenter.ums.mapper;

import com.baoli.ums.entity.Address;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author ys
 * @since 2020-01-30
 */
public interface AddressMapper extends BaseMapper<Address> {

    void changeDft(String memberId);
}
