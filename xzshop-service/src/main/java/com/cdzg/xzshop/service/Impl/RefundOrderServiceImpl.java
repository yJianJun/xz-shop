package com.cdzg.xzshop.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cdzg.customer.vo.response.CustomerLoginResponse;
import com.cdzg.universal.vo.response.user.UserBaseInfoVo;
import com.cdzg.universal.vo.response.user.UserLoginResponse;
import com.cdzg.xzshop.domain.*;
import com.cdzg.xzshop.enums.RefundTypeEnum;
import com.cdzg.xzshop.filter.auth.LoginSessionUtils;
import com.cdzg.xzshop.mapper.RefundOrderMapper;
import com.cdzg.xzshop.service.*;
import com.cdzg.xzshop.utils.DateUtil;
import com.cdzg.xzshop.vo.admin.SystemTimeConfigVO;
import com.cdzg.xzshop.vo.admin.refund.*;
import com.cdzg.xzshop.vo.app.refund.ApplyRefundVO;
import com.cdzg.xzshop.vo.app.refund.BuyerShipVO;
import com.cdzg.xzshop.vo.app.refund.SellerRefuseReceiptVO;
import com.cdzg.xzshop.vo.common.BasePageRequest;
import com.cdzg.xzshop.vo.common.PageResultVO;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Transactional
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
    @Autowired
    private GoodsSpuService goodsSpuService;
    @Autowired
    private ReturnGoodsInfoService returnGoodsInfoService;
    @Autowired
    private SystemTimeConfigService systemTimeConfigService;


    @Override
    public PageResultVO<RefundOrderListVO> getRefundOrderPage(RefundOrderQueryVO queryVO) {
        PageResultVO<RefundOrderListVO> pageResultVO = new PageResultVO<>();
        UserLoginResponse adminUser = LoginSessionUtils.getAdminUser();
        LambdaQueryWrapper<RefundOrder> wrapper = new LambdaQueryWrapper<>();
        if (!adminUser.getIsAdmin()) {
            wrapper.eq(RefundOrder::getOrgId, adminUser.getUserBaseInfo().getOrganizationId().longValue());
        }
        if (StringUtils.isNotEmpty(queryVO.getId())) {
            wrapper.eq(RefundOrder::getId, queryVO.getId());
        }
        if (StringUtils.isNotEmpty(queryVO.getShopName())) {
            wrapper.like(RefundOrder::getShopName, queryVO.getShopName());
        }
        if (StringUtils.isNotEmpty(queryVO.getUserPhone())) {
            wrapper.like(RefundOrder::getUserPhone, queryVO.getUserPhone());
        }
        if (Objects.nonNull(queryVO.getStatus())) {
            wrapper.eq(RefundOrder::getStatus, queryVO.getStatus());
        }
        if (StringUtils.isNotEmpty(queryVO.getStartTime())) {
            wrapper.ge(RefundOrder::getCreateTime, queryVO.getStartTime());
        }
        if (StringUtils.isNotEmpty(queryVO.getEndTime())) {
            wrapper.le(RefundOrder::getCreateTime, queryVO.getEndTime());
        }
        wrapper.orderByDesc(RefundOrder::getCreateTime).ge(RefundOrder::getStatus, 0);
        Page<RefundOrder> page = this.page(new Page<>(queryVO.getCurrentPage(), queryVO.getPageSize()), wrapper);
        pageResultVO.setData(page.getRecords().stream().map(o -> {
            RefundOrderListVO vo = new RefundOrderListVO();
            BeanUtils.copyProperties(o, vo);
            vo.setCreateTime(DateUtil.formatDateTime(o.getCreateTime()));
            return vo;
        }).collect(Collectors.toList()));
        pageResultVO.setPageParams(page);
        return pageResultVO;
    }

    @Override
    public RefundOrderStatisticVO getRefundOrderStatistic() {
        RefundOrderStatisticVO vo = new RefundOrderStatisticVO();
        int total = this.count(lambdaQuery().ge(RefundOrder::getStatus, 0));
        int returnToDo = this.count(lambdaQuery().eq(RefundOrder::getStatus, 1));
        int refundToDo = this.count(lambdaQuery().eq(RefundOrder::getStatus, 6));
        int refundSuccess = this.count(lambdaQuery().eq(RefundOrder::getStatus, 8));
        vo.setTotal(total);
        vo.setReturnToDo(returnToDo);
        vo.setRefundToDo(refundToDo);
        vo.setRefundSuccess(refundSuccess);
        return vo;
    }

    @Override
    public String applyRefundOrder(ApplyRefundVO vo) {
        Date nowDate = new Date();
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
        // 组装数据
        LocalDateTime now = LocalDateTime.now();
        RefundOrder refundOrder = new RefundOrder();
        BeanUtils.copyProperties(vo, refundOrder);
        refundOrder.setImg(StringUtils.join(vo.getImg(), ","));
        refundOrder.setCreateTime(now);
        refundOrder.setOrderId(orderId);
        refundOrder.setOrderAmount(order.getTotalMoney());
        refundOrder.setPayType(order.getPayMethod());
        refundOrder.setOrderItemId(orderItemId);
        refundOrder.setGoodsName(orderItem.getGoodsName());
        refundOrder.setGoodsNumber(orderItem.getGoodsCount().toString());
        refundOrder.setRefundAmount(orderItem.getGoodsUnitPrice().multiply(orderItem.getGoodsCount()));
        refundOrder.setShopName(shop.getShopName());
        refundOrder.setOrgId(Long.parseLong(shop.getShopUnion()));
        refundOrder.setUserId(appUser.getUserBaseInfo().getId());
        refundOrder.setUserPhone(appUser.getMobile());
        this.save(refundOrder);

        // 所有订单明细
        List<OrderItem> orderItemList = orderItemService.getByOrderId(orderId);

        // 如果所有明细都退了，修改订单状态
        long refundedCount = orderItemList.stream().filter(o -> !o.getId().equals(orderItemId) && o.getStatus().equals(1)).count();
        boolean refundAll = refundedCount + 1 == orderItemList.size();

        // 退款类型
        Integer type = vo.getType();
        Order modifyOrder = new Order();
        modifyOrder.setId(orderId);
        modifyOrder.setUpdateTime(nowDate);
        modifyOrder.setUpdateBy(appUser.getCustomerId());

        String processContent = null;
        // 修改原订单状态
        if (RefundTypeEnum.REFUND.getCode().equals(type)) {
            processContent = appUser.getUserBaseInfo().getUserName() + "提交退款申请";
            refundOrder.setStatus(6);
            modifyOrder.setOrderStatus(6);
            orderService.updateById(modifyOrder);
        } else {
            processContent = appUser.getUserBaseInfo().getUserName() + "提交退货申请";
            refundOrder.setStatus(1);
            if (refundAll) {
                modifyOrder.setOrderStatus(8);
                orderService.updateById(modifyOrder);
            }
        }
        // 保存流程
        refundProcessService.saveProcess(refundOrder.getId(), Long.parseLong(appUser.getCustomerId()), processContent);
        this.save(refundOrder);
        return null;
    }

    @Override
    public String refuseRefund(RefuseRefundVO vo) {
        Long id = vo.getId();
        RefundOrder refundOrder = this.getById(id);
        if (!Lists.newArrayList(1, 6).contains(refundOrder.getStatus())) {
            return "该退款订单状态有误，操作无效！";
        }
        RefundOrder modify = new RefundOrder();
        modify.setId(id);
        if (RefundTypeEnum.REFUND.getCode().equals(refundOrder.getRefundType())) {
            modify.setStatus(7);
        } else {
            modify.setStatus(2);
        }
        modify.setRefuseReason(vo.getReason());
        this.updateById(modify);
        // 流程记录
        UserLoginResponse adminUser = LoginSessionUtils.getAdminUser();
        refundProcessService.saveProcess(id, adminUser.getUserId(), adminUser.getUserBaseInfo().getUserName() + "拒绝退款。");
        return null;
    }

    @Override
    public String agreeRefund(Long id) {
        RefundOrder refundOrder = this.getById(id);
        RefundOrder modify = new RefundOrder();
        modify.setId(id);
        if (refundOrder.getStatus().equals(1)) {
            modify.setStatus(3);
        } else if (refundOrder.getStatus().equals(6)) {
            modify.setStatus(8);
            // TODO 支付退款接口

        } else {
            return "该退款订单状态有误，操作无效！";
        }
        this.updateById(modify);
        // 流程记录
        UserLoginResponse adminUser = LoginSessionUtils.getAdminUser();
        refundProcessService.saveProcess(id, adminUser.getUserId(), adminUser.getUserBaseInfo().getUserName() + "同意退款。");
        return null;
    }

    @Override
    public String buyerShip(BuyerShipVO vo) {
        Long id = vo.getId();
        RefundOrder refundOrder = this.getById(id);
        if (!refundOrder.getStatus().equals(3)) {
            return "该退款订单状态有误，操作无效！";
        }
        RefundOrder modify = new RefundOrder();
        modify.setId(id);
        modify.setStatus(4);
        modify.setLogisticsCompany(vo.getLogisticsCompany());
        modify.setLogisticsNumber(vo.getLogisticsNumber());
        this.updateById(modify);
        // 流程记录
        CustomerLoginResponse appUser = LoginSessionUtils.getAppUser();
        refundProcessService.saveProcess(id, Long.parseLong(appUser.getCustomerId()), appUser.getUserBaseInfo().getUserName() + "买家已发货。");
        return null;
    }

    @Override
    public String sellerAgreeReceipt(Long id) {
        RefundOrder refundOrder = this.getById(id);
        if (!refundOrder.getStatus().equals(4)) {
            return "该退款订单状态有误，操作无效！";
        }
        // TODO 支付退款


        // 流程记录
        UserLoginResponse adminUser = LoginSessionUtils.getAdminUser();
        refundProcessService.saveProcess(id, adminUser.getUserId(), adminUser.getUserBaseInfo().getUserName() + "卖家已收货。");
        return null;
    }

    @Override
    public String sellerRefuseReceipt(SellerRefuseReceiptVO vo) {
        Long id = vo.getId();
        RefundOrder refundOrder = this.getById(id);
        if (!refundOrder.getStatus().equals(4)) {
            return "该退款订单状态有误，操作无效！";
        }
        RefundOrder modify = new RefundOrder();
        modify.setId(id);
        modify.setStatus(5);
        modify.setRefuseReceiptReason(vo.getRefuseReason());
        this.updateById(modify);
        // TODO 商家拒绝收货后流程不清楚

        // 流程记录
        UserLoginResponse adminUser = LoginSessionUtils.getAdminUser();
        refundProcessService.saveProcess(id, adminUser.getUserId(), adminUser.getUserBaseInfo().getUserName() + "卖家拒绝收货。");
        return null;
    }

    @Override
    public void refundCallBack(Long refundOrderId) {
        RefundOrder refundOrder = this.getById(refundOrderId);
        if (Objects.isNull(refundOrder)) {
            log.error("没有该退款订单：" + refundOrderId);
            throw new RuntimeException("没有该退款订单：" + refundOrderId);
        }
        // 退款订单
        RefundOrder modify = new RefundOrder();
        modify.setId(refundOrderId);
        modify.setStatus(8);
        this.updateById(modify);
        // 订单
        Order modifyOrder = new Order();
        modifyOrder.setId(refundOrder.getOrderId());
        if (RefundTypeEnum.REFUND.getCode().equals(refundOrder.getRefundType())) {
            modifyOrder.setOrderStatus(7);
            orderService.updateById(modifyOrder);
        } else {
            // 判断订单是否全退
            List<OrderItem> orderItems = orderItemService.getByOrderId(refundOrder.getOrderId());
            List<Long> collect = orderItems.stream().filter(o -> o.getStatus().equals(1)).map(OrderItem::getId).collect(Collectors.toList());
            if (collect.size() == orderItems.size()) {
                List<RefundOrder> list = this.list(lambdaQuery().eq(RefundOrder::getOrderId, refundOrder.getOrderId()));
                long count = list.stream().filter(o -> o.getStatus().equals(8)).count();
                if (count == collect.size()) {
                    // 修改订单状态
                    modifyOrder.setOrderStatus(9);
                    orderService.updateById(modifyOrder);
                }
            }
        }
    }

    @Override
    public RefundOrderDetailVO getAdminDetailById(Long id) {
        RefundOrderDetailVO vo = new RefundOrderDetailVO();
        RefundOrder refundOrder = this.getById(id);
        BeanUtils.copyProperties(refundOrder, vo);
        Order order = orderService.getById(refundOrder.getOrderId());
        vo.setGoodsInfoVOS(getGoodsInfoVOS(refundOrder));
        // 补齐费用信息
        vo.setOrderAmount(order.getTotalMoney());
        vo.setFare(order.getFreight());
        vo.setGoodsAmount(order.getGoodsPrice());
        // 操作流程时间
        List<RefundProcess> processes = refundProcessService.getByRefundOrderId(id);
        for (RefundProcess process : processes) {
            Integer status = process.getStatus();
            String time = DateUtil.formatDateTime(process.getCreateTime());
            switch (status) {
                // 处理时间
                case 2:
                case 3: vo.setDealTime(time);
                // 买家发货
                case 4: vo.setBuyerShipTime(time);
                // 卖家收货处理时间
                case 5:
                case 6: vo.setSellerDealGoodsTime(time);
                // 如果是退款则为处理时间， 如果是退货就是卖家收货处理时间
                case 7:
                    if (RefundTypeEnum.REFUND.getCode().equals(refundOrder.getRefundType())) {
                        vo.setDealTime(time);
                    } else {
                        vo.setSellerDealGoodsTime(time);
                    }
                // 处理退款时间
                case 8:
                case 9: vo.setReturnMoneyTime(time);
            }
        }
        return vo;
    }

    @Override
    public RefundOrderDetailAppVO getAppDetailById(Long id) {
        RefundOrderDetailAppVO vo = new RefundOrderDetailAppVO();
        RefundOrder refundOrder = this.getById(id);
        BeanUtils.copyProperties(refundOrder, vo);
        Order order = orderService.getById(refundOrder.getOrderId());
        vo.setGoodsInfoVOS(getGoodsInfoVOS(refundOrder));
        // 商家收货地址
        ReturnGoodsInfo returnGoodsInfo = returnGoodsInfoService.findOneByShopId(Long.parseLong(order.getShopId()));
        ShopReturnAddressVO addressVO = new ShopReturnAddressVO();
        BeanUtils.copyProperties(returnGoodsInfo, addressVO);
        vo.setShopReturnAddressVO(addressVO);
        // 剩余时间
        SystemTimeConfigVO systemTimeConfig = systemTimeConfigService.getSystemTimeConfig();
        Integer status = refundOrder.getStatus();
        Integer minute = 0;
        RefundProcess process = refundProcessService.getOne(refundProcessService.lambdaQuery()
                .eq(RefundProcess::getRefundOrderId, id)
                .eq(RefundProcess::getStatus, status));
        LocalDateTime startTime = process.getCreateTime();
        switch (status) {
            // 处理时间
            case 1: minute = systemTimeConfig.getSystemAutoDeal();
            // 买家待发货
            case 3: minute = systemTimeConfig.getSystemAutoFail();
            // 如果是退款则为处理时间， 如果是退货就是卖家收货处理时间
            case 7:
                if (RefundTypeEnum.REFUND.getCode().equals(refundOrder.getRefundType())) {
                    minute = systemTimeConfig.getAutoRefund();
                } else {
                    minute = systemTimeConfig.getSystemAutoRefund();
                }
        }
        Duration duration = Duration.between(startTime, LocalDateTime.now());
        long restTime = duration.toMinutes();
        vo.setRestTime(minute - restTime);
        return vo;
    }

    /**
     * 获取退货/退款的商品信息
     * @param refundOrder
     * @return
     */
    private List<GoodsInfoVO> getGoodsInfoVOS(RefundOrder refundOrder){
        List<OrderItem> orderItems = orderItemService.getByOrderId(refundOrder.getOrderId());
        List<GoodsInfoVO> goodsInfoVOS = Lists.newArrayList();
        // 商品信息
        List<OrderItem> refundOrderItems = null;
        if (RefundTypeEnum.REFUND.getCode().equals(refundOrder.getRefundType())) {
            refundOrderItems = orderItems;
        } else {
            refundOrderItems = orderItems.stream().filter(o -> o.getId().equals(refundOrder.getOrderItemId())).collect(Collectors.toList());
        }
        List<Long> goodsIds = orderItems.stream().map(OrderItem::getGoodsId).collect(Collectors.toList());
        Map<Long, GoodsSpu> goodsMap = goodsSpuService.getMapByIds(goodsIds);
        goodsInfoVOS.addAll(refundOrderItems.stream().map(o -> {
            GoodsInfoVO goodsInfoVO = new GoodsInfoVO();
            BeanUtils.copyProperties(o, goodsInfoVO);
            goodsInfoVO.setTotalPrice(o.getGoodsUnitPrice().multiply(o.getGoodsCount()));
            GoodsSpu goodsSpu = goodsMap.get(o.getId());
            if (Objects.nonNull(goodsSpu) && CollectionUtils.isNotEmpty(goodsSpu.getShowImgs())) {
                goodsInfoVO.setImg(goodsSpu.getShowImgs().get(0));
            }
            return goodsInfoVO;
        }).collect(Collectors.toList()));
        return goodsInfoVOS;
    }

    @Override
    public PageResultVO<RefundOrderListAppVO> getRefundAppPage(BasePageRequest request) {
        PageResultVO<RefundOrderListAppVO> pageResultVO = new PageResultVO<>();
        String userId = LoginSessionUtils.getAppUser().getCustomerId();
        LambdaQueryWrapper<RefundOrder> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RefundOrder::getUserId, userId)
                .ge(RefundOrder::getStatus, 0)
                .orderByDesc(RefundOrder::getCreateTime);
        Page<RefundOrder> page = this.page(new Page<>(request.getCurrentPage(), request.getPageSize()), wrapper);
        // 查订单明细
        List<RefundOrder> records = page.getRecords();
        List<Long> orderIds = records.stream().map(RefundOrder::getOrderId).collect(Collectors.toList());
        orderIds.add(-1L);
        List<OrderItem> orderItems = orderItemService.list(orderItemService.lambdaQuery().in(OrderItem::getOrderId, orderIds));
        // 查商品
        List<Long> goodsIds = orderItems.stream().map(OrderItem::getGoodsId).collect(Collectors.toList());
        goodsIds.add(-1L);
        Map<Long, GoodsSpu> goodsMap = goodsSpuService.getMapByIds(goodsIds);

        pageResultVO.setData(records.stream().map(o -> {
            RefundOrderListAppVO vo = new RefundOrderListAppVO();
            BeanUtils.copyProperties(o, vo);
            List<OrderItem> refundOrderItems = null;
            if (RefundTypeEnum.REFUND.getCode().equals(o.getRefundType())) {
                refundOrderItems = orderItems;
            } else {
                refundOrderItems = orderItems.stream().filter(a -> a.getId().equals(o.getOrderItemId())).collect(Collectors.toList());
            }
            vo.setGoodsInfoVOList(refundOrderItems.stream().map(item -> {
                GoodsInfoVO goodsInfoVO = new GoodsInfoVO();
                BeanUtils.copyProperties(item, goodsInfoVO);
                goodsInfoVO.setTotalPrice(item.getGoodsUnitPrice().multiply(item.getGoodsCount()));
                GoodsSpu goodsSpu = goodsMap.get(item.getId());
                if (Objects.nonNull(goodsSpu) && CollectionUtils.isNotEmpty(goodsSpu.getShowImgs())) {
                    goodsInfoVO.setImg(goodsSpu.getShowImgs().get(0));
                }
                return goodsInfoVO;
            }).collect(Collectors.toList()));
            return vo;
        }).collect(Collectors.toList()));
        pageResultVO.setPageParams(page);
        return pageResultVO;
    }

}
