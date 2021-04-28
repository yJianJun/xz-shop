package com.cdzg.xzshop.controller.admin;

import com.cdzg.xzshop.common.CommonResult;
import com.cdzg.xzshop.config.annotations.api.IgnoreAuth;
import com.cdzg.xzshop.config.annotations.api.WebApi;
import com.cdzg.xzshop.constant.ReceivePaymentType;
import com.cdzg.xzshop.domain.GoodsCategory;
import com.cdzg.xzshop.domain.ReceivePaymentInfo;
import com.cdzg.xzshop.domain.ShopInfo;
import com.cdzg.xzshop.service.GoodsCategoryService;
import com.cdzg.xzshop.vo.admin.*;
import com.cdzg.xzshop.vo.common.PageResultVO;
import com.framework.utils.core.api.ApiResponse;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Objects;

@RestController
@RequestMapping("admin/goods/category")
@Validated
@Api(tags = "admin_商品分类管理")
public class GoodsCategoryController {


    @Autowired
    GoodsCategoryService goodsCategoryService;


    @WebApi
    @PostMapping("/add")
    @IgnoreAuth
    @ApiOperation("新建商品分类")
    public ApiResponse add(@ApiParam(value = "商品分类添加参数模型", required = true)@RequestBody @Valid GoodsCategoryAddVo addVo) {

        //UserLoginResponse adminUser = LoginSessionUtils.getAdminUser();
        //if (adminUser == null) {
        //    return ApiResponse.buildCommonErrorResponse("登录失效，请重新登录");
        //}

        GoodsCategory category = GoodsCategory.builder()
                .categoryName(addVo.getName())
                .createUser("")  //yjjtodo 后面需修改 目前测试
                .gmtCreate(LocalDateTime.now())
                .gmtUpdate(LocalDateTime.now())
                .level(addVo.getLevel())
                .parentId(addVo.getParentId())
                .status(addVo.getStatus())
                .build();

        goodsCategoryService.insert(category);
        return CommonResult.buildSuccessResponse();
    }


    @WebApi
    @PostMapping("/page")
    @IgnoreAuth
    @ApiOperation("分页查询商品类别列表")
    public ApiResponse<PageResultVO<GoodsCategory>> page(@ApiParam(value = "商品类别分页参数模型", required = true)@RequestBody @Valid GoodsCategoryPageVo vo) {
        //UserLoginResponse adminUser = LoginSessionUtils.getAdminUser();
        //if (adminUser == null) {
        //    return ApiResponse.buildCommonErrorResponse("登录失效，请重新登录");
        //}
        PageResultVO<GoodsCategory> resultVO = goodsCategoryService.page(vo.getCurrentPage(), vo.getPageSize(), vo.getLevel(), vo.getName());
        return CommonResult.buildSuccessResponse(resultVO);
    }



}
