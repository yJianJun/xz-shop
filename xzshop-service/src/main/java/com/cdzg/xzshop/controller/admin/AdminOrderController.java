package com.cdzg.xzshop.controller.admin;

import com.cdzg.xzshop.config.annotations.api.MobileApi;
import com.cdzg.xzshop.config.annotations.api.WebApi;
import com.cdzg.xzshop.service.OrderItemService;
import com.cdzg.xzshop.service.OrderService;
import com.framework.utils.core.api.ApiResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName : AdminOrderController
 * @Description : admin订单管理
 * @Author : EvilPet
 * @Date: 2021-05-25 10:26
 */

@RestController
@Api(tags = "32_admin_admin订单管理")
@RequestMapping("admin/order")
public class AdminOrderController {

    @Autowired
    private OrderService orderService;


    @Autowired
    private OrderItemService orderItemService;

    @WebApi
    @PostMapping("/list")
    @ApiOperation("32001-订单列表")
    public ApiResponse list(){



        return null;
    }

    @WebApi
    @GetMapping("/getById")
    @ApiOperation("32002-订单详情")
    public ApiResponse getById(){



        return null;
    }




}
