package com.cdzg.xzshop.controller.app;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.cdzg.customer.vo.response.CustomerBaseInfoVo;
import com.cdzg.xzshop.config.annotations.api.MobileApi;
import com.cdzg.xzshop.domain.GoodsSpu;
import com.cdzg.xzshop.domain.ShopInfo;
import com.cdzg.xzshop.domain.ShoppingCart;
import com.cdzg.xzshop.filter.auth.LoginSessionUtils;
import com.cdzg.xzshop.service.GoodsSpuService;
import com.cdzg.xzshop.service.ShopInfoService;
import com.cdzg.xzshop.service.ShoppingCartService;
import com.cdzg.xzshop.vo.shoppingcart.request.AddShoppingCartReqVO;
import com.cdzg.xzshop.vo.shoppingcart.request.AppDeleteShoppingCartReqVO;
import com.cdzg.xzshop.vo.shoppingcart.request.AppUpdateShoppingCartGoodsNumReqVO;
import com.cdzg.xzshop.vo.shoppingcart.response.AppShoppingCartGoodsRespVO;
import com.cdzg.xzshop.vo.shoppingcart.response.AppShoppingCartListRespVO;
import com.framework.utils.core.api.ApiConst;
import com.framework.utils.core.api.ApiResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
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
@RequestMapping("app/shoppingCart")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private GoodsSpuService goodsSpuService;

    @Autowired
    private ShopInfoService shopInfoService;


    @MobileApi
    @GetMapping("/list")
    @ApiOperation("30001-我的购物车")
    public ApiResponse<List<AppShoppingCartListRespVO>> list() {
        CustomerBaseInfoVo appUserInfo = getAppUserInfo();
        if (!Optional.ofNullable(appUserInfo).isPresent()) {
            return ApiResponse.buildCommonErrorResponse("登录过期，请重新登录");
        }
        List<AppShoppingCartListRespVO> resultList = new ArrayList<>();
        //查询用户所有购物车
        LambdaQueryWrapper<ShoppingCart> shoppingCartWrapper = new LambdaQueryWrapper<>();
        shoppingCartWrapper.eq(ShoppingCart::getAppUserId, appUserInfo.getId()).eq(ShoppingCart::getDeleted, 0).orderByDesc(ShoppingCart::getCreateTime);
        List<ShoppingCart> shoppingCartList = shoppingCartService.list(shoppingCartWrapper);
        if (CollectionUtils.isEmpty(shoppingCartList)) {
            return ApiResponse.buildResponse(ApiConst.Code.CODE_SUCCESS.code(), resultList, "购物车暂无商品");
        }
        List<Long> goodsIdList = shoppingCartList.stream().map(ShoppingCart::getGoodsId).collect(Collectors.toList());
        List<Long> shopIdList = shoppingCartList.stream().map(ShoppingCart::getShopId).distinct().collect(Collectors.toList());
        //商品信息
        LambdaQueryWrapper<GoodsSpu> goodsWrapper = new LambdaQueryWrapper<>();
        goodsWrapper.in(GoodsSpu::getId, goodsIdList);
        List<GoodsSpu> goodsSpuList = goodsSpuService.list(goodsWrapper);
        //店铺信息
        LambdaQueryWrapper<ShopInfo> shopInfoWrapper = new LambdaQueryWrapper<>();
        shopInfoWrapper.in(ShopInfo::getId, shopIdList);
        List<ShopInfo> shopInfoList = shopInfoService.list(shopInfoWrapper);
        if (CollectionUtils.isEmpty(goodsSpuList) || CollectionUtils.isEmpty(shopInfoList)) {
            return ApiResponse.buildResponse(ApiConst.Code.CODE_SUCCESS.code(), resultList, "购物车暂无商品");
        }
        //封装返回值
        shopInfoList.forEach(s -> {
            AppShoppingCartListRespVO appShoppingCartListRespVO = new AppShoppingCartListRespVO();
            appShoppingCartListRespVO.setShopId(s.getId() + "");
            appShoppingCartListRespVO.setShopName(s.getShopName());
            resultList.add(appShoppingCartListRespVO);
        });
        resultList.forEach(r -> {
            List<AppShoppingCartGoodsRespVO> shoppingCartGoodsList = new ArrayList<>();
            List<ShoppingCart> shopAndGoodsIdList = shoppingCartList.stream().filter(s -> (s.getShopId() + "").equals(r.getShopId())).collect(Collectors.toList());
            shopAndGoodsIdList.forEach(s -> goodsSpuList.forEach(g -> {
                if (s.getGoodsId().equals(g.getId())) {
                    AppShoppingCartGoodsRespVO shoppingCartGoods = new AppShoppingCartGoodsRespVO();
                    BeanUtils.copyProperties(g, shoppingCartGoods);
                    BeanUtils.copyProperties(s, shoppingCartGoods);
                    shoppingCartGoods.setGoodsId(s.getGoodsId() + "");
                    shoppingCartGoods.setGoodsImg(CollectionUtils.isEmpty(g.getShowImgs()) ? null : g.getShowImgs().get(0));
                    //默认正常 商品当前状态 0-正常 1-库存不足 2-已下架或已删除
                    shoppingCartGoods.setGoodsStatus(0);
                    if (s.getGoodsNumber() > g.getStock()) {
                        shoppingCartGoods.setGoodsStatus(1);
                    }
                    if (!g.getStatus() || g.getIsDelete()) {
                        shoppingCartGoods.setGoodsStatus(2);
                    }
                    shoppingCartGoodsList.add(shoppingCartGoods);
                }
            }));
            r.setGoodsList(shoppingCartGoodsList);
        });
        //处理空数据
        for (int i = resultList.size(); i > 0; i--) {
            if (resultList.get(i - 1).getGoodsList().size() == 0) {
                resultList.remove(i - 1);
            }
        }
        if (resultList.size() == 0) {
            return ApiResponse.buildResponse(ApiConst.Code.CODE_SUCCESS.code(), resultList, "购物车暂无数据");
        }
        return ApiResponse.buildResponse(ApiConst.Code.CODE_SUCCESS.code(), resultList, "成功");
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
        if (!Optional.ofNullable(goodsSpu).isPresent() || goodsSpu.getIsDelete() || !goodsSpu.getStatus()) {
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

    @MobileApi
    @ApiOperation(value = "30003-删除购物车商品")
    @PostMapping(value = "/deleteShoppingCart")
    public ApiResponse<String> deleteShoppingCart(@RequestBody AppDeleteShoppingCartReqVO reqVO) {
        CustomerBaseInfoVo appUserInfo = getAppUserInfo();
        if (!Optional.ofNullable(appUserInfo).isPresent()) {
            return ApiResponse.buildCommonErrorResponse("登录过期，请重新登录");
        }
        LambdaUpdateWrapper<ShoppingCart> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(ShoppingCart::getDeleted, 1).set(ShoppingCart::getUpdateBy, appUserInfo.getId() + "").set(ShoppingCart::getUpdateTime, new Date()).eq(ShoppingCart::getAppUserId, appUserInfo.getId()).in(ShoppingCart::getId, reqVO.getShoppingCartIds());
        if (shoppingCartService.update(updateWrapper)) {
            return ApiResponse.buildResponse(ApiConst.Code.CODE_SUCCESS, "操作成功");
        }
        return ApiResponse.buildResponse(ApiConst.Code.CODE_SERVER_ERROR, "操作失败");
    }

    @MobileApi
    @ApiOperation(value = "30004-修改购物车商品数量")
    @PostMapping(value = "/updateGoodsNum")
    public ApiResponse<String> updateGoodsNum(@RequestBody @Valid AppUpdateShoppingCartGoodsNumReqVO reqVO) {
        GoodsSpu goodsSpu = goodsSpuService.getById(reqVO.getGoodsId());
        if (goodsSpu.getIsDelete() || !goodsSpu.getStatus()) {
            return ApiResponse.buildCommonErrorResponse("该商品已经下架了");
        }
        if (reqVO.getUpdateNum() > goodsSpu.getStock()) {
            return ApiResponse.buildCommonErrorResponse("商品库存不足");
        }
        //修改购物车
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setId(Long.valueOf(reqVO.getShoppingCartId()));
        shoppingCart.setUpdateBy(getAppUserInfo().getId() + "");
        shoppingCart.setUpdateTime(new Date());
        shoppingCart.setGoodsNumber(reqVO.getUpdateNum());
        boolean b = shoppingCartService.updateById(shoppingCart);
        if (b) {
            return ApiResponse.buildSuccessResponse("修改成功");
        }
        return ApiResponse.buildCommonErrorResponse("修改失败");
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
