package com.cdzg.xzshop.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cdzg.xzshop.domain.RefundOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface RefundOrderMapper extends BaseMapper<RefundOrder> {

    /**
     * 根据状态和时间获取超时需要处理的退款退货订单
     * @param status 状态
     * @param type 1退款 2退货退款
     * @param time 当前时间减去超时时间后的时间
     * @return
     */
    List<Long> findAutoRefund(@Param("status") Integer status, @Param("type") Integer type, @Param("time") LocalDateTime time);

}
