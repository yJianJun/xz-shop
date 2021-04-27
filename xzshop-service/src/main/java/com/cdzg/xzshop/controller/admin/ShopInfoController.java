package com.cdzg.xzshop.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cdzg.universal.vo.response.user.UserLoginResponse;
import com.cdzg.xzshop.config.annotations.api.IgnoreAuth;
import com.cdzg.xzshop.config.annotations.api.WebApi;
import com.cdzg.xzshop.domain.ShopInfo;
import com.cdzg.xzshop.filter.auth.LoginSessionUtils;
import com.cdzg.xzshop.service.ShopInfoService;
import com.cdzg.xzshop.vo.admin.ShopPageVo;
import com.cdzg.xzshop.vo.admin.ShopSwitchStatusVO;
import com.framework.utils.core.api.ApiResponse;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
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
    @IgnoreAuth
    @ApiOperation("店铺批量上下架")
    public ApiResponse<String> batchPutOnDown(@ApiParam(value = "店铺批量上下架参数", required = true)@RequestBody @Valid ShopSwitchStatusVO statusVO) {

        //UserLoginResponse adminUser = LoginSessionUtils.getAdminUser();
        //if (adminUser == null) {
        //    return ApiResponse.buildCommonErrorResponse("登录失效，请重新登录");
        //}
        List<Long> list = statusVO.getList();
        if (CollectionUtils.isNotEmpty(list)){

            QueryWrapper<ShopInfo> queryWrapper = new QueryWrapper<>();
            for (int i = 0; i < list.size(); i++) {

                Long id = list.get(i);
                ShopInfo shopInfo = shopInfoService.getBaseMapper().selectOne(queryWrapper.eq("id",id));
                if (Objects.nonNull(shopInfo)){
                    shopInfo.setStatus(statusVO.getFlag());
                    shopInfoService.updateById(shopInfo);
                }
            }
            return ApiResponse.buildSuccessResponse("编辑成功");
        }
        return ApiResponse.buildCommonErrorResponse("编辑失败");
    }

    @WebApi
    @PostMapping("/page")
    @IgnoreAuth
    @ApiOperation("分页查询店铺列表")
    public ApiResponse<PageInfo<ShopInfo>> page(@ApiParam(value = "店铺分页参数模型", required = true)@RequestBody @Valid ShopPageVo vo) {
        //UserLoginResponse adminUser = LoginSessionUtils.getAdminUser();
        //if (adminUser == null) {
        //    return ApiResponse.buildCommonErrorResponse("登录失效，请重新登录");
        //}
        PageInfo<ShopInfo> pageInfo = shopInfoService.findAllByShopNameLikeAndStatusAndGmtPutOnTheShelfBetweenEqualwithPage(vo.getPageNum(), vo.getPageSize(), vo.getShopName(), vo.getStatus(), vo.getStart(), vo.getEnd());
        return ApiResponse.buildSuccessResponse(pageInfo);
    }


}

