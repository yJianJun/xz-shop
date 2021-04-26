package com.cdzg.xzshop.service;

import java.util.List;
import com.cdzg.xzshop.domain.GoodsSpu;

public interface GoodsSpuService {


    int updateBatch(List<GoodsSpu> list);

    int updateBatchSelective(List<GoodsSpu> list);

    int batchInsert(List<GoodsSpu> list);

    int insertOrUpdate(GoodsSpu record);

    int insertOrUpdateSelective(GoodsSpu record);

    int deleteByPrimaryKey(Long id);

    int insert(GoodsSpu record);

    int insertSelective(GoodsSpu record);

    GoodsSpu selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(GoodsSpu record);

    int updateByPrimaryKey(GoodsSpu record);
}

