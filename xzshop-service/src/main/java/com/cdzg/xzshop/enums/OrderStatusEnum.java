package com.cdzg.xzshop.enums;

/**
 * 订单类型枚举 1待付款2.待发货3.已发货4.已完成5.已关闭
 */
public enum OrderStatusEnum {
    //1待付款2.待发货3.已发货4.已完成5.已关闭

    WAIT_PAY(1, "待付款"),
    WAIT_SEND(2, "待发货"),
    ALREADY_SEND(3, "已发货"),
    FINISH(4, "已完成"),
    CLOSED(5, "已关闭");

    private Integer code;

    private String name;

    OrderStatusEnum(Integer code, String name) {
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
        for (OrderStatusEnum value : OrderStatusEnum.values()) {
            if (value.getCode().equals(code)) {
                return value.getName();
            }
        }
        return "";
    }
}
