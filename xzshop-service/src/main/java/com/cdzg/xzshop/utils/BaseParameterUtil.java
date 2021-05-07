package com.cdzg.xzshop.utils;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cdzg.base.vo.response.parameter.BaseParameterDetailResVO;
import com.cdzg.base.vo.response.parameter.BaseParameterRespVO;
import com.cdzg.base.vo.response.parameter.TreeVO;
import com.cdzg.universal.vo.request.organization.UserBelongOrgQueryVO;
import com.cdzg.universal.vo.response.organization.OrgBelongResponse;
import com.cdzg.universal.vo.response.organization.OrganiationSearchResponse;
import com.cdzg.xzshop.vo.common.IdsReqVo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @ClassName : BaseParameterUtil
 * @Description : 基础参数工具类
 * @Author : XLQ
 * @Date: 2020-12-04 16:43
 */
@Component
@Lazy
public class BaseParameterUtil {

    @Autowired
    private RedisUtil redisUtil;

    @Value("${universal.getOrgByIds.url}")
    private String getOrgByIdsUrl;

    @Value("${universal.judgeUserBelongOrg.url}")
    private String judgeUserBelongOrg;

    /**
     * 根据传入的type,返回map
     *
     * @param type
     * @return
     */
    public Map<String, Map<String, String>> getParameters(String type) {
        String[] split = null;
        if (StringUtils.isNotEmpty(type)) {
            split = type.split(",");
        } else {
            return null;
        }
        Map<String, Map<String, String>> baseParameterMap = getAllParameter();
        Map<String, Map<String, String>> baseParameterTempMap = new HashMap<>();
        if (!Objects.isNull(baseParameterMap)) {
            for (String s1 : split) {
                String trim = s1.trim();
                if (baseParameterMap.containsKey(trim)) {
                    baseParameterTempMap.put(trim, baseParameterMap.get(trim));
                }
            }
        }
        return baseParameterTempMap;
    }


    /**
     * 把code翻译中文
     *
     * @param type 参数类型
     * @param code code
     * @return
     */
    public String getParameterName(String type, Integer code) {
        //从redis中获取数据
        Map<String, Map<String, String>> baseParameterMap = getAllParameter();
        if (!Objects.isNull(baseParameterMap)) {
            return baseParameterMap.get(type).get(code + "");
        }
        return null;
    }


    /**
     * 把type翻译中文
     *
     * @param type
     * @return
     */
    public String getParameterTypeName(String type) {
        Map<String, Map<String, String>> baseParameterMap = getAllParameter();
        if (!Objects.isNull(baseParameterMap)) {
            return baseParameterMap.get("type").get(type);
        }
        return null;
    }

    /**
     * 根据传入的type,返回map
     *
     * @param objects
     * @return
     */
    public Map<String, Map<String, String>> getParameters(String... objects) {
        Map<String, Map<String, String>> baseParameterMap = getAllParameter();
        Map<String, Map<String, String>> baseParameterTempMap = new HashMap<>();
        if (!Objects.isNull(baseParameterMap)) {
            for (String s1 : objects) {
                String trim = s1.trim();
                if (baseParameterMap.containsKey(trim)) {
                    baseParameterTempMap.put(trim, baseParameterMap.get(trim));
                }
            }
        }
        return baseParameterTempMap;
    }

    private Map<String, Map<String, String>> getAllParameter() {
        Map<String, Map<String, String>> baseParameterMap = null;
        String s = (String) redisUtil.getCacheSimple("XZ_BASE_PARAMETER");
        if (s != null) {
            baseParameterMap = JSONObject.parseObject(s, Map.class);
        }
        return baseParameterMap;
    }

    private Map<String, List<BaseParameterDetailResVO>> getParameterList() {
        Map<String, List<BaseParameterDetailResVO>> baseParameterList = null;
        String s = (String) redisUtil.getCacheSimple("XZ_BASE_PARAMETER_LIST");
        if (s != null) {
            baseParameterList = JSONObject.parseObject(s, Map.class);
        }
        return baseParameterList;
    }

    public List<BaseParameterRespVO> getParameterList(String type) {
        if (StringUtils.isBlank(type)) {
            return null;
        }
        String[] split = type.split(",");
        List<BaseParameterRespVO> baseParameterList = new ArrayList<>();
        //获取数据
        Map<String, Map<String, String>> allParameter = this.getAllParameter();
        Map<String, String> typeTemp = allParameter.get("type");
        Map<String, List<BaseParameterDetailResVO>> baseParameterTemp = this.getParameterList();
        for (String object : split) {
            object = object.trim();
            BaseParameterRespVO baseParameterRespVO = new BaseParameterRespVO();
            baseParameterRespVO.setType(object);
            baseParameterRespVO.setTitle(typeTemp.get(object));
            baseParameterRespVO.setList(baseParameterTemp.getOrDefault(object, Lists.newArrayList()));
            baseParameterList.add(baseParameterRespVO);
        }
        return baseParameterList;
    }

    /**
     * 获取省市区树
     *
     * @return
     */
    public List<TreeVO> getAreaList() {
        //从redis中获取数据
        String s = (String) redisUtil.getCacheStr("XZ_BASE_AREA");
        List<TreeVO> result = JSON.parseArray(s, TreeVO.class);
        if (CollectionUtils.isNotEmpty(result)) {
            return result;
        }
        return new ArrayList<>();
    }

    /**
     * 根据ids查询组织架构
     * @param ids
     * @return
     */
    public Map<Long, OrganiationSearchResponse> getOrgMapByIds(List<Long> ids){
        Map<Long, OrganiationSearchResponse> map = Maps.newHashMap();
        Map<String, String> headers = Maps.newHashMap();
        IdsReqVo idsReqVo = new IdsReqVo();
        idsReqVo.setIds(ids);
        try {
            Gson gson = new Gson();
            JSONObject result = HttpsClientUtil.doPostJson(getOrgByIdsUrl, null, headers, gson.toJson(idsReqVo));
            if (Objects.nonNull(result.get("code")) && result.get("code").equals(200)) {
                for (Map.Entry<String, Object> data : result.getJSONObject("data").entrySet()) {
                    map.put(Long.parseLong(data.getKey()), gson.fromJson(gson.toJson(data.getValue()), OrganiationSearchResponse.class));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }



    /**
     * 根据name获取地点code
     * 如果无数据默认返回拉萨市code 540100
     * @param areaName
     * @return
     */
    public Integer getAreaCodeByName(String areaName) {
        List<TreeVO> areaList = getAreaList();
        Map<Integer, String> areaMap = new HashMap<>();
        treeToMap(areaMap, areaList);
        if (!areaMap.isEmpty()) {
            for (Map.Entry<Integer, String> entry : areaMap.entrySet()) {
                if (entry.getValue().equals(areaName)) {
                    return entry.getKey();
                }
            }
        }
        return 540100;
    }

    /**
     * 根据name获取地点code
     *
     * @param areaCode
     * @return
     */
    public String getAreaNameByCode(Integer areaCode) {
        List<TreeVO> areaList = getAreaList();
        Map<Integer, String> areaMap = new HashMap<>();
        treeToMap(areaMap, areaList);
        if (!areaMap.isEmpty()) {
            return areaMap.get(areaCode);
        }
        return null;
    }


    private void treeToMap(Map<Integer, String> areaMap, List<TreeVO> areaList) {
        if (CollectionUtils.isNotEmpty(areaList)) {
            for (TreeVO treeVO : areaList) {
                areaMap.put(treeVO.getId(), treeVO.getName());
                if (CollectionUtils.isNotEmpty(treeVO.getChildren())) {
                    treeToMap(areaMap, treeVO.getChildren());
                }
            }
        }
    }


    /**
     * 根据ids查询组织架构
     * @return
     */
    public OrgBelongResponse judgeUserBelongOrg(UserBelongOrgQueryVO queryVO){
        OrgBelongResponse response = new OrgBelongResponse();
        Map<String, String> headers = Maps.newHashMap();
        try {
            Gson gson = new Gson();
            JSONObject result = HttpsClientUtil.doPostJson(judgeUserBelongOrg, null, headers, gson.toJson(queryVO));
            if (Objects.nonNull(result.get("code")) && result.get("code").equals(200)) {
                response = gson.fromJson(result.getString("data"), OrgBelongResponse.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

}