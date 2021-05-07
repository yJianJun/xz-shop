package com.cdzg.xzshop.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cdzg.universal.vo.response.user.UserBaseInfoVo;
import com.cdzg.xzshop.domain.RefundOrder;
import com.cdzg.xzshop.filter.auth.LoginSessionUtils;
import com.cdzg.xzshop.mapper.RefundOrderMapper;
import com.cdzg.xzshop.service.RefundOrderService;
import com.cdzg.xzshop.vo.admin.RefundOrderListVO;
import com.cdzg.xzshop.vo.admin.RefundOrderQueryVO;
import com.cdzg.xzshop.vo.admin.RefundOrderStatisticVO;
import com.cdzg.xzshop.vo.common.PageResultVO;
import org.springframework.stereotype.Service;

@Service
public class RefundOrderServiceImpl extends ServiceImpl<RefundOrderMapper, RefundOrder> implements RefundOrderService {




    @Override
    public PageResultVO<RefundOrderListVO> getRefundOrderPage(RefundOrderQueryVO queryVO) {
        UserBaseInfoVo userBaseInfo = LoginSessionUtils.getAdminUser().getUserBaseInfo();
        return null;
    }

    @Override
    public RefundOrderStatisticVO getRefundOrderStatistic() {
        // TODO
        return null;
    }
}
