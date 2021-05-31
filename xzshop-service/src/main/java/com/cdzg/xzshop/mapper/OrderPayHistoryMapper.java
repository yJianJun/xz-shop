package com.cdzg.xzshop.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cdzg.xzshop.domain.OrderPayHistory;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface OrderPayHistoryMapper extends BaseMapper<OrderPayHistory> {
    int updateBatch(List<OrderPayHistory> list);

    int batchInsert(@Param("list") List<OrderPayHistory> list);

    int insertOrUpdate(OrderPayHistory record);

    int insertOrUpdateSelective(OrderPayHistory record);
}