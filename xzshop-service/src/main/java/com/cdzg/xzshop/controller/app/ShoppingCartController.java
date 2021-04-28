package com.cdzg.xzshop.controller.app;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

import com.cdzg.customer.vo.response.CustomerBaseInfoVo;
import com.cdzg.xzshop.config.annotations.api.MobileApi;
import com.cdzg.xzshop.filter.auth.LoginSessionUtils;
import com.cdzg.xzshop.service.ShoppingCartService;
import com.cdzg.xzshop.vo.shoppingcart.response.AppShoppingCartListRespVO;
import com.framework.utils.core.api.ApiResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



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


    /**
     * 获取登录用户信息
     * @return
     */
    private CustomerBaseInfoVo getAppUserInfo(){
        return LoginSessionUtils.getAppUser().getUserBaseInfo();
    }
}
