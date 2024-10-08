package com.cdzg.xzshop.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cdzg.xzshop.domain.ShopInfo;
import java.time.LocalDateTime;import java.util.Collection;import java.util.Date;import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ShopInfoMapper extends BaseMapper<ShopInfo> {
    int updateBatch(List<ShopInfo> list);

    int updateBatchSelective(List<ShopInfo> list);

    int batchInsert(@Param("list") List<ShopInfo> list);

    int insertOrUpdate(ShopInfo record);

    int insertOrUpdateSelective(ShopInfo record);

    List<ShopInfo> findAllByShopNameLikeAndStatusAndGmtPutOnTheShelfBetweenEqual(@Param("likeShopName") String likeShopName, @Param("status") Boolean status, @Param("minGmtPutOnTheShelf") Date minGmtPutOnTheShelf, @Param("maxGmtPutOnTheShelf") Date maxGmtPutOnTheShelf);

    int updateStatusByIdIn(@Param("updatedStatus") Boolean updatedStatus, @Param("idCollection") Collection<Long> idCollection);

    int updateStatusAndGmtPutOnTheShelfByIdIn(@Param("updatedStatus") Boolean updatedStatus, @Param("updatedGmtPutOnTheShelf") LocalDateTime updatedGmtPutOnTheShelf, @Param("idCollection") Collection<Long> idCollection);

    ShopInfo findOneByShopUnion(@Param("shopUnion")String shopUnion);
}