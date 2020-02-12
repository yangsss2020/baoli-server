package com.baoli.manage.sms.controller;


import com.baoli.common.vo.R;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author ys
 * @since 2020-02-11
 */
@RestController
@RequestMapping("/sms/admin")
public class AdminController {
    @GetMapping("/current")
    @ApiOperation("获取当前登陆的用户")
    public R currentAdminName(Authentication authentication){
        Object principal = authentication.getPrincipal();
        return R.ok().data(principal);
    }
}

