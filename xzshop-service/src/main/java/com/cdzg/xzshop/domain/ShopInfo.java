package com.cdzg.xzshop.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import javax.persistence.*;
import lombok.Builder;
import lombok.Data;

/**
 * 店铺信息表
 */
@ApiModel(value = "com-cdzg-xzshop-domain-ShopInfo")
@Data
@Builder
@Table(name = "shop_info")
public class ShopInfo implements Serializable {
    /**
     * id
     */
    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "JDBC")
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 店铺名称
     */
    @Column(name = "shop_name")
    @ApiModelProperty(value = "店铺名称")
    private String shopName;

    /**
     * 所属工会
     */
    @Column(name = "shop_union")
    @ApiModelProperty(value = "所属工会")
    private String shopUnion;

    /**
     * 运营部门
     */
    @Column(name = "department")
    @ApiModelProperty(value = "运营部门")
    private String department;

    /**
     * 店铺联系电话
     */
    @Column(name = "phone")
    @ApiModelProperty(value = "店铺联系电话")
    private String phone;

    /**
     * 店铺logo
     */
    @Column(name = "logo")
    @ApiModelProperty(value = "店铺logo")
    private String logo;

    /**
     * 二级分类id
     */
    @Column(name = "category_id_level2")
    @ApiModelProperty(value = "二级分类id")
    private Long categoryIdLevel2;

    /**
     * 一级分类id
     */
    @Column(name = "category_id_level1")
    @ApiModelProperty(value = "一级分类id")
    private Long categoryIdLevel1;

    /**
     * 店铺上线时间
     */
    @Column(name = "gmt_put_on_the_shelf")
    @ApiModelProperty(value = "店铺上线时间")
    private LocalDateTime gmtPutOnTheShelf;

    /**
     * gmtCreate
     */
    @Column(name = "gmt_create")
    @ApiModelProperty(value = "gmtCreate")
    private LocalDateTime gmtCreate;

    /**
     * gmtUpdate
     */
    @Column(name = "gmt_update")
    @ApiModelProperty(value = "gmtUpdate")
    private LocalDateTime gmtUpdate;

    /**
     * 店铺联系人
     */
    @Column(name = "contact_person")
    @ApiModelProperty(value = "店铺联系人")
    private String contactPerson;

    /**
     * 状态：1:上线,0未上线
     */
    @Column(name = "`status`")
    @ApiModelProperty(value = "状态：1:上线,0未上线")
    private Boolean status;

    /**
     * 店铺创建人
     */
    @Column(name = "create_user")
    @ApiModelProperty(value = "店铺创建人")
    private String createUser;

    /**
     * 运费
     */
    @Column(name = "fare")
    @ApiModelProperty(value = "运费")
    private BigDecimal fare;

    private static final long serialVersionUID = 1L;
}