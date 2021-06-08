package com.cdzg.xzshop.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 订单明细表
 * 
 * @author XLQ
 * @email xinliqiang3@163.com
 * @date 2021-05-10 17:12:43
 */
@Data
@TableName("order_item")
public class OrderItem implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
	@TableId
	private Long id;
	/**
	 * 订单id
	 */
	private Long orderId;
	/**
	 * 商品id
	 */
	private Long goodsId;
	/**
	 * 商品名称
	 */
	private String goodsName;
	/**
	 * 数量
	 */
	private Integer goodsCount;
	/**
	 * 单价
	 */
	private BigDecimal goodsUnitPrice;
	/**
	 * 逻辑删除标志 1-是 0-否
	 */
	private Integer deleted;
	/**
	 * 状态 0正常 1.退款中2.已退款3.退货中4.已退货5.拒绝退款6.拒绝退货
	 */
	private Integer status;

}
