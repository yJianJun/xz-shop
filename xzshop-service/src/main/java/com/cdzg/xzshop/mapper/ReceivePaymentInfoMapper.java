package com.cdzg.xzshop.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cdzg.xzshop.constant.ReceivePaymentType;import com.cdzg.xzshop.domain.ReceivePaymentInfo;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ReceivePaymentInfoMapper extends BaseMapper<ReceivePaymentInfo> {
    int updateBatch(List<ReceivePaymentInfo> list);

    int updateBatchSelective(List<ReceivePaymentInfo> list);

    int batchInsert(@Param("list") List<ReceivePaymentInfo> list);

    int insertOrUpdate(ReceivePaymentInfo record);

    int insertOrUpdateSelective(ReceivePaymentInfo record);

    List<ReceivePaymentInfo> findAllByShopId(@Param("shopId") Long shopId);

    ReceivePaymentInfo findOneByShopIdAndType(@Param("shopId") Long shopId, @Param("type") ReceivePaymentType type);
}