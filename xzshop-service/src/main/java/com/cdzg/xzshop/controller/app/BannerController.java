package com.cdzg.xzshop.controller.app;


import com.cdzg.cms.api.vo.app.request.CmsAppBannerRequest;
import com.cdzg.cms.api.vo.app.response.CmsAppBannerResponse;
import com.cdzg.customer.vo.response.CustomerLoginResponse;
import com.cdzg.xzshop.componet.BannerClient;
import com.cdzg.xzshop.config.annotations.api.IgnoreAuth;
import com.cdzg.xzshop.config.annotations.api.MobileApi;
import com.cdzg.xzshop.filter.auth.LoginSessionUtils;
import com.cdzg.xzshop.utils.BaseParameterUtil;
import com.cdzg.xzshop.vo.app.homepage.AppTrainingHomePageReqVO;
import com.framework.utils.core.api.ApiResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@Api(tags = "01_app_banner管理")
@RequestMapping("app/banner")
public class BannerController {


    @Autowired
    private BannerClient bannerClient;

    @Autowired
    BaseParameterUtil baseParameterUtil;

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

    @MobileApi
    @GetMapping("/homePage")
    @ApiOperation("商城首页轮播图")
    public ApiResponse<List<CmsAppBannerResponse>> homePage() {

        String token = LoginSessionUtils.getAppUser().getTicketString();
        //banner信息
        CmsAppBannerRequest bannerReq = new CmsAppBannerRequest();
        bannerReq.setBannerGroupId("800");
        List<CmsAppBannerResponse> bannerConfigList = bannerClient.getBannerConfigList(bannerReq, token);
        return ApiResponse.buildSuccessResponse((!CollectionUtils.isEmpty(bannerConfigList))?(bannerConfigList.size() > 10 ? bannerConfigList.subList(0,10):bannerConfigList):null);
    }

}