package com.cdzg.xzshop.controller.admin;

import com.cdzg.universal.vo.response.user.UserLoginResponse;
import com.cdzg.xzshop.common.BaseException;
import com.cdzg.xzshop.common.CommonResult;
import com.cdzg.xzshop.common.ResultCode;
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
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("admin/shop")
@Validated
@Api(tags = "admin_店铺管理")
public class ShopInfoController {


    @Autowired
    ShopInfoService shopInfoService;

    @WebApi
    @PostMapping("/batch/switch")
    @ApiOperation("店铺批量上下架")
    public ApiResponse<String> batchPutOnDown(@ApiParam(value = "店铺批量上下架参数", required = true) @RequestBody @Valid ShopSwitchStatusVO statusVO) {

        if(!LoginSessionUtils.isAdmin()){
            throw new BaseException(ResultCode.FORBIDDEN);
        }
        List<Long> list = statusVO.getList();
        Boolean flag = statusVO.getFlag();

        shopInfoService.batchPutOnDown(list, flag);
        return ApiResponse.buildSuccessResponse("编辑成功");
    }

    @WebApi
    @PostMapping("/page")
    @ApiOperation("分页查询店铺列表")
    public ApiResponse<PageResultVO<ShopInfo>> page(@ApiParam(value = "店铺分页参数模型", required = true) @RequestBody @Valid ShopPageVo vo) {

        if(!LoginSessionUtils.isAdmin()){
            throw new BaseException(ResultCode.FORBIDDEN);
        }
        PageResultVO<ShopInfo> pageInfo = shopInfoService.page(vo.getCurrentPage(), vo.getPageSize(), vo.getShopName(), vo.getStatus(), vo.getStart(), vo.getEnd());
        return ApiResponse.buildSuccessResponse(pageInfo);
    }

    @WebApi
    @PostMapping("/add")
    @ApiOperation("新建店铺-运营端")
    public ApiResponse add(@ApiParam(value = "店铺添加参数模型", required = true) @RequestBody @Valid ShopInfoAddVo addVo) {

        if(!LoginSessionUtils.isAdmin()){
            throw new BaseException(ResultCode.FORBIDDEN);
        }
        UserLoginResponse adminUser = LoginSessionUtils.getAdminUser();
        shopInfoService.add(addVo, adminUser.getUserBaseInfo().getUserName());
        return CommonResult.buildSuccessResponse();
    }

    @WebApi
    @GetMapping("/get")
    @ApiOperation("店铺详情-运营端")
    public ApiResponse<ShopInfoUpdateVO> get(@Valid @RequestParam("id") @NotNull @ApiParam(value = "店铺id", required = true) Long id) {

        return CommonResult.buildSuccessResponse(shopInfoService.get(id));
    }

    @WebApi
    @GetMapping("/get/seller")
    @ApiOperation("店铺详情-商家端")
    public ApiResponse<ShopInfoUpdateVO> get() {

        UserLoginResponse user = LoginSessionUtils.getAdminUser();
        return CommonResult.buildSuccessResponse(shopInfoService.get(user.getUserBaseInfo().getOrganizationId()));
    }

    @WebApi
    @PostMapping("/update")
    @ApiOperation("编辑店铺-运营端/商家端通用")
    public ApiResponse update(@ApiParam(value = "店铺编辑参数模型", required = true) @RequestBody @Valid ShopInfoUpdateVO vo) {

        shopInfoService.update(vo);
        return CommonResult.buildSuccessResponse();
    }


}

