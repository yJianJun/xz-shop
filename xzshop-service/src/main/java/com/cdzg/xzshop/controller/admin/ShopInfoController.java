package com.cdzg.xzshop.controller.admin;

import com.cdzg.xzshop.common.CommonResult;
import com.cdzg.xzshop.config.annotations.api.IgnoreAuth;
import com.cdzg.xzshop.config.annotations.api.WebApi;
import com.cdzg.xzshop.domain.ReceivePaymentInfo;
import com.cdzg.xzshop.domain.ReturnGoodsInfo;
import com.cdzg.xzshop.domain.ShopInfo;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
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
    @IgnoreAuth
    @ApiOperation("店铺批量上下架")
    public ApiResponse<String> batchPutOnDown(@ApiParam(value = "店铺批量上下架参数", required = true)@RequestBody @Valid ShopSwitchStatusVO statusVO) {

        //UserLoginResponse adminUser = LoginSessionUtils.getAdminUser();
        //if (adminUser == null) {
        //    return ApiResponse.buildCommonErrorResponse("登录失效，请重新登录");
        //}
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
    public ApiResponse add(@ApiParam(value = "店铺添加参数模型", required = true)@RequestBody @Valid ShopInfoAddVo addVo) {

        //UserLoginResponse adminUser = LoginSessionUtils.getAdminUser();
        //if (adminUser == null) {
        //    return ApiResponse.buildCommonErrorResponse("登录失效，请重新登录");
        //}
        shopInfoService.add(addVo);
        return CommonResult.buildSuccessResponse();
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
        ReturnGoodsInfo returnGoodsInfo = returnGoodsInfoService.findOneByShopId(id);

        ShopInfoDetailTo detail = ShopInfoDetailTo.builder()
                .shopInfo(shopInfo)
                .returnGoodsInfo(returnGoodsInfo)
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
        shopInfoService.update(vo);
        return CommonResult.buildSuccessResponse();
    }


}

