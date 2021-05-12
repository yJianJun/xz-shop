package com.cdzg.xzshop.controller.admin;

import com.cdzg.universal.vo.response.user.UserLoginResponse;
import com.cdzg.xzshop.common.BaseException;
import com.cdzg.xzshop.common.CommonResult;
import com.cdzg.xzshop.common.ResultCode;
import com.cdzg.xzshop.config.annotations.api.WebApi;
import com.cdzg.xzshop.domain.GoodsSpu;
import com.cdzg.xzshop.domain.ReceivePaymentInfo;
import com.cdzg.xzshop.domain.ReturnGoodsInfo;
import com.cdzg.xzshop.domain.ShopInfo;
import com.cdzg.xzshop.filter.auth.LoginSessionUtils;
import com.cdzg.xzshop.service.GoodsSpuService;
import com.cdzg.xzshop.to.admin.GoodsSpuTo;
import com.cdzg.xzshop.vo.admin.*;
import com.cdzg.xzshop.vo.common.PageResultVO;
import com.framework.utils.core.api.ApiResponse;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("admin/goods")
@Validated
@Api(tags = "admin_商品管理")
public class GoodsSpuController {

    @Autowired
    GoodsSpuService goodsSpuService;

    @WebApi
    @PostMapping("/add")
    @ApiOperation("新建商品-运营端")
    public ApiResponse add(@ApiParam(value = "商品添加参数模型", required = true) @RequestBody @Valid GoodsSpuAddVo addVo) {

        UserLoginResponse adminUser = LoginSessionUtils.getAdminUser();
        goodsSpuService.add(addVo, adminUser.getUserBaseInfo());
        return CommonResult.buildSuccessResponse();
    }

    @WebApi
    @PostMapping("/batch/switch")
    @ApiOperation("商品批量上下架")
    public ApiResponse batchPutOnDown(@ApiParam(value = "商品批量上下架参数", required = true) @RequestBody @Valid GoodsSpuSwitchStatusVO statusVO) {

        List<Long> list = statusVO.getList();
        Boolean flag = statusVO.getFlag();
        goodsSpuService.batchPutOnDown(list, flag);
        return CommonResult.buildSuccessResponse();
    }

    @WebApi
    @GetMapping("/get")
    @ApiOperation("商品详情-运营端")
    public ApiResponse<GoodsSpuUpdateVO> get(@Valid @RequestParam("spuNo") @NotNull @ApiParam(value = "商品编号", required = true) Long spuNo ) {

        GoodsSpu goodsSpu = goodsSpuService.findOneBySpuNo(spuNo);

        if (Objects.nonNull(goodsSpu)){
            GoodsSpuUpdateVO updateVO = new GoodsSpuUpdateVO();
            BeanUtils.copyProperties(goodsSpu,updateVO);
            return CommonResult.buildSuccessResponse(updateVO);
        }
        throw new BaseException(ResultCode.DATA_ERROR);
    }

    @WebApi
    @PostMapping("/update")
    @ApiOperation("编辑商品")
    public ApiResponse update(@ApiParam(value = "商品编辑参数模型", required = true)@RequestBody @Valid GoodsSpuUpdateVO vo ) {

        goodsSpuService.update(vo);
        return CommonResult.buildSuccessResponse();
    }

    @WebApi
    @PostMapping("/page")
    @ApiOperation("分页查询商品列表")
    public ApiResponse<PageResultVO<GoodsSpuTo>> page(@ApiParam(value = "商品分页参数模型", required = true)@RequestBody @Valid GoodsSpuPageVo vo) {

        PageResultVO<GoodsSpuTo> page = goodsSpuService.page(vo.getCurrentPage(), vo.getPageSize(), vo.getStatus(), vo.getGoodsName(), vo.getStart(), vo.getEnd(), vo.getSpuNo(), vo.getCategoryIdLevel1(), vo.getCategoryIdLevel2(),vo.getShopName());
        return ApiResponse.buildSuccessResponse(page);
    }


}
