package com.baoli.manage.pms.controller;


import com.baoli.common.constans.ResultCodeEnum;
import com.baoli.common.vo.R;
import com.baoli.manage.pms.service.BaseParamService;
import com.baoli.pms.entity.BaseParam;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 商品基本属性:搜索属性 前端控制器
 * </p>
 *
 * @author ys
 * @since 2020-01-08
 */
@RestController
@RequestMapping("/pms/base-param")
@Api(tags = "商品基本属性")
public class BaseParamController {
    @Autowired
    private BaseParamService baseParamService;

    @GetMapping
    @ApiOperation("根据cid3查询商品基本属性")
    public R findByCid3(@RequestParam("cid3") Long cid3) {
        List<BaseParam> list = this.baseParamService.list(new LambdaQueryWrapper<BaseParam>().eq(BaseParam::getCid, cid3));
        if (CollectionUtils.isEmpty(list)) {
            return R.setResult(ResultCodeEnum.NOT_FOUND);
        }
        return R.ok().data(list);
    }
}

