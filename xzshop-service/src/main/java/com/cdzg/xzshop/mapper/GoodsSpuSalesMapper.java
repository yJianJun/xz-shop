package com.cdzg.xzshop.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cdzg.xzshop.domain.GoodsSpuSales;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface GoodsSpuSalesMapper extends BaseMapper<GoodsSpuSales> {
    int updateBatch(List<GoodsSpuSales> list);

    int batchInsert(@Param("list") List<GoodsSpuSales> list);

    int insertOrUpdate(GoodsSpuSales record);

    int insertOrUpdateSelective(GoodsSpuSales record);

    GoodsSpuSales findOneBySpuNo(@Param("spuNo")Long spuNo);


    /**
     * 批量更改销量
     * @param updateList
     * @return
     */
    int batchUpdateSales(@Param("list")List<GoodsSpuSales> updateList);

}