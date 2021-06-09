package com.cdzg.xzshop.service.Impl;

import com.alibaba.fastjson.JSONObject;
import com.cdzg.xzshop.utils.HttpUtils;
import com.cdzg.xzshop.utils.HttpsClientUtil;
import com.cdzg.xzshop.vo.order.request.AppPayPointsReqVO;
import com.framework.utils.core.api.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @ClassName : UserPointsService
 * @Description : 用户积分服务
 * @Author : EvilPet
 * @Date: 2021-06-01 13:33
 */

@Service
@Slf4j
public class UserPointsService {


    @Value("${user.points.request.url:http://47.105.37.49/xzunion/}")
    private String userPointsUrl;

    @Value("${user.points.getUserPoints.uri:app/points/getUserEffectivePoints}")
    private String getUserEffectivePointsUri;

    @Value("${user.points.getUserPoints.uri:app/points/payPoint}")
    private String payPointUri;

    /**
     * 根据token查询用户有效积分余额
     *
     * @param token
     * @return
     */
    public Integer getUserEffectivePoints(String token) {
        Map<String, String> headers = new HashMap<>(2);
        headers.put("token", token);
        headers.put("content-type", HttpUtils.CONTENT_TYPE_JSON);
        try {
            int retry = 0;
            while (retry < 3) {
                retry++;
                JSONObject jsonObject = HttpsClientUtil.doGet(userPointsUrl, getUserEffectivePointsUri, headers, null);
                log.info("查询用户积分返回：{}", JSONObject.toJSONString(jsonObject));
                if (Objects.nonNull(jsonObject) && jsonObject.getInteger("code") == 200) {
                    return jsonObject.getInteger("data");
                }
            }
        } catch (Exception e) {
            log.error("查询用户可用积分error：{}", e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 积分消费接口
     * @return
     */
    public JSONObject payPoint(String token,AppPayPointsReqVO payPointsReqVO){
        Map<String, String> headers = new HashMap<>(2);
        headers.put("token", token);
        headers.put("content-type", HttpUtils.CONTENT_TYPE_JSON);
        try {
            int retry = 0;
            while (retry < 3) {
                retry++;
                JSONObject jsonObject = HttpsClientUtil.doPost(userPointsUrl, payPointUri, headers, null,JSONObject.toJSONString(payPointsReqVO));
                log.info("积分消费接口返回：{}", JSONObject.toJSONString(jsonObject));
                if (Objects.nonNull(jsonObject)) {
                    return jsonObject;
                }
            }
        } catch (Exception e) {
            log.error("查询用户可用积分error：{}", e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

}
