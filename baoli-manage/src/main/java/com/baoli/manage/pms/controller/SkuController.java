package com.baoli.manage.pms.controller;


import com.baoli.common.constans.ResultCodeEnum;
import com.baoli.common.vo.R;
import com.baoli.manage.pms.service.SkuService;
import com.baoli.manage.pms.service.SkuStockService;
import com.baoli.manage.query.SkuQuery;
import com.baoli.vo.SkuVo;
import com.baoli.pms.entity.Sku;
import com.baoli.pms.entity.SkuStock;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author ys
 * @since 2020-01-08
 */
@RestController
@RequestMapping("/pms/sku")
@Api(tags = "商品sku信息")
public class SkuController {
    @Autowired
    private SkuService skuService;
    @Autowired
    private SkuStockService skuStockService;

    @GetMapping
    @ApiOperation("根据spuId查询sku")
    public R findBySpuId(@RequestParam Long spuId) {
        List<Sku> skus = this.skuService.list(new LambdaQueryWrapper<Sku>().eq(Sku::getSpuId, spuId));
        if (CollectionUtils.isEmpty(skus)) {
            return R.setResult(ResultCodeEnum.NOT_FOUND);
        }
        return R.ok().data(skus);
    }

    @GetMapping("skus")
    @ApiOperation("根据spuid查询sku及库存")
    public R findSkuAndStockBySpuId(@RequestParam Long spuId) {
        List<Sku> skus = this.skuService.list(new LambdaQueryWrapper<Sku>().eq(Sku::getSpuId, spuId));
        if (CollectionUtils.isEmpty(skus)) {
            return R.setResult(ResultCodeEnum.NOT_FOUND);
        }
        List<SkuVo> list = skus.stream().map(sku -> {
            SkuVo skuVo = new SkuVo();
            BeanUtils.copyProperties(sku, skuVo);
            System.out.println(sku);
            System.out.println(skuVo.getTitle());
            SkuStock stock = this.skuStockService.getById(sku.getId());
            skuVo.setSkuStock(stock);
            return skuVo;
        }).collect(Collectors.toList());
        return R.ok().data(list);
    }

    @PostMapping("{spuId}")
    @ApiOperation("新增sku")
    public R save(@PathVariable Long spuId, @RequestBody List<SkuVo> skuVoList) {
        this.skuService.saveSku(spuId, skuVoList);
        return R.ok();
    }
    @PostMapping("sale-param")
    @ApiOperation("新增sku和spuParam属性")
    public R saveSkuAndParam(@RequestBody SkuQuery skuQuery){
        this.skuService.saveSkuAndParam(skuQuery);
        return R.ok();
    }
}

