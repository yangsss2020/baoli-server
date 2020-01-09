package com.baoli.common.handle;

import com.baoli.common.constans.ResultCodeEnum;
import com.baoli.common.exception.BaoliException;
import com.baoli.common.util.ExceptionUtil;
import com.baoli.common.vo.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.naming.SizeLimitExceededException;

/**
 * @author ys
 * @create 2020-01-09-1:35
 */
@ControllerAdvice
@Slf4j
@ResponseBody
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public R error(Exception e) {
        log.error(ExceptionUtil.getMessage(e));
        return R.error();
    }

    @ExceptionHandler(BadSqlGrammarException.class)
    @ResponseBody
    public R error(BadSqlGrammarException e) {
        log.error(ExceptionUtil.getMessage(e));
        return R.setResult(ResultCodeEnum.BAD_SQL_GRAMMAR);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseBody
    public R error(HttpMessageNotReadableException e) {
        return R.setResult(ResultCodeEnum.JSON_PARSE_ERROR);
    }


    @ExceptionHandler(SizeLimitExceededException.class)
    @ResponseBody
    public R error(SizeLimitExceededException e) {
        log.error(ExceptionUtil.getMessage(e));
        return R.setResult(ResultCodeEnum.FILE_UPLOAD_ERROR);
    }

    @ExceptionHandler(BaoliException.class)
    public R error(BaoliException e) {
        log.error(ExceptionUtil.getMessage(e));
        return R.error().message(e.getMessage()).code(e.getCode());
    }
}
