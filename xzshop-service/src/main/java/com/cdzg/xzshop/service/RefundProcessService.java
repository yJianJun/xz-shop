package com.cdzg.xzshop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cdzg.xzshop.domain.RefundProcess;

import java.util.List;

public interface RefundProcessService extends IService<RefundProcess> {

    /**
     * 根据退款订单id查询操作流程记录
     * @param refundOrderId
     * @return
     */
    List<RefundProcess> getByRefundOrderId(Long refundOrderId);

    void saveProcess(Long refundOrderId, Long userId, String content);

}
