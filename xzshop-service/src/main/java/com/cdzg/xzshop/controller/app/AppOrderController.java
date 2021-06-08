package com.cdzg.xzshop.controller.app;

import com.cdzg.customer.vo.response.CustomerBaseInfoVo;
import com.cdzg.customer.vo.response.CustomerLoginResponse;
import com.cdzg.xzshop.config.annotations.api.MobileApi;
import com.cdzg.xzshop.filter.auth.LoginSessionUtils;
import com.cdzg.xzshop.service.GoodsSpuService;
import com.cdzg.xzshop.service.Impl.UserPointsService;
import com.cdzg.xzshop.service.OrderItemService;
import com.cdzg.xzshop.service.OrderService;
import com.cdzg.xzshop.vo.order.request.SettlementGoodsListReqVO;
import com.cdzg.xzshop.vo.order.request.SettlementReqVO;
import com.cdzg.xzshop.vo.order.response.SettlementRespVo;
import com.framework.utils.core.api.ApiResponse;
import io.netty.util.internal.ObjectUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

/**
 * @ClassName : AppOrderController
 * @Description : app用户订单controller
 * @Author : EvilPet
 * @Date: 2021-05-25 10:26
 */

@RestController
@Api(tags = "31_app_订单相关接口")
@RequestMapping("app/order")
public class AppOrderController {

    @Autowired
    private OrderService orderService;


    @Autowired
    private OrderItemService orderItemService;


    @Autowired
    private GoodsSpuService goodsSpuService;

    @Autowired
    private UserPointsService userPointsService;


//    @MobileApi
//    @GetMapping("/settlement")
//    @ApiOperation("31001-结算接口")
//    public ApiResponse<SettlementRespVo> settlement(@Valid @RequestBody SettlementReqVO reqVO){
//        //1.判断商品可用性
//        List<SettlementGoodsListReqVO> goodsList = reqVO.getGoodsList();
//
//
//
//
//
//        //2.计算金额
//
//
//        //3.封装返回参数
//        SettlementRespVo settlementRespVo = new SettlementRespVo();
//        return null;
//    }


    @MobileApi
    @GetMapping("/commitOrder")
    @ApiOperation("31002-提交订单")
    public ApiResponse<String> commitOrder(){


        return null;
    }

    @MobileApi
    @GetMapping("/commitPointsOrder")
    @ApiOperation("31003-提交积分订单订单")
    public ApiResponse<String> commitPointsOrder(){


        return null;
    }





    @MobileApi
    @GetMapping("/getUserPoints")
    @ApiOperation("31004-查询用户可用积分余额")
    public ApiResponse<Integer> getUserPoints(){
        CustomerLoginResponse appUser = LoginSessionUtils.getAppUser();
        if (Objects.isNull(appUser)) {
            return ApiResponse.buildCommonErrorResponse("登录失效，请重新登录");
        }
        int userPoints = userPointsService.getUserEffectivePoints(LoginSessionUtils.getAppUser().getTicketString());
        return ApiResponse.buildSuccessResponse(userPoints);
    }

    /**
     * 获取登录用户信息
     *
     * @return
     */
    private CustomerBaseInfoVo getAppUserInfo() {
        return LoginSessionUtils.getAppUser().getUserBaseInfo();
    }

}
