package com.cdzg.xzshop.service;

import com.cdzg.xzshop.domain.GoodsCategory;
import java.util.List;

public interface GoodsCategoryService {


    int deleteByPrimaryKey(Long id);

    int insert(GoodsCategory record);

    int insertOrUpdate(GoodsCategory record);

    int insertOrUpdateSelective(GoodsCategory record);

    int insertSelective(GoodsCategory record);

    GoodsCategory selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(GoodsCategory record);

    int updateByPrimaryKey(GoodsCategory record);

    int updateBatch(List<GoodsCategory> list);

    int updateBatchSelective(List<GoodsCategory> list);

    int batchInsert(List<GoodsCategory> list);

}

