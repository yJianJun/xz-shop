package com.cdzg.xzshop.controller.app;

import com.cdzg.xzshop.common.BaseException;
import com.cdzg.xzshop.common.CommonResult;
import com.cdzg.xzshop.common.ResultCode;
import com.cdzg.xzshop.config.annotations.api.IgnoreAuth;
import com.cdzg.xzshop.config.annotations.api.MobileApi;
import com.cdzg.xzshop.enums.PaymentType;
import com.cdzg.xzshop.domain.GoodsSpu;
import com.cdzg.xzshop.domain.ShopInfo;
import com.cdzg.xzshop.domain.UserGoodsFavorites;
import com.cdzg.xzshop.filter.auth.LoginSessionUtils;
import com.cdzg.xzshop.service.GoodsSpuService;
import com.cdzg.xzshop.service.ShopInfoService;
import com.cdzg.xzshop.service.UserGoodsFavoritesService;
import com.cdzg.xzshop.to.app.GoodsSpuDescriptionTo;
import com.cdzg.xzshop.to.app.GoodsSpuHomePageTo;
import com.cdzg.xzshop.vo.app.homepage.GoodsSpuHomePageVo;
import com.cdzg.xzshop.vo.app.homepage.GoodsSpuShopPageVo;
import com.cdzg.xzshop.vo.common.BasePageRequest;
import com.cdzg.xzshop.vo.common.PageResultVO;
import com.framework.utils.core.api.ApiResponse;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController("app_goodsSpuController")
@Slf4j
@RequestMapping("app/goods")
@Validated
@Api(tags = "app_商品相关")
public class GoodsSpuController {

    @Autowired
    GoodsSpuService goodsSpuService;

    @Autowired
    ShopInfoService shopInfoService;

    @Autowired
    UserGoodsFavoritesService favoritesService;

    @MobileApi
    @PostMapping("/homePage")
    @IgnoreAuth
    @ApiOperation("商城首页商品列表")
    public ApiResponse<PageResultVO<GoodsSpu>> homePage(@ApiParam(value = "商品分页参数模型", required = true) @RequestBody @Valid GoodsSpuHomePageVo vo) {

        PaymentType paymentType = vo.getPaymentType();
        Boolean sort = vo.getSort();
        Boolean type = vo.getType();
        PageResultVO<GoodsSpu> resultVO = goodsSpuService.homePage(vo.getCurrentPage(), vo.getPageSize(), paymentType, sort,type);
        return CommonResult.buildSuccessResponse(resultVO);
    }

    @MobileApi
    @GetMapping("/detail/{spu}")
    @ApiOperation("商品详情")
    public ApiResponse<GoodsSpuDescriptionTo> detail(@ApiParam(value = "商品编号", required = true) @PathVariable("spu") @Valid @NotNull Long spu) {

        String customerId = LoginSessionUtils.getAppUser().getCustomerId();
        GoodsSpu spuNo = goodsSpuService.findOneBySpuNoAndIsDeleteFalse(spu);
        GoodsSpuHomePageTo to = goodsSpuService.spuWithSalesIsCollect(spuNo,customerId);

        ShopInfo shopInfo = shopInfoService.getById(spuNo.getShopId());

        GoodsSpuDescriptionTo description = new GoodsSpuDescriptionTo();
        description.setSpu(to);
        description.setShopInfo(shopInfo);

        return CommonResult.buildSuccessResponse(description);
    }

    @MobileApi
    @ApiOperation(value = "收藏/取消收藏")
    @GetMapping("/collect/{spu}")
    public ApiResponse<Boolean> collect(@ApiParam(value = "商品编号", required = true) @PathVariable("spu") @Valid @NotNull Long spu) {

        GoodsSpu goodsSpu = goodsSpuService.findOneBySpuNoAndIsDeleteFalse(spu);

        if (Objects.isNull(goodsSpu)) {
            throw new BaseException(ResultCode.VALIDATE_FAILED,"系统不存在此商品！");
        }

        String customerId = LoginSessionUtils.getAppUser().getCustomerId();

        UserGoodsFavorites goodsFavorites = favoritesService.findOneByUserIdAndSpuNo(customerId, spu);

        if (Objects.isNull(goodsFavorites)) {

            UserGoodsFavorites userGoodsFavorites = UserGoodsFavorites.builder()
                    .spuNo(spu)
                    .userId(customerId)
                    .gmtCreate(LocalDateTime.now())
                    .gmtUpdate(LocalDateTime.parse("1000-01-01T00:00:00"))
                    .build();
            favoritesService.save(userGoodsFavorites);
            return CommonResult.buildSuccessResponse(true);
        } else {
            favoritesService.removeById(goodsFavorites.getId());
            return CommonResult.buildSuccessResponse(false);
        }
    }

    @MobileApi
    @ApiOperation(value = "用户收藏商品列表")
    @PostMapping("/collect/list")
    public ApiResponse<PageResultVO<GoodsSpu>> listCollection(@ApiParam(value = "列表分页模型", required = true) @RequestBody @Valid BasePageRequest vo) {

        String customerId = LoginSessionUtils.getAppUser().getCustomerId();
        List<Long> spuNos = favoritesService.findByUserId(customerId).stream().map(UserGoodsFavorites::getSpuNo).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(spuNos)){
            return CommonResult.buildSuccessResponse(new PageResultVO<GoodsSpu>());
        }
        PageResultVO<GoodsSpu> page = goodsSpuService.findBySpuNoInwithPage(vo.getCurrentPage(), vo.getPageSize(), spuNos);
        return CommonResult.buildSuccessResponse(page);
    }

    @MobileApi
    @PostMapping("/shop/list")
    @ApiOperation("店铺商品列表")
    public ApiResponse<PageResultVO<GoodsSpu>> listByShop(@ApiParam(value = "店铺商品列表分页模型", required = true) @RequestBody @Valid GoodsSpuShopPageVo vo) {

        PageResultVO<GoodsSpu> resultVO = goodsSpuService.pageByShop(vo.getCurrentPage(), vo.getPageSize(), vo.getShopId());
        return CommonResult.buildSuccessResponse(resultVO);
    }
}
