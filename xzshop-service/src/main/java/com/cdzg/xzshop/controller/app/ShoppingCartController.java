package com.cdzg.xzshop.controller.app;

import java.util.Date;
import java.util.Optional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.cdzg.customer.vo.response.CustomerBaseInfoVo;
import com.cdzg.xzshop.config.annotations.api.MobileApi;
import com.cdzg.xzshop.domain.GoodsSpu;
import com.cdzg.xzshop.domain.ShoppingCart;
import com.cdzg.xzshop.filter.auth.LoginSessionUtils;
import com.cdzg.xzshop.service.GoodsSpuService;
import com.cdzg.xzshop.service.ShoppingCartService;
import com.cdzg.xzshop.vo.shoppingcart.request.AddShoppingCartReqVO;
import com.cdzg.xzshop.vo.shoppingcart.request.AppDeleteShoppingCartReqVO;
import com.cdzg.xzshop.vo.shoppingcart.response.AppShoppingCartListRespVO;
import com.framework.utils.core.api.ApiConst;
import com.framework.utils.core.api.ApiResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


/**
 * 购物车管理
 *
 * @author EvilPet
 * @email xinliqiang3@163.com
 * @date 2021-04-28 09:52:07
 */
@RestController
@Api(tags = "30_app_购物车管理")
@RequestMapping("app/shoppingcart")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private GoodsSpuService goodsSpuService;


    @MobileApi
    @PostMapping("/list")
    @ApiOperation("30001-我的购物车")
    public ApiResponse<AppShoppingCartListRespVO> list() {
        //TODO
        CustomerBaseInfoVo appUserInfo = getAppUserInfo();
        if (!Optional.ofNullable(appUserInfo).isPresent()) {
            return ApiResponse.buildCommonErrorResponse("登录过期，请重新登录");
        }

        return null;
    }

    @MobileApi
    @PostMapping("/addShoppingCart")
    @ApiOperation("30002-添加商品到购物车")
    public ApiResponse<String> addShoppingCart(@RequestBody @Valid AddShoppingCartReqVO request) {
        CustomerBaseInfoVo appUserInfo = getAppUserInfo();
        if (!Optional.ofNullable(appUserInfo).isPresent()) {
            return ApiResponse.buildCommonErrorResponse("登录过期，请重新登录");
        }
        //库存校验
        GoodsSpu goodsSpu = goodsSpuService.getById(request.getGoodsId());
        if (!Optional.ofNullable(goodsSpu).isPresent() || goodsSpu.getIsDelete() || goodsSpu.getStatus()) {
            return ApiResponse.buildCommonErrorResponse("商品不存在或已下架");
        }
        if (goodsSpu.getStock() <= request.getGoodsNumber()) {
            return ApiResponse.buildCommonErrorResponse("商品库存不足");
        }
        //查询该用户是否存在该商品的购物车
        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShoppingCart::getAppUserId, appUserInfo.getId()).eq(ShoppingCart::getGoodsId, request.getGoodsId()).eq(ShoppingCart::getDeleted, 0);
        ShoppingCart shoppingCart = shoppingCartService.getOne(wrapper);
        Date date = new Date();
        boolean flag;
        if (Optional.ofNullable(shoppingCart).isPresent()) {
            int goodsNumber = shoppingCart.getGoodsNumber() + request.getGoodsNumber();
            shoppingCart.setGoodsNumber(goodsNumber);
            shoppingCart.setUpdateBy(appUserInfo.getId() + "");
            shoppingCart.setUpdateTime(date);
            flag = shoppingCartService.updateById(shoppingCart);
        } else {
            //新增购物车
            shoppingCart = new ShoppingCart();
            shoppingCart.setAppUserId(appUserInfo.getId());
            shoppingCart.setCreateBy(appUserInfo.getId() + "");
            shoppingCart.setCreateTime(date);
            shoppingCart.setGoodsId(goodsSpu.getId());
            shoppingCart.setGoodsNumber(request.getGoodsNumber());
            shoppingCart.setShopId(goodsSpu.getShopId());
            flag = shoppingCartService.save(shoppingCart);
        }
        if (flag) {
            return ApiResponse.buildResponse(ApiConst.Code.CODE_SUCCESS, "添加商品成功");
        }
        return ApiResponse.buildCommonErrorResponse("添加失败");
    }

    @ApiOperation(value = "30003-删除购物车商品")
    @RequestMapping(value = "/deleteShoppingCart", method = RequestMethod.POST)
    public ApiResponse deleteShoppingCart(@RequestBody AppDeleteShoppingCartReqVO reqVO) {
        CustomerBaseInfoVo appUserInfo = getAppUserInfo();
        if (!Optional.ofNullable(appUserInfo).isPresent()) {
            return ApiResponse.buildCommonErrorResponse("登录过期，请重新登录");
        }
        LambdaUpdateWrapper<ShoppingCart> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(ShoppingCart::getAppUserId, appUserInfo.getId()).in(ShoppingCart::getId, reqVO.getShoppingCartIds());
        if (shoppingCartService.update(updateWrapper)) {
            return ApiResponse.buildResponse(ApiConst.Code.CODE_SUCCESS, "操作成功");
        }
        return ApiResponse.buildResponse(ApiConst.Code.CODE_SERVER_ERROR, "操作失败");
    }


    /**
     * 获取登录用户信息
     *
     * @return
     */
    private CustomerBaseInfoVo getAppUserInfo() {
        return LoginSessionUtils.getAppUser().getUserBaseInfo();
    }
}
