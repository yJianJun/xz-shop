package com.cdzg.xzshop.mapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cdzg.xzshop.vo.order.request.AdminQueryOrderListReqVO;
import com.cdzg.xzshop.vo.order.request.AppQueryOrderListReqVO;
import com.cdzg.xzshop.vo.order.response.*;
import org.apache.ibatis.annotations.Param;

import com.cdzg.xzshop.domain.Order;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 订单表
 * 
 * @author XLQ
 * @email xinliqiang3@163.com
 * @date 2021-05-10 17:12:43
 */
@Mapper
@Repository
public interface OrderMapper extends BaseMapper<Order> {

    Order findById(@Param("id")Long id);

    String findShopIdById(@Param("id")Long id);

    /**
     * app用户查询订单列表
     * @param page
     * @param reqVO
     * @return
     */
    List<UserOrderListRespVO> listForApp(Page<Order> page, @Param("param") AppQueryOrderListReqVO reqVO);

    /**
     * 订单列表顶部统计
     * @param shopId
     * @return
     */
    AdminOrderStatisticsRespVO topStatisticsForAdmin(Long shopId);

    /**
     * 物流公司列表
     * @return
     */
    List<ExpressCodingRespVO> logisticsList();

    /**
     * admin pageList
     * @param page
     * @param reqVO
     * @return
     */
    List<AdminOrderListRespVO> pageListFroAdmin(Page<Order> page, @Param("param")AdminQueryOrderListReqVO reqVO);

    /**
     * admin batch export
     * @param reqVO
     * @return
     */
    List<AdminOrderListExport> batchExportForAdmin(@Param("param")AdminQueryOrderListReqVO reqVO);
}
