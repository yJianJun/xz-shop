package com.cdzg.xzshop.service;

import java.util.List;
public interface GoodsImgService{


    int deleteByPrimaryKey(Long id);

    int insert(GoodsImg record);

    int insertOrUpdate(GoodsImg record);

    int insertOrUpdateSelective(GoodsImg record);

    int insertSelective(GoodsImg record);

    GoodsImg selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(GoodsImg record);

    int updateByPrimaryKey(GoodsImg record);

    int updateBatch(List<GoodsImg> list);

    int updateBatchSelective(List<GoodsImg> list);

    int batchInsert(List<GoodsImg> list);

}
