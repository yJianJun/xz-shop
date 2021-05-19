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
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/search")
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
    @RequestMapping(value = "/history", method = RequestMethod.POST)
    @ApiOperation(value = "搜索历史关键词列表")
    @MobileApi
    public ApiResponse<List<String>> getSearchHistory(@ApiParam(value = "分页参数模型", required = true) @RequestBody @Valid BasePageRequest pageRequest) {

        String customerId = LoginSessionUtils.getAppUser().getCustomerId();
        PageResultVO<String> page = searchHistoryService.searchHistorywithPage(pageRequest.getCurrentPage(), pageRequest.getPageSize(), Long.parseLong(customerId));
        return CommonResult.buildSuccessResponse(page.getData());
    }

    @MobileApi
    @PostMapping("/goods")
    @ApiOperation("搜索商品列表")
    public ApiResponse<PageResultVO<GoodsSpu>> search(@ApiParam(value = "商品搜索列表分页模型", required = true) @RequestBody @Valid GoodsSpuSearchPageVo vo) {

        String customerId = LoginSessionUtils.getAppUser().getCustomerId();
        return CommonResult.buildSuccessResponse(goodsSpuService.search(vo,customerId));
    }


}
