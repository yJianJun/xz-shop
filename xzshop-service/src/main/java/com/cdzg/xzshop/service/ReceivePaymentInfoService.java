package com.cdzg.xzshop.service;
import com.cdzg.xzshop.constant.ReceivePaymentType;

import java.util.List;
import com.cdzg.xzshop.domain.ReceivePaymentInfo;

public interface ReceivePaymentInfoService {


    int insert(ReceivePaymentInfo record);

    int insertOrUpdate(ReceivePaymentInfo record);

    int insertOrUpdateSelective(ReceivePaymentInfo record);

    int updateBatch(List<ReceivePaymentInfo> list);

    int updateBatchSelective(List<ReceivePaymentInfo> list);

    int batchInsert(List<ReceivePaymentInfo> list);


    List<ReceivePaymentInfo> findAllByShopId(Long shopId);


    ReceivePaymentInfo findOneByShopIdAndType(Long shopId, ReceivePaymentType type);


}





