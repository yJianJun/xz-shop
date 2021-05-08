package com.cdzg.xzshop.service;
import java.time.LocalDateTime;

import java.util.Collection;
import java.util.List;

import com.cdzg.universal.vo.response.user.UserBaseInfoVo;
import com.cdzg.xzshop.domain.GoodsSpu;
import com.cdzg.xzshop.vo.admin.GoodsSpuAddVo;
import com.cdzg.xzshop.vo.admin.GoodsSpuUpdateVO;
import com.cdzg.xzshop.vo.common.PageResultVO;
import com.github.pagehelper.PageInfo;

public interface GoodsSpuService {


    int updateBatch(List<GoodsSpu> list);

    int batchInsert(List<GoodsSpu> list);

    int insertOrUpdate(GoodsSpu record);

    int insertOrUpdateSelective(GoodsSpu record);

    int insert(GoodsSpu record);

    void add(GoodsSpuAddVo addVo, UserBaseInfoVo userName);

    void batchPutOnDown(List<Long> list, Boolean flag);

	int updateStatusAndGmtPutOnTheShelfByIdIn(Boolean updatedStatus,LocalDateTime updatedGmtPutOnTheShelf,Collection<Long> idCollection);

	GoodsSpu findOneBySpuNo(Long spuNo);

    void update(GoodsSpuUpdateVO vo);

    PageResultVO<GoodsSpu> page(int page, int pageSize, Boolean status, String goodsName, LocalDateTime minGmtPutOnTheShelf, LocalDateTime maxGmtPutOnTheShelf, Long spuNo, Long categoryIdLevel1, Long categoryIdLevel2);
}



