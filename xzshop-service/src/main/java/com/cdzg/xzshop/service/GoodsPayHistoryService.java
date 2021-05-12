package com.cdzg.xzshop.service;

import java.util.List;
import com.cdzg.xzshop.domain.GoodsPayHistory;

public interface GoodsPayHistoryService {

    int insert(GoodsPayHistory record);

    int insertOrUpdate(GoodsPayHistory record);

    int insertOrUpdateSelective(GoodsPayHistory record);

    int updateBatch(List<GoodsPayHistory> list);

    int updateBatchSelective(List<GoodsPayHistory> list);

    int batchInsert(List<GoodsPayHistory> list);

}

