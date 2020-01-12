package com.baoli.common.vo;

import com.baoli.common.constans.ResultCodeEnum;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ys
 * @create 2020-01-09-1:10
 */
@Data
@ApiModel("全局同意返回结果")
public class R {
    @ApiModelProperty("返回码")
    private Integer code;
    @ApiModelProperty("是否成功")
    private Boolean success;
    @ApiModelProperty("返回消息")
    private String message;
    @ApiModelProperty("返回数据")
    private Object data;

    private R() {
    }

    public static R ok() {
        R r = new R();
        r.setCode(ResultCodeEnum.SUCCESS.getCode());
        r.setSuccess(ResultCodeEnum.SUCCESS.getSuccess());
        r.setMessage(ResultCodeEnum.SUCCESS.getMessage());
        return r;
    }

    public static R error() {
        R r = new R();
        r.setCode(ResultCodeEnum.UNKNOWN_REASON.getCode());
        r.setSuccess(ResultCodeEnum.UNKNOWN_REASON.getSuccess());
        r.setMessage(ResultCodeEnum.UNKNOWN_REASON.getMessage());
        return r;
    }

    public R data(Object data) {
        this.setData(data);
        return this;
    }

//    public R data(String key, Object value) {
//        this.data.put(key, value);
//        return this;
//    }

    public R message(String message) {
        this.setMessage(message);
        return this;
    }

    public R code(Integer code) {
        this.setCode(code);
        return this;
    }

    public R success(Boolean success) {
        this.setSuccess(success);
        return this;
    }

    public static R setResult(ResultCodeEnum resultCodeEnum) {
        R r = new R();
        r.setCode(resultCodeEnum.getCode());
        r.setSuccess(resultCodeEnum.getSuccess());
        r.setMessage(resultCodeEnum.getMessage());
        return r;
    }

    public static Map<String, Object> pageResult(Page page) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("total", page.getTotal());
        map.put("size", page.getSize());
        map.put("pages", page.getPages());
        map.put("data", page.getRecords());
        return map;
    }
}
