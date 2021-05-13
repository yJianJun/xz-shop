package com.cdzg.xzshop.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cdzg.xzshop.domain.GoodsPayHistory;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface GoodsPayHistoryMapper extends BaseMapper<GoodsPayHistory> {
    int updateBatch(List<GoodsPayHistory> list);

    int updateBatchSelective(List<GoodsPayHistory> list);

    int batchInsert(@Param("list") List<GoodsPayHistory> list);

    int insertOrUpdate(GoodsPayHistory record);

    int insertOrUpdateSelective(GoodsPayHistory record);
}