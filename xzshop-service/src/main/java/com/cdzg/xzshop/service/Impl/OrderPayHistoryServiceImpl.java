package com.cdzg.xzshop.service.Impl;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cdzg.xzshop.mapper.OrderPayHistoryMapper;
import java.util.List;
import com.cdzg.xzshop.domain.OrderPayHistory;
import com.cdzg.xzshop.service.OrderPayHistoryService;

@Service
public class OrderPayHistoryServiceImpl extends ServiceImpl<OrderPayHistoryMapper, OrderPayHistory> implements OrderPayHistoryService {

    @Override
    public int updateBatch(List<OrderPayHistory> list) {
        return baseMapper.updateBatch(list);
    }

    @Override
    public int batchInsert(List<OrderPayHistory> list) {
        return baseMapper.batchInsert(list);
    }

    @Override
    public int insertOrUpdate(OrderPayHistory record) {
        return baseMapper.insertOrUpdate(record);
    }

    @Override
    public int insertOrUpdateSelective(OrderPayHistory record) {
        return baseMapper.insertOrUpdateSelective(record);
    }
}

