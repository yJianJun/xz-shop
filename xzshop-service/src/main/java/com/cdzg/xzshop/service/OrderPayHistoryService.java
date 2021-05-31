package com.cdzg.xzshop.service;

import java.util.List;
import com.cdzg.xzshop.domain.OrderPayHistory;
import com.baomidou.mybatisplus.extension.service.IService;

public interface OrderPayHistoryService extends IService<OrderPayHistory> {


    int updateBatch(List<OrderPayHistory> list);

    int batchInsert(List<OrderPayHistory> list);

    int insertOrUpdate(OrderPayHistory record);

    int insertOrUpdateSelective(OrderPayHistory record);

}

