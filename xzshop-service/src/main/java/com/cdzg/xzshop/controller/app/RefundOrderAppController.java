package com.cdzg.xzshop.controller.app;

import com.cdzg.xzshop.config.annotations.api.MobileApi;
import com.cdzg.xzshop.service.RefundOrderService;
import com.cdzg.xzshop.vo.admin.refund.RefundOrderDetailAppVO;
import com.cdzg.xzshop.vo.admin.refund.RefundOrderListAppVO;
import com.cdzg.xzshop.vo.app.refund.ApplyRefundVO;
import com.cdzg.xzshop.vo.app.refund.BuyerShipVO;
import com.cdzg.xzshop.vo.common.BasePageRequest;
import com.cdzg.xzshop.vo.common.PageResultVO;
import com.framework.utils.core.api.ApiResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 订单退款/退货
 */
@Slf4j
@RestController
@RequestMapping("app/refundOrder")
@Api(tags = "06_app_订单退款退货")
public class RefundOrderAppController {

    @Autowired
    private RefundOrderService refundOrderService;

    @PostMapping("/applyRefundOrder")
    @ApiOperation("06001-申请退款")
    @MobileApi
    public ApiResponse<String> applyRefundOrder(@RequestBody ApplyRefundVO vo) {
        log.info("RefundOrderAppController-applyRefundOrder vo:{}", vo);
        String info = refundOrderService.applyRefundOrder(vo);
        if (StringUtils.isNotEmpty(info)) {
            return ApiResponse.buildCommonErrorResponse(info);
        }
        return ApiResponse.buildSuccessResponse(null);
    }

    @PostMapping("/buyerShip")
    @ApiOperation("06002-买家发货填写物流单号")
    @MobileApi
    public ApiResponse<String> buyerShip(@RequestBody BuyerShipVO vo) {
        log.info("RefundOrderAppController-buyerShip vo:{}", vo);
        String info = refundOrderService.buyerShip(vo);
        if (StringUtils.isNotEmpty(info)) {
            return ApiResponse.buildCommonErrorResponse(info);
        }
        return ApiResponse.buildSuccessResponse(null);
    }

    @GetMapping("/getAppDetailById/{id}")
    @ApiOperation("06003-获取退货退款详情")
    @MobileApi
    public ApiResponse<RefundOrderDetailAppVO> getAppDetailById(@PathVariable Long id) {
        log.info("RefundOrderAppController-getAppDetailById id:{}", id);
        return ApiResponse.buildSuccessResponse(refundOrderService.getAppDetailById(id));
    }

    @PostMapping("/getRefundAppPage")
    @ApiOperation("06004-app获取退款分页")
    @MobileApi
    public ApiResponse<PageResultVO<RefundOrderListAppVO>> getRefundAppPage(@RequestBody BasePageRequest request) {
        log.info("RefundOrderAppController-getRefundAppPage request:{}", request);
        return ApiResponse.buildSuccessResponse(refundOrderService.getRefundAppPage(request));
    }

    @GetMapping("/revokeRefund/{id}")
    @ApiOperation("06005-撤销退货退款")
    @MobileApi
    public ApiResponse<String> revokeRefund(@PathVariable Long id) {
        log.info("RefundOrderAppController-revokeRefund id:{}", id);
        String info = refundOrderService.revokeRefund(id);
        if (StringUtils.isNotEmpty(info)) {
            return ApiResponse.buildCommonErrorResponse(info);
        }
        return ApiResponse.buildSuccessResponse(null);
    }

}
