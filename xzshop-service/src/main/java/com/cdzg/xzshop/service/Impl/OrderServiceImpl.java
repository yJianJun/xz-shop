package com.cdzg.xzshop.service.Impl;

import java.util.*;

import ch.qos.logback.core.util.StringCollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cdzg.cms.api.constants.StringUtil;
import com.cdzg.xzshop.domain.Order;
import com.cdzg.xzshop.domain.OrderItem;
import com.cdzg.xzshop.mapper.OrderItemMapper;
import com.cdzg.xzshop.mapper.OrderMapper;
import com.cdzg.xzshop.service.OrderItemService;
import com.cdzg.xzshop.service.OrderService;
import com.cdzg.xzshop.vo.common.PageResultVO;
import com.cdzg.xzshop.vo.order.request.AdminQueryOrderListReqVO;
import com.cdzg.xzshop.vo.order.request.AppQueryOrderListReqVO;
import com.cdzg.xzshop.vo.order.request.CommitOrderGoodsReqVO;
import com.cdzg.xzshop.vo.order.request.CommitOrderReqVO;
import com.cdzg.xzshop.vo.order.response.*;
import com.framework.utils.core.api.ApiResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;


@Service("orderService")
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Autowired
    private OrderItemService orderItemService;


    @Override
    public Order findById(Long id) {
        return orderMapper.findById(id);
    }

    @Override
    public String findShopIdById(Long id) {
        return orderMapper.findShopIdById(id);
    }

    /**
     * app用户提交商城订单
     *
     * @param request
     * @return
     */
    @Override
    @Transactional
    public Order commitOrder(CommitOrderReqVO request) {
        List<CommitOrderGoodsReqVO> goodsList = request.getGoodsList();
        Date date = new Date();
        //保存订单信息及订单商品信息
        Order order = new Order();
        List<OrderItem> orderItems = new ArrayList<>();
        BeanUtils.copyProperties(request, order);
        order.setFreight(request.getFare());
        order.setCreateTime(date);
        order.setPayMoney(request.getTotalMoney());
        if (request.getOrderType() == 1) {
            //积分订单
            order.setPayMethod(3);
            order.setPayTime(date);
            order.setOrderStatus(2);
        } else {
            order.setOrderStatus(1);
        }
        int i = baseMapper.insert(order);
        boolean save = false;
        if (i > 0) {
            goodsList.forEach(g -> {
                OrderItem orderItem = new OrderItem();
                BeanUtils.copyProperties(g, orderItem);
                orderItem.setOrderId(order.getId());
                orderItem.setGoodsId(Long.valueOf(g.getGoodsId()));
                orderItem.setStatus(0);
                orderItem.setGoodsUnitPrice(g.getPromotionPrice());
                orderItems.add(orderItem);
            });
            save = orderItemService.saveBatch(orderItems);
        }
        if (i > 0 && save) {
            return order;
        }
        return null;
    }

    /**
     * 回滚订单
     *
     * @param id 订单id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void rollbackCommitOrder(Long id) {
        baseMapper.deleteById(id);
        HashMap<String, Object> map = new HashMap<>(1);
        map.put("orderId", id);
        orderItemMapper.deleteByMap(map);
    }

    /**
     * 分页查询app用户订单列表
     *
     * @param reqVO
     * @return
     */
    @Override
    public PageResultVO<UserOrderListRespVO> listForApp(AppQueryOrderListReqVO reqVO) {
        PageResultVO<UserOrderListRespVO> result = new PageResultVO<>();
        Page<Order> page = new Page<>();
        page.setCurrent(reqVO.getCurrentPage());
        page.setSize(reqVO.getPageSize());
        List<UserOrderListRespVO> pageList = baseMapper.listForApp(page, reqVO);
        if (!CollectionUtils.isEmpty(pageList)) {
            pageList.forEach(p -> {
                //订单商品信息
                List<OrderGoodsListRespVO> orderGoodsList = orderItemMapper.getListByOrderId(p.getId());
                //处理商品图片，获取第一张
                dealGoodsPic(orderGoodsList);
                orderGoodsList.forEach(o->o.setPaymentMethod(p.getOrderType()));
                p.setOrderGoodsList(orderGoodsList);
            });
            result.setData(pageList);
        } else {
            result.setData(new ArrayList<>());
        }
        result.setPageParams(page);
        return result;
    }


    /**
     * app用户查看订单详情(admin同时使用)
     *
     * @param orderId
     * @param customerId
     * @param shopId
     * @return
     */
    @Override
    public AppOrderDetailRespVO getByIdForApp(String orderId, Long customerId,Long shopId) {
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Order::getId, orderId).eq(Order::getDeleted, 0);
        if (ObjectUtils.isNotNull(customerId)) {
            wrapper.eq(Order::getCustomerId, customerId);
        }
        if (ObjectUtils.isNotNull(shopId)) {
            wrapper.eq(Order::getShopId, shopId);
        }
        Order order = baseMapper.selectOne(wrapper);
        if (Objects.nonNull(order)) {
            AppOrderDetailRespVO result = new AppOrderDetailRespVO();
            BeanUtils.copyProperties(order, result);
            result.setCustomerId(order.getCustomerId() + "");
            result.setId(orderId);
            List<OrderGoodsListRespVO> orderGoodsList = orderItemMapper.getListByOrderId(orderId);
            dealGoodsPic(orderGoodsList);
            orderGoodsList.forEach(o->o.setPaymentMethod(order.getOrderType()));
            result.setOrderGoodsList(orderGoodsList);
            return result;
        }
        return null;
    }


    /**
     * admin查询订单列表
     * @param reqVO
     * @return
     */
    @Override
    public PageResultVO<AdminOrderListRespVO> pageListForAdmin(AdminQueryOrderListReqVO reqVO) {
        PageResultVO<AdminOrderListRespVO> result = new PageResultVO<>();
        LambdaQueryWrapper<Order> queryWrapper = getListQueryWrapper(reqVO);
        Page<Order> page = new Page<>();
        page.setCurrent(reqVO.getCurrentPage());
        page.setPages(reqVO.getPageSize());
        Page<Order> orderPage = baseMapper.selectPage(page, queryWrapper);
        result.setPageParams(orderPage);
        List<Order> records = orderPage.getRecords();
        List<AdminOrderListRespVO> orderList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(records)) {
            records.forEach(r->{
                AdminOrderListRespVO order = new AdminOrderListRespVO();
                BeanUtils.copyProperties(r,order);
                order.setId(r.getId() + "");
                order.setCustomerId(r.getCustomerId() + "");
                order.setPayMoney(r.getPayMoney().toString());
                orderList.add(order);
            });
        }
        result.setData(orderList);
        return result;
    }

    @Override
    public ApiResponse<AdminOrderStatisticsRespVO> topStatisticsForAdmin(Long shopId) {
        AdminOrderStatisticsRespVO result = baseMapper.topStatisticsForAdmin(shopId);
        return ApiResponse.buildSuccessResponse(result);
    }

    @Override
    public List<ExpressCodingRespVO> logisticsList() {
        return baseMapper.logisticsList();
    }

    @Override
    public List<AdminOrderListExport> batchExportForAdmin(AdminQueryOrderListReqVO reqVO) {
        List<AdminOrderListExport> result = new ArrayList<>();
        LambdaQueryWrapper<Order> queryWrapper = getListQueryWrapper(reqVO);
        List<Order> ordersList = baseMapper.selectList(queryWrapper);
        if (!CollectionUtils.isEmpty(ordersList)) {
            ordersList.forEach(r->{
                AdminOrderListExport order = new AdminOrderListExport();
                BeanUtils.copyProperties(r,order);
                order.setId(r.getId() + "");
                order.setCustomerId(r.getCustomerId() + "");
                order.setPayMoney(r.getPayMoney().toString());
                order.setAddress(order.getProvince() + order.getCity() + order.getArea() + order.getAddress());
                result.add(order);
            });
        }
        return result;
    }

    private LambdaQueryWrapper<Order> getListQueryWrapper(AdminQueryOrderListReqVO reqVO){
        LambdaQueryWrapper<Order> queryWrapper = new LambdaQueryWrapper<>();
        if (ObjectUtils.isNotNull(reqVO)) {
            if (StringUtils.isNotBlank(reqVO.getOrderId())) {
                queryWrapper.like(Order::getId,reqVO.getOrderId());
            }
            if (StringUtils.isNotBlank(reqVO.getShopId())) {
                queryWrapper.eq(Order::getShopId,reqVO.getShopId());
            }
            if (StringUtils.isNotBlank(reqVO.getCustomerAccount())) {
                queryWrapper.like(Order::getCustomerAccount,reqVO.getCustomerAccount());
            }
            if (ObjectUtils.isNotNull(reqVO.getOrderStatus()) && reqVO.getOrderStatus() > 0) {
                queryWrapper.eq(Order::getOrderStatus,reqVO.getOrderStatus());
            }
            if (ObjectUtils.isNotNull(reqVO.getStartTime())) {
                queryWrapper.gt(Order::getCreateTime,reqVO.getStartTime());
            }
            if (ObjectUtils.isNotNull(reqVO.getEndTime())) {
                queryWrapper.lt(Order::getCreateTime,reqVO.getEndTime());
            }
        }
        return queryWrapper;
    }

    /**
     * 处理商品图片，获取第一张(数据库存储规则逗号隔开)
     *
     * @param orderGoodsList
     */
    private void dealGoodsPic(List<OrderGoodsListRespVO> orderGoodsList) {
        if (!CollectionUtils.isEmpty(orderGoodsList)) {
            orderGoodsList.forEach(og->{
                String goodsImg = og.getGoodsImg();
                if (StringUtil.isNotBlank(goodsImg)) {
                    String[] split = goodsImg.split(",");
                    og.setGoodsImg(split[0]);
                }
            });
        }

    }


}