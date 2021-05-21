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
import com.cdzg.xzshop.componet.SnowflakeIdWorker;
import com.cdzg.xzshop.config.pay.AlipayConfig;
import com.cdzg.xzshop.domain.GoodsSpu;
import com.cdzg.xzshop.domain.Order;
import com.cdzg.xzshop.service.pay.PayService;
import io.swagger.util.Json;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service("aliPayService")
@Slf4j
public class ALiPayServiceImpl implements PayService {

    @Autowired
    private SnowflakeIdWorker snowflakeIdWorker;

    @Override
    public String pay(Long orderId, String ipAddress, List<GoodsSpu> spus, Order order) throws Exception {

        AlipayClient alipayClient = AlipayConfig.buildAlipayClient();
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
        model.setOutTradeNo(orderId+"");
        //该笔订单允许的最晚付款时间，逾期将关闭交易。取值范围：1m～15d。m-分钟，h-小时，d-天，1c-当天（1c-当天的情况下，无论交易何时创建，都在0点关闭）。
        // 该参数数值不接受小数点， 如 1.5h，可转换为 90m。
        model.setTimeoutExpress("30m");
        //yjjtodo 测试:订单总金额，单位为元，精确到小数点后两位，取值范围[0.01,100000000]
        model.setTotalAmount("0.01");
        //销售产品码，商家和支付宝签约的产品码
        model.setProductCode("QUICK_MSECURITY_PAY");
        request.setBizModel(model);
        request.setNotifyUrl(AlipayConfig.getNotifyUrl());
        try {
            //这里和普通的接口调用不同，使用的是sdkExecute
            AlipayTradeAppPayResponse response = alipayClient.sdkExecute(request);
            //就是orderString 可以直接给客户端请求，无需再做处理。
            log.info(Json.pretty(response.getBody()));
            return response.getBody();
        } catch (Exception e) {
            log.error("支付宝支付调用接口异常:{}", Json.pretty(e.getStackTrace()));
            throw e;
        }
    }

    private String getSpusName(List<GoodsSpu> spus) {
        return spus.stream().map(GoodsSpu::getGoodsName).collect(Collectors.joining(","));
    }

    /**
     *
     * @param request
     * @param response
     * @return
     * yjjtodo 在步骤四验证签名正确后，必须再严格按照如下描述校验通知数据的正确性。
     *
     * 1.商户需要验证该通知数据中的 out_trade_no 是否为商户系统中创建的订单号；
     *
     * 2.判断 total_amount 是否确实为该订单的实际金额（即商户订单创建时的金额）；
     *
     * 3.校验通知中的 seller_id（或者 seller_email ) 是否为 out_trade_no 这笔单据的对应的操作方（有的时候，一个商户可能有多个seller_id/seller_email）；
     *
     * 4.验证 app_id 是否为该商户本身。
     *
     * 5.过滤重复的通知结果数据。
     *
     * 上述 1、2、3、4有任何一个验证不通过，则表明本次通知是异常通知，务必忽略。
     *
     * 在支付宝的业务通知中，只有交易通知状态为 TRADE_SUCCESS 或 TRADE_FINISHED 时，支付宝才会认定为买家付款成功。
     */

    @Override
    public String callBack(HttpServletRequest request, HttpServletResponse response) {

        Map<String, String> receiveMap = getReceiveMap(request);

        boolean signVerified = false;
        try {
            //3.签名验证(对支付宝返回的数据验证，确定是支付宝返回的)
            //切记alipaypublickey是支付宝的公钥，请去open.alipay.com对应应用下查看。
            //boolean AlipaySignature.rsaCheckV1(Map<String, String> params, String publicKey, String charset, String sign_type)
            signVerified = AlipaySignature.rsaCheckV1(receiveMap, AlipayConfig.getAlipayPublicKey(), "utf-8", "RSA2");
            log.info("支付宝支付验签结果:{}", signVerified);
        } catch (AlipayApiException e) {
            log.error("支付宝支付验签异常:{}", Json.pretty(e.getStackTrace()));
            return "failure";
        }

        // 当前交易状态
        String tradeStatus = receiveMap.get("trade_status");
        //商品订单号
        String out_trade_no = receiveMap.get("out_trade_no");

        //返回状态存入redis中
        //对验签进行处理
        if (signVerified) {

            /** yjjtodo 验签成功后
            按照支付结果异步通知中的描述，对支付结果中的业务内容进行1\2\3\4二次校验，校验成功后在response中返回success，校验失败返回failure
             yjjtodo: 对支付宝回调通知的订单号 在数据库进行幂等 去重 是否存在进行判断
             */

            //验签通过
            // OrderInfo info = orderInfoService.findOneByOrderNo(Long.valueOf(out_trade_no));

            //1、商户需要验证该通知数据中的out_trade_no是否为商户系统中创建的订单号，
            //if (Objects.isNull(info)) {
            //
            //    log.error("订单支付宝支付失败 -> 系统不存在此交易订单！");
            //    return "failure";
            //}

            //OrderStatus status = info.getStatus();
            //// 订单已经支付
            //if (status != OrderStatus.Unpaid && status != OrderStatus.Payment_Failed) {
            //
            //    log.error("订单号:" + out_trade_no + "的订单状态异常！");
            //    return "failure";
            //}

            /**
             * 在支付宝的业务通知中，只有交易通知状态为TRADE_SUCCESS或TRADE_FINISHED时，支付宝才会认定为买家付款成功。
             */
            if ("TRADE_FINISHED".equals(tradeStatus) || "TRADE_SUCCESS".equals(tradeStatus)) {

                //支付成功后的业务处理
                //return updateRecord(info, true, receiveMap);
                return "success";
            } else {
                //支付失败后的业务处理
                //  return updateRecord(info, false, receiveMap);
                return "failure";
            }
        } else {  //验签不通过 验签失败则记录异常日志，并在response中返回failure.
            log.error("订单支付宝验签失败 -> 验签参数:{}", Json.pretty(receiveMap));
            return "failure";
        }
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
     */
    @Override
    public String refund(String outTradeNo, String tradeNo, String refundAmount) throws AlipayApiException {
        String returnStr = null;

        long refundId = snowflakeIdWorker.nextId(); //yjjtodo 雪花算法生成退款交易号

        //todo:订单编号,支付宝交易号不为空
        try {
            ////获得初始化的AlipayClient
            AlipayClient alipayClient = AlipayConfig.getAlipayClient();
            //创建API对应的request类
            AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
            ////设置业务参数
            //alipay.trade.refund
            AlipayTradeRefundModel model = new AlipayTradeRefundModel();
            model.setOutTradeNo(outTradeNo);
            model.setTradeNo(tradeNo);
            //yjjtodo 退款金额，单位为元，精确到小数点后两位，取值范围[0.01,100000000]
            model.setRefundAmount("0.01");
            model.setOutRequestNo(Long.toString(refundId));
           // model.setRefundReason(refundReason);

            request.setBizModel(model);
            AlipayTradeRefundResponse response = alipayClient.execute(request);
            if (response.isSuccess()) {
                log.info("支付宝app退款成功");
                returnStr = response.getBody();
            } else {
                returnStr = response.getSubMsg();//失败会返回错误信息
                log.debug("支付宝app退款失败:{}", returnStr);
            }

        } catch (Exception e) {
            log.error("支付宝app退款报错:{}", Json.pretty(e.getStackTrace()));
            throw e;
        }
        return returnStr;
    }

    /**
     * 查看支付订单信息
     *
     * @param outTradeNo 商户网站唯一订单号
     * @param tradeNo    支付宝交易号
     * @return 公共响应参数 code,msg    响应参数: https://docs.open.alipay.com/api_1/alipay.trade.query
     */
    @Override
    public String query(String outTradeNo, String tradeNo) throws AlipayApiException {

        AlipayClient alipayClient = AlipayConfig.buildAlipayClient();
        AlipayTradeQueryRequest alipayTradeQueryRequest = new AlipayTradeQueryRequest();
        AlipayTradeQueryModel model = new AlipayTradeQueryModel();
        model.setOutTradeNo(outTradeNo);
        model.setTradeNo(tradeNo);
        alipayTradeQueryRequest.setBizModel(model);
        try {
            AlipayTradeQueryResponse alipayTradeQueryResponse = alipayClient.execute(alipayTradeQueryRequest);
            return alipayTradeQueryResponse.getBody();
        } catch (AlipayApiException e) {
            log.error("支付宝app查询支付订单报错:{}", Json.pretty(e.getStackTrace()));
            throw e;
        }
    }
}
