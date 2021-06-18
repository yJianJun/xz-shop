package com.cdzg.xzshop.controller.app;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cdzg.cms.api.constants.StringUtil;
import com.cdzg.cms.api.vo.xzunion.ApiRes;
import com.cdzg.customer.vo.response.CustomerBaseInfoVo;
import com.cdzg.customer.vo.response.CustomerLoginResponse;
import com.cdzg.xzshop.config.annotations.api.IgnoreAuth;
import com.cdzg.xzshop.config.annotations.api.MobileApi;
import com.cdzg.xzshop.constant.PaymentType;
import com.cdzg.xzshop.domain.GoodsSpu;
import com.cdzg.xzshop.domain.Order;
import com.cdzg.xzshop.domain.OrderItem;
import com.cdzg.xzshop.domain.ShopInfo;
import com.cdzg.xzshop.filter.auth.LoginSessionUtils;
import com.cdzg.xzshop.service.*;
import com.cdzg.xzshop.service.Impl.UserPointsService;
import com.cdzg.xzshop.utils.RabbitmqUtil;
import com.cdzg.xzshop.vo.admin.SystemTimeConfigVO;
import com.cdzg.xzshop.vo.common.PageResultVO;
import com.cdzg.xzshop.vo.order.request.*;
import com.cdzg.xzshop.vo.order.response.AppOrderDetailRespVO;
import com.cdzg.xzshop.vo.order.response.CommitOrderRespVO;
import com.cdzg.xzshop.vo.order.response.SettlementRespVo;
import com.cdzg.xzshop.vo.order.response.UserOrderListRespVO;
import com.framework.utils.core.api.ApiResponse;
import io.netty.util.internal.ObjectUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.common.recycler.Recycler;
import org.hibernate.validator.constraints.Range;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @ClassName : AppOrderController
 * @Description : app用户订单controller
 * @Author : EvilPet
 * @Date: 2021-05-25 10:26
 */

@RestController
@Api(tags = "31_app_订单相关接口")
@RequestMapping("app/order")
@Slf4j
public class AppOrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderItemService orderItemService;

    @Autowired
    private GoodsSpuService goodsSpuService;

    @Autowired
    private UserPointsService userPointsService;

    @Autowired
    private ShopInfoService shopInfoService;

    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private SystemTimeConfigService systemTimeConfigService;

    @Autowired
    private RabbitmqUtil rabbitmqUtil;


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

//    @IgnoreAuth
//    @GetMapping("/mq")
//    @ApiOperation("31001-mq")
//    public Boolean mq(String msg, Integer time) {
//        rabbitmqUtil.sendAutoCancelOrderDelayMessage(msg,time);
//        return true;
//    }


    @MobileApi
    @PostMapping("/commitOrder")
    @ApiOperation("31002-提交订单")
    public ApiResponse<CommitOrderRespVO> commitOrder(@RequestBody @Valid CommitOrderReqVO request) {
        CustomerBaseInfoVo appUserInfo = getAppUserInfo();
        if (Objects.isNull(appUserInfo)) {
            return ApiResponse.buildCommonErrorResponse("登录信息失效，请先登录");
        }
        request.setCustomerId(appUserInfo.getId());
        request.setCustomerAccount(appUserInfo.getMobile());
        //提交订单校验 店铺信息校验
        ShopInfo shopInfo = shopInfoService.getById(request.getShopId());
        if (Objects.isNull(shopInfo) || !shopInfo.getStatus()) {
            return ApiResponse.buildCommonErrorResponse("该店铺已下线，下单失败");
        }
        request.setShopName(shopInfo.getShopName());
        if (request.getOrderType() == 2 && shopInfo.getFare().compareTo(request.getFare()) != 0) {
            return ApiResponse.buildCommonErrorResponse("店铺运费已更改，请刷新购物车或商品列表重新下单");
        }
        if (request.getOrderType() == 1 && request.getFare().compareTo(BigDecimal.ZERO) != 0) {
            return ApiResponse.buildCommonErrorResponse("运费错误，积分商品免运费");
        }
        //商品状态库存及金额校验
        List<CommitOrderGoodsReqVO> commitGoodsList = request.getGoodsList();
        List<String> goodsIds = commitGoodsList.stream().map(CommitOrderGoodsReqVO::getGoodsId).collect(Collectors.toList());
        List<GoodsSpu> goodsSpuList = goodsSpuService.listByIds(goodsIds);
        if (CollectionUtils.isEmpty(goodsSpuList) || commitGoodsList.size() != goodsSpuList.size()) {
            return ApiResponse.buildCommonErrorResponse("该订单有商品已删除,请刷新购物车或商品列表重新下单");
        }
        List<GoodsSpu> collect = goodsSpuList.stream().filter(g -> !g.getStatus() || g.getIsDelete()).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(collect)) {
            return ApiResponse.buildCommonErrorResponse("该订单有下架商品,请刷新购物车或商品列表重新下单");
        }
        List<String> underStockGoodsNames = new ArrayList<>();
        List<String> changePriceGoodsNames = new ArrayList<>();
        goodsSpuList.forEach(g -> commitGoodsList.forEach(c -> {
            if (c.getGoodsId().equals(g.getId() + "")) {
                if (g.getStock().intValue() < c.getGoodsCount()) {
                    underStockGoodsNames.add(c.getGoodsName());
                }
                if (request.getOrderType() == 2 && g.getPromotionPrice().compareTo(c.getPromotionPrice()) != 0) {
                    changePriceGoodsNames.add(c.getGoodsName());
                }
                if (request.getOrderType() == 1 && g.getFractionPrice().compareTo(c.getPromotionPrice()) != 0) {
                    changePriceGoodsNames.add(c.getGoodsName());
                }
            }
        }));
        if (!CollectionUtils.isEmpty(underStockGoodsNames)) {
            return ApiResponse.buildCommonErrorResponse("该商品库存不足:" + underStockGoodsNames + ",请重新下单");
        }
        if (!CollectionUtils.isEmpty(changePriceGoodsNames)) {
            return ApiResponse.buildCommonErrorResponse("该商品价格有变化:" + changePriceGoodsNames + ",请重新下单");
        }
        //提交订单金额校验
        BigDecimal goodsPrice = request.getGoodsPrice();
        BigDecimal fare = request.getFare();
        BigDecimal totalMoney = request.getTotalMoney();
        if (goodsPrice.add(fare).compareTo(totalMoney) != 0) {
            return ApiResponse.buildCommonErrorResponse("商品金额错误，请刷新购物车或商品列表重新下单");
        }
        //商品属性验证，积分商品orRMB商品
        long count = goodsSpuList.stream().filter(g -> g.getPaymentMethod().getValue() == 1).count();
        if (request.getOrderType() == 2 && count > 0) {
            return ApiResponse.buildCommonErrorResponse("包含积分商品，请单独下单购买");
        } else if (request.getOrderType() == 1 && commitGoodsList.size() != count) {
            return ApiResponse.buildCommonErrorResponse("包含非积分商品，请单独下单购买");
        }

        //提交订单
        SystemTimeConfigVO systemTimeConfig = systemTimeConfigService.getSystemTimeConfig();
        request.setSysCancelConfig(systemTimeConfig.getCancelOrder());
        Order order = orderService.commitOrder(request);
        if (Objects.nonNull(order)) {
            //如果积分订单扣除用户积分，扣除失败就删除积分订单
            if (request.getOrderType() == 1) {
                AppPayPointsReqVO payPointsReqVO = new AppPayPointsReqVO();
                payPointsReqVO.setCustomerId(appUserInfo.getId() + "");
                payPointsReqVO.setOrderId(order.getId() + "");
                payPointsReqVO.setPointsNumber(order.getTotalMoney().intValue());
                //积分商品单商品结算
                payPointsReqVO.setGoodsName(commitGoodsList.get(0).getGoodsName());
                payPointsReqVO.setGoodsCount(commitGoodsList.get(0).getGoodsCount());
                JSONObject response = userPointsService.payPoint(LoginSessionUtils.getAppUser().getTicketString(), payPointsReqVO);
                if (Objects.isNull(response)) {
                    orderService.rollbackCommitOrder(order.getId());
                    return ApiResponse.buildCommonErrorResponse("系统错误，请稍后重新下单");
                }
                if (response.getInteger("code") != 200) {
                    orderService.rollbackCommitOrder(order.getId());
                    return ApiResponse.buildCommonErrorResponse(response.getString("msg"));
                }
            }
            //减库存 加销量
            goodsSpuService.updateGoodsStockAndSales(commitGoodsList);
            //删除用户购物车
            List<String> shoppingCartIds = commitGoodsList.stream().map(CommitOrderGoodsReqVO::getShoppingCartId).filter(Objects::nonNull).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(shoppingCartIds)) {
                shoppingCartService.removeByIds(shoppingCartIds);
            }
            //封装返回值
            CommitOrderRespVO result = new CommitOrderRespVO();
            result.setOrderId(order.getId() + "");
            result.setPayMoney(order.getTotalMoney().setScale(2, BigDecimal.ROUND_HALF_UP));
            result.setShopId(request.getShopId());
            result.setShopName(request.getShopName());
            if (request.getOrderType() == 2) {
                //计算付款倒计时 ms，并发送mq消息，定时清理未付款的订单
                result.setRemainingTime((long) (systemTimeConfig.getCancelOrder() * 60 * 1000));
                rabbitmqUtil.sendAutoCancelOrderDelayMessage(order.getId() + "", systemTimeConfig.getCancelOrder() * 60 * 1000);
            } else {
                //TODO 查询用户所在工会
                result.setLaborUnionName("西藏自治区总工会");
            }
            return ApiResponse.buildSuccessResponse(result);
        }
        return ApiResponse.buildCommonErrorResponse("订单提交失败");
    }

    @MobileApi
    @GetMapping("/getUserPoints")
    @ApiOperation("31003-查询用户可用积分余额")
    public ApiResponse<Integer> getUserPoints() {
        CustomerLoginResponse appUser = LoginSessionUtils.getAppUser();
        if (Objects.isNull(appUser)) {
            return ApiResponse.buildCommonErrorResponse("登录失效，请重新登录");
        }
        int userPoints = userPointsService.getUserEffectivePoints(LoginSessionUtils.getAppUser().getTicketString());
        return ApiResponse.buildSuccessResponse(userPoints);
    }

    @MobileApi
    @PostMapping("/list")
    @ApiOperation("31004-订单列表")
    public ApiResponse<PageResultVO<UserOrderListRespVO>> list(@RequestBody @Valid AppQueryOrderListReqVO reqVO) {
        CustomerBaseInfoVo appUserInfo = getAppUserInfo();
        if (Objects.isNull(appUserInfo)) {
            return ApiResponse.buildCommonErrorResponse("登录信息失效，请先登录");
        }
        reqVO.setCustomerId(appUserInfo.getId() + "");
        PageResultVO<UserOrderListRespVO> result = orderService.listForApp(reqVO);
        return ApiResponse.buildSuccessResponse(result);
    }

    @MobileApi
    @GetMapping("/getById/{orderId}")
    @ApiOperation("31005-订单详情")
    public ApiResponse<AppOrderDetailRespVO> getById(@PathVariable("orderId") String orderId) {
        CustomerBaseInfoVo appUserInfo = getAppUserInfo();
        if (Objects.isNull(appUserInfo)) {
            return ApiResponse.buildCommonErrorResponse("登录信息失效，请先登录");
        }
        AppOrderDetailRespVO result = orderService.getByIdForApp(orderId, appUserInfo.getId());
        if (Objects.isNull(result)) {
            return ApiResponse.buildCommonErrorResponse("订单不存在或已删除");
        }
        //处理倒计时 & shopName
        ShopInfo shopInfo = shopInfoService.getById(result.getShopId());
        result.setShopName(shopInfo.getShopName());
        if (result.getOrderStatus() == 1 || result.getOrderStatus() == 3) {
            dealRemainingTime(result);
        }
        return ApiResponse.buildSuccessResponse(result);
    }


    @MobileApi
    @GetMapping("/cancelOrder/{orderId}")
    @ApiOperation("31006-取消订单")
    public ApiResponse<String> cancelOrder(@PathVariable("orderId") String orderId) {
        CustomerBaseInfoVo appUserInfo = getAppUserInfo();
        if (Objects.isNull(appUserInfo)) {
            return ApiResponse.buildCommonErrorResponse("登录信息失效，请先登录");
        }
        //取消订单
        Order order = orderService.getById(orderId);
        if (order.getOrderStatus() != 1) {
            return ApiResponse.buildCommonErrorResponse("该状态订单无法取消订单");
        }
        Date date = new Date();
        order.setOrderStatus(5);
        order.setUpdateBy(appUserInfo.getId() + "");
        order.setUpdateTime(date);
        boolean b = orderService.updateById(order);
        if (b) {
            //归还库存，减销量
            List<OrderItem> itemList = orderItemService.getByOrderId(order.getId());
            if (!CollectionUtils.isEmpty(itemList)) {
                List<CommitOrderGoodsReqVO> commitGoodsList = new ArrayList<>();
                itemList.forEach(i -> {
                    CommitOrderGoodsReqVO param = new CommitOrderGoodsReqVO();
                    param.setGoodsId(i.getGoodsId() + "");
                    param.setGoodsCount(-i.getGoodsCount());
                    commitGoodsList.add(param);
                });
                goodsSpuService.updateGoodsStockAndSales(commitGoodsList);
            }
        }
        return ApiResponse.buildSuccessResponse("取消成功");
    }

    @MobileApi
    @GetMapping("/deleteOrder/{orderId}")
    @ApiOperation("31007-删除订单")
    public ApiResponse<String> deleteOrder(@PathVariable("orderId") String orderId) {
        CustomerBaseInfoVo appUserInfo = getAppUserInfo();
        if (Objects.isNull(appUserInfo)) {
            return ApiResponse.buildCommonErrorResponse("登录信息失效，请先登录");
        }
        Order order = orderService.getById(orderId);
        if (order.getOrderStatus() != 4 && order.getOrderStatus() != 5) {
            return ApiResponse.buildCommonErrorResponse("该订单尚未完成，无法删除");
        }
        order.setDeleted(1);
        order.setUpdateBy(appUserInfo.getId() + "");
        order.setUpdateTime(new Date());
        boolean b = orderService.updateById(order);
        if (b) {
            return ApiResponse.buildSuccessResponse("删除成功");
        }
        return ApiResponse.buildCommonErrorResponse("删除失败，请稍后重试");
    }

    @MobileApi
    @GetMapping("/confirmOrder/{orderId}")
    @ApiOperation("31008-确认订单(确认收货)")
    public ApiResponse<String> confirmOrder(@PathVariable("orderId") String orderId) {
        CustomerBaseInfoVo appUserInfo = getAppUserInfo();
        if (Objects.isNull(appUserInfo)) {
            return ApiResponse.buildCommonErrorResponse("登录信息失效，请先登录");
        }
        Order order = orderService.getById(orderId);
        if (order.getOrderStatus() != 3) {
            return ApiResponse.buildCommonErrorResponse("只有已发货状态的订单才能确认收货");
        }
        Date date = new Date();
        order.setOrderStatus(4);
        order.setDealTime(date);
        order.setUpdateBy(appUserInfo.getId() + "");
        order.setUpdateTime(date);
        boolean b = orderService.updateById(order);
        if (b) {
            return ApiResponse.buildSuccessResponse("确认收货成功");
        }
        return ApiResponse.buildCommonErrorResponse("确认收货失败，请稍后重试");
    }

    @MobileApi
    @GetMapping("/getRemainingTime/{orderId}")
    @ApiOperation("31009-返回订单支付剩余时间，单位ms")
    public ApiResponse<String> getRemainingTime(@PathVariable("orderId") String orderId) {
        Order order = orderService.getById(orderId);
        if (order.getOrderStatus() != 1) {
            return ApiResponse.buildCommonErrorResponse("该订单当前状态不能支付");
        }
        SystemTimeConfigVO systemTimeConfig = systemTimeConfigService.getSystemTimeConfig();
        long now = System.currentTimeMillis();
        long createTime = order.getCreateTime().getTime();
        long remainingTime = createTime + (systemTimeConfig.getCancelOrder() * 60 * 1000) - now;
        String result = remainingTime >= 0 ? remainingTime + "" : "0";
        return ApiResponse.buildSuccessResponse(result);
    }


    /**
     * 获取登录用户信息
     *
     * @return
     */
    private CustomerBaseInfoVo getAppUserInfo() {
        CustomerLoginResponse appUser = LoginSessionUtils.getAppUser();
        if (Objects.nonNull(appUser)) {
            CustomerBaseInfoVo userBaseInfo = appUser.getUserBaseInfo();
            if (Objects.nonNull(userBaseInfo)) {
                return userBaseInfo;
            }
        }
        return null;
    }

    /**
     * 计算处理倒计时
     *
     * @param result
     */
    private void dealRemainingTime(AppOrderDetailRespVO result) {
        long now = System.currentTimeMillis();
        if (result.getOrderStatus() == 1) {
            //待支付订单
            long createTime = result.getCreateTime().getTime();
            long time = createTime + (result.getSysCancelConfig() * 60 * 1000) - now;
            result.setRemainingTime(time >= 0 ? time : 0L);
        } else {
            //待收货订单
            long deliverTime = result.getDeliverTime().getTime();
            long time = deliverTime + (result.getSysSureConfig() * 60 * 1000) - now;
            result.setRemainingTime(time >= 0 ? time : 0L);
        }
    }

}
