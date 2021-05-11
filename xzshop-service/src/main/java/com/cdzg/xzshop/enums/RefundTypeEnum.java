package com.cdzg.xzshop.enums;

/**
 * 退款类型
 */
public enum RefundTypeEnum {

    REFUND(0, "退款"),
    RETURN(1, "退货退款");


    private Integer code;

    private String name;

    RefundTypeEnum(Integer code, String name) {
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
