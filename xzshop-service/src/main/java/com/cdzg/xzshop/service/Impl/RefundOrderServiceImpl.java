package com.cdzg.xzshop.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cdzg.customer.vo.response.CustomerLoginResponse;
import com.cdzg.universal.vo.response.user.UserLoginResponse;
import com.cdzg.xzshop.componet.pay.PayDecoration;
import com.cdzg.xzshop.constant.PaymentMethod;
import com.cdzg.xzshop.domain.*;
import com.cdzg.xzshop.enums.RefundTypeEnum;
import com.cdzg.xzshop.filter.auth.LoginSessionUtils;
import com.cdzg.xzshop.mapper.RefundOrderMapper;
import com.cdzg.xzshop.service.*;
import com.cdzg.xzshop.to.app.RefundTo;
import com.cdzg.xzshop.utils.DateUtil;
import com.cdzg.xzshop.utils.ExcelExportUtils;
import com.cdzg.xzshop.utils.RabbitmqUtil;
import com.cdzg.xzshop.vo.admin.SystemTimeConfigVO;
import com.cdzg.xzshop.vo.admin.refund.*;
import com.cdzg.xzshop.vo.app.refund.ApplyRefundVO;
import com.cdzg.xzshop.vo.app.refund.BuyerShipVO;
import com.cdzg.xzshop.vo.app.refund.RefundOrderDetailQueryVO;
import com.cdzg.xzshop.vo.app.refund.SellerRefuseReceiptVO;
import com.cdzg.xzshop.vo.common.BasePageRequest;
import com.cdzg.xzshop.vo.common.PageResultVO;
import com.cdzg.xzshop.vo.order.request.CommitOrderGoodsReqVO;
import com.cdzg.xzshop.vo.pay.RefundParam;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    @Autowired
    private PayDecoration payDecoration;
    @Autowired
    private RabbitmqUtil rabbitmqUtil;


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
        UserLoginResponse adminUser = LoginSessionUtils.getAdminUser();
        RefundOrderStatisticVO vo = new RefundOrderStatisticVO();

        if (adminUser.getIsAdmin()) {
            int total = lambdaQuery().ge(RefundOrder::getStatus, 0).count();
            int returnToDo = lambdaQuery().eq(RefundOrder::getStatus, 1).count();
            int refundToDo = lambdaQuery().eq(RefundOrder::getStatus, 7).count();
            int refundSuccess = lambdaQuery().eq(RefundOrder::getStatus, 9).count();
            vo.setTotal(total);
            vo.setReturnToDo(returnToDo);
            vo.setRefundToDo(refundToDo);
            vo.setRefundSuccess(refundSuccess);
        } else {
            int total = lambdaQuery().ge(RefundOrder::getStatus, 0).eq(RefundOrder::getOrgId, adminUser.getUserBaseInfo().getOrganizationId().longValue()).count();
            int returnToDo = lambdaQuery().eq(RefundOrder::getStatus, 1).eq(RefundOrder::getOrgId, adminUser.getUserBaseInfo().getOrganizationId().longValue()).count();
            int refundToDo = lambdaQuery().eq(RefundOrder::getStatus, 7).eq(RefundOrder::getOrgId, adminUser.getUserBaseInfo().getOrganizationId().longValue()).count();
            int refundSuccess = lambdaQuery().eq(RefundOrder::getStatus, 9).eq(RefundOrder::getOrgId, adminUser.getUserBaseInfo().getOrganizationId().longValue()).count();
            vo.setTotal(total);
            vo.setReturnToDo(returnToDo);
            vo.setRefundToDo(refundToDo);
            vo.setRefundSuccess(refundSuccess);
        }
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
        }
        if (order.getPayMethod().equals(3)) {
            return "积分购买无法退款!";
        }
        // 订单商品项
        Long orderItemId = vo.getOrderItemId();
        // 店铺
        ShopInfo shop = shopInfoService.getById(order.getShopId());
        // 用户
        CustomerLoginResponse appUser = LoginSessionUtils.getAppUser();
        if (!appUser.getUserBaseInfo().getId().equals(order.getCustomerId())) {
            return "订单号有误!";
        }
        // 组装数据
        LocalDateTime now = LocalDateTime.now();
        RefundOrder refundOrder = new RefundOrder();
        BeanUtils.copyProperties(vo, refundOrder);
        refundOrder.setImg(StringUtils.join(vo.getImg(), ","));
        refundOrder.setCreateTime(now);
        refundOrder.setRefundType(vo.getType());
        refundOrder.setOrderAmount(order.getTotalMoney());
        refundOrder.setPayType(order.getPayMethod());
        refundOrder.setShopName(shop.getShopName());
        refundOrder.setShopId(shop.getId());
        refundOrder.setOrgId(Long.parseLong(shop.getShopUnion()));
        refundOrder.setUserId(appUser.getUserBaseInfo().getId());
        refundOrder.setUserPhone(appUser.getUserBaseInfo().getMobile());

        // 所有订单明细
        List<OrderItem> orderItemList = orderItemService.getByOrderId(orderId);

        // 退款类型
        Integer type = vo.getType();
        Order modifyOrder = new Order();
        modifyOrder.setId(orderId);
        modifyOrder.setUpdateTime(nowDate);
        modifyOrder.setUpdateBy(appUser.getCustomerId());

        String processContent = null;
        // 修改原订单状态
        if (RefundTypeEnum.REFUND.getCode().equals(type)) {
            // 不同类型的商品信息保存不一样
            refundOrder.setGoodsName(orderItemList.stream().map(OrderItem::getGoodsName).collect(Collectors.joining(",")));
            refundOrder.setGoodsNumber(orderItemList.stream().map(OrderItem::getGoodsCount).map(Object::toString).collect(Collectors.joining(",")));
            BigDecimal refundMoney = orderItemList.stream().map(o -> o.getGoodsUnitPrice().multiply(BigDecimal.valueOf(o.getGoodsCount())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            refundOrder.setRefundAmount(refundMoney);
            processContent = appUser.getUserBaseInfo().getMobile() + "提交退款申请";
            // 修改状态
            refundOrder.setStatus(7);
            revertOrderStatus(refundOrder, 1);
        } else {
            OrderItem orderItem = orderItemService.getById(orderItemId);
            refundOrder.setOrderItemId(orderItemId);
            refundOrder.setGoodsName(orderItem.getGoodsName());
            refundOrder.setGoodsNumber(orderItem.getGoodsCount().toString());
            refundOrder.setRefundAmount(orderItem.getGoodsUnitPrice().multiply(BigDecimal.valueOf(orderItem.getGoodsCount())));
            processContent = appUser.getUserBaseInfo().getMobile() + "提交退货申请";
            refundOrder.setStatus(1);
            revertOrderStatus(refundOrder, 3);
        }
        this.save(refundOrder);
        // 提交mq
        SystemTimeConfigVO systemTimeConfig = systemTimeConfigService.getSystemTimeConfig();
        if (RefundTypeEnum.REFUND.getCode().equals(type)) {
            rabbitmqUtil.sendAutoRefundDelayMessage(refundOrder.getId(), systemTimeConfig.getAutoRefund() * 60000);
        } else {
            rabbitmqUtil.sendSystemAutoDealDelayMessage(refundOrder.getId(), systemTimeConfig.getSystemAutoDeal() * 60000);
        }
        // 保存流程
        refundProcessService.save(new RefundProcess(refundOrder.getId(), processContent, refundOrder.getStatus(), Long.parseLong(appUser.getCustomerId())));
        return null;
    }

    @Override
    public String refuseRefund(RefuseRefundVO vo) {
        Long id = vo.getId();
        RefundOrder refundOrder = this.getById(id);
        if (!Lists.newArrayList(1, 7).contains(refundOrder.getStatus())) {
            return "该退款订单状态有误，操作无效！";
        }
        RefundOrder modify = new RefundOrder();
        modify.setId(id);
        if (refundOrder.getStatus().equals(7)) {
            modify.setStatus(8);
        } else {
            modify.setStatus(2);
        }
        modify.setRefuseReason(vo.getReason());
        this.updateById(modify);
        // 修改订单明细
        if (RefundTypeEnum.REFUND.getCode().equals(refundOrder.getRefundType())) {
            revertOrderStatus(refundOrder, 5);
        } else {
            revertOrderStatus(refundOrder, 6);
        }
        // 流程记录
        UserLoginResponse adminUser = LoginSessionUtils.getAdminUser();
        refundProcessService.save(new RefundProcess(refundOrder.getId(), adminUser.getUserBaseInfo().getUserName() + "拒绝退款。",
                modify.getStatus(), adminUser.getUserId()));
        return null;
    }

    @Override
    public String agreeRefund(Long id) {
        RefundOrder refundOrder = this.getById(id);
        UserLoginResponse adminUser = LoginSessionUtils.getAdminUser();
        if (refundOrder.getStatus().equals(1)) {
            // 退货
            RefundOrder modify = new RefundOrder();
            modify.setId(id);
            modify.setStatus(3);
            this.updateById(modify);
            refundProcessService.save(new RefundProcess(refundOrder.getId(), adminUser.getUserBaseInfo().getUserName() + "同意退货。",
                    modify.getStatus(), adminUser.getUserId()));
            // 提交mq
            SystemTimeConfigVO systemTimeConfig = systemTimeConfigService.getSystemTimeConfig();
            rabbitmqUtil.sendSystemAutoFailDelayMessage(refundOrder.getId(), systemTimeConfig.getSystemAutoFail() * 60000);
        } else if (refundOrder.getStatus().equals(7)) {
            // 退款
            RefundTo refundTo = payRefund(refundOrder, adminUser);
            if (!refundTo.isStatus()) {
                return refundTo.getErrCodeDesc();
            }
        } else {
            return "该退款订单状态有误，操作无效！";
        }
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
        refundProcessService.save(new RefundProcess(id, appUser.getUserBaseInfo().getUserName() + "买家已发货。",
                modify.getStatus(), Long.parseLong(appUser.getCustomerId())));
        return null;
    }

    @Override
    public String sellerAgreeReceipt(Long id) {
        RefundOrder refundOrder = this.getById(id);
        if (!refundOrder.getStatus().equals(4)) {
            return "该退款订单状态有误，操作无效！";
        }
        RefundOrder modify = new RefundOrder();
        modify.setId(id);
        modify.setStatus(7);
        this.updateById(modify);
        // 流程记录
        UserLoginResponse adminUser = LoginSessionUtils.getAdminUser();
        refundProcessService.save(new RefundProcess(id, adminUser.getUserBaseInfo().getUserName() + "卖家已收货。",
                modify.getStatus(), adminUser.getUserId()));
        // 提交mq
        SystemTimeConfigVO systemTimeConfig = systemTimeConfigService.getSystemTimeConfig();
        rabbitmqUtil.sendSystemAutoRefundDelayMessage(refundOrder.getId(), systemTimeConfig.getSystemAutoRefund() * 60000);
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
        revertOrderStatus(refundOrder, 6);
        // 流程记录
        UserLoginResponse adminUser = LoginSessionUtils.getAdminUser();
        refundProcessService.save(new RefundProcess(id, adminUser.getUserBaseInfo().getUserName() + "卖家拒绝收货。",
                modify.getStatus(), adminUser.getUserId()));
        return null;
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
                case 3:
                    vo.setDealTime(time);
                    break;
                // 买家发货
                case 4:
                    vo.setBuyerShipTime(time);
                    break;
                // 卖家收货处理时间
                case 5:
                case 6:
                    vo.setSellerDealGoodsTime(time);
                    break;
                // 如果是退货就是卖家收货处理时间
                case 7:
                    if (RefundTypeEnum.RETURN.getCode().equals(refundOrder.getRefundType())) {
                        vo.setSellerDealGoodsTime(time);
                    }
                    break;
                // 处理退款时间
                case 8:
                case 9:
                    vo.setReturnMoneyTime(time);
                    break;
            }
        }
        return vo;
    }

    @Override
    public RefundOrderDetailAppVO getAppDetailById(RefundOrderDetailQueryVO queryVO) {
        RefundOrder refundOrder = null;
        // 根据传入的参数是否包含退款单id走逻辑，没有就查
        Long refundId = queryVO.getRefundId();
        if (Objects.isNull(refundId)) {
            Long orderItemId = queryVO.getOrderItemId();
            if (Objects.isNull(orderItemId)) {
                refundOrder = lambdaQuery().eq(RefundOrder::getOrderId, queryVO.getOrderId())
                        .orderByDesc(RefundOrder::getCreateTime)
                        .last(" limit 1").one();
            } else {
                refundOrder = lambdaQuery().eq(RefundOrder::getOrderId, queryVO.getOrderId())
                        .eq(RefundOrder::getOrderItemId, orderItemId)
                        .orderByDesc(RefundOrder::getCreateTime)
                        .last(" limit 1").one();
            }
        } else {
            refundOrder = this.getById(refundId);
        }
        if (Objects.isNull(refundOrder)) {
            return null;
        }
        refundId = refundOrder.getId();
        RefundOrderDetailAppVO vo = new RefundOrderDetailAppVO();
        BeanUtils.copyProperties(refundOrder, vo);
        if (StringUtils.isNotEmpty(refundOrder.getImg())) {
            vo.setImg(Stream.of(refundOrder.getImg().split(",")).collect(Collectors.toList()));
        }

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
        RefundProcess process = refundProcessService.lambdaQuery()
                .eq(RefundProcess::getRefundOrderId, refundId)
                .eq(RefundProcess::getStatus, status)
                .one();
        LocalDateTime startTime = process.getCreateTime();
        switch (status) {
            // 处理时间
            case 1:
                minute = systemTimeConfig.getSystemAutoDeal();
                break;
            // 买家待发货
            case 3:
                minute = systemTimeConfig.getSystemAutoFail();
                break;
            // 如果是退款则为处理时间， 如果是退货就是卖家收货处理时间
            case 7:
                if (RefundTypeEnum.REFUND.getCode().equals(refundOrder.getRefundType())) {
                    minute = systemTimeConfig.getAutoRefund();
                } else {
                    minute = systemTimeConfig.getSystemAutoRefund();
                }
                break;
        }
        Duration duration = Duration.between(startTime, LocalDateTime.now());
        long restTime = duration.toMillis();
        long time = minute * 60000 - restTime;
        // TODO 暂时先用这个时间代替，后面再改
        vo.setRestTime(time > 0 ? time : 0);
        // 流程状态
        List<RefundProcess> processes = refundProcessService.lambdaQuery()
                .eq(RefundProcess::getRefundOrderId, refundId)
                .orderByAsc(RefundProcess::getCreateTime)
                .list();
        vo.setProcessList(processes.stream().map(RefundProcess::getStatus).collect(Collectors.toList()));
        // 退款时间
        Optional<RefundProcess> first = processes.stream().filter(o -> o.getStatus().equals(9)).findFirst();
        first.ifPresent(o -> vo.setRefundTime(DateUtil.formatDateTime(o.getCreateTime())));
        return vo;
    }

    /**
     * 获取退货/退款的商品信息
     *
     * @param refundOrder
     * @return
     */
    private List<GoodsInfoVO> getGoodsInfoVOS(RefundOrder refundOrder) {
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
            goodsInfoVO.setTotalPrice(o.getGoodsUnitPrice().multiply(BigDecimal.valueOf(o.getGoodsCount())));
            GoodsSpu goodsSpu = goodsMap.get(o.getGoodsId());
            if (Objects.nonNull(goodsSpu) && CollectionUtils.isNotEmpty(goodsSpu.getShowImgs())) {
                goodsInfoVO.setSpuNo(goodsSpu.getSpuNo());
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
                .orderByDesc(RefundOrder::getCreateTime);
        Page<RefundOrder> page = this.page(new Page<>(request.getCurrentPage(), request.getPageSize()), wrapper);
        // 查订单明细
        List<RefundOrder> records = page.getRecords();
        List<Long> orderIds = records.stream().map(RefundOrder::getOrderId).collect(Collectors.toList());
        orderIds.add(-1L);
        List<OrderItem> orderItems = orderItemService.lambdaQuery().in(OrderItem::getOrderId, orderIds).list();
        // 查商品
        List<Long> goodsIds = orderItems.stream().map(OrderItem::getGoodsId).collect(Collectors.toList());
        goodsIds.add(-1L);
        Map<Long, GoodsSpu> goodsMap = goodsSpuService.getMapByIds(goodsIds);

        pageResultVO.setData(records.stream().map(o -> {
            RefundOrderListAppVO vo = new RefundOrderListAppVO();
            BeanUtils.copyProperties(o, vo);
            List<OrderItem> refundOrderItems = null;
            if (RefundTypeEnum.REFUND.getCode().equals(o.getRefundType())) {
                refundOrderItems = orderItems.stream().filter(a -> a.getOrderId().equals(o.getOrderId())).collect(Collectors.toList());
            } else {
                refundOrderItems = orderItems.stream().filter(a -> a.getId().equals(o.getOrderItemId())).collect(Collectors.toList());
            }
            vo.setGoodsInfoVOList(refundOrderItems.stream().map(item -> {
                GoodsInfoVO goodsInfoVO = new GoodsInfoVO();
                BeanUtils.copyProperties(item, goodsInfoVO);
                goodsInfoVO.setTotalPrice(item.getGoodsUnitPrice().multiply(BigDecimal.valueOf(item.getGoodsCount())));
                GoodsSpu goodsSpu = goodsMap.get(item.getGoodsId());
                if (Objects.nonNull(goodsSpu) && CollectionUtils.isNotEmpty(goodsSpu.getShowImgs())) {
                    goodsInfoVO.setSpuNo(goodsSpu.getSpuNo());
                    goodsInfoVO.setImg(goodsSpu.getShowImgs().get(0));
                }
                return goodsInfoVO;
            }).collect(Collectors.toList()));
            return vo;
        }).collect(Collectors.toList()));
        pageResultVO.setPageParams(page);
        return pageResultVO;
    }

    @Override
    public String revokeRefund(Long id) {
        RefundOrder refundOrder = this.getById(id);
        Long userId = refundOrder.getUserId();
        CustomerLoginResponse appUser = LoginSessionUtils.getAppUser();
        if (appUser.getUserBaseInfo().getId().equals(userId)) {
            RefundOrder modify = new RefundOrder();
            modify.setId(id);
            modify.setStatus(0);
            this.updateById(modify);
            // 流程记录
            refundProcessService.save(new RefundProcess(id, appUser.getUserBaseInfo().getUserName() + "买家撤销申请。",
                    modify.getStatus(), appUser.getUserBaseInfo().getId()));
            revertOrderStatus(refundOrder, 0);
            return null;
        }
        return "退款单号有误！";
    }

    @Override
    public void autoRefund(Long refundOrderId) {
        // 查询买家提交退款，卖家未处理的退款单id
        RefundOrder refundOrder = this.getById(refundOrderId);
        // 如果超时,状态还未修改
        if (refundOrder.getStatus().equals(7)) {
            payRefund(refundOrder, null);
        }
    }

    @Override
    public void systemAutoDeal(Long refundOrderId) {
        // 买家提交退货退款申请，卖家未处理的退款单id
        RefundOrder refundOrder = this.getById(refundOrderId);
        if (refundOrder.getStatus().equals(1)) {
            updateRefundOrderStatus(refundOrder, 3);
        }
    }

    @Override
    public void systemAutoFail(Long refundOrderId) {
        // 卖家同意退货，买家未处理的退款单id，改为撤销状态
        RefundOrder refundOrder = this.getById(refundOrderId);
        if (refundOrder.getStatus().equals(3)) {
            updateRefundOrderStatus(refundOrder, 0);
            revertOrderStatus(refundOrder, 0);
        }
    }

    @Override
    public String sellerNotReceipt(SellerRefuseReceiptVO vo) {
        Long id = vo.getId();
        RefundOrder refundOrder = this.getById(id);
        if (!refundOrder.getStatus().equals(4)) {
            return "该退款订单状态有误，操作无效！";
        }
        RefundOrder modify = new RefundOrder();
        modify.setId(id);
        modify.setStatus(6);
        modify.setRefuseReceiptReason(vo.getRefuseReason());
        this.updateById(modify);
        revertOrderStatus(refundOrder, 6);
        // 流程记录
        UserLoginResponse adminUser = LoginSessionUtils.getAdminUser();
        refundProcessService.save(new RefundProcess(id, adminUser.getUserBaseInfo().getUserName() + "卖家拒绝收货。",
                modify.getStatus(), adminUser.getUserId()));
        return null;
    }

    @Override
    public void export(RefundOrderQueryVO queryVO, HttpServletResponse response) {
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
        List<RefundOrder> list = this.list(wrapper);
        List<RefundOrderExportVO> collect = list.stream().map(o -> {
            RefundOrderExportVO vo = new RefundOrderExportVO();
            BeanUtils.copyProperties(o, vo);
            vo.setId(o.getId().toString());
            vo.setOrderId(o.getOrderId().toString());
            vo.setRefundType(o.getRefundType().equals(1) ? "退款" : "退货退款");
            String status = null;
            switch (o.getStatus()) {
                case 1:
                    status = "退货处理中";
                    break;
                case 2:
                    status = "拒绝退货";
                    break;
                case 3:
                    status = "买家待发货";
                    break;
                case 4:
                    status = "卖家待收货";
                    break;
                case 5:
                    status = "收货拒绝";
                    break;
                case 6:
                    status = "未收到货";
                    break;
                case 7:
                    status = "退款处理中";
                    break;
                case 8:
                    status = "拒绝退款";
                    break;
                case 9:
                    status = "退款成功";
                    break;
            }
            vo.setStatus(status);
            vo.setCreateTime(DateUtil.formatDateTime(o.getCreateTime()));
            return vo;
        }).collect(Collectors.toList());

        try {
            ExcelExportUtils.export("退款订单列表", "退款订单列表", collect, RefundOrderExportVO.class, response);
        } catch (IOException e) {
            log.error("退款订单列表导出报错", e);
        }
    }

    /**
     * 修改订单明细的状态
     *
     * @param refundOrder 退款订单
     * @param status 要修改的orderitem状态
     */
    private void revertOrderStatus(RefundOrder refundOrder, Integer status) {
        boolean refund = refundOrder.getRefundType().equals(RefundTypeEnum.REFUND.getCode());
        // 还原订单明细状态
        LambdaUpdateWrapper<OrderItem> updateWrapper = new LambdaUpdateWrapper<>();
        if (refund) {
            updateWrapper.eq(OrderItem::getOrderId, refundOrder.getOrderId());
        } else {
            updateWrapper.eq(OrderItem::getId, refundOrder.getOrderItemId());
        }
        updateWrapper.set(OrderItem::getStatus, status);
        orderItemService.update(updateWrapper);
        // 退货完成修改订单状态为交易关闭
        if (status.equals(2) || status.equals(4)) {
            Order order = new Order();
            order.setId(refundOrder.getOrderId());
            order.setOrderStatus(5);
            orderService.updateById(order);
        }
    }

    /**
     * 归还库存，减销量
     * @param itemList 要归还库存的的订单明细列表
     */
    private void returnStock(List<OrderItem> itemList) {
        //归还库存，减销量
        if (CollectionUtils.isNotEmpty(itemList)) {
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

    /**
     * 系统处理退款单状态,同时添加流程
     * @param refundOrder 退款订单
     * @param status 要修改的状态
     */
    private void updateRefundOrderStatus(RefundOrder refundOrder, Integer status) {
        RefundOrder modify = new RefundOrder();
        modify.setId(refundOrder.getId());
        modify.setStatus(status);
        this.updateById(modify);
        // 增加流程
        refundProcessService.save(new RefundProcess(refundOrder.getId(), "流程超时系统自动处理。", status, 1L));
    }

    /**
     * 支付退款
     * @param refundOrder 退款订单
     * @param adminUser 操作人
     * @return 退款信息
     */
    private RefundTo payRefund(RefundOrder refundOrder, UserLoginResponse adminUser){
        RefundParam refundParam = new RefundParam();
        refundParam.setOrderno(refundOrder.getOrderId());
        refundParam.setRefundFee(refundOrder.getRefundAmount().toString());
        refundParam.setRefundId(refundOrder.getId());
        refundParam.setType(refundOrder.getPayType() == 1 ? PaymentMethod.Alipay : PaymentMethod.Wechat);
        RefundTo refundTo = null;
        try {
            refundTo = payDecoration.refund(refundParam);
        } catch (Exception e) {
            log.error("RefundOrderServiceImpl-autoBatchOrderAndProcess", e);
        }
        if (Objects.nonNull(refundTo) && refundTo.isStatus()) {
            // 还原库存
            List<OrderItem> orderItems = null;
            if (refundOrder.getRefundType().equals(1)) {
                orderItems = orderItemService.getByOrderId(refundOrder.getOrderId());
            } else {
                orderItems = Lists.newArrayList(orderItemService.getById(refundOrder.getOrderItemId()));
            }
            this.returnStock(orderItems);
            // 修改退款单状态
            if (Objects.isNull(adminUser)) {
                updateRefundOrderStatus(refundOrder, 9);
            } else {
                RefundOrder modify = new RefundOrder();
                modify.setId(refundOrder.getId());
                modify.setStatus(9);
                this.updateById(modify);
                // 增加流程
                refundProcessService.save(new RefundProcess(refundOrder.getId(), adminUser.getUserBaseInfo().getUserName() + "同意退款",
                        9, adminUser.getUserId()));
            }

            // 修改订单状态
            if (refundOrder.getRefundType().equals(1)) {
                revertOrderStatus(refundOrder, 2);
            } else {
                revertOrderStatus(refundOrder, 4);
            }
        }
        return refundTo;
    }

}
