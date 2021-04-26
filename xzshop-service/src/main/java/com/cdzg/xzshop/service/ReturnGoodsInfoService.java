package com.cdzg.xzshop.service;

import java.util.List;
import com.cdzg.xzshop.domain.ReturnGoodsInfo;
public interface ReturnGoodsInfoService{


    int deleteByPrimaryKey(Long id);

    int insert(ReturnGoodsInfo record);

    int insertOrUpdate(ReturnGoodsInfo record);

    int insertOrUpdateSelective(ReturnGoodsInfo record);

    int insertSelective(ReturnGoodsInfo record);

    ReturnGoodsInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ReturnGoodsInfo record);

    int updateByPrimaryKey(ReturnGoodsInfo record);

    int updateBatch(List<ReturnGoodsInfo> list);

    int updateBatchSelective(List<ReturnGoodsInfo> list);

    int batchInsert(List<ReturnGoodsInfo> list);

}
