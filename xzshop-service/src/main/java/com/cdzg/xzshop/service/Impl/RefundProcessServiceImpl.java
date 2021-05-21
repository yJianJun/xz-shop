package com.cdzg.xzshop.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cdzg.xzshop.domain.RefundProcess;
import com.cdzg.xzshop.mapper.RefundProcessMapper;
import com.cdzg.xzshop.service.RefundProcessService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RefundProcessServiceImpl extends ServiceImpl<RefundProcessMapper, RefundProcess> implements RefundProcessService {


    @Override
    public List<RefundProcess> getByRefundOrderId(Long refundOrderId) {
        return this.list(lambdaQuery().eq(RefundProcess::getRefundOrderId, refundOrderId));
    }

}
