package com.cdzg.xzshop.service;

import java.util.Date;
import java.time.LocalDateTime;
import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cdzg.xzshop.domain.ShopInfo;
import com.github.pagehelper.PageInfo;

public interface ShopInfoService extends IService<ShopInfo> {

    int insert(ShopInfo record);

    int insertOrUpdate(ShopInfo record);

    int insertOrUpdateSelective(ShopInfo record);

    int updateBatch(List<ShopInfo> list);

    int updateBatchSelective(List<ShopInfo> list);

    int batchInsert(List<ShopInfo> list);

    PageInfo<ShopInfo> findAllByShopNameAndStatusAndGmtPutOnTheShelfBetweenEqualwithPage(int page, int pageSize, String shopName, Boolean status, Date minGmtPutOnTheShelf, Date maxGmtPutOnTheShelf);
}





