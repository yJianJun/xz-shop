package com.cdzg.xzshop.service.Impl;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import com.cdzg.xzshop.domain.ReceivePaymentInfo;
import com.cdzg.xzshop.mapper.ReceivePaymentInfoMapper;
import com.cdzg.xzshop.service.ReceivePaymentInfoService;

@Service
public class ReceivePaymentInfoServiceImpl implements ReceivePaymentInfoService {

    @Resource
    private ReceivePaymentInfoMapper receivePaymentInfoMapper;


    @Override
    public int insert(ReceivePaymentInfo record) {
        return receivePaymentInfoMapper.insert(record);
    }

    @Override
    public int insertOrUpdate(ReceivePaymentInfo record) {
        return receivePaymentInfoMapper.insertOrUpdate(record);
    }

    @Override
    public int insertOrUpdateSelective(ReceivePaymentInfo record) {
        return receivePaymentInfoMapper.insertOrUpdateSelective(record);
    }

    @Override
    public int updateBatch(List<ReceivePaymentInfo> list) {
        return receivePaymentInfoMapper.updateBatch(list);
    }

    @Override
    public int updateBatchSelective(List<ReceivePaymentInfo> list) {
        return receivePaymentInfoMapper.updateBatchSelective(list);
    }

    @Override
    public int batchInsert(List<ReceivePaymentInfo> list) {
        return receivePaymentInfoMapper.batchInsert(list);
    }

}



