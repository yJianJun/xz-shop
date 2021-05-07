package com.cdzg.xzshop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cdzg.xzshop.domain.RefundOrder;
import com.cdzg.xzshop.vo.admin.RefundOrderListVO;
import com.cdzg.xzshop.vo.admin.RefundOrderQueryVO;
import com.cdzg.xzshop.vo.admin.RefundOrderStatisticVO;
import com.cdzg.xzshop.vo.common.PageResultVO;

public interface RefundOrderService extends IService<RefundOrder> {

    /**
     * 分页查询退货订单
     * @param queryVO
     * @return
     */
    PageResultVO<RefundOrderListVO> getRefundOrderPage(RefundOrderQueryVO queryVO);

    /**
     *
     * @return
     */
    RefundOrderStatisticVO getRefundOrderStatistic();


}
