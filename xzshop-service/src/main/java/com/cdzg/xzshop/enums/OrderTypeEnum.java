package com.cdzg.xzshop.enums;

/**
 * 订单类型枚举 1-积分订单 2-普通订单
 */
public enum OrderTypeEnum {

    //1-积分订单 2-普通订单
    INTEGRAL_ORDER(1, "积分订单"),
    REGULAR_ORDERS(2, "普通订单");

    private Integer code;

    private String name;

    OrderTypeEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static String getNameByCode(Integer code) {
        if (code == null) {
            return "";
        }
        for (OrderTypeEnum value : OrderTypeEnum.values()) {
            if (value.getCode().equals(code)) {
                return value.getName();
            }
        }
        return "";
    }
}
