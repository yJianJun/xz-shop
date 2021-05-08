package com.cdzg.xzshop.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cdzg.universal.vo.response.user.UserLoginResponse;
import com.cdzg.xzshop.common.CommonResult;
import com.cdzg.xzshop.common.ResultCode;
import com.cdzg.xzshop.config.annotations.api.IgnoreAuth;
import com.cdzg.xzshop.config.annotations.api.WebApi;
import com.cdzg.xzshop.constant.ReceivePaymentType;
import com.cdzg.xzshop.domain.GoodsCategory;
import com.cdzg.xzshop.domain.ReceivePaymentInfo;
import com.cdzg.xzshop.domain.ShopInfo;
import com.cdzg.xzshop.filter.auth.LoginSessionUtils;
import com.cdzg.xzshop.service.GoodsCategoryService;
import com.cdzg.xzshop.to.admin.GoodsCategoryTo;
import com.cdzg.xzshop.to.admin.ShopInfoDetailTo;
import com.cdzg.xzshop.vo.admin.*;
import com.cdzg.xzshop.vo.common.PageResultVO;
import com.framework.utils.core.api.ApiResponse;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.*;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
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
    @ApiOperation("新建商品分类")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "Authorization token", required = false, dataType = "string", paramType = "header")
    })
    public ApiResponse add(@ApiParam(value = "商品分类添加参数模型", required = true) @RequestBody @Valid GoodsCategoryAddVo addVo) {

        UserLoginResponse adminUser = LoginSessionUtils.getAdminUser();
        goodsCategoryService.add(adminUser.getUserBaseInfo().getUserName(),addVo);
        return CommonResult.buildSuccessResponse();
    }

    @WebApi
    @PostMapping("/update")
    @ApiOperation("编辑商品分类")
    public ApiResponse update(@ApiParam(value = "商品分类编辑参数模型", required = true) @RequestBody @Valid GoodsCategoryUpdateVo updateVo) {

        GoodsCategory goodsCategory = goodsCategoryService.selectByPrimaryKey(updateVo.getId());
        if (Objects.nonNull(goodsCategory)){

            goodsCategory.setCategoryName(updateVo.getName());
            goodsCategory.setGmtUpdate(LocalDateTime.now());
            goodsCategory.setStatus(updateVo.getStatus());
            goodsCategory.setLevel(updateVo.getLevel());
            goodsCategory.setParentId(updateVo.getParentId());

            goodsCategoryService.insertOrUpdate(goodsCategory);
            return CommonResult.buildSuccessResponse();
        }
        return CommonResult.error(ResultCode.DATA_ERROR);
    }


    @WebApi
    @PostMapping("/list")
    @ApiOperation("查询商品类别列表")
    public ApiResponse<List<GoodsCategoryTo>> list(@ApiParam(value = "商品类别参数模型", required = true) @RequestBody @Valid GoodsCategoryPageVo vo) {

        List<GoodsCategoryTo> resultVO = goodsCategoryService.list(vo.getLevel(), vo.getName());
        return CommonResult.buildSuccessResponse(resultVO);
    }


    @WebApi
    @PostMapping("/sub/page")
    @ApiOperation("分页查询二级分类列表")
    public ApiResponse<PageResultVO<GoodsCategory>> getSub(@ApiParam(value = "商品二级分类分页参数模型", required = true) @RequestBody @Valid GoodsCategorySubPageVo vo) {

        PageResultVO<GoodsCategory> resultVO = goodsCategoryService.pageSub(vo.getCurrentPage(), vo.getPageSize(), vo.getId(), 2);
        return CommonResult.buildSuccessResponse(resultVO);
    }

    @WebApi
    @GetMapping("/delete")
    @ApiOperation("商品分类删除")
    public ApiResponse delete(@Valid @RequestParam("id") @NotNull @ApiParam(value = "商品分类id", required = true) Long id) {

        int delete = goodsCategoryService.deleteByPrimaryKey(id);
        return CommonResult.buildSuccessResponse();
    }

    @WebApi
    @GetMapping("/get")
    @ApiOperation("商品分类详情")
    public ApiResponse<GoodsCategory> get(@Valid @RequestParam("id") @NotNull @ApiParam(value = "商品分类id", required = true) Long id) {

        GoodsCategory goodsCategory = goodsCategoryService.selectByPrimaryKey(id);
        return CommonResult.buildSuccessResponse(goodsCategory);
    }


    @WebApi
    @PostMapping("/batch/switch")
    @ApiOperation("商品分类批量禁用启用")
    public ApiResponse batchPutOnDown(@ApiParam(value = "商品分类批量禁用启用参数", required = true) @RequestBody @Valid GoodsCategorySwitchStatusVo statusVO) {

        List<Long> list = statusVO.getList();
        Boolean flag = statusVO.getFlag();

        if (CollectionUtils.isNotEmpty(list)) {

            goodsCategoryService.batchPutOnDown(list, flag);
            return CommonResult.buildSuccessResponse();
        }
        return CommonResult.error(ResultCode.PARAMETER_ERROR);
    }
}
