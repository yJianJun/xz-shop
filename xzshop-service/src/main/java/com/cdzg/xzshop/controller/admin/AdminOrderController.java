package com.cdzg.xzshop.controller.admin;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.cdzg.universal.vo.response.user.UserLoginResponse;
import com.cdzg.xzshop.config.annotations.api.MobileApi;
import com.cdzg.xzshop.config.annotations.api.WebApi;
import com.cdzg.xzshop.domain.ShopInfo;
import com.cdzg.xzshop.filter.auth.LoginSessionUtils;
import com.cdzg.xzshop.service.OrderItemService;
import com.cdzg.xzshop.service.OrderService;
import com.cdzg.xzshop.service.ShopInfoService;
import com.cdzg.xzshop.utils.RabbitmqUtil;
import com.cdzg.xzshop.vo.common.PageResultVO;
import com.cdzg.xzshop.vo.order.request.AdminDeliverReqVO;
import com.cdzg.xzshop.vo.order.request.AdminQueryOrderListReqVO;
import com.cdzg.xzshop.vo.order.request.AppQueryOrderListReqVO;
import com.cdzg.xzshop.vo.order.response.*;
import com.framework.utils.core.api.ApiConst;
import com.framework.utils.core.api.ApiResponse;
import com.google.common.annotations.VisibleForTesting;
import io.netty.util.internal.ObjectUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

/**
 * @ClassName : AdminOrderController
 * @Description : admin订单管理
 * @Author : EvilPet
 * @Date: 2021-05-25 10:26
 */

@RestController
@Api(tags = "32_admin_admin订单管理")
@RequestMapping("admin/order")
public class AdminOrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private ShopInfoService shopInfoService;

    @Autowired
    private RabbitmqUtil rabbitmqUtil;

    @WebApi
    @PostMapping("/list")
    @ApiOperation("32001-admin订单列表")
    public ApiResponse<PageResultVO<AdminOrderListRespVO>> list(@RequestBody @Valid AdminQueryOrderListReqVO reqVO) {
        UserLoginResponse adminUser = LoginSessionUtils.getAdminUser();
        if (ObjectUtils.isNull(adminUser)) {
            return ApiResponse.buildCommonErrorResponse("登录失效，请重新登录");
        }
        if (!adminUser.getIsAdmin()) {
            //非超管，查询本店铺的数据
            ShopInfo shopInfo = shopInfoService.findOneByShopUnion(adminUser.getUserBaseInfo().getOrganizationId().toString());
            if (ObjectUtils.isNull(shopInfo)) {
                return ApiResponse.buildCommonErrorResponse("您的公会尚未创建店铺");
            }
            reqVO.setShopId(shopInfo.getId() + "");
        }
        PageResultVO<AdminOrderListRespVO> result = orderService.pageListForAdmin(reqVO);
        return ApiResponse.buildSuccessResponse(result);
    }

    @WebApi
    @GetMapping("/getById/{orderId}")
    @ApiOperation("32002-订单详情")
    public ApiResponse<AppOrderDetailRespVO> getById(@PathVariable("orderId") String orderId) {
        UserLoginResponse adminUser = LoginSessionUtils.getAdminUser();
        if (ObjectUtils.isNull(adminUser)) {
            return ApiResponse.buildCommonErrorResponse("登录失效，请重新登录");
        }
        Long shopId = null;
        if (!adminUser.getIsAdmin()) {
            //非超管，查询本店铺的数据
            ShopInfo shopInfo = shopInfoService.findOneByShopUnion(adminUser.getUserBaseInfo().getOrganizationId().toString());
            if (ObjectUtils.isNull(shopInfo)) {
                return ApiResponse.buildCommonErrorResponse("您的公会尚未创建店铺");
            }
            shopId = shopInfo.getId();
        }
        AppOrderDetailRespVO result = orderService.getByIdForApp(orderId, null, shopId);
        if (Objects.isNull(result)) {
            return ApiResponse.buildCommonErrorResponse("订单不存在或已删除");
        }
        return ApiResponse.buildSuccessResponse(result);
    }


    @WebApi
    @GetMapping("/topStatistics")
    @ApiOperation("32003-订单列表顶部统计")
    public ApiResponse<AdminOrderStatisticsRespVO> topStatistics() {
        UserLoginResponse adminUser = LoginSessionUtils.getAdminUser();
        if (ObjectUtils.isNull(adminUser)) {
            return ApiResponse.buildCommonErrorResponse("登录失效，请重新登录");
        }
        Long shopId = null;
        if (!adminUser.getIsAdmin()) {
            //非超管，查询本店铺的数据
            ShopInfo shopInfo = shopInfoService.findOneByShopUnion(adminUser.getUserBaseInfo().getOrganizationId().toString());
            if (ObjectUtils.isNull(shopInfo)) {
                return ApiResponse.buildCommonErrorResponse("您的公会尚未创建店铺");
            }
            shopId = shopInfo.getId();
        }
        return orderService.topStatisticsForAdmin(shopId);
    }

    @WebApi
    @GetMapping("/exportExcel")
    @ApiOperation("32004-批量导出excel")
    public ApiResponse<String> exportExcel(HttpServletRequest request, HttpServletResponse response) {

        return null;
    }

    @ApiOperation("32005-快递公司查询")
    @GetMapping(value = "/logisticsList")
    public ApiResponse<List<ExpressCodingRespVO>> logisticsList() {
        List<ExpressCodingRespVO> result = orderService.logisticsList();
        if (result != null) {
            return ApiResponse.buildSuccessResponse(result);
        }
        return ApiResponse.buildCommonErrorResponse("查询失败");
    }


    @WebApi
    @PostMapping("/deliver")
    @ApiOperation("32006-发货")
    public ApiResponse<String> deliver(@RequestBody @Valid AdminDeliverReqVO request) {

        return null;
    }






}
