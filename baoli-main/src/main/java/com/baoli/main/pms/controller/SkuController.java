package com.baoli.main.pms.controller;


import com.baoli.common.vo.R;
import com.baoli.main.pms.service.SkuService;
import com.baoli.pms.entity.SkuStock;
import com.baoli.vo.SkuVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author ys
 * @since 2020-01-08
 */
@RestController
@RequestMapping("/pms/sku")
public class SkuController {
    @Autowired
    private SkuService skuService;
    @GetMapping("{id}")
    public R findSkuVoBySkuId(@PathVariable Long id){
        SkuVo skuVo= this.skuService.findSkuVoBySkuId(id);
        return R.ok().data(skuVo);
    }
    @GetMapping("stock/{id}")
    public SkuStock findSkuStockBySkuId(@PathVariable Long id){
       return this.skuService.findSkuStockBySkuId(id);
    }
    @ApiOperation("减库存")
    @GetMapping("stock/decrement/{id}/{quantity}")
    public R stockDecrement(@PathVariable Long id, @PathVariable Integer quantity){
        Boolean flag = this.skuService.stockDecrement(id, quantity);
        if(flag){
            return R.ok();
        }else {
            return R.error();
        }
    }

}

