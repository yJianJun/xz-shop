package com.cdzg.xzshop.service;

import java.util.List;
import com.cdzg.xzshop.domain.GoodsSpu;
public interface GoodsSpuService{


    int updateBatch(List<GoodsSpu> list);

    int updateBatchSelective(List<GoodsSpu> list);

    int batchInsert(List<GoodsSpu> list);

    int insertOrUpdate(GoodsSpu record);

    int insertOrUpdateSelective(GoodsSpu record);

}
