package com.baoli.manage.upload.controller;

import com.baoli.common.vo.R;
import com.baoli.manage.upload.service.UploadService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author ys
 * @create 2020-01-09-19:42
 */
@RestController
@RequestMapping("upload")
@Api(tags = "文件上传")
public class UploadController {
    @Autowired
    private UploadService uploadService;

    @PostMapping("img")
    @ApiOperation("图片上传")
    public R uploadImage(@RequestParam MultipartFile file) {
        String url = this.uploadService.uploadImg(file);
        if (StringUtils.isBlank(url)) {
            return R.error().message("上传失败");
        }
        return R.ok().message("上传成功").data(url);
    }
    @DeleteMapping("img")
    @ApiOperation("图片删除")
    public R deleteImage(@RequestParam String key){
        this.uploadService.deleteImg(key);
        return R.ok().message("删除成功");
    }
}
