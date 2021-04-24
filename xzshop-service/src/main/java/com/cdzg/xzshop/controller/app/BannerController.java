package com.cdzg.xzshop.controller.app;


import com.cdzg.customer.vo.response.CustomerLoginResponse;
import com.cdzg.xzshop.config.annotations.api.IgnoreAuth;
import com.cdzg.xzshop.config.annotations.api.MobileApi;
import com.cdzg.xzshop.filter.auth.LoginSessionUtils;
import com.framework.utils.core.api.ApiResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = "01_app_banner管理")
@RequestMapping("app/test")
public class BannerController {

    @GetMapping("/apptest")
    @ApiOperation("01001-banner管理test")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "Authorization token", required = false, dataType = "string", paramType = "header")
    })
    @MobileApi
    public ApiResponse apptest() {
        CustomerLoginResponse customerLoginResponse=LoginSessionUtils.getAppUser();
        return ApiResponse.buildSuccessResponse(customerLoginResponse);
    }

}