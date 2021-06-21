package com.cdzg.xzshop.service;
import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cdzg.xzshop.domain.Order;
import com.cdzg.xzshop.vo.common.PageResultVO;
import com.cdzg.xzshop.vo.order.request.AdminQueryOrderListReqVO;
import com.cdzg.xzshop.vo.order.request.AppQueryOrderListReqVO;
import com.cdzg.xzshop.vo.order.request.CommitOrderReqVO;
import com.cdzg.xzshop.vo.order.response.*;
import com.framework.utils.core.api.ApiResponse;


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


    /**
     * app用户查看订单详情
     * @param orderId
     * @param customerId
     * @param shopId
     * @return
     */
    AppOrderDetailRespVO getByIdForApp(String orderId,Long customerId,Long shopId);

    /**
     * admin查看订单列表
     * @param reqVO
     * @return
     */
    PageResultVO<AdminOrderListRespVO> pageListForAdmin(AdminQueryOrderListReqVO reqVO);

    /**
     * admin顶部统计
     * @param shopId
     * @return
     */
    ApiResponse<AdminOrderStatisticsRespVO> topStatisticsForAdmin(Long shopId);

    /**
     * 物流公司列表查询
     * @return
     */
    List<ExpressCodingRespVO> logisticsList();

}

