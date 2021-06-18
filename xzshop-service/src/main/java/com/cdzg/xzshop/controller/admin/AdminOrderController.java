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
import com.cdzg.xzshop.vo.common.PageResultVO;
import com.cdzg.xzshop.vo.order.request.AdminQueryOrderListReqVO;
import com.cdzg.xzshop.vo.order.request.AppQueryOrderListReqVO;
import com.cdzg.xzshop.vo.order.response.AdminOrderListRespVO;
import com.cdzg.xzshop.vo.order.response.UserOrderListRespVO;
import com.framework.utils.core.api.ApiResponse;
import io.netty.util.internal.ObjectUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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
    private OrderItemService orderItemService;

    @Autowired
    private ShopInfoService shopInfoService;

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
    @GetMapping("/getById")
    @ApiOperation("32002-订单详情")
    public ApiResponse getById() {


        return null;
    }


}
