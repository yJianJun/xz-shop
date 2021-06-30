package com.cdzg.xzshop.componet;

import com.beust.jcommander.internal.Maps;
import com.cdzg.xzshop.enums.BaseEnum;
import org.springframework.core.convert.converter.Converter;

import java.util.Map;
import java.util.Objects;

public class StringToEnumConverter<T extends BaseEnum> implements Converter<String, T> {
    private Map<String, T> enumMap = Maps.newHashMap();

    public StringToEnumConverter(Class<T> enumType) {
        T[] enums = enumType.getEnumConstants();
        for (T e : enums) {
            enumMap.put(e.getValue().toString(), e);
        }
    }

    @Override
    public T convert(String source) {
        T t = enumMap.get(source);
        if (Objects.isNull(t)) {
            throw new IllegalArgumentException("无法匹配对应的枚举类型");
        }
        return t;
    }
}
