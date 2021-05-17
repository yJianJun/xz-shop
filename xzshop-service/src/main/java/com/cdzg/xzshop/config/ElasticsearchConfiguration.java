package com.cdzg.xzshop.config;


import com.cdzg.xzshop.constant.PaymentType;
import com.cdzg.xzshop.handler.*;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.elasticsearch.config.ElasticsearchConfigurationSupport;
import org.springframework.data.elasticsearch.core.ElasticsearchEntityMapper;
import org.springframework.data.elasticsearch.core.EntityMapper;
import org.springframework.data.elasticsearch.core.convert.ElasticsearchCustomConversions;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * elasticsearch处理localDateTime
 *
 * @author yangle
 * @date 2020-10-13 19:20
 */
@Configuration
public class ElasticsearchConfiguration extends ElasticsearchConfigurationSupport {

    @Bean
    @Override
    public ElasticsearchCustomConversions elasticsearchCustomConversions() {

        List<Converter<?,?>> converters= new ArrayList<>();

        // LocalDateTime
        converters.add(DateToLocalDateTimeConverter.INSTANCE);
        converters.add(StringToLocalDateTimeConverter.INSTANCE);
        converters.add(LongToLocalDateTimeConverter.INSTANCE);
        converters.add(LocalDateTimeToStringConverter.INSTANCE);

        // List
        converters.add(StringToListConverter.INSTANCE);
        converters.add(ListToStringConverter.INSTANCE);

        //PaymentType
        converters.add(PaymentTypeToIntConverter.INSTANCE);
        converters.add(IntToPaymentTypeConverter.INSTANCE);

        return new ElasticsearchCustomConversions(converters);
    }

    @Bean
    @Override
    public EntityMapper entityMapper() {

        ElasticsearchEntityMapper entityMapper = new ElasticsearchEntityMapper(elasticsearchMappingContext(),
                new DefaultConversionService());
        entityMapper.setConversions(elasticsearchCustomConversions());
        return entityMapper;
    }

    // Direction: ES -> Java
    @ReadingConverter
    enum DateToLocalDateTimeConverter implements Converter<Date, LocalDateTime> {

        INSTANCE;

        @Override
        public LocalDateTime convert(Date date) {
            Instant instant = date.toInstant();
            return instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
        }
    }

    // Direction: ES -> Java
    @ReadingConverter
    enum IntToPaymentTypeConverter implements Converter<Integer, PaymentType> {

        INSTANCE;

        @Override
        public PaymentType convert(Integer code) {

            for (PaymentType value : PaymentType.values()) {
                if (value.getValue().equals(code)) {
                    return value;
                }
            }
            throw new RuntimeException("unknown code");
        }
    }

    // Direction: Java -> ES
    @WritingConverter
    enum ListToStringConverter implements Converter<List<String>,String> {

        INSTANCE;

        @Override
        public String convert(List<String> source) {
            return Joiner.on(",").skipNulls().join(source);
        }
    }

    // Direction: Java -> ES
    @WritingConverter
    enum LocalDateTimeToStringConverter implements Converter<LocalDateTime,String> {

        INSTANCE;

        @Override
        public String convert(LocalDateTime time) {
            DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            return df.format(time);
        }
    }

    // Direction: ES -> Java
    @ReadingConverter
    enum LongToLocalDateTimeConverter implements Converter<Long, LocalDateTime> {

        INSTANCE;

        @Override
        public java.time.LocalDateTime convert(Long source) {
            return Instant.ofEpochMilli(source).atZone(ZoneId.systemDefault()).toLocalDateTime();
        }
    }

    // Direction: Java -> ES
    @WritingConverter
    enum PaymentTypeToIntConverter implements Converter<PaymentType,Integer> {

        INSTANCE;

        @Override
        public Integer convert(PaymentType source) {
            return source.getValue();
        }
    }

    // Direction: ES -> Java
    @ReadingConverter
    enum StringToListConverter implements Converter<String, List<String>> {

        INSTANCE;
        @Override
        public List<String> convert(String source) {
            return stringToList(source);
        }

        private List<String> stringToList(String str) {

            List<String> list = new ArrayList<>();

            if (Strings.isNullOrEmpty(str)) {
                return list;
            } else {

                List<String> strings = Arrays.stream(StringUtils.split(str, ",")).collect(Collectors.toList());
                list.addAll(strings);
                return list;
            }
        }
    }

    // Direction: ES -> Java
    @ReadingConverter
    enum StringToLocalDateTimeConverter implements Converter<String, LocalDateTime> {

        INSTANCE;

        @Override
        public java.time.LocalDateTime convert(String source) {
            DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            return LocalDateTime.parse(source, df);
        }
    }

}