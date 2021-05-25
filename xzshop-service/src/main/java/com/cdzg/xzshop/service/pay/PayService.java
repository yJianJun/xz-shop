package com.cdzg.xzshop.service.pay;

import com.alibaba.fastjson.JSONObject;
import com.cdzg.xzshop.domain.GoodsSpu;
import com.cdzg.xzshop.domain.Order;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public interface PayService {

    Object query(String transactionId,String outTradeNo) throws Exception;
    /**
     * 创建支付订单，对应文章中第一步，由合理的业务 service 调用，
     * 比如：购买商品业务中：
     * 用户选择商品提交服务器 ->
     * 服务器计算价格，生成业务订单、生成支付订单 ->
     * 用户支付 ->
     * 服务器确认支付结果，更新支付订单状态、更新业务订单状态 ->
     * 绑定物流单 -> ...
     * 此处，应该是在第二步中生成该支付订单
     *
     * @param orderId   公司业务订单号
     * @param ipAddress 客户端APP IP 地址
     * @param spus
     * @param order
     * @return 返回的信息直接发给客户端即可
     * @throws IOException
     */
    Object pay(Long orderId, String ipAddress, List<GoodsSpu> spus, Order order) throws Exception;

    /**
     * 微信服务器调用该接口，进行数据异步传回作用
     *
     * @param request
     * @param response
     * @return 一个代表支付成功／失败的 XML 信息（失败原因应该是：签名失败，成功则表示确认收到数据，微信不需要再发数据到服务器）
     */
    String callBack(HttpServletRequest request, HttpServletResponse response);

    Object refund(String tradeno, Long orderno,Long refundId,String refundFee) throws Exception;
}
