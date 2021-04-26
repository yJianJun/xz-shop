package com.cdzg.xzshop.controller.admin;

import com.cdzg.universal.vo.response.user.UserLoginResponse;
import com.cdzg.xzshop.config.annotations.api.WebApi;
import com.cdzg.xzshop.domain.ShopInfo;
import com.cdzg.xzshop.filter.auth.LoginSessionUtils;
import com.cdzg.xzshop.service.ShopInfoService;
import com.cdzg.xzshop.vo.admin.ShopSwitchStatusVO;
import com.framework.utils.core.api.ApiResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
    @ApiOperation("店铺批量上下架")
    public ApiResponse<String> batchPutOnDown(@RequestBody @Valid ShopSwitchStatusVO statusVO) {

        List<Long> list = statusVO.getList();
        if (CollectionUtils.isNotEmpty(list)){

            for (int i = 0; i < list.size(); i++) {

                ShopInfo shopInfo = shopInfoService.selectByPrimaryKey(list.get(i));
                if (Objects.nonNull(shopInfo)){
                    shopInfo.setStatus(statusVO.getFlag());
                    shopInfoService.insertOrUpdate(shopInfo);
                }
            }
            return ApiResponse.buildSuccessResponse("编辑成功");
        }
        return ApiResponse.buildCommonErrorResponse("编辑失败");
    }
}

