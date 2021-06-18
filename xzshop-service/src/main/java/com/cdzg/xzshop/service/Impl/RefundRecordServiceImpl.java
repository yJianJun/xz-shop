package com.cdzg.xzshop.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cdzg.xzshop.domain.RefundRecord;
import com.cdzg.xzshop.mapper.RefundRecordMapper;
import com.cdzg.xzshop.service.RefundRecordService;
import com.cdzg.xzshop.to.app.RefundTo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class RefundRecordServiceImpl extends ServiceImpl<RefundRecordMapper, RefundRecord> implements RefundRecordService {

    @Override
    public boolean addRecord(RefundTo refundTo) {
        RefundRecord record = new RefundRecord();
        BeanUtils.copyProperties(refundTo, record);
        record.setType(refundTo.getType().getValue());
        return this.save(record);
    }
}
