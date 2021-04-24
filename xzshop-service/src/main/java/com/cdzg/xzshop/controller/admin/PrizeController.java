package com.cdzg.xzshop.controller.admin;

import com.cdzg.universal.vo.response.user.UserLoginResponse;
import com.cdzg.xzshop.config.annotations.api.IgnoreAuth;
import com.cdzg.xzshop.config.annotations.api.WebApi;
import com.cdzg.xzshop.filter.auth.LoginSessionUtils;
import com.framework.utils.core.api.ApiResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("admin/prize") // app接口统一使用app/**    web接口统一使用admin/**
@Api(tags = "02_admin_奖品")
public class PrizeController {


    @PostMapping("/admintest")
    @ApiOperation("02002-奖品test")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "Authorization token", required = false, dataType = "string", paramType = "header")
    })
    @WebApi
    public ApiResponse admintest() {
        UserLoginResponse userLoginResponse=LoginSessionUtils.getAdminUser();
        return ApiResponse.buildSuccessResponse(userLoginResponse);
    }


    @PostMapping("/test1")
    @ApiOperation("02001-接口demo")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "Authorization token", required = false, dataType = "string", paramType = "header")
    })
    @WebApi //标注此接口为web接口 app接口标注 @MobileApi
    @IgnoreAuth //不需要token
    public ApiResponse admintest1() {
        UserLoginResponse userLoginResponse=LoginSessionUtils.getAdminUser();
        return ApiResponse.buildSuccessResponse(userLoginResponse);
    }

}
