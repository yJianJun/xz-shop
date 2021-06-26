package com.cdzg.xzshop.enums;

/**
 * 订单支付方式枚举 支付方式0-还未支付,1-支付宝,2-微信支付,3-积分
 */
public enum PayMethodEnum {

    //1-积分订单 2-普通订单
    WAIT_PAY(0, "待支付"),
    ALI_PAY(1, "支付宝"),
    WECHAT_PAY(2, "微信支付"),
    INTEGRAL_PAY(3, "积分支付");

    private Integer code;

    private String name;

    PayMethodEnum(Integer code, String name) {
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
        for (PayMethodEnum value : PayMethodEnum.values()) {
            if (value.getCode().equals(code)) {
                return value.getName();
            }
        }
        return "";
    }
}
