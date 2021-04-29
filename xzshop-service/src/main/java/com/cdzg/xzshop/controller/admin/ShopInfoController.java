package com.cdzg.xzshop.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cdzg.universal.vo.response.user.UserLoginResponse;
import com.cdzg.xzshop.common.BaseException;
import com.cdzg.xzshop.common.CommonResult;
import com.cdzg.xzshop.common.ResultCode;
import com.cdzg.xzshop.config.annotations.api.IgnoreAuth;
import com.cdzg.xzshop.config.annotations.api.WebApi;
import com.cdzg.xzshop.constant.ReceivePaymentType;
import com.cdzg.xzshop.domain.ReceivePaymentInfo;
import com.cdzg.xzshop.domain.ShopInfo;
import com.cdzg.xzshop.filter.auth.LoginSessionUtils;
import com.cdzg.xzshop.mapper.ReceivePaymentInfoMapper;
import com.cdzg.xzshop.service.ReceivePaymentInfoService;
import com.cdzg.xzshop.service.ShopInfoService;
import com.cdzg.xzshop.to.admin.ShopInfoDetailTo;
import com.cdzg.xzshop.vo.admin.*;
import com.cdzg.xzshop.vo.common.PageResultVO;
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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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

                    if (statusVO.getFlag()){
                        shopInfo.setGmtPutOnTheShelf(LocalDateTime.now());
                    }
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
    public ApiResponse<PageResultVO<ShopInfo>> page(@ApiParam(value = "店铺分页参数模型", required = true)@RequestBody @Valid ShopPageVo vo) {
        //UserLoginResponse adminUser = LoginSessionUtils.getAdminUser();
        //if (adminUser == null) {
        //    return ApiResponse.buildCommonErrorResponse("登录失效，请重新登录");
        //}
        PageResultVO<ShopInfo> pageInfo = shopInfoService.page(vo.getCurrentPage(), vo.getPageSize(), vo.getShopName(), vo.getStatus(), vo.getStart(), vo.getEnd());
        return ApiResponse.buildSuccessResponse(pageInfo);
    }

    @WebApi
    @PostMapping("/add")
    @IgnoreAuth
    @ApiOperation("新建店铺-运营端")
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

    @WebApi
    @GetMapping("/get")
    @IgnoreAuth
    @ApiOperation("店铺详情-运营端")
    public ApiResponse<ShopInfoDetailTo> get(@Valid @RequestParam("id") @NotNull @ApiParam(value = "店铺id", required = true) Long id ) {

        //UserLoginResponse adminUser = LoginSessionUtils.getAdminUser();
        //if (adminUser == null) {
        //    return ApiResponse.buildCommonErrorResponse("登录失效，请重新登录");
        //}

        ShopInfo shopInfo = shopInfoService.getById(id);
        List<ReceivePaymentInfo> paymentInfos = receivePaymentInfoService.findAllByShopId(id);

        ShopInfoDetailTo detail = ShopInfoDetailTo.builder()
                .shopInfo(shopInfo)
                .payments(paymentInfos).build();

        return CommonResult.buildSuccessResponse(detail);
    }

    @WebApi
    @PostMapping("/update")
    @IgnoreAuth
    @ApiOperation("编辑店铺")
    public ApiResponse update(@ApiParam(value = "店铺编辑参数模型", required = true)@RequestBody @Valid ShopInfoUpdateVO vo ) {

        //UserLoginResponse adminUser = LoginSessionUtils.getAdminUser();
        //if (adminUser == null) {
        //    return ApiResponse.buildCommonErrorResponse("登录失效，请重新登录");
        //}

        ShopInfo shopInfo  = shopInfoService.getById(vo.getId());

        if (Objects.nonNull(shopInfo)){

            shopInfo.setShopName(vo.getShopName());
            shopInfo.setContactPerson(vo.getPerson());
            shopInfo.setDepartment(vo.getDepartment());
            shopInfo.setFare(vo.getFare());
            shopInfo.setLogo(vo.getLogoUrl());
            shopInfo.setGmtUpdate(LocalDateTime.now());
            shopInfo.setShopUnion(vo.getUnion());
            shopInfo.setPhone(vo.getContact());

            shopInfoService.saveOrUpdate(shopInfo);
        }else {
           throw new BaseException(ResultCode.DATA_ERROR);
        }

        ReceivePaymentInfo alipay = receivePaymentInfoService.findOneByShopIdAndType(shopInfo.getId(), ReceivePaymentType.Alipay);
        ReceivePaymentInfo wechat = receivePaymentInfoService.findOneByShopIdAndType(shopInfo.getId(), ReceivePaymentType.Wechat);

        AliPayReceiveVo aliPayVo = vo.getAliPayVo();

        if (Objects.nonNull(aliPayVo)){
            if (Objects.nonNull(alipay)){

                alipay.setAppid(aliPayVo.getAppId());
                alipay.setPrivateKey(aliPayVo.getPrivateKey());
                alipay.setPublicKey(alipay.getPublicKey());
                alipay.setSigntype(aliPayVo.getSigntype());
                receivePaymentInfoService.insertOrUpdate(alipay);
            }else {
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
        }

        WeChatReceiveVo wxPayVo = vo.getWxPayVo();
        if (Objects.nonNull(wxPayVo)){
            if (Objects.nonNull(wechat)){

                wechat.setAppid(wxPayVo.getAppId());
                wechat.setKeyPath(wxPayVo.getKeyPath());
                wechat.setMchid(wxPayVo.getMchId());
                wechat.setPrivateKey(wxPayVo.getPrivateKey());

                receivePaymentInfoService.insertOrUpdate(wechat);
            }else {

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
        }

        return CommonResult.buildSuccessResponse();
    }


}

