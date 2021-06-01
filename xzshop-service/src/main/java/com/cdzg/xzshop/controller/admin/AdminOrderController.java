package com.cdzg.xzshop.controller.admin;

import com.cdzg.xzshop.service.OrderItemService;
import com.cdzg.xzshop.service.OrderService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
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




}
