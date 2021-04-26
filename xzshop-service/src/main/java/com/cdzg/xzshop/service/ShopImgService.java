package com.cdzg.xzshop.service;

import java.util.List;
import com.cdzg.xzshop.domain.ShopImg;
public interface ShopImgService{


    int deleteByPrimaryKey(Long id);

    int insert(ShopImg record);

    int insertOrUpdate(ShopImg record);

    int insertOrUpdateSelective(ShopImg record);

    int insertSelective(ShopImg record);

    ShopImg selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ShopImg record);

    int updateByPrimaryKey(ShopImg record);

    int updateBatch(List<ShopImg> list);

    int updateBatchSelective(List<ShopImg> list);

    int batchInsert(List<ShopImg> list);

}
