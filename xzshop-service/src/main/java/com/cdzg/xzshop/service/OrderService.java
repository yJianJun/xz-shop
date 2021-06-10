package com.cdzg.xzshop.service;
import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cdzg.xzshop.domain.Order;
import com.cdzg.xzshop.vo.common.PageResultVO;
import com.cdzg.xzshop.vo.order.request.AppQueryOrderListReqVO;
import com.cdzg.xzshop.vo.order.request.CommitOrderReqVO;
import com.cdzg.xzshop.vo.order.response.CommitOrderRespVO;
import com.cdzg.xzshop.vo.order.response.UserOrderListRespVO;
import com.framework.utils.core.api.ApiResponse;

import java.util.Map;

/**
 * 订单表
 *
 * @author XLQ
 * @email xinliqiang3@163.com
 * @date 2021-05-10 17:12:43
 */
public interface OrderService extends IService<Order> {



	Order findById(Long id);



	String findShopIdById(Long id);


    /**
     * app用户提交商城订单
     * @param request
     * @return
     */
    Order commitOrder(CommitOrderReqVO request);

    /**
     * 回滚订单
     * @param id 订单id
     */
    void rollbackCommitOrder(Long id);

    /**
     * 分页查询app用户订单列表
     * @param reqVO
     * @return
     */
    PageResultVO<UserOrderListRespVO> listForApp(AppQueryOrderListReqVO reqVO);
}

