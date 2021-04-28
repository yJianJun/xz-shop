package com.cdzg.xzshop.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cdzg.xzshop.domain.ShoppingCart;
import com.cdzg.xzshop.mapper.ShoppingCartMapper;
import com.cdzg.xzshop.service.ShoppingCartService;
import org.springframework.stereotype.Service;


/**
 * 购物车管理
 *
 * @author EvilPet
 * @email xinliqiang3@163.com
 * @date 2021-04-28 09:52:07
 */
@Service("shoppingCartService")
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {


}