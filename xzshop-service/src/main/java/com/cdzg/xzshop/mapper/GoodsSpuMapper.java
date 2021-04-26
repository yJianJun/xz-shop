package com.cdzg.xzshop.mapper;

import com.cdzg.xzshop.domain.GoodsSpu;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface GoodsSpuMapper{
    int updateBatch(List<GoodsSpu> list);

    int updateBatchSelective(List<GoodsSpu> list);

    int batchInsert(@Param("list") List<GoodsSpu> list);

    int insertOrUpdate(GoodsSpu record);

    int insertOrUpdateSelective(GoodsSpu record);
}