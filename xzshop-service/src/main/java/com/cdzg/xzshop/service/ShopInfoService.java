package com.cdzg.xzshop.service;

import java.util.Collection;
import java.util.Date;
import java.time.LocalDateTime;
import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cdzg.xzshop.domain.ShopInfo;
import com.cdzg.xzshop.vo.admin.ShopInfoAddVo;
import com.cdzg.xzshop.vo.admin.ShopInfoUpdateVO;
import com.cdzg.xzshop.vo.common.PageResultVO;

public interface ShopInfoService extends IService<ShopInfo> {

    int insert(ShopInfo record);

    int insertOrUpdate(ShopInfo record);

    int insertOrUpdateSelective(ShopInfo record);

    int updateBatch(List<ShopInfo> list);

    int updateBatchSelective(List<ShopInfo> list);

    int batchInsert(List<ShopInfo> list);

    PageResultVO<ShopInfo> page(int page, int pageSize, String likeShopName, Boolean status, Date minGmtPutOnTheShelf, Date maxGmtPutOnTheShelf);

    void batchPutOnDown(List<Long> list, Boolean flag);

    void add(ShopInfoAddVo addVo, String adminUser);

    void update(ShopInfoUpdateVO vo);


    int updateStatusAndGmtPutOnTheShelfByIdIn(Boolean updatedStatus, LocalDateTime updatedGmtPutOnTheShelf, Collection<Long> idCollection);

}







