package com.cdzg.xzshop.controller.app;

import com.cdzg.xzshop.config.annotations.api.MobileApi;
import com.cdzg.xzshop.service.ReasonsForRefundService;
import com.framework.utils.core.api.ApiResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Api(tags = "04_app_退款原因")
@RequestMapping("app/reasonsForRefund")
public class ReasonsForRefundAppController {

    @Autowired
    private ReasonsForRefundService reasonsForRefundService;

    @GetMapping("/getReasonsForRefundList")
    @ApiOperation("04001-获取退款原因")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "Authorization token", required = false, dataType = "string", paramType = "header")
    })
    @MobileApi
    public ApiResponse<List<String>> getReasonsForRefundList() {
        return ApiResponse.buildSuccessResponse(reasonsForRefundService.getReasonsForRefund());
    }

    @GetMapping("/getReturnReasonList")
    @ApiOperation("04002-获取退货原因")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "Authorization token", required = false, dataType = "string", paramType = "header")
    })
    @MobileApi
    public ApiResponse<List<String>> getReturnReasonList() {
        return ApiResponse.buildSuccessResponse(reasonsForRefundService.getReturnReason());
    }

}
