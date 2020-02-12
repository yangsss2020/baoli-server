package com.baoli.ucenter.ums.service;

import com.baoli.ums.entity.Address;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ys
 * @since 2020-01-30
 */
public interface AddressService extends IService<Address> {

    void saveAddress(Address address);
}
