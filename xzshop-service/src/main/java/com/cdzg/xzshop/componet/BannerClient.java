package com.cdzg.xzshop.componet;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cdzg.cms.api.vo.app.request.CmsAppBannerRequest;
import com.cdzg.cms.api.vo.app.response.CmsAppBannerResponse;
import com.cdzg.xzshop.utils.HttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName : BannerClient
 * @Description : banner服务
 * @Author : XLQ
 * @Date: 2021-01-15 11:05
 */

@Component
@Slf4j
public class BannerClient {

    @Value("${cms.banner.request.url:http://47.105.37.49/app/}")
    private String cmsBannerURL;

    @Value("${cms.banner.request.uri:banner/common}")
    private String cmsBannerURI;

    /**
     * 获取banner
     *
     * @param request
     * @param token   app token
     * @return
     */
    public List<CmsAppBannerResponse> getBannerConfigList(CmsAppBannerRequest request, String token) {
        log.info("获取app banner {}", request);
        try {
            Map<String, String> headerMap = new HashMap<>(1);
            headerMap.put("token", token);
            String post = HttpUtils.post(cmsBannerURL + cmsBannerURI + "?bannerGroupId=" + request.getBannerGroupId() , HttpUtils.CONTENT_TYPE_JSON, headerMap, null, null);
            log.info("请求banner接口返回： {}", post);
            if (StringUtils.isNotBlank(post)) {
                JSONObject jsonObject = JSONObject.parseObject(post);
                if (jsonObject.getInteger("code") == 200 && jsonObject.getString("data") != null) {
                    String data = jsonObject.getString("data");
                    return JSONArray.parseArray(data, CmsAppBannerResponse.class);
                }
            }
        } catch (Exception e) {
            log.error("获取app banner error: {}", e.getMessage());
        }
        return null;
    }
}
