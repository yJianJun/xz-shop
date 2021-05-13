package com.cdzg.xzshop.service.Impl;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import com.cdzg.xzshop.mapper.GoodsPayHistoryMapper;
import com.cdzg.xzshop.domain.GoodsPayHistory;
import com.cdzg.xzshop.service.GoodsPayHistoryService;

@Service
public class GoodsPayHistoryServiceImpl implements GoodsPayHistoryService {

    @Resource
    private GoodsPayHistoryMapper goodsPayHistoryMapper;

    @Override
    public int insert(GoodsPayHistory record) {
        return goodsPayHistoryMapper.insert(record);
    }

    @Override
    public int insertOrUpdate(GoodsPayHistory record) {
        return goodsPayHistoryMapper.insertOrUpdate(record);
    }

    @Override
    public int insertOrUpdateSelective(GoodsPayHistory record) {
        return goodsPayHistoryMapper.insertOrUpdateSelective(record);
    }

    @Override
    public int updateBatch(List<GoodsPayHistory> list) {
        return goodsPayHistoryMapper.updateBatch(list);
    }

    @Override
    public int updateBatchSelective(List<GoodsPayHistory> list) {
        return goodsPayHistoryMapper.updateBatchSelective(list);
    }

    @Override
    public int batchInsert(List<GoodsPayHistory> list) {
        return goodsPayHistoryMapper.batchInsert(list);
    }

}

