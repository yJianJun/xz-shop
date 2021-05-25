package com.cdzg.xzshop.controller.admin;

import com.cdzg.xzshop.common.CommonResult;
import com.cdzg.xzshop.config.annotations.api.WebApi;
import com.cdzg.xzshop.config.pay.WechatPayConfig;
import com.framework.utils.core.api.ApiResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Objects;
import java.util.UUID;

@RestController
@Slf4j
@RequestMapping("admin/upload")
@Validated
@Api(tags = "文件上传-本地服务器")
public class UploadController {

    @WebApi
    @PostMapping("/wechat")
    @ApiOperation("上传微信支付证书")
    public ApiResponse<String> create(@RequestPart MultipartFile file) throws IOException {

        //if (file.isEmpty()) {
        //    return CommonResult.buildCommonErrorResponse("上传失败，请选择文件");
        //}
        //
        //String fileSuffix= Objects.requireNonNull(file.getOriginalFilename()).substring(file.getOriginalFilename().lastIndexOf("."));
        //String fileName= WechatPayConfig.getMchId()+fileSuffix;
        //String filePath = WechatPayConfig.getKeyPath() + "/" + fileName;
        //
        //File dest = new File(filePath);
        //Files.copy(file.getInputStream(), dest.toPath());
        //return CommonResult.buildSuccessResponse(dest.getAbsolutePath());
        return null;
    }


    public String uploadFile(MultipartFile file, HttpServletRequest request) {
        try{
            //创建文件在服务器端的存放路径
            String dir=request.getServletContext().getRealPath("/upload");
            File fileDir = new File(dir);
            if(!fileDir.exists()) {
                fileDir.mkdirs();
            }
            //生成文件在服务器端存放的名字
            String fileSuffix=file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
            String fileName= UUID.randomUUID().toString()+fileSuffix;
            File files = new File(fileDir+"/"+fileName);
            //上传
            file.transferTo(files);

        }catch(Exception e) {
            e.printStackTrace();
            return "上传失败";
        }
        return "上传成功";
    }

}
