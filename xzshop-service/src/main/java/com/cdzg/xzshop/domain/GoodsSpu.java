package com.cdzg.xzshop.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import javax.persistence.*;

/**
    * 商品spu
    */
@ApiModel(value="com-cdzg-xzshop-domain-GoodsSpu")
@Table(name = "goods_spu")
public class GoodsSpu implements Serializable {
    /**
     * id
     */
    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "JDBC")
    @ApiModelProperty(value="id")
    private Long id;

    /**
     * 商品编号，唯一
     */
    @Column(name = "spu_no")
    @ApiModelProperty(value="商品编号，唯一")
    private Long spuNo;

    /**
     * 商品名称
     */
    @Column(name = "goods_name")
    @ApiModelProperty(value="商品名称")
    private String goodsName;

    /**
     * 商品广告词
     */
    @Column(name = "ad_word")
    @ApiModelProperty(value="商品广告词")
    private String adWord;

    /**
     * 原价
     */
    @Column(name = "price")
    @ApiModelProperty(value="原价")
    private BigDecimal price;

    /**
     * 售价
     */
    @Column(name = "promotion_price")
    @ApiModelProperty(value="售价")
    private BigDecimal promotionPrice;

    /**
     * 积分售价
     */
    @Column(name = "fraction_price")
    @ApiModelProperty(value="积分售价")
    private BigDecimal fractionPrice;

    /**
     * 二级分类id
     */
    @Column(name = "category_id_level2")
    @ApiModelProperty(value="二级分类id")
    private Long categoryIdLevel2;

    /**
     * 一级分类id
     */
    @Column(name = "category_id_level1")
    @ApiModelProperty(value="一级分类id")
    private Long categoryIdLevel1;

    /**
     * 支付方式:1:积分换购 2:线上支付
     */
    @Column(name = "payment_method")
    @ApiModelProperty(value="支付方式:1:积分换购 2:线上支付")
    private Byte paymentMethod;

    /**
     * 商家id
     */
    @Column(name = "shop_id")
    @ApiModelProperty(value="商家id")
    private Long shopId;

    /**
     * 商品是否上架
     */
    @Column(name = "`status`")
    @ApiModelProperty(value="商品是否上架")
    private Boolean status;

    /**
     * 商品库存
     */
    @Column(name = "stock")
    @ApiModelProperty(value="商品库存")
    private Long stock;

    /**
     * 创建人用户ID
     */
    @Column(name = "create_user")
    @ApiModelProperty(value="创建人用户ID")
    private String createUser;

    /**
     * 商品描述
     */
    @Column(name = "description")
    @ApiModelProperty(value="商品描述")
    private String description;

    /**
     * 商品上架时间
     */
    @Column(name = "gmt_put_on_the_shelf")
    @ApiModelProperty(value="商品上架时间")
    private LocalDateTime gmtPutOnTheShelf;

    /**
     * gmtCreate
     */
    @Column(name = "gmt_create")
    @ApiModelProperty(value="gmtCreate")
    private LocalDateTime gmtCreate;

    /**
     * gmtUpdate
     */
    @Column(name = "gmt_update")
    @ApiModelProperty(value="gmtUpdate")
    private Date gmtUpdate;

    private static final long serialVersionUID = 1L;

    /**
     * 获取id
     *
     * @return id - id
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置id
     *
     * @param id id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取商品编号，唯一
     *
     * @return spu_no - 商品编号，唯一
     */
    public Long getSpuNo() {
        return spuNo;
    }

    /**
     * 设置商品编号，唯一
     *
     * @param spuNo 商品编号，唯一
     */
    public void setSpuNo(Long spuNo) {
        this.spuNo = spuNo;
    }

    /**
     * 获取商品名称
     *
     * @return goods_name - 商品名称
     */
    public String getGoodsName() {
        return goodsName;
    }

    /**
     * 设置商品名称
     *
     * @param goodsName 商品名称
     */
    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName == null ? null : goodsName.trim();
    }

    /**
     * 获取商品广告词
     *
     * @return ad_word - 商品广告词
     */
    public String getAdWord() {
        return adWord;
    }

    /**
     * 设置商品广告词
     *
     * @param adWord 商品广告词
     */
    public void setAdWord(String adWord) {
        this.adWord = adWord == null ? null : adWord.trim();
    }

    /**
     * 获取原价
     *
     * @return price - 原价
     */
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * 设置原价
     *
     * @param price 原价
     */
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    /**
     * 获取售价
     *
     * @return promotion_price - 售价
     */
    public BigDecimal getPromotionPrice() {
        return promotionPrice;
    }

    /**
     * 设置售价
     *
     * @param promotionPrice 售价
     */
    public void setPromotionPrice(BigDecimal promotionPrice) {
        this.promotionPrice = promotionPrice;
    }

    /**
     * 获取积分售价
     *
     * @return fraction_price - 积分售价
     */
    public BigDecimal getFractionPrice() {
        return fractionPrice;
    }

    /**
     * 设置积分售价
     *
     * @param fractionPrice 积分售价
     */
    public void setFractionPrice(BigDecimal fractionPrice) {
        this.fractionPrice = fractionPrice;
    }

    /**
     * 获取二级分类id
     *
     * @return category_id_level2 - 二级分类id
     */
    public Long getCategoryIdLevel2() {
        return categoryIdLevel2;
    }

    /**
     * 设置二级分类id
     *
     * @param categoryIdLevel2 二级分类id
     */
    public void setCategoryIdLevel2(Long categoryIdLevel2) {
        this.categoryIdLevel2 = categoryIdLevel2;
    }

    /**
     * 获取一级分类id
     *
     * @return category_id_level1 - 一级分类id
     */
    public Long getCategoryIdLevel1() {
        return categoryIdLevel1;
    }

    /**
     * 设置一级分类id
     *
     * @param categoryIdLevel1 一级分类id
     */
    public void setCategoryIdLevel1(Long categoryIdLevel1) {
        this.categoryIdLevel1 = categoryIdLevel1;
    }

    /**
     * 获取支付方式:1:积分换购 2:线上支付
     *
     * @return payment_method - 支付方式:1:积分换购 2:线上支付
     */
    public Byte getPaymentMethod() {
        return paymentMethod;
    }

    /**
     * 设置支付方式:1:积分换购 2:线上支付
     *
     * @param paymentMethod 支付方式:1:积分换购 2:线上支付
     */
    public void setPaymentMethod(Byte paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    /**
     * 获取商家id
     *
     * @return shop_id - 商家id
     */
    public Long getShopId() {
        return shopId;
    }

    /**
     * 设置商家id
     *
     * @param shopId 商家id
     */
    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    /**
     * 获取商品是否上架
     *
     * @return status - 商品是否上架
     */
    public Boolean getStatus() {
        return status;
    }

    /**
     * 设置商品是否上架
     *
     * @param status 商品是否上架
     */
    public void setStatus(Boolean status) {
        this.status = status;
    }

    /**
     * 获取商品库存
     *
     * @return stock - 商品库存
     */
    public Long getStock() {
        return stock;
    }

    /**
     * 设置商品库存
     *
     * @param stock 商品库存
     */
    public void setStock(Long stock) {
        this.stock = stock;
    }

    /**
     * 获取创建人用户ID
     *
     * @return create_user - 创建人用户ID
     */
    public String getCreateUser() {
        return createUser;
    }

    /**
     * 设置创建人用户ID
     *
     * @param createUser 创建人用户ID
     */
    public void setCreateUser(String createUser) {
        this.createUser = createUser == null ? null : createUser.trim();
    }

    /**
     * 获取商品描述
     *
     * @return description - 商品描述
     */
    public String getDescription() {
        return description;
    }

    /**
     * 设置商品描述
     *
     * @param description 商品描述
     */
    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    /**
     * 获取商品上架时间
     *
     * @return gmt_put_on_the_shelf - 商品上架时间
     */
    public LocalDateTime getGmtPutOnTheShelf() {
        return gmtPutOnTheShelf;
    }

    /**
     * 设置商品上架时间
     *
     * @param gmtPutOnTheShelf 商品上架时间
     */
    public void setGmtPutOnTheShelf(LocalDateTime gmtPutOnTheShelf) {
        this.gmtPutOnTheShelf = gmtPutOnTheShelf;
    }

    /**
     * 获取gmtCreate
     *
     * @return gmt_create - gmtCreate
     */
    public LocalDateTime getGmtCreate() {
        return gmtCreate;
    }

    /**
     * 设置gmtCreate
     *
     * @param gmtCreate gmtCreate
     */
    public void setGmtCreate(LocalDateTime gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    /**
     * 获取gmtUpdate
     *
     * @return gmt_update - gmtUpdate
     */
    public Date getGmtUpdate() {
        return gmtUpdate;
    }

    /**
     * 设置gmtUpdate
     *
     * @param gmtUpdate gmtUpdate
     */
    public void setGmtUpdate(Date gmtUpdate) {
        this.gmtUpdate = gmtUpdate;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", spuNo=").append(spuNo);
        sb.append(", goodsName=").append(goodsName);
        sb.append(", adWord=").append(adWord);
        sb.append(", price=").append(price);
        sb.append(", promotionPrice=").append(promotionPrice);
        sb.append(", fractionPrice=").append(fractionPrice);
        sb.append(", categoryIdLevel2=").append(categoryIdLevel2);
        sb.append(", categoryIdLevel1=").append(categoryIdLevel1);
        sb.append(", paymentMethod=").append(paymentMethod);
        sb.append(", shopId=").append(shopId);
        sb.append(", status=").append(status);
        sb.append(", stock=").append(stock);
        sb.append(", createUser=").append(createUser);
        sb.append(", description=").append(description);
        sb.append(", gmtPutOnTheShelf=").append(gmtPutOnTheShelf);
        sb.append(", gmtCreate=").append(gmtCreate);
        sb.append(", gmtUpdate=").append(gmtUpdate);
        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        GoodsSpu other = (GoodsSpu) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getSpuNo() == null ? other.getSpuNo() == null : this.getSpuNo().equals(other.getSpuNo()))
            && (this.getGoodsName() == null ? other.getGoodsName() == null : this.getGoodsName().equals(other.getGoodsName()))
            && (this.getAdWord() == null ? other.getAdWord() == null : this.getAdWord().equals(other.getAdWord()))
            && (this.getPrice() == null ? other.getPrice() == null : this.getPrice().equals(other.getPrice()))
            && (this.getPromotionPrice() == null ? other.getPromotionPrice() == null : this.getPromotionPrice().equals(other.getPromotionPrice()))
            && (this.getFractionPrice() == null ? other.getFractionPrice() == null : this.getFractionPrice().equals(other.getFractionPrice()))
            && (this.getCategoryIdLevel2() == null ? other.getCategoryIdLevel2() == null : this.getCategoryIdLevel2().equals(other.getCategoryIdLevel2()))
            && (this.getCategoryIdLevel1() == null ? other.getCategoryIdLevel1() == null : this.getCategoryIdLevel1().equals(other.getCategoryIdLevel1()))
            && (this.getPaymentMethod() == null ? other.getPaymentMethod() == null : this.getPaymentMethod().equals(other.getPaymentMethod()))
            && (this.getShopId() == null ? other.getShopId() == null : this.getShopId().equals(other.getShopId()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
            && (this.getStock() == null ? other.getStock() == null : this.getStock().equals(other.getStock()))
            && (this.getCreateUser() == null ? other.getCreateUser() == null : this.getCreateUser().equals(other.getCreateUser()))
            && (this.getDescription() == null ? other.getDescription() == null : this.getDescription().equals(other.getDescription()))
            && (this.getGmtPutOnTheShelf() == null ? other.getGmtPutOnTheShelf() == null : this.getGmtPutOnTheShelf().equals(other.getGmtPutOnTheShelf()))
            && (this.getGmtCreate() == null ? other.getGmtCreate() == null : this.getGmtCreate().equals(other.getGmtCreate()))
            && (this.getGmtUpdate() == null ? other.getGmtUpdate() == null : this.getGmtUpdate().equals(other.getGmtUpdate()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getSpuNo() == null) ? 0 : getSpuNo().hashCode());
        result = prime * result + ((getGoodsName() == null) ? 0 : getGoodsName().hashCode());
        result = prime * result + ((getAdWord() == null) ? 0 : getAdWord().hashCode());
        result = prime * result + ((getPrice() == null) ? 0 : getPrice().hashCode());
        result = prime * result + ((getPromotionPrice() == null) ? 0 : getPromotionPrice().hashCode());
        result = prime * result + ((getFractionPrice() == null) ? 0 : getFractionPrice().hashCode());
        result = prime * result + ((getCategoryIdLevel2() == null) ? 0 : getCategoryIdLevel2().hashCode());
        result = prime * result + ((getCategoryIdLevel1() == null) ? 0 : getCategoryIdLevel1().hashCode());
        result = prime * result + ((getPaymentMethod() == null) ? 0 : getPaymentMethod().hashCode());
        result = prime * result + ((getShopId() == null) ? 0 : getShopId().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getStock() == null) ? 0 : getStock().hashCode());
        result = prime * result + ((getCreateUser() == null) ? 0 : getCreateUser().hashCode());
        result = prime * result + ((getDescription() == null) ? 0 : getDescription().hashCode());
        result = prime * result + ((getGmtPutOnTheShelf() == null) ? 0 : getGmtPutOnTheShelf().hashCode());
        result = prime * result + ((getGmtCreate() == null) ? 0 : getGmtCreate().hashCode());
        result = prime * result + ((getGmtUpdate() == null) ? 0 : getGmtUpdate().hashCode());
        return result;
    }
}