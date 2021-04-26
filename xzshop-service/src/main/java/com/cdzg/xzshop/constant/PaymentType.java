package com.cdzg.xzshop.constant;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.core.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum PaymentType implements IEnum<Integer> {

    /**
     * 1:积分换购 2:线上支付
     */
    Integral(1),
    Wallet(2);

    @EnumValue
    @JsonValue
    private int code;

    PaymentType(int code) {
        this.code = code;
    }


    @JsonCreator
    public static PaymentType fromText(String text) {

        int code = Integer.parseInt(text);
        for (PaymentType value : PaymentType.values()) {
            if (value.code == code) {

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

