package com.baoli.main.pms.service.impl;

import com.baoli.main.pms.mapper.SpuDetailMapper;
import com.baoli.pms.entity.Spu;
import com.baoli.main.pms.mapper.SpuMapper;
import com.baoli.main.pms.service.SpuService;
import com.baoli.pms.entity.SpuDetail;
import com.baoli.vo.SpuVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 商品spu 服务实现类
 * </p>
 *
 * @author ys
 * @since 2020-01-08
 */
@Service
public class SpuServiceImpl extends ServiceImpl<SpuMapper, Spu> implements SpuService {
    @Autowired
    private SpuMapper spuMapper;
    @Autowired
    private SpuDetailMapper spuDetailMapper;


    @Override
    public SpuVo findSpuVoById(long spuId) {
        Spu spu = this.spuMapper.selectById(spuId);
        SpuDetail spuDetail = this.spuDetailMapper.selectById(spuId);
        SpuVo spuVo = new SpuVo();
        BeanUtils.copyProperties(spu,spuVo);
        spuVo.setSpuDetail(spuDetail);
        return spuVo;
    }

    @Override
    public List<SpuVo> findSpuVo() {
        List<Spu> spuList = spuMapper.selectList(new LambdaQueryWrapper<>(new Spu().setSaleable(true)));
        List<SpuVo> list = spuList.stream().map(spu -> {
            SpuVo spuVo = new SpuVo();
            BeanUtils.copyProperties(spu, spuVo);
            SpuDetail spuDetail = this.spuDetailMapper.selectById(spu.getId());
            spuVo.setSpuDetail(spuDetail);
            return spuVo;
        }).collect(Collectors.toList());
        return list;
    }
}
