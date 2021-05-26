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
import com.cdzg.xzshop.domain.UserGoodsFavorites;
import com.cdzg.xzshop.filter.auth.LoginSessionUtils;
import com.cdzg.xzshop.service.GoodsSpuService;
import com.cdzg.xzshop.service.ShopInfoService;
import com.cdzg.xzshop.service.UserGoodsFavoritesService;
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
import io.swagger.annotations.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
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

        String customerId = null;
        try {
            customerId = LoginSessionUtils.getAppUser().getCustomerId();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
}
