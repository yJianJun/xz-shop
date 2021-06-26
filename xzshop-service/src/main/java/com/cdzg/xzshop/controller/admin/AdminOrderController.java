package com.cdzg.xzshop.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.cdzg.universal.vo.response.user.UserLoginResponse;
import com.cdzg.xzshop.config.annotations.api.MobileApi;
import com.cdzg.xzshop.config.annotations.api.WebApi;
import com.cdzg.xzshop.domain.Order;
import com.cdzg.xzshop.domain.ShopInfo;
import com.cdzg.xzshop.filter.auth.LoginSessionUtils;
import com.cdzg.xzshop.service.OrderItemService;
import com.cdzg.xzshop.service.OrderService;
import com.cdzg.xzshop.service.ShopInfoService;
import com.cdzg.xzshop.service.SystemTimeConfigService;
import com.cdzg.xzshop.utils.RabbitmqUtil;
import com.cdzg.xzshop.vo.admin.SystemTimeConfigVO;
import com.cdzg.xzshop.vo.common.PageResultVO;
import com.cdzg.xzshop.vo.order.request.AdminDeliverReqVO;
import com.cdzg.xzshop.vo.order.request.AdminQueryOrderListReqVO;
import com.cdzg.xzshop.vo.order.request.AppQueryOrderListReqVO;
import com.cdzg.xzshop.vo.order.response.*;
import com.framework.utils.core.api.ApiConst;
import com.framework.utils.core.api.ApiResponse;
import com.google.common.annotations.VisibleForTesting;
import com.wuwenze.poi.ExcelKit;
import io.netty.util.internal.ObjectUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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

    @Autowired
    private SystemTimeConfigService systemTimeConfigService;

    @WebApi
    @PostMapping("/list")
    @ApiOperation("32001-admin订单列表")
    public ApiResponse<PageResultVO<AdminOrderListRespVO>> list(@RequestBody @Valid AdminQueryOrderListReqVO reqVO) {
        UserLoginResponse adminUser = LoginSessionUtils.getAdminUser();
        if (ObjectUtils.isNull(adminUser)) {
            return ApiResponse.buildCommonErrorResponse("登录失效，请重新登录");
        }
        List<Long> shopIds = new ArrayList<>();
        if (!adminUser.getIsAdmin()) {
            //非超管，查询本店铺的数据
            ShopInfo shopInfo = shopInfoService.findOneByShopUnion(adminUser.getUserBaseInfo().getOrganizationId().toString());
            if (ObjectUtils.isNull(shopInfo)) {
                return ApiResponse.buildCommonErrorResponse("您的公会尚未创建店铺");
            }
            shopIds.add(shopInfo.getId());
        }else if (adminUser.getIsAdmin() && StringUtils.isNotBlank(reqVO.getShopName())){
            //超管，判断店铺名称搜索条件
            LambdaQueryWrapper<ShopInfo> shopInfoLambdaQueryWrapper = new LambdaQueryWrapper<>();
            shopInfoLambdaQueryWrapper.eq(ShopInfo::getStatus,1).like(ShopInfo::getShopName,reqVO.getShopName());
            List<ShopInfo> shopList = shopInfoService.list(shopInfoLambdaQueryWrapper);
            if (CollectionUtils.isEmpty(shopList)) {
                //若空，直接返回
                PageResultVO<AdminOrderListRespVO> result = new PageResultVO<>();
                result.setPageSize(reqVO.getPageSize());
                result.setTotalNum(0);
                result.setTotalPage(0);
                result.setCurrentPage(reqVO.getCurrentPage());
                result.setData(new ArrayList<>());
                return ApiResponse.buildSuccessResponse(result);
            }
            shopIds = shopList.stream().map(ShopInfo::getId).collect(Collectors.toList());
        }
        reqVO.setShopIds(shopIds);
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
    @PostMapping("/batchExport")
    @ApiOperation("32004-批量导出excel")
    public ApiResponse<String> batchExport(@RequestBody @Valid AdminQueryOrderListReqVO reqVO, HttpServletResponse response) {
        UserLoginResponse adminUser = LoginSessionUtils.getAdminUser();
        if (ObjectUtils.isNull(adminUser)) {
            return ApiResponse.buildCommonErrorResponse("登录失效，请重新登录");
        }
        List<Long> shopIds = new ArrayList<>();
        if (!adminUser.getIsAdmin()) {
            //非超管，查询本店铺的数据
            ShopInfo shopInfo = shopInfoService.findOneByShopUnion(adminUser.getUserBaseInfo().getOrganizationId().toString());
            if (ObjectUtils.isNull(shopInfo)) {
                return ApiResponse.buildCommonErrorResponse("您的公会尚未创建店铺");
            }
            shopIds.add(shopInfo.getId());
        }else if (adminUser.getIsAdmin() && StringUtils.isNotBlank(reqVO.getShopName())){
            //超管，判断店铺名称搜索条件
            LambdaQueryWrapper<ShopInfo> shopInfoLambdaQueryWrapper = new LambdaQueryWrapper<>();
            shopInfoLambdaQueryWrapper.eq(ShopInfo::getStatus,1).like(ShopInfo::getShopName,reqVO.getShopName());
            List<ShopInfo> shopList = shopInfoService.list(shopInfoLambdaQueryWrapper);
            if (CollectionUtils.isEmpty(shopList)) {
                return ApiResponse.buildCommonErrorResponse("暂无数据");
            }
            shopIds = shopList.stream().map(ShopInfo::getId).collect(Collectors.toList());
        }
        reqVO.setShopIds(shopIds);
        List<AdminOrderListExport> list = orderService.batchExportForAdmin(reqVO);
        if (Objects.nonNull(list) && list.size() > 65533) {
            return ApiResponse.buildCommonErrorResponse("导出订单数量最大不能超过65533条！");
        }
        // 导出
        ExcelKit.$Export(AdminOrderListExport.class, response)
                .downXlsx(list, false);
        return ApiResponse.buildSuccessResponse("导出成功");
    }

    @WebApi
    @ApiOperation("32005-快递公司码表查询")
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
        Order order = orderService.getById(request.getOrderId());
        if (order.getOrderStatus() != 2) {
            return ApiResponse.buildCommonErrorResponse("当前订单状态不能发货!");
        }
        if (Objects.nonNull(shopId) && !order.getShopId().equals(shopId + "")) {
            return ApiResponse.buildCommonErrorResponse("非本店铺订单无法发货!");
        }
        SystemTimeConfigVO systemTimeConfig = systemTimeConfigService.getSystemTimeConfig();
        Date date = new Date();
        BeanUtils.copyProperties(request, order);
        order.setDeliverTime(date);
        //如果无配置，默认15天，单位分钟
        order.setSysSureConfig(Objects.nonNull(systemTimeConfig) ? systemTimeConfig.getSureOrder() : 21600);
        order.setUpdateBy(adminUser.getUserId() + "");
        order.setUpdateTime(date);
        boolean b = orderService.updateById(order);
        if (b) {
            //发送定时自动确认收货的mq消息
            rabbitmqUtil.sendAutoSureOrderDelayMessage(request.getOrderId(), order.getSysSureConfig() * 60 * 1000);
            return ApiResponse.buildSuccessResponse("发货成功！");
        }
        return ApiResponse.buildCommonErrorResponse("发货失败，请联系管理员！");
    }


}
