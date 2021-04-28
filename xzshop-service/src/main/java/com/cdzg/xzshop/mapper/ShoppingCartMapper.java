package com.cdzg.xzshop.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cdzg.xzshop.domain.ShoppingCart;
import org.apache.ibatis.annotations.Mapper;

/**
 * 购物车管理
 * 
 * @author EvilPet
 * @email xinliqiang3@163.com
 * @date 2021-04-28 09:52:07
 */

@Mapper
public interface ShoppingCartMapper extends BaseMapper<ShoppingCart> {
	
}
