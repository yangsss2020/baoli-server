package com.baoli.main.pms.controller;

import com.baoli.common.vo.R;
import com.baoli.main.pms.service.CarouselService;
import com.baoli.pms.entity.Carousel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ys
 * @create 2020-01-15-11:29
 */
@Api("pages数据")
@RequestMapping("pages")
@RestController
public class HomeController {
    @Autowired
    private CarouselService carouselService;

    @ApiOperation("首页数据")
    @PostMapping("getpageconfig")
    public R getPageConfig() {
        Map<String, Object> result = new HashMap<>();
        result.put("id", 1);
        result.put("code", "mobile_home");
        result.put("name", "移动端首页");
        result.put("desc", "移动端首页相关操作，可视化移动端、小程序端首页布局");
        result.put("layout", 1);
        result.put("type", 1);
        List<Object> items = new ArrayList<>();
        //1 首页轮播图
        List<Carousel> carouselList = this.carouselService.findAll();
        Map<String, Object> carouseData = new HashMap<>();
        carouseData.put("duration", 2500);
        carouseData.put("list", carouselList);
        Map<String, Object> carouselResult = generateResult(1122, "imgSlide", "mobile_home", 0, 1, carouseData);
        items.add(carouselResult);
        //2 搜索框
        Map<String, Object> searchData = new HashMap<>();
        searchData.put("keywords", "请输入关键字搜索");
        searchData.put("style", "round");
        Map<String, Object> searchResult = generateResult(1123, "search", "mobile_home", 1, 2, searchData);
        items.add(searchResult);
        //3.record
        Map<String, Object> recordData = new HashMap<>();
        Map<String,Object> style = new HashMap<>();
        style.put("top",8);
        style.put("left",0);
        recordData.put("style",style);
        Map<String, Object> recordResult = generateResult(1124, "record", "mobile_home", 2, 3, recordData);
        items.add(recordResult);


        result.put("items", items);
        return R.ok().data(result);
    }

    private Map<String, Object> generateResult(Integer id, String widget_code, String page_code, Integer position_id, Integer sort, Object data) {
        Map<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("widget_code", widget_code);
        result.put("page_code", page_code);
        result.put("position_id", position_id);
        result.put("sort", sort);
        result.put("params", data);
        return result;
    }
}
