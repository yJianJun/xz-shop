package com.cdzg.xzshop.componet.pay;


import com.cdzg.xzshop.common.BaseException;
import com.cdzg.xzshop.common.ResultCode;
import com.cdzg.xzshop.enums.PaymentMethod;
import com.cdzg.xzshop.domain.Order;
import com.cdzg.xzshop.service.OrderItemService;
import com.cdzg.xzshop.service.OrderService;
import com.cdzg.xzshop.service.RefundRecordService;
import com.cdzg.xzshop.service.pay.PayService;
import com.cdzg.xzshop.to.app.QueryOrderTo;
import com.cdzg.xzshop.to.app.RefundTo;
import com.cdzg.xzshop.vo.pay.RefundParam;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;

@Slf4j
@Component
@Validated
public class PayDecoration {

    @Autowired
    @Qualifier("weChatService")
    private PayService wxPayService;

    @Autowired
    @Qualifier("aliPayService")
    private PayService aliPayService;

    @Resource
    private OrderService orderService;

    @Resource
    private OrderItemService orderItemService;

    @Autowired
    private RefundRecordService refundRecordService;

    /**
     *
     * @param transactionId
     * @param outTradeNo
     * @param type
     * @return  有2种返回类型 AlipayTradeQueryResponse / WxPayOrderQueryResult
     * @throws Exception
     */
    @ApiOperation("查询交易订单信息")
    public QueryOrderTo query(@Valid @NotBlank @ApiParam(value = "微信/支付宝交易号", required = true) String transactionId,
                                     @Valid @NotBlank @ApiParam(value = "商品订单号", required = true) String outTradeNo,
                                     @Valid @NotNull @ApiParam(value = "支付方式", required = true, allowableValues = "1,2") PaymentMethod type) throws Exception {

        if (PaymentMethod.Wechat == type) {

           return query(wxPayService, transactionId, outTradeNo);
        } else {
          return query(aliPayService, transactionId, outTradeNo);
        }
    }

    /**
     *
     * @param refundParam
     * @return RefundTo
     * @throws Exception
     */
    @ApiOperation("订单退款")
    public RefundTo refund(RefundParam refundParam) throws Exception {
        String refundFee = refundParam.getRefundFee();
        String tradeno = refundParam.getTransactionId();
        Long orderId = refundParam.getOrderno();
        Long refundId = refundParam.getRefundId();
        PaymentMethod type = refundParam.getType();

        Order order = orderService.getById(orderId);
        List<Long> itemsIds = orderItemService.findIdByOrderIdAndDeleted(orderId,0);

        if (Objects.isNull(order) || CollectionUtils.isEmpty(itemsIds)) {
            throw new BaseException("无此订单");
        }

        // 订单状态（1待付款2.待发货3.已发货4.已完成5.已关闭）
        Integer orderStatus = order.getOrderStatus();

        if (orderStatus == 1 || orderStatus ==5){
            throw new BaseException(ResultCode.Illegal);
        }

        if (PaymentMethod.Wechat == type) {

           return refund(wxPayService, tradeno, orderId,refundId,refundFee);
        } else {
            return refund(aliPayService, tradeno, orderId,refundId,refundFee);
        }
    }

    RefundTo refund(PayService payService, String tradeno, Long orderno, Long refundId, String refundFee) throws Exception {
        RefundTo refundTo = payService.refund(tradeno, orderno, refundId, refundFee);
        refundRecordService.addRecord(refundTo);
        return refundTo;
    }

    QueryOrderTo query(PayService payService, String transactionId, String outTradeNo) throws Exception {
        return payService.query(transactionId, outTradeNo);
    }


}
