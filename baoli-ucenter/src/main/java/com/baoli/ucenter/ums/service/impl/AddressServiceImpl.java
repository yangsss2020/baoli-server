package com.baoli.ucenter.ums.service.impl;

import com.baoli.ums.entity.Address;
import com.baoli.ucenter.ums.mapper.AddressMapper;
import com.baoli.ucenter.ums.service.AddressService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ys
 * @since 2020-01-30
 */
@Service
public class AddressServiceImpl extends ServiceImpl<AddressMapper, Address> implements AddressService {
    @Autowired
    private AddressMapper addressMapper;
    @Override
    public void saveAddress(Address address) {
        if (address.getDft()){
            this.addressMapper.changeDft(address.getMemberId());
        }
        this.saveOrUpdate(address);
    }
}
