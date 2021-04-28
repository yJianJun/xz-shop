package com.cdzg.xzshop.controller.app;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.cdzg.customer.vo.response.CustomerBaseInfoVo;
import com.cdzg.xzshop.config.annotations.api.MobileApi;
import com.cdzg.xzshop.domain.ShoppingCart;
import com.cdzg.xzshop.filter.auth.LoginSessionUtils;
import com.cdzg.xzshop.service.ShoppingCartService;
import com.cdzg.xzshop.vo.shoppingcart.request.AppDeleteShoppingCartReqVO;
import com.cdzg.xzshop.vo.shoppingcart.response.AppShoppingCartListRespVO;
import com.framework.utils.core.api.ApiConst;
import com.framework.utils.core.api.ApiResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.catalina.Wrapper;
import org.apache.catalina.webresources.WarResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


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
    public ApiResponse addShoppingCart() {
        //TODO
        CustomerBaseInfoVo appUserInfo = getAppUserInfo();
        if (!Optional.ofNullable(appUserInfo).isPresent()) {
            return ApiResponse.buildCommonErrorResponse("登录过期，请重新登录");
        }
        return ApiResponse.buildResponse(ApiConst.Code.CODE_SUCCESS, "添加商品成功");
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
