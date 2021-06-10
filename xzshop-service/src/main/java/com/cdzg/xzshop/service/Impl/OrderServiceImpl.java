package com.cdzg.xzshop.service.Impl;

import java.util.*;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
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
import com.cdzg.xzshop.vo.order.request.AppQueryOrderListReqVO;
import com.cdzg.xzshop.vo.order.request.CommitOrderGoodsReqVO;
import com.cdzg.xzshop.vo.order.request.CommitOrderReqVO;
import com.cdzg.xzshop.vo.order.response.AppOrderDetailRespVO;
import com.cdzg.xzshop.vo.order.response.OrderGoodsListRespVO;
import com.cdzg.xzshop.vo.order.response.UserOrderListRespVO;
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
     * app用户查看订单详情
     *
     * @param orderId
     * @param customerId
     * @return
     */
    @Override
    public AppOrderDetailRespVO getByIdForApp(String orderId, Long customerId) {
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Order::getId, orderId).eq(Order::getCustomerId, customerId).eq(Order::getDeleted, 0);
        Order order = baseMapper.selectOne(wrapper);
        if (Objects.nonNull(order)) {
            AppOrderDetailRespVO result = new AppOrderDetailRespVO();
            BeanUtils.copyProperties(order, result);
            result.setId(orderId);
            List<OrderGoodsListRespVO> orderGoodsList = orderItemMapper.getListByOrderId(orderId);
            dealGoodsPic(orderGoodsList);
            result.setOrderGoodsList(orderGoodsList);
            return result;
        }
        return null;
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