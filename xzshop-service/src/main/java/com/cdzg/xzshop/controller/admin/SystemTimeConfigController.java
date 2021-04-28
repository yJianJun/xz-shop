package com.cdzg.xzshop.controller.admin;

import com.cdzg.xzshop.config.annotations.api.WebApi;
import com.cdzg.xzshop.service.SystemTimeConfigService;
import com.cdzg.xzshop.vo.admin.SystemTimeConfigVO;
import com.framework.utils.core.api.ApiResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("admin/systemTimeConfig")
@Api(tags = "03_admin_商城配置项")
public class SystemTimeConfigController {

    @Autowired
    private SystemTimeConfigService systemTimeConfigService;

    @WebApi
    @GetMapping("getSystemTimeConfig")
    @ApiOperation("03001-查询配置")
    public ApiResponse<SystemTimeConfigVO> getSystemTimeConfig(){
        log.info("SystemTimeConfigController-getSystemTimeConfig");
        return ApiResponse.buildSuccessResponse(systemTimeConfigService.getSystemTimeConfig());
    }

    @WebApi
    @PostMapping("updateSystemTimeConfig")
    @ApiOperation("03002-修改配置")
    public ApiResponse<String> updateSystemTimeConfig(@RequestBody SystemTimeConfigVO vo){
        log.info("SystemTimeConfigController-updateSystemTimeConfig vo:{}", vo);
        return ApiResponse.buildSuccessResponse(systemTimeConfigService.updateConfig(vo));
    }


}
