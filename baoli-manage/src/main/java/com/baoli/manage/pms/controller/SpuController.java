package com.baoli.manage.pms.controller;


import com.baoli.common.constans.ResultCodeEnum;
import com.baoli.common.vo.R;
import com.baoli.manage.query.SpuQuery;
import com.baoli.manage.vo.SaleParamVo;
import com.baoli.vo.SpuVo;
import com.baoli.pms.entity.Spu;
import com.baoli.manage.pms.service.SpuService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 商品spu 前端控制器
 * </p>
 *
 * @author ys
 * @since 2020-01-08
 */
@RestController
@RequestMapping("/pms/spu")
@Api(tags = "商品spu")
public class SpuController {
    @Autowired
    private SpuService spuService;

    @GetMapping
    @ApiOperation("分页条件查询spu")
    public R findByPage(SpuQuery spuQuery) {
        QueryWrapper<Spu> wrapper = new QueryWrapper<>();

        if (spuQuery.getCid3() != null) {
            wrapper.lambda().eq(Spu::getCid3, spuQuery.getCid3());
        }
        if (spuQuery.getSaleable() != null) {
            wrapper.lambda().eq(Spu::getSaleable, spuQuery.getSaleable());
        }
        if (StringUtils.isNotBlank(spuQuery.getKeywords())) {
            wrapper.lambda().like(Spu::getTitle, "%" + spuQuery.getKeywords() + "%");

        }
        if (!CollectionUtils.isEmpty(spuQuery.getCreateTime())) {
            wrapper.lambda().ge(Spu::getCreateTime, spuQuery.getCreateTime().get(0))
                    .le(Spu::getCreateTime, spuQuery.getCreateTime().get(1));
        }
        if (!CollectionUtils.isEmpty(spuQuery.getUpdateTime())) {
            wrapper.lambda().ge(Spu::getUpdateTime, spuQuery.getUpdateTime().get(0))
                    .le(Spu::getUpdateTime, spuQuery.getUpdateTime().get(1));
        }
        Page<Spu> page = new Page<>(spuQuery.getPage(), spuQuery.getSize());
        this.spuService.page(page, wrapper);

        return R.ok().data(R.pageResult(page));
    }

    @PostMapping
    @ApiOperation("新增spu")
    public R save(@RequestBody SpuVo spuVo){
        this.spuService.saveSpu(spuVo);
        return R.ok();
    }
    @PutMapping
    @ApiOperation("修改spu")
    public R update(@RequestBody SpuVo spuVo){
        this.spuService.updateSpu(spuVo);
        return R.ok();
    }
    @DeleteMapping("{id}")
    @ApiOperation("删除spu")
    public R delete(@PathVariable Long id){
        this.spuService.deleteSpu(id);
        return R.ok();
    }

    @GetMapping("sale-param")
    @ApiOperation("查找spu的销售属性")
    public R findSaleParamBySpuId(@RequestParam("spuId") Long spuId){
       List<SaleParamVo> list= this.spuService.findSaleParamBySpuId(spuId);
       if(CollectionUtils.isEmpty(list)){
           return R.setResult(ResultCodeEnum.NOT_FOUND);
       }
       return R.ok().data(list);
    }
}

