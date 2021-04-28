package com.cdzg.xzshop.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 购物车管理
 * 
 * @author EvilPet
 * @email xinliqiang3@163.com
 * @date 2021-04-28 09:52:07
 */
@Data
@TableName("shopping_cart")
public class ShoppingCart implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 购物车id
	 */
	@TableId
	private Long id;
	/**
	 * 店铺id
	 */
	private Long shopId;
	/**
	 * 商品id
	 */
	private Long goodsId;
	/**
	 * 商品数量
	 */
	private Integer goodsNumber;
	/**
	 * app用户id
	 */
	private Long appUserId;
	/**
	 * 创建人
	 */
	private String createBy;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 更新人
	 */
	private String updateBy;
	/**
	 * 更新时间
	 */
	private Date updateTime;
	/**
	 * 逻辑删除标志 1-是 0-否
	 */
	private Integer deleted;

}
