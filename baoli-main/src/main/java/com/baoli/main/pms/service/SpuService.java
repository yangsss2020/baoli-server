package com.baoli.main.pms.service;

import com.baoli.pms.entity.Spu;
import com.baoli.vo.SpuVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 商品spu 服务类
 * </p>
 *
 * @author ys
 * @since 2020-01-08
 */
public interface SpuService extends IService<Spu> {

    SpuVo findSpuVoById(long spuId);
    List<SpuVo> findSpuVo();
}
