package com.cdzg.xzshop.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cdzg.xzshop.domain.ReasonsForRefund;
import com.cdzg.xzshop.mapper.ReasonsForRefundMapper;
import com.cdzg.xzshop.service.ReasonsForRefundService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReasonsForRefundServiceImpl extends ServiceImpl<ReasonsForRefundMapper, ReasonsForRefund> implements ReasonsForRefundService {


    @Override
    public List<String> getReasonsForRefund() {
        LambdaQueryWrapper<ReasonsForRefund> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ReasonsForRefund::getType, 0);
        return this.list(wrapper).stream().map(ReasonsForRefund::getContent).collect(Collectors.toList());
    }

    @Override
    public List<String> getReturnReason() {
        LambdaQueryWrapper<ReasonsForRefund> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ReasonsForRefund::getType, 1);
        return this.list(wrapper).stream().map(ReasonsForRefund::getContent).collect(Collectors.toList());
    }
}
