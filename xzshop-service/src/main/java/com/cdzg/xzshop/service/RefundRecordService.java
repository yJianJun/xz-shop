package com.cdzg.xzshop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cdzg.xzshop.domain.RefundRecord;
import com.cdzg.xzshop.to.app.RefundTo;

public interface RefundRecordService extends IService<RefundRecord> {

    /**
     * 添加数据
     * @param refundTo
     * @return
     */
    boolean addRecord(RefundTo refundTo);
}
