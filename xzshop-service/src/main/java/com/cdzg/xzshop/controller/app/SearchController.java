package com.cdzg.xzshop.controller.app;


import com.cdzg.xzshop.common.CommonResult;
import com.cdzg.xzshop.config.annotations.api.IgnoreAuth;
import com.cdzg.xzshop.config.annotations.api.MobileApi;
import com.cdzg.xzshop.domain.GoodsSpu;
import com.cdzg.xzshop.filter.auth.LoginSessionUtils;
import com.cdzg.xzshop.service.GoodsSpuService;
import com.cdzg.xzshop.service.SearchHistoryService;
import com.cdzg.xzshop.service.ShopInfoService;
import com.cdzg.xzshop.vo.app.GoodsSpuSearchPageVo;
import com.cdzg.xzshop.vo.common.BasePageRequest;
import com.cdzg.xzshop.vo.common.PageResultVO;
import com.framework.utils.core.api.ApiResponse;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("app/search")
@Validated
@Api(tags = {"app_商城搜索"}, description = "搜索相关接口")
public class SearchController {

    @Autowired
    GoodsSpuService goodsSpuService;

    @Autowired
    ShopInfoService shopInfoService;

    @Autowired
    SearchHistoryService searchHistoryService;


    /**
     * 热门搜索列表
     */
    @RequestMapping(value = "/history", method = RequestMethod.GET)
    @ApiOperation(value = "搜索历史关键词列表")
    @MobileApi
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "Authorization token", required = false, dataType = "string", paramType = "header")
    })
    public ApiResponse<List<String>> getSearchHistory() {

        String customerId = LoginSessionUtils.getAppUser().getCustomerId();
        List<String> data = searchHistoryService.findKeyWordByUserIdOrderByCountDesc(Long.parseLong(customerId));
        return CommonResult.buildSuccessResponse((!CollectionUtils.isEmpty(data)) ? (data.size() > 10 ? data.subList(0, 10) : data) : null);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    @ApiOperation(value = "删除历史关键词")
    @MobileApi
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "Authorization token", required = false, dataType = "string", paramType = "header")
    })
    public ApiResponse deleteSearchHistory() {

        String customerId = LoginSessionUtils.getAppUser().getCustomerId();
       // searchHistoryService.deleteByKeyWordAndUserId(keyWord,Long.parseLong(customerId));
        searchHistoryService.deleteByUserId(Long.parseLong(customerId));
        return CommonResult.buildSuccessResponse();
    }

    @MobileApi
    @PostMapping("/goods")
    @ApiOperation("搜索商品列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "Authorization token", required = false, dataType = "string", paramType = "header")
    })
    public ApiResponse<PageResultVO<GoodsSpu>> search(@ApiParam(value = "商品搜索列表分页模型", required = true) @RequestBody @Valid GoodsSpuSearchPageVo vo) {

        String customerId = LoginSessionUtils.getAppUser().getCustomerId();
        return CommonResult.buildSuccessResponse(goodsSpuService.search(vo, customerId));
    }


}
