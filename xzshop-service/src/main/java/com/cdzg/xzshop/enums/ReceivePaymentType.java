package com.cdzg.xzshop.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.core.enums.IEnum;
import com.cdzg.xzshop.enums.PaymentType;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ReceivePaymentType implements IEnum<Integer>,BaseEnum {

    /**
     * 1:支付宝支付 2:微信支付
     */
    Alipay(1),
    Wechat(2);

    @EnumValue
    @JsonValue
    private int code;

    ReceivePaymentType(int code) {
        this.code = code;
    }


    @JsonCreator
    public static PaymentType fromText(String text) {

        int code = Integer.parseInt(text);
        for (PaymentType value : PaymentType.values()) {
            if (value.getValue() == code) {

                return value;
            }
        }
        throw new RuntimeException("unknown code");
    }


    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return code + "";
    }

    @Override
    public Integer getValue() {
        return code;
    }
}

