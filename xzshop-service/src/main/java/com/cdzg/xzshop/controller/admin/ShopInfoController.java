package com.cdzg.xzshop.controller.admin;

import com.cdzg.universal.vo.response.user.UserLoginResponse;
import com.cdzg.xzshop.common.CommonResult;
import com.cdzg.xzshop.config.annotations.api.IgnoreAuth;
import com.cdzg.xzshop.config.annotations.api.WebApi;
import com.cdzg.xzshop.constant.PaymentType;
import com.cdzg.xzshop.constant.ReceivePaymentType;
import com.cdzg.xzshop.domain.ReceivePaymentInfo;
import com.cdzg.xzshop.domain.ReturnGoodsInfo;
import com.cdzg.xzshop.domain.ShopInfo;
import com.cdzg.xzshop.filter.auth.LoginSessionUtils;
import com.cdzg.xzshop.service.ReceivePaymentInfoService;
import com.cdzg.xzshop.service.ReturnGoodsInfoService;
import com.cdzg.xzshop.service.ShopInfoService;
import com.cdzg.xzshop.to.admin.ShopInfoDetailTo;
import com.cdzg.xzshop.vo.admin.*;
import com.cdzg.xzshop.vo.common.PageResultVO;
import com.framework.utils.core.api.ApiResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("admin/shop")
@Validated
@Api(tags = "admin_店铺管理")
public class ShopInfoController {


    @Autowired
    ShopInfoService shopInfoService;

    @Autowired
    ReceivePaymentInfoService receivePaymentInfoService;

    @Autowired
    ReturnGoodsInfoService returnGoodsInfoService;

    @WebApi
    @PostMapping("/batch/switch")
    @ApiOperation("店铺批量上下架")
    public ApiResponse<String> batchPutOnDown(@ApiParam(value = "店铺批量上下架参数", required = true)@RequestBody @Valid ShopSwitchStatusVO statusVO) {

        List<Long> list = statusVO.getList();
        Boolean flag = statusVO.getFlag();

        if (CollectionUtils.isNotEmpty(list)){

            shopInfoService.batchPutOnDown(list,flag);
            return ApiResponse.buildSuccessResponse("编辑成功");
        }
        return ApiResponse.buildCommonErrorResponse("编辑失败");
    }

    @WebApi
    @PostMapping("/page")
    @ApiOperation("分页查询店铺列表")
    public ApiResponse<PageResultVO<ShopInfo>> page(@ApiParam(value = "店铺分页参数模型", required = true)@RequestBody @Valid ShopPageVo vo) {

        PageResultVO<ShopInfo> pageInfo = shopInfoService.page(vo.getCurrentPage(), vo.getPageSize(), vo.getShopName(), vo.getStatus(), vo.getStart(), vo.getEnd());
        return ApiResponse.buildSuccessResponse(pageInfo);
    }

    @WebApi
    @PostMapping("/add")
    @ApiOperation("新建店铺-运营端")
    public ApiResponse add(@ApiParam(value = "店铺添加参数模型", required = true)@RequestBody @Valid ShopInfoAddVo addVo) {

        UserLoginResponse adminUser = LoginSessionUtils.getAdminUser();
        shopInfoService.add(addVo,adminUser.getUserBaseInfo().getUserName());
        return CommonResult.buildSuccessResponse();
    }

    @WebApi
    @GetMapping("/get")
    @ApiOperation("店铺详情-运营端")
    public ApiResponse<ShopInfoUpdateVO> get(@Valid @RequestParam("id") @NotNull @ApiParam(value = "店铺id", required = true) Long id ) {

        ShopInfo shopInfo = shopInfoService.getById(id);
        List<ReceivePaymentInfo> paymentInfos = receivePaymentInfoService.findAllByShopId(id);
        ReturnGoodsInfo returnGoodsInfo = returnGoodsInfoService.findOneByShopId(id);

        ShopInfoUpdateVO updateVO = ShopInfoUpdateVO.builder().build();
        BeanUtils.copyProperties(shopInfo,updateVO);
        updateVO.setUnion(shopInfo.getShopUnion());
        updateVO.setLogoUrl(shopInfo.getLogo());
        updateVO.setPerson(shopInfo.getContactPerson());
        updateVO.setContact(shopInfo.getPhone());
        updateVO.setReceiveMoney(setReceiveMoney(paymentInfos));
        setReceiveVo(paymentInfos,updateVO);
        ReturnGoodsInfoVo infoVo = new ReturnGoodsInfoVo();
        BeanUtils.copyProperties(returnGoodsInfo,infoVo);
        updateVO.setReturnfoVo(infoVo);

        return CommonResult.buildSuccessResponse(updateVO);
    }

    private void setReceiveVo(List<ReceivePaymentInfo> paymentInfos, ShopInfoUpdateVO updateVO) {

        for (ReceivePaymentInfo paymentInfo : paymentInfos) {

            if (paymentInfo.getType()==ReceivePaymentType.Alipay){

                AliPayReceiveVo aliPayReceiveVo = new AliPayReceiveVo();
                aliPayReceiveVo.setAppId(paymentInfo.getAppid());
                BeanUtils.copyProperties(paymentInfo,aliPayReceiveVo);
                updateVO.setAliPayVo(aliPayReceiveVo);
            }

            if (paymentInfo.getType()==ReceivePaymentType.Wechat){

                WeChatReceiveVo chatReceiveVo = new WeChatReceiveVo();
                chatReceiveVo.setAppId(paymentInfo.getAppid());
                chatReceiveVo.setMchId(paymentInfo.getMchid());
                BeanUtils.copyProperties(paymentInfo,chatReceiveVo);
                updateVO.setWxPayVo(chatReceiveVo);
            }
        }
    }

    private int setReceiveMoney(List<ReceivePaymentInfo> paymentInfos) {

        List<Integer> flag = new ArrayList<>();
        for (ReceivePaymentInfo paymentInfo : paymentInfos) {
            if (paymentInfo.getStatus()){

                if (ReceivePaymentType.Alipay == paymentInfo.getType()){
                    flag.add(1);
                }
                if (ReceivePaymentType.Wechat == paymentInfo.getType()){
                    flag.add(2);
                }
            }
        }

        if (flag.size() == 2){
            return 3;
        }else {
            return flag.get(0);
        }
    }

    @WebApi
    @PostMapping("/update")
    @ApiOperation("编辑店铺")
    public ApiResponse update(@ApiParam(value = "店铺编辑参数模型", required = true)@RequestBody @Valid ShopInfoUpdateVO vo ) {

        shopInfoService.update(vo);
        return CommonResult.buildSuccessResponse();
    }


}

