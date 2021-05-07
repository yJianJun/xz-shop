package com.cdzg.xzshop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cdzg.xzshop.domain.ReasonsForRefund;

import java.util.List;

public interface ReasonsForRefundService extends IService<ReasonsForRefund> {
    /**
     * 退款原因
     * @return
     */
    List<String> getReasonsForRefund();

    /**
     * 退货原因
     * @return
     */
    List<String> getReturnReason();

}
