package com.cdzg.xzshop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cdzg.xzshop.domain.RefundOrder;
import com.cdzg.xzshop.vo.admin.refund.*;
import com.cdzg.xzshop.vo.app.refund.ApplyRefundVO;
import com.cdzg.xzshop.vo.app.refund.BuyerShipVO;
import com.cdzg.xzshop.vo.app.refund.SellerRefuseReceiptVO;
import com.cdzg.xzshop.vo.common.BasePageRequest;
import com.cdzg.xzshop.vo.common.PageResultVO;

import javax.servlet.http.HttpServletResponse;

public interface RefundOrderService extends IService<RefundOrder> {

    /**
     * 分页查询退货订单
     * @param queryVO
     * @return
     */
    PageResultVO<RefundOrderListVO> getRefundOrderPage(RefundOrderQueryVO queryVO);

    /**
     * 退货订单统计
     * @return
     */
    RefundOrderStatisticVO getRefundOrderStatistic();

    /**
     * 申请退款
     * @param vo
     * @return
     */
    String applyRefundOrder(ApplyRefundVO vo);

    /**
     * 拒绝退款/退货
     * @param vo
     * @return
     */
    String refuseRefund(RefuseRefundVO vo);

    /**
     * 同意退款/退货
     * @param id
     * @return
     */
    String agreeRefund(Long id);

    /**
     * 买家发货填写物流单号
     * @param vo
     * @return
     */
    String buyerShip(BuyerShipVO vo);

    /**
     * 卖家收货
     * @param id
     * @return
     */
    String sellerAgreeReceipt(Long id);

    /**
     * 卖家拒绝收货
     * @param vo
     * @return
     */
    String sellerRefuseReceipt(SellerRefuseReceiptVO vo);

    /**
     * 根据id获取退款详情
     * @param id
     * @return
     */
    RefundOrderDetailVO getAdminDetailById(Long id);

    /**
     * 根据id获取退款详情app
     * @param id
     * @return
     */
    RefundOrderDetailAppVO getAppDetailById(Long id);

    /**
     * app获取退款分页
     * @return
     */
    PageResultVO<RefundOrderListAppVO> getRefundAppPage(BasePageRequest request);

    /**
     * app撤销退款退货申请
     * @param id
     * @return
     */
    String revokeRefund(Long id);

    /**
     * 买家提交退款，卖家未处理，自动退款
     */
    void autoRefund();

    /**
     * 买家提交退货退款/换货申请，卖家未处理，系统自动处理
     */
    void systemAutoDeal();

    /**
     * 卖家同意退货/换货，买家未处理，系统自动失败
     */
    void systemAutoFail();

    /**
     * 卖家确认收货，未处理退款，系统自动退款
     */
    void systemAutoRefund();

    /**
     * 卖家未收到货
     * @param vo
     * @return
     */
    String sellerNotReceipt(SellerRefuseReceiptVO vo);

    /**
     * 导出
     * @param queryVO
     * @param response
     */
    void export(RefundOrderQueryVO queryVO, HttpServletResponse response);
}
