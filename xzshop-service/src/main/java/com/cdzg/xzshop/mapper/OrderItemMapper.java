package com.cdzg.xzshop.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cdzg.xzshop.domain.GoodsSpu;
import com.cdzg.xzshop.domain.OrderItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

/**
 * 订单明细表
 *
 * @author XLQ
 * @email xinliqiang3@163.com
 * @date 2021-05-10 17:12:43
 */
@Mapper
@Repository
public interface OrderItemMapper extends BaseMapper<OrderItem> {

    List<Long> findIdByOrderIdAndDeleted(@Param("orderId") Long orderId, @Param("deleted") Integer deleted);
}
