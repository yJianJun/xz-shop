package com.cdzg.xzshop.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cdzg.universal.vo.response.user.UserLoginResponse;
import com.cdzg.xzshop.config.annotations.api.IgnoreAuth;
import com.cdzg.xzshop.config.annotations.api.WebApi;
import com.cdzg.xzshop.constant.ReceivePaymentType;
import com.cdzg.xzshop.domain.ReceivePaymentInfo;
import com.cdzg.xzshop.domain.ShopInfo;
import com.cdzg.xzshop.filter.auth.LoginSessionUtils;
import com.cdzg.xzshop.mapper.ReceivePaymentInfoMapper;
import com.cdzg.xzshop.service.ReceivePaymentInfoService;
import com.cdzg.xzshop.service.ShopInfoService;
import com.cdzg.xzshop.vo.admin.*;
import com.framework.utils.core.api.ApiResponse;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("admin/shop")
@Validated
@Api(tags = "admin_店铺管理")
public class ShopInfoController {


    @Autowired
    ShopInfoService shopInfoService;

    @Autowired
    ReceivePaymentInfoService receivePaymentInfoService;


    @WebApi
    @PostMapping("/batch/switch")
    @IgnoreAuth
    @ApiOperation("店铺批量上下架")
    public ApiResponse<String> batchPutOnDown(@ApiParam(value = "店铺批量上下架参数", required = true)@RequestBody @Valid ShopSwitchStatusVO statusVO) {

        //UserLoginResponse adminUser = LoginSessionUtils.getAdminUser();
        //if (adminUser == null) {
        //    return ApiResponse.buildCommonErrorResponse("登录失效，请重新登录");
        //}
        List<Long> list = statusVO.getList();
        if (CollectionUtils.isNotEmpty(list)){

            QueryWrapper<ShopInfo> queryWrapper = new QueryWrapper<>();
            for (int i = 0; i < list.size(); i++) {

                Long id = list.get(i);
                ShopInfo shopInfo = shopInfoService.getBaseMapper().selectOne(queryWrapper.eq("id",id));
                if (Objects.nonNull(shopInfo)){
                    shopInfo.setStatus(statusVO.getFlag());
                    shopInfoService.updateById(shopInfo);
                }
            }
            return ApiResponse.buildSuccessResponse("编辑成功");
        }
        return ApiResponse.buildCommonErrorResponse("编辑失败");
    }

    @WebApi
    @PostMapping("/page")
    @IgnoreAuth
    @ApiOperation("分页查询店铺列表")
    public ApiResponse<PageInfo<ShopInfo>> page(@ApiParam(value = "店铺分页参数模型", required = true)@RequestBody @Valid ShopPageVo vo) {
        //UserLoginResponse adminUser = LoginSessionUtils.getAdminUser();
        //if (adminUser == null) {
        //    return ApiResponse.buildCommonErrorResponse("登录失效，请重新登录");
        //}
        PageInfo<ShopInfo> pageInfo = shopInfoService.findAllByShopNameLikeAndStatusAndGmtPutOnTheShelfBetweenEqualwithPage(vo.getCurrentPage(), vo.getPageSize(), vo.getShopName(), vo.getStatus(), vo.getStart(), vo.getEnd());
        return ApiResponse.buildSuccessResponse(pageInfo);
    }

    @WebApi
    @PostMapping("/add")
    @IgnoreAuth
    @ApiOperation("新建店铺")
    public ApiResponse<String> add(@ApiParam(value = "店铺添加参数模型", required = true)@RequestBody @Valid ShopInfoAddVo addVo) {

        //UserLoginResponse adminUser = LoginSessionUtils.getAdminUser();
        //if (adminUser == null) {
        //    return ApiResponse.buildCommonErrorResponse("登录失效，请重新登录");
        //}

        ShopInfo shopInfo = ShopInfo.builder()
                .shopName(addVo.getShopName())
                .createUser("")   //yjjtodo 需改
                .contactPerson(addVo.getPerson())
                .department(addVo.getDepartment())
                .fare(addVo.getFare())
                .gmtCreate(LocalDateTime.now())
                .logo(addVo.getLogoUrl())
                .status(false)
                .shopUnion(addVo.getUnion())
                .phone(addVo.getContact())
                .build();

        shopInfoService.save(shopInfo);

        AliPayReceiveVo aliPayVo = addVo.getAliPayVo();

        if (Objects.nonNull(aliPayVo)){

            ReceivePaymentInfo aliInfo = ReceivePaymentInfo.builder()
                    .appid(aliPayVo.getAppId())
                    .shopId(shopInfo.getId())
                    .keyPath("")
                    .mchid("")
                    .privateKey(aliPayVo.getPrivateKey())
                    .publicKey(aliPayVo.getPublicKey())
                    .signtype(aliPayVo.getSigntype())
                    .type(ReceivePaymentType.Alipay)
                    .build();
            receivePaymentInfoService.insert(aliInfo);
        }

        WeChatReceiveVo wxPayVo = addVo.getWxPayVo();
        if (Objects.nonNull(wxPayVo)){

            ReceivePaymentInfo aliInfo = ReceivePaymentInfo.builder()
                    .appid(wxPayVo.getAppId())
                    .signtype("")
                    .shopId(shopInfo.getId())
                    .keyPath(wxPayVo.getKeyPath())
                    .mchid(wxPayVo.getMchId())
                    .privateKey(wxPayVo.getPrivateKey())
                    .publicKey("")
                    .type(ReceivePaymentType.Wechat)
                    .build();

            receivePaymentInfoService.insert(aliInfo);
        }

        return ApiResponse.buildSuccessResponse("成功");
    }


}

