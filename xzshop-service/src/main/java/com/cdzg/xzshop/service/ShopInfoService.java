package com.cdzg.xzshop.service;

import java.util.List;
import com.cdzg.xzshop.domain.ShopInfo;
public interface ShopInfoService{


    int deleteByPrimaryKey(Long id);

    int insert(ShopInfo record);

    int insertOrUpdate(ShopInfo record);

    int insertOrUpdateSelective(ShopInfo record);

    int insertSelective(ShopInfo record);

    ShopInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ShopInfo record);

    int updateByPrimaryKey(ShopInfo record);

    int updateBatch(List<ShopInfo> list);

    int updateBatchSelective(List<ShopInfo> list);

    int batchInsert(List<ShopInfo> list);

}
