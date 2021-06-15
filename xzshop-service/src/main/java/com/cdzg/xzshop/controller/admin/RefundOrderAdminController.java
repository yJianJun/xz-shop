package com.cdzg.xzshop.controller.admin;

import com.cdzg.xzshop.config.annotations.api.WebApi;
import com.cdzg.xzshop.service.RefundOrderService;
import com.cdzg.xzshop.vo.admin.refund.*;
import com.cdzg.xzshop.vo.app.refund.SellerRefuseReceiptVO;
import com.cdzg.xzshop.vo.common.PageResultVO;
import com.framework.utils.core.api.ApiResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@Slf4j
@RestController
@RequestMapping("admin/refundOrder")
@Api(tags = "05_admin_退货退款管理")
public class RefundOrderAdminController {

    @Autowired
    private RefundOrderService refundOrderService;

    @WebApi
    @PostMapping("/getRefundOrderPage")
    @ApiOperation("05001-分页查询退货订单")
    public ApiResponse<PageResultVO<RefundOrderListVO>> getRefundOrderPage(@RequestBody RefundOrderQueryVO queryVO) {
        log.info("RefundOrderAdminController-getRefundOrderPage queryVO:{}", queryVO);
        return ApiResponse.buildSuccessResponse(refundOrderService.getRefundOrderPage(queryVO));
    }

    @WebApi
    @GetMapping("/getRefundOrderStatistic")
    @ApiOperation("05002-退货订单统计")
    public ApiResponse<RefundOrderStatisticVO> getRefundOrderStatistic() {
        log.info("RefundOrderAdminController-getRefundOrderStatistic");
        return ApiResponse.buildSuccessResponse(refundOrderService.getRefundOrderStatistic());
    }

    @WebApi
    @PostMapping("/refuseRefund")
    @ApiOperation("05003-拒绝退款/退货")
    public ApiResponse<String> refuseRefund(@RequestBody RefuseRefundVO vo) {
        log.info("RefundOrderAdminController-refuseRefund vo:{}", vo);
        String info = refundOrderService.refuseRefund(vo);
        if (StringUtils.isNotEmpty(info)) {
            return ApiResponse.buildCommonErrorResponse(info);
        }
        return ApiResponse.buildSuccessResponse(info);
    }

    @WebApi
    @PostMapping("/agreeRefund/{id}")
    @ApiOperation("05004-同意退款/退货")
    public ApiResponse<String> agreeRefund(@PathVariable Long id) {
        log.info("RefundOrderAdminController-agreeRefund id:{}", id);
        String info = refundOrderService.agreeRefund(id);
        if (StringUtils.isNotEmpty(info)) {
            return ApiResponse.buildCommonErrorResponse(info);
        }
        return ApiResponse.buildSuccessResponse(info);
    }

    @WebApi
    @PostMapping("/sellerAgreeReceipt/{id}")
    @ApiOperation("05005-卖家收货")
    public ApiResponse<String> sellerAgreeReceipt(@PathVariable Long id) {
        log.info("RefundOrderAdminController-sellerAgreeReceipt id:{}", id);
        String info = refundOrderService.sellerAgreeReceipt(id);
        if (StringUtils.isNotEmpty(info)) {
            return ApiResponse.buildCommonErrorResponse(info);
        }
        return ApiResponse.buildSuccessResponse(info);
    }

    @WebApi
    @PostMapping("/sellerRefuseReceipt")
    @ApiOperation("05006-卖家拒绝收货")
    public ApiResponse<String> sellerRefuseReceipt(@RequestBody SellerRefuseReceiptVO vo) {
        log.info("RefundOrderAdminController-sellerRefuseReceipt vo:{}", vo);
        String info = refundOrderService.sellerRefuseReceipt(vo);
        if (StringUtils.isNotEmpty(info)) {
            return ApiResponse.buildCommonErrorResponse(info);
        }
        return ApiResponse.buildSuccessResponse(info);
    }

    @WebApi
    @GetMapping("/getAdminDetailById/{id}")
    @ApiOperation("05007-根据id获取退款详情")
    public ApiResponse<RefundOrderDetailVO> getAdminDetailById(@PathVariable Long id) {
        log.info("RefundOrderAdminController-getAdminDetailById");
        return ApiResponse.buildSuccessResponse(refundOrderService.getAdminDetailById(id));
    }

    @WebApi
    @PostMapping("/sellerNotReceipt")
    @ApiOperation("05008-卖家未收到货")
    public ApiResponse<String> sellerNotReceipt(@RequestBody SellerRefuseReceiptVO vo) {
        log.info("RefundOrderAdminController-sellerNotReceipt vo:{}", vo);
        String info = refundOrderService.sellerNotReceipt(vo);
        if (StringUtils.isNotEmpty(info)) {
            return ApiResponse.buildCommonErrorResponse(info);
        }
        return ApiResponse.buildSuccessResponse(info);
    }

    @WebApi
    @PostMapping("/export")
    @ApiOperation("05009-导出")
    public void export(@RequestBody RefundOrderQueryVO queryVO, HttpServletResponse response) {
        log.info("RefundOrderAdminController-export: queryVO:{}", queryVO);
        refundOrderService.export(queryVO, response);
    }
}
