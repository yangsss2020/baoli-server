package com.baoli.manage.pms.controller;


import com.baoli.common.vo.R;
import com.baoli.manage.pms.service.SaleParamService;
import com.baoli.pms.entity.SaleParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 商品销售属性 前端控制器
 * </p>
 *
 * @author ys
 * @since 2020-01-08
 */
@RestController
@RequestMapping("/pms/sale-param")
@Api(tags = "销售属性")
public class SaleParamController {
    @Autowired
    private SaleParamService saleParamService;
    @ApiOperation("新增销售属性")
    @PostMapping
    public R save(
            @RequestBody SaleParam saleParam
    ) {
        boolean flag = this.saleParamService.save(saleParam);
        if(flag){
            return R.ok();
        }else {
            return R.error().message("添加失败");
        }
    }
}

