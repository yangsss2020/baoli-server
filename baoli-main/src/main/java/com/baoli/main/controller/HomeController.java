package com.baoli.main.controller;

import com.baoli.common.constans.MainCacheConstant;
import com.baoli.common.vo.R;
import com.baoli.main.pms.service.CarouselService;
import com.baoli.main.pms.service.GoodsService;
import com.baoli.main.query.GoodsQuery;
import com.baoli.main.sms.service.NavbarService;
import com.baoli.main.sms.service.NoticeService;
import com.baoli.main.vo.Goods;
import com.baoli.pms.entity.Carousel;
import com.baoli.sms.entity.Navbar;
import com.baoli.sms.entity.Notice;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.redisson.api.RBucket;
import org.redisson.api.RList;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
    @Autowired
    private NoticeService noticeService;
    @Autowired
    private NavbarService navbarService;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private RedissonClient redissonClient;

    @ApiOperation("首页数据")
    @PostMapping("getpageconfig")
    public R getPageConfig() throws ExecutionException, InterruptedException {
        //异步获取数据
        ExecutorService pool = Executors.newFixedThreadPool(10);

        CompletableFuture<List<Carousel>> s1 = CompletableFuture.supplyAsync(() -> {
            RList<Carousel> rList = redissonClient.getList(MainCacheConstant.CAROUSEL_ALL);
            List<Carousel> carouselList;
            if (rList.isEmpty()) {
                carouselList = this.carouselService.findAll();
                rList.addAll(carouselList);
            } else {
                carouselList = rList.readAll();
            }
            return carouselList;
        }, pool);
        CompletableFuture<List<Notice>> s2 = CompletableFuture.supplyAsync(() -> {
            RList<Notice> rList = this.redissonClient.getList(MainCacheConstant.NOTICE_ALL);
            List<Notice> noticeList;
            if (rList.isEmpty()) {
                noticeList = this.noticeService.list(new LambdaQueryWrapper<>(new Notice().setEnable(true)));
                rList.addAll(noticeList);
            } else {
                noticeList = rList.readAll();
            }
            return noticeList;
        });
        CompletableFuture<IPage<Navbar>> s3 = CompletableFuture.supplyAsync(() -> {
            IPage<Navbar> page = new Page<>(1, 4);
            RBucket<IPage<Navbar>> bucket = redissonClient.getBucket(MainCacheConstant.NAVBAR_ALL);
            IPage<Navbar> iPage;
            if (bucket.isExists()) {
                iPage = bucket.get();
            } else {
                iPage = this.navbarService.page(page, new LambdaQueryWrapper<>(new Navbar().setEnable(true)));
                bucket.set(iPage);
            }
            return iPage;
        });
        CompletableFuture<List<Goods>> s4 = CompletableFuture.supplyAsync(() -> this.goodsService.findGoods("sold"));
        CompletableFuture<List<Goods>> s5 = CompletableFuture.supplyAsync(() -> this.goodsService.findGoods("createTime"));
        CompletableFuture<Void> allOf = CompletableFuture.allOf(s1, s2, s3, s4, s5);
//        allOf.join();
        allOf.get();
        List<Carousel> carouselList = s1.get();
        List<Notice> noticeList = s2.get();
        IPage<Navbar> navbarIPage = s3.get();
        List<Goods> hotGoodsList = s4.get();
        List<Goods> newGoods = s5.get();
        //1 首页轮播图
        Map<String, Object> result = new HashMap<>();
        result.put("id", 1);
        result.put("code", "mobile_home");
        result.put("name", "移动端首页");
        result.put("desc", "移动端首页相关操作，可视化移动端、小程序端首页布局");
        result.put("layout", 1);
        result.put("type", 1);
        List<Object> items = new ArrayList<>();
        //1 首页轮播图

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
        Map<String, Object> style = new HashMap<>();
        style.put("top", 8);
        style.put("left", 0);
        recordData.put("style", style);
        Map<String, Object> recordResult = generateResult(1124, "record", "mobile_home", 2, 3, recordData);
        items.add(recordResult);
        //4.公告
//        noticeList = this.noticeService.list(new LambdaQueryWrapper<>(new Notice().setEnable(true)));
        Map<String, Object> noticeData = new HashMap<>();
        noticeData.put("type", "auto");
        noticeData.put("list", noticeList);
        Map<String, Object> noticeResult = generateResult(1125, "notice", "mobile_home", 3, 4, noticeData);
        items.add(noticeResult);
        //5.navbar数据
//        IPage<Navbar> page = new Page<>(1, 4);
//        IPage<Navbar> navbarIPage = this.navbarService.page(page, new LambdaQueryWrapper<>(new Navbar().setEnable(true)));
        Map<String, Object> navbarData = new HashMap<>();
        navbarData.put("limit", navbarIPage.getSize());
        navbarData.put("list", navbarIPage.getRecords());
        Map<String, Object> navbarResult = generateResult(1126, "navBar", "mobile_home", 4, 5, navbarData);
        items.add(navbarResult);
//        List<Goods> hotGoodsList = this.goodsService.findGoods("sold");
        //7.猜你喜欢
        Map<String, Object> likeGoodsData = new HashMap<>();
        likeGoodsData.put("title", "猜你喜欢");
        likeGoodsData.put("lookMore", "false");
        likeGoodsData.put("type", "auto");
        likeGoodsData.put("classifyId", "");
        likeGoodsData.put("brandId", "");
        likeGoodsData.put("limit", 6);
        likeGoodsData.put("display", "slide");
        likeGoodsData.put("column", 2);
        likeGoodsData.put("list", hotGoodsList);
        Map<String, Object> likeGoodsResult = generateResult(1150, "goods", "mobile_home", 7, 8, likeGoodsData);
        items.add(likeGoodsResult);
        //8.热销商品数据
        Map<String, Object> hotGoodsData = new HashMap<>();
        hotGoodsData.put("title", "热销商品");
        hotGoodsData.put("lookMore", "true");
        hotGoodsData.put("type", "auto");
        hotGoodsData.put("classifyId", "");
        hotGoodsData.put("brandId", "");
        hotGoodsData.put("limit", 6);
        hotGoodsData.put("display", "list");
        hotGoodsData.put("column", 2);
        hotGoodsData.put("list", hotGoodsList);
        Map<String, Object> hotGoodsResult = generateResult(1151, "goods", "mobile_home", 8, 9, hotGoodsData);
        hotGoodsResult.put("sortBy", "sold");
        items.add(hotGoodsResult);
        //9.最新发布
//        List<Goods> newGoods = this.goodsService.findGoods("createTime");
        Map<String, Object> newGoodsData = new HashMap<>();
        newGoodsData.put("title", "最新发布");
        newGoodsData.put("lookMore", "true");
        newGoodsData.put("type", "auto");
        newGoodsData.put("classifyId", "");
        newGoodsData.put("brandId", "");
        newGoodsData.put("limit", 6);
        newGoodsData.put("display", "list");
        newGoodsData.put("column", 2);
        newGoodsData.put("list", newGoods);
        Map<String, Object> newGoodsResult = generateResult(1152, "goods", "mobile_home", 9, 19, newGoodsData);
        newGoodsResult.put("sortBy", "createTime");
        items.add(newGoodsResult);
        //全部数据添加
        result.put("items", items);
        return R.ok().data(result);
    }

    @PostMapping("test")
    public R searchGoods(@RequestBody GoodsQuery goodsQuery) {
        System.out.println(goodsQuery);
        return R.ok();
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
