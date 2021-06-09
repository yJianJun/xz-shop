package com.cdzg.xzshop.service.pay.impl;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.domain.AlipayTradeQueryModel;
import com.alipay.api.domain.AlipayTradeRefundModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.cdzg.xzshop.domain.OrderPayHistory;
import com.cdzg.xzshop.mapper.OrderPayHistoryMapper;
import com.cdzg.xzshop.to.app.QueryOrderTo;
import com.cdzg.xzshop.to.app.RefundTo;
import com.cdzg.xzshop.utils.pay.PayClientUtils;
import com.cdzg.xzshop.config.pay.AlipayConfig;
import com.cdzg.xzshop.constant.ReceivePaymentType;
import com.cdzg.xzshop.domain.GoodsSpu;
import com.cdzg.xzshop.domain.Order;
import com.cdzg.xzshop.domain.ReceivePaymentInfo;
import com.cdzg.xzshop.mapper.OrderMapper;
import com.cdzg.xzshop.mapper.ReceivePaymentInfoMapper;
import com.cdzg.xzshop.service.pay.PayService;
import io.swagger.util.Json;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service("aliPayService")
@Slf4j
public class ALiPayServiceImpl implements PayService {

    @Resource
    private OrderMapper orderMapper;

    @Resource
    private ReceivePaymentInfoMapper paymentInfoMapper;

    @Resource
    private OrderPayHistoryMapper payHistoryMapper;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public String pay(String ipAddress, List<GoodsSpu> spus, Order order) throws Exception {

        AlipayClient aliPayClient = PayClientUtils.getAliPayClient(order.getId() + "");
        //实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
        AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
        //SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
        AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
        //对一笔交易的具体描述信息
        String spuNames = getSpusName(spus);
        model.setBody(spuNames);
        //商品的标题/交易标题/订单标题/订单关键字等
        model.setSubject("西藏职工app-商城商品订单");
        //商户系统唯一订单号
        model.setOutTradeNo(order.getId() + "");
        //该笔订单允许的最晚付款时间，逾期将关闭交易。取值范围：1m～15d。m-分钟，h-小时，d-天，1c-当天（1c-当天的情况下，无论交易何时创建，都在0点关闭）。
        // 该参数数值不接受小数点， 如 1.5h，可转换为 90m。
        model.setTimeoutExpress("30m");
        //todo:测试:订单总金额，单位为元，精确到小数点后两位，取值范围[0.01,100000000]
        model.setTotalAmount("0.01");
        //销售产品码，商家和支付宝签约的产品码
        model.setProductCode("QUICK_MSECURITY_PAY");
        request.setBizModel(model);
        request.setNotifyUrl(AlipayConfig.getNotifyUrl());
        try {
            //这里和普通的接口调用不同，使用的是sdkExecute
            AlipayTradeAppPayResponse response = aliPayClient.sdkExecute(request);
            //就是orderString 可以直接给客户端请求，无需再做处理。
            log.info(Json.pretty(response));
            return response.getBody();
        } catch (Exception e) {
            log.error("支付宝支付调用接口异常:{}", Json.pretty(e.getStackTrace()));
            throw e;
        }
    }

    private String getSpusName(List<GoodsSpu> spus) {
        return spus.stream().map(GoodsSpu::getGoodsName).collect(Collectors.joining(","));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public String callBack(HttpServletRequest request, HttpServletResponse response) {

        Map<String, String> receiveMap = getReceiveMap(request);
        //商品订单号
        String out_trade_no = receiveMap.get("out_trade_no");
        Order order = orderMapper.findById(Long.parseLong(out_trade_no));

        // 1.商户需要验证该通知数据中的 out_trade_no 是否为商户系统中创建的订单号；
        if (Objects.isNull(order)) {

            log.error("订单支付宝支付失败 -> 系统不存在此交易订单！");
            return "failure";
        }

        ReceivePaymentInfo receivePaymentInfo = paymentInfoMapper.findOneByShopIdAndType(Long.parseLong(order.getShopId()), ReceivePaymentType.Alipay);
        boolean signVerified = false;
        try {
            //3.签名验证(对支付宝返回的数据验证，确定是支付宝返回的)
            //切记alipaypublickey是支付宝的公钥，请去open.alipay.com对应应用下查看。
            //boolean AlipaySignature.rsaCheckV1(Map<String, String> params, String publicKey, String charset, String sign_type)
            signVerified = AlipaySignature.rsaCheckV1(receiveMap,receivePaymentInfo.getPublicKey(), "utf-8", receivePaymentInfo.getSigntype());
            log.info("支付宝支付验签结果:{}", signVerified);
        } catch (Exception e) {
            log.error("支付宝支付验签异常:{}", Json.pretty(e.getStackTrace()));
            return "failure";
        }

        // 当前交易状态
        String tradeStatus = receiveMap.get("trade_status");
        // 该笔订单的资金总额，单位为 RMB-Yuan。取值范围为[0.01,100000000.00]，精确到小数点后两位。
        String total_amount = receiveMap.get("total_amount");
        //支付宝分配给开发者的应用 Id。
        String app_id = receiveMap.get("app_id");
        // 该交易在支付宝系统中的交易流水号。
        String trade_no = receiveMap.get("trade_no");

        //返回状态存入redis中
        //对验签进行处理
        if (!signVerified) {
            //验签不通过 验签失败则记录异常日志，并在response中返回failure.
            log.error("订单支付宝验签失败 -> 验签参数:{}", Json.pretty(receiveMap));
            return "failure";
        }
        //验签通过
        /**
         按照支付结果异步通知中的描述，对支付结果中的业务内容进行1\2\3\4二次校验，校验成功后在response中返回success，校验失败返回failure
         */

        // 2.过滤重复的通知结果数据。 订单状态（1待付款2.待发货3.已发货4.已完成5.已关闭）
        Integer orderStatus = order.getOrderStatus();
        if (orderStatus != 1) {
            log.error("订单号:" + out_trade_no + "的订单状态异常！");
            return "failure";
        }

        // 3.判断 total_amount 是否确实为该订单的实际金额（即商户订单创建时的金额）；
        if (order.getPayMoney().compareTo(new BigDecimal(total_amount)) != 0) {
            log.error("订单号:" + out_trade_no + "金额:" + "与实际支付金额不等");
            return "failure";
        }

        //4.验证 app_id 是否为该商户本身。
        ReceivePaymentInfo paymentInfo = paymentInfoMapper.findOneByShopIdAndType(Long.parseLong(order.getShopId()), ReceivePaymentType.Alipay);
        if (!paymentInfo.getAppid().equals(app_id)) {
            log.error("订单号:" + out_trade_no + "对应店家收款信息与实际收款账号信息不符");
            return "failure";
        }
        //5.校验通知中的 seller_id（或者 seller_email ) 是否为 out_trade_no 这笔单据的对应的操作方（有的时候，一个商户可能有多个seller_id/seller_email）；
        //上述 1、2、3、4有任何一个验证不通过，则表明本次通知是异常通知，务必忽略。

        /**
         * 在支付宝的业务通知中，只有交易通知状态为TRADE_SUCCESS或TRADE_FINISHED时，支付宝才会认定为买家付款成功。
         */
        if ("TRADE_FINISHED".equals(tradeStatus) || "TRADE_SUCCESS".equals(tradeStatus)) {

            //todo:支付成功后的业务处理
            //return updateRecord(info, true, receiveMap);
            addHistoryRecord(out_trade_no,total_amount,order.getTotalMoney(),trade_no,true);
            return "success";
        } else {
            //todo:支付失败后的业务处理
            //  return updateRecord(info, false, receiveMap);
            addHistoryRecord(out_trade_no,total_amount,order.getTotalMoney(), trade_no, false);
            return "failure";
        }
    }

    private void addHistoryRecord(String out_trade_no, String total_amount, BigDecimal orderMoney, String trade_no, Boolean status) {

        OrderPayHistory history = OrderPayHistory.builder()
                .orderNumber(Long.parseLong(out_trade_no))
                .paymentAmount(new BigDecimal(total_amount))
                .payNumber(trade_no)
                .theTotalAmountOfOrders(orderMoney)
                .type(ReceivePaymentType.Alipay)
                .status(status)
                .remark("")
                .build();
        /* 添加支付历史记录 */
        payHistoryMapper.insertOrUpdate(history);
    }


    /**
     * <p>返回说明: Map<String,String> receiveMap
     **/
    private Map<String, String> getReceiveMap(HttpServletRequest request) {

        //获取支付宝POST过来反馈信息
        Map<String, String> params = new HashMap<String, String>();
        //从支付宝回调的request域中取值
        Map requestParams = request.getParameterMap();

        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用。
            //valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            // 乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
            // valueStr = new String(valueStr.getBytes("ISO-8859-1"), "gbk");
            params.put(name, valueStr);
        }

        return params;
    }

    /**
     * 支付宝订单退款
     * * @param outTradeNo   本系统生成的商户订单编号
     * * @param tradeNo      支付宝订单交易号
     * * @param refundAmount 退款金额 不得大于订单金额
     * * @param refundReason 退款说明
     * * @param outRequestNo 标识一次退款请求，同一笔交易多次退款需要保证唯一，如需部分退款，则此参数必传。
     * * @return 公共返回参数 code,msg,   响应参数实例: https://docs.open.alipay.com/api_1/alipay.trade.refund
     *
     * 当交易发生之后一段时间内，由于买家或者卖家的原因需要退款时，卖家可以通过退款接口将支付款退还给买家，
     * 支付宝将在收到退款请求并且验证成功之后，按照退款规则将支付款按原路退到买家帐号上。
     * 交易超过约定时间（签约时设置的可退款时间）的订单无法进行退款
     *
     * 支付宝退款支持单笔交易分多次退款，
     * 多次退款需要提交原支付订单的商户订单号和设置不同的退款单号。
     *
     * 一笔退款失败后重新提交，要采用原来的退款单号。
     * 总退款金额不能超过用户实际支付金额
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public RefundTo refund(String tradeNo, Long outTradeNo, Long refundId, String refundAmount) throws Exception {

        try {
            ////获得初始化的AlipayClient
            AlipayClient alipayClient = PayClientUtils.getAliPayClient(outTradeNo + "");
            //创建API对应的request类
            AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
            AlipayTradeRefundModel model = new AlipayTradeRefundModel();
            //订单支付时传入的商户订单号，商家自定义且保证商家系统中唯一。与支付宝交易号 trade_no 不能同时为空。
            model.setOutTradeNo(outTradeNo+"");
            //	支付宝交易号，和商户订单号 out_trade_no 不能同时为空。
            model.setTradeNo(tradeNo);
            //todo：测试时0.01 需要退款的金额，该金额不能大于订单金额,单位为元，支持两位小数
            model.setRefundAmount("0.01");
            //标识一次退款请求，同一笔交易多次退款需要保证唯一，如需部分退款，则此参数必传。
            model.setOutRequestNo(Long.toString(refundId));
            //	退款原因说明，商家自定义。
            model.setRefundReason("正常退款");

            request.setBizModel(model);
            AlipayTradeRefundResponse response = alipayClient.execute(request);
            if (response.isSuccess()) {
                log.info("支付宝app退款成功");
                return transferTo(response,Long.toString(refundId));
            } else {
                log.debug("支付宝app退款失败:{}", response.getSubMsg());//失败会返回错误信息
                return transferTo(response,Long.toString(refundId));
            }
        } catch (Exception e) {
            log.error("支付宝app退款报错:{}", Json.pretty(e.getStackTrace()));
            throw e;
        }
    }

    private RefundTo transferTo(AlipayTradeRefundResponse response, String refundId) {

        RefundTo refundTo = new RefundTo();
        refundTo.setRefundFee(response.getRefundFee());
        refundTo.setTradeNo(response.getTradeNo());
        refundTo.setOutRequestNo(refundId);
        refundTo.setOutTradeNo(response.getOutTradeNo());
        refundTo.setType(ReceivePaymentType.Alipay);
        return refundTo;
    }

    /**
     * 查看支付订单信息
     *
     * @param outTradeNo 商户网站唯一订单号 订单支付时传入的商户订单号,和支付宝交易号不能同时为空。trade_no,out_trade_no如果同时存在优先取trade_no
     * @param tradeNo    支付宝交易号  支付宝交易号，和商户订单号不能同时为空
     * @return 公共响应参数 code,msg    响应参数: https://docs.open.alipay.com/api_1/alipay.trade.query
     *
     * 该接口提供所有支付宝支付订单的查询，商户可以通过该接口主动查询订单状态，完成下一步的业务逻辑。
     * 需要调用查询接口的情况：
     * 当商户后台、网络、服务器等出现异常，商户系统最终未接收到支付通知；
     * 调用支付接口后，返回系统错误或未知交易状态情况；
     * 调用alipay.trade.pay，返回INPROCESS的状态；
     * 调用alipay.trade.cancel之前，需确认支付状态；
     */
    @Override
    public QueryOrderTo query(String tradeNo, String outTradeNo) throws Exception {

        AlipayClient aliPayClient = PayClientUtils.getAliPayClient(outTradeNo);
        AlipayTradeQueryRequest alipayTradeQueryRequest = new AlipayTradeQueryRequest();
        AlipayTradeQueryModel model = new AlipayTradeQueryModel();
        model.setOutTradeNo(outTradeNo);
        model.setTradeNo(tradeNo);
        alipayTradeQueryRequest.setBizModel(model);
        try {
            AlipayTradeQueryResponse response = aliPayClient.execute(alipayTradeQueryRequest);
            QueryOrderTo queryOrderTo = new QueryOrderTo();
            queryOrderTo.setBuyerUserId(response.getBuyerUserId());
            queryOrderTo.setTradeStatus(response.getTradeStatus());
            queryOrderTo.setType(ReceivePaymentType.Alipay);
            queryOrderTo.setOutTradeNo(response.getOutTradeNo());
            queryOrderTo.setTotalAmount(response.getTotalAmount());
            queryOrderTo.setTradeNo(response.getTradeNo());
            return queryOrderTo;
        } catch (AlipayApiException e) {
            log.error("支付宝app查询支付订单报错:{}", Json.pretty(e.getStackTrace()));
            throw e;
        }
    }
}
