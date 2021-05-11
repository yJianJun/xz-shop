package com.cdzg.xzshop.enums;

/**
 * 退款状态
 */
public enum RefundStatusEnum {
    REVOKE(0, "撤销"),
    APPLY_REFUND(1, "申请退款"),
    REJECT_RETURN(2, "拒绝退货"),
    CUSTOMER_WAIT_SHIP(3, "买家待发货"),
    SHOP_WAIT_RECEIPT(4, "卖家待收货"),
    SHOP_REJECT_RECEIPT(5, "收货拒绝"),
    REJECT_REFUND(6, "拒绝退款"),
    REFUND_SUCCESS(7, "退款成功"),
    ;

    private Integer code;

    private String name;

    RefundStatusEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
