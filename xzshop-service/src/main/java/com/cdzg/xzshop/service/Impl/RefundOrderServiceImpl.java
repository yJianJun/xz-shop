package com.cdzg.xzshop.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cdzg.customer.vo.response.CustomerLoginResponse;
import com.cdzg.universal.vo.response.user.UserBaseInfoVo;
import com.cdzg.xzshop.domain.*;
import com.cdzg.xzshop.enums.RefundTypeEnum;
import com.cdzg.xzshop.filter.auth.LoginSessionUtils;
import com.cdzg.xzshop.mapper.RefundOrderMapper;
import com.cdzg.xzshop.service.*;
import com.cdzg.xzshop.vo.admin.RefundOrderListVO;
import com.cdzg.xzshop.vo.admin.RefundOrderQueryVO;
import com.cdzg.xzshop.vo.admin.RefundOrderStatisticVO;
import com.cdzg.xzshop.vo.app.returnorder.ApplyRefundVO;
import com.cdzg.xzshop.vo.common.PageResultVO;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Service
public class RefundOrderServiceImpl extends ServiceImpl<RefundOrderMapper, RefundOrder> implements RefundOrderService {

    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderItemService orderItemService;
    @Autowired
    private ShopInfoService shopInfoService;
    @Autowired
    private RefundProcessService refundProcessService;


    @Override
    public PageResultVO<RefundOrderListVO> getRefundOrderPage(RefundOrderQueryVO queryVO) {
        UserBaseInfoVo userBaseInfo = LoginSessionUtils.getAdminUser().getUserBaseInfo();
        return null;
    }

    @Override
    public RefundOrderStatisticVO getRefundOrderStatistic() {
        // TODO
        return null;
    }

    @Transactional
    @Override
    public String applyRefundOrder(ApplyRefundVO vo) {
        // 订单
        Long orderId = vo.getOrderId();
        Order order = orderService.getById(orderId);
        if (order.getOrderStatus().equals(1)) {
            return "该订单还未付款!";
        } else if (order.getOrderStatus().equals(5)) {
            return "该订单已关闭!";
        } else if (order.getOrderStatus().equals(6)) {
            return "该订单退款中!";
        } else if (order.getOrderStatus().equals(8)) {
            return "该订单已退款!";
        } else if (order.getOrderStatus().equals(9)) {
            return "该订单退货中!";
        } else if (order.getOrderStatus().equals(10)) {
            return "该订单已退货!";
        }
        if (order.getPayMethod().equals(3)) {
            return "积分购买无法退款!";
        }
        // 订单商品项
        Long orderItemId = vo.getOrderItemId();
        OrderItem orderItem = orderItemService.getById(orderItemId);
        // 店铺
        ShopInfo shop = shopInfoService.getById(order.getShopId());
        // 用户
        CustomerLoginResponse appUser = LoginSessionUtils.getAppUser();

        LocalDateTime now = LocalDateTime.now();
        RefundOrder refundOrder = new RefundOrder();
        BeanUtils.copyProperties(vo, refundOrder);
        refundOrder.setImg(StringUtils.join(vo.getImg(), ","));
        refundOrder.setStatus(1);
        refundOrder.setCreateTime(now);
        refundOrder.setOrderId(orderId);
        refundOrder.setOrderAmount(order.getTotalMoney());
        refundOrder.setPayType(order.getPayMethod());
        refundOrder.setOrderItemId(orderItemId);
        refundOrder.setGoodsName(orderItem.getGoodsName());
        refundOrder.setGoodsNumber(orderItem.getGoodsCount().intValue());
        refundOrder.setRefundAmount(orderItem.getGoodsUnitPrice().multiply(orderItem.getGoodsCount()));
        refundOrder.setShopName(shop.getShopName());
        refundOrder.setOrgId(Long.parseLong(shop.getShopUnion()));
        refundOrder.setUserId(appUser.getUserBaseInfo().getId());
        refundOrder.setUserPhone(appUser.getMobile());
        this.save(refundOrder);
        // 记录流程
        RefundProcess process = new RefundProcess();
        process.setCreateBy(Long.parseLong(appUser.getCustomerId()));
        process.setCreateTime(now);
        process.setRefundOrderId(refundOrder.getId());

        // 所有订单明细
        List<OrderItem> orderItemList = orderItemService.getByOrderId(orderId);

        // 如果所有明细都退了，修改订单状态
        long refundedCount = orderItemList.stream().filter(o -> !o.getId().equals(orderItemId) && o.getStatus().equals(1)).count();
        boolean refundAll = refundedCount + 1 == orderItemList.size();

        // 退款类型
        Integer type = vo.getType();

        if (RefundTypeEnum.REFUND.getCode().equals(type)) {
            process.setContent(appUser.getUserBaseInfo().getUserName() + "提交退款申请");

        } else {
            process.setContent(appUser.getUserBaseInfo().getUserName() + "提交退货申请");
        }
        refundProcessService.save(process);
        return null;
    }


}
