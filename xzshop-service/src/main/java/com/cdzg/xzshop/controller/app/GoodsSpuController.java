package com.cdzg.xzshop.controller.app;

import com.cdzg.universal.vo.response.user.UserLoginResponse;
import com.cdzg.xzshop.common.BaseException;
import com.cdzg.xzshop.common.CommonResult;
import com.cdzg.xzshop.common.ResultCode;
import com.cdzg.xzshop.config.annotations.api.IgnoreAuth;
import com.cdzg.xzshop.config.annotations.api.MobileApi;
import com.cdzg.xzshop.config.annotations.api.WebApi;
import com.cdzg.xzshop.constant.PaymentType;
import com.cdzg.xzshop.domain.GoodsSpu;
import com.cdzg.xzshop.domain.ShopInfo;
import com.cdzg.xzshop.filter.auth.LoginSessionUtils;
import com.cdzg.xzshop.service.GoodsSpuService;
import com.cdzg.xzshop.service.ShopInfoService;
import com.cdzg.xzshop.to.admin.GoodsSpuTo;
import com.cdzg.xzshop.to.app.GoodsSpuDescriptionTo;
import com.cdzg.xzshop.to.app.GoodsSpuHomePageTo;
import com.cdzg.xzshop.vo.admin.GoodsSpuAddVo;
import com.cdzg.xzshop.vo.admin.GoodsSpuPageVo;
import com.cdzg.xzshop.vo.admin.GoodsSpuSwitchStatusVO;
import com.cdzg.xzshop.vo.admin.GoodsSpuUpdateVO;
import com.cdzg.xzshop.vo.app.GoodsSpuSearchPageVo;
import com.cdzg.xzshop.vo.app.homepage.GoodsSpuHomePageVo;
import com.cdzg.xzshop.vo.common.PageResultVO;
import com.framework.utils.core.api.ApiResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;

@RestController("app_goodsSpuController")
@RequestMapping("app/goods")
@Validated
@Api(tags = "app_商品相关")
public class GoodsSpuController {

    @Autowired
    GoodsSpuService goodsSpuService;

    @Autowired
    ShopInfoService shopInfoService;


    @MobileApi
    @PostMapping("/homePage")
    @IgnoreAuth
    @ApiOperation("商城首页商品列表")
    public ApiResponse<PageResultVO<GoodsSpuHomePageTo>> homePage(@ApiParam(value = "商品分页参数模型", required = true) @RequestBody @Valid GoodsSpuHomePageVo vo) {

        PaymentType paymentType = vo.getPaymentType();
        Boolean sort = vo.getSort();
        PageResultVO<GoodsSpuHomePageTo> resultVO = goodsSpuService.homePage(vo.getCurrentPage(), vo.getPageSize(), paymentType, sort);
        return CommonResult.buildSuccessResponse(resultVO);
    }

    @MobileApi
    @GetMapping("/detail/{spu}")
    @IgnoreAuth
    @ApiOperation("商品详情")
    public ApiResponse<GoodsSpuDescriptionTo> detail(@ApiParam(value = "商品编号", required = true) @PathVariable("spu") @Valid @NotNull Long spu) {

        GoodsSpu spuNo = goodsSpuService.findOneBySpuNoAndIsDeleteFalse(spu);
        GoodsSpuHomePageTo to = goodsSpuService.spuWithSales(spuNo);

        ShopInfo shopInfo = shopInfoService.getById(spuNo.getShopId());

        GoodsSpuDescriptionTo description = new GoodsSpuDescriptionTo();
        description.setSpu(to);
        description.setShopInfo(shopInfo);

        return CommonResult.buildSuccessResponse(description);
    }

    @MobileApi
    @PostMapping("/search")
    @IgnoreAuth
    @ApiOperation("商城首页商品列表")
    public ApiResponse<PageResultVO<GoodsSpu>> search(@ApiParam(value = "商品搜索列表分页模型", required = true) @RequestBody @Valid GoodsSpuSearchPageVo vo) {

       return CommonResult.buildSuccessResponse(goodsSpuService.search(vo));
    }


}
