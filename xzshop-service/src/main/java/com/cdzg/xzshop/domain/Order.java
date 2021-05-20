package com.cdzg.xzshop.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 订单表
 * 
 * @author XLQ
 * @email xinliqiang3@163.com
 * @date 2021-05-10 17:12:43
 */
@Data
@TableName("order")
public class Order implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 订单id
	 */
	@TableId
	private Long id;
	/**
	 * app用户id
	 */
	private Long customerId;
	/**
	 * 订单类型 0-普通订单 1-积分订单
	 */
	private Integer orderType;
	/**
	 * 商品总价格（不含运费）单位：元
	 */
	private BigDecimal goodsPrice;
	/**
	 * 运费  单位：元
	 */
	private BigDecimal freight;
	/**
	 * 订单总金额(商品总额+运费) 单位：元
	 */
	private BigDecimal totalMoney;
	/**
	 * 实际支付金额 单位：元
	 */
	private BigDecimal payMoney;
	/**
	 * 备注(app用户下单备注)
	 */
	private String orderRemarks;
	/**
	 * 店铺id
	 */
	private String shopId;
	/**
	 * 订单生成时间
	 */
	private Date createTime;
	/**
	 * 订单状态（1待付款2.待发货3.已发货4.已完成5.已关闭）
	 */
	private Integer orderStatus;
	/**
	 * 支付方式0-还未支付,1-支付宝,2-微信支付,3-积分
	 */
	private Integer payMethod;
	/**
	 * 付款时间
	 */
	private Date payTime;
	/**
	 * 发货时间
	 */
	private Date deliverTime;
	/**
	 * 成交时间(确认收货)
	 */
	private Date dealTime;
	/**
	 * 物流公司编号（关联物流表）
	 */
	private String logisticsCompanyCode;
	/**
	 * 物流编号(2.11新增)
	 */
	private String logisticsNumber;
	/**
	 * 物流备注
	 */
	private String logisticsRemarks;
	/**
	 * 省(收货地址)
	 */
	private String province;
	/**
	 * 市(收货地址)
	 */
	private String city;
	/**
	 * 区(收货地址)
	 */
	private String area;
	/**
	 * 详细地址(收货地址)
	 */
	private String address;
	/**
	 * 收货人姓名
	 */
	private String consigneeName;
	/**
	 * 收货人电话
	 */
	private String consigneePhone;
	/**
	 * 修改时间
	 */
	private Date updateTime;
	/**
	 * 修改人
	 */
	private String updateBy;
	/**
	 * 逻辑删除标志 1-是 0-否
	 */
	private Integer deleted;

}
