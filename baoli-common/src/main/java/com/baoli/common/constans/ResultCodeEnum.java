package com.baoli.common.constans;

import lombok.Getter;

/**
 * @author ys
 * @create 2020-01-09-1:16
 */
@Getter
public enum ResultCodeEnum {
    SUCCESS(true, 20000, "成功"),
    UNKNOWN_REASON(false, 20001, "未知错误"),
    NOT_FOUND(false, 20002, "未查询到数据"),
    ERROR(false, 20003, "错误"),
    BAD_SQL_GRAMMAR(false, 21001, "sql语法错误"),
    JSON_PARSE_ERROR(false, 21002, "json解析异常"),
    PARAM_ERROR(false, 21003, "参数不正确"),
    FILE_UPLOAD_ERROR(false, 21004, "文件上传错误"),
    VALID_ERROR(false, 20010, "参数校验错误"),
    LOGIN_ERROR(false, 14006, "登陆错误"),
    NOT_LOGIN_IN(false, 14006, "请登陆再操作"),
    AUTH_DENIED(false, 14003, "您没有权限,请联系管理员"),
    BAD_TOKEN(false, 14008, "无效的token"),
    LOGIN_EXPIRATION(false, 14007, "登陆信息过期"),
    UNDERSTOCK(false, 15001, "库存不足");
    private Boolean success;
    private Integer code;
    private String message;

    ResultCodeEnum(Boolean success, Integer code, String message) {
        this.success = success;
        this.code = code;
        this.message = message;
    }
}
