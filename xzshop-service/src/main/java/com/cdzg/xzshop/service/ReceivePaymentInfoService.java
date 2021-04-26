package com.cdzg.xzshop.service;

import java.util.List;
import com.cdzg.xzshop.domain.ReceivePaymentInfo;

public interface ReceivePaymentInfoService {


    int deleteByPrimaryKey(Long id);

    int insert(ReceivePaymentInfo record);

    int insertOrUpdate(ReceivePaymentInfo record);

    int insertOrUpdateSelective(ReceivePaymentInfo record);

    int insertSelective(ReceivePaymentInfo record);

    ReceivePaymentInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ReceivePaymentInfo record);

    int updateByPrimaryKey(ReceivePaymentInfo record);

    int updateBatch(List<ReceivePaymentInfo> list);

    int updateBatchSelective(List<ReceivePaymentInfo> list);

    int batchInsert(List<ReceivePaymentInfo> list);

}


