package com.baoli.main.pms.controller;

import com.baoli.common.constans.ResultCodeEnum;
import com.baoli.common.util.ExceptionUtil;
import com.baoli.common.vo.R;
import com.baoli.main.pms.service.GoodsService;
import com.baoli.main.query.GoodsQuery;
import com.baoli.main.vo.GoodsDetile;
import com.baoli.main.vo.GoodsSearchVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * @author ys
 * @create 2020-01-15-18:40
 */
@RestController
@RequestMapping("goods")
@Api(tags = "商品")
@Slf4j
public class GoodsController {
    @Autowired
    private GoodsService goodsService;
    @PostMapping("getlist")
    public R searchGoods(@RequestBody GoodsQuery goodsQuery){
        GoodsSearchVo searchResult = this.goodsService.search(goodsQuery);
//        if(CollectionUtils.isEmpty(list)){
//            return R.setResult(ResultCodeEnum.NOT_FOUND);
//        }
        return R.ok().data(searchResult);
    }
    @ApiOperation("获取商品详情")
    @PostMapping("getdetial")
    public R getDetail(@RequestBody Map<String,Object> map){
        GoodsDetile goodsDetile= null;
        try {
            goodsDetile = this.goodsService.getDetail(NumberUtils.toLong(map.get("id").toString()));
        } catch (ExecutionException e) {
            ExceptionUtil.getMessage(e);
        } catch (InterruptedException e) {
            ExceptionUtil.getMessage(e);
        }
        if(goodsDetile!=null){
            return R.ok().data(goodsDetile);
        }
        return R.setResult(ResultCodeEnum.NOT_FOUND);
    }
}
