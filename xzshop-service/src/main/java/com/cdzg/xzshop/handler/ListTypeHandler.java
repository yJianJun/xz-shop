package com.cdzg.xzshop.handler;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.apache.poi.ss.formula.functions.T;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@MappedTypes({List.class})
@MappedJdbcTypes({JdbcType.VARCHAR})
public class ListTypeHandler extends BaseTypeHandler<List<String>> {


    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, List<String> parameter, JdbcType jdbcType) throws SQLException {
        String str = Joiner.on(",").skipNulls().join(parameter);
        ps.setString(i, str);
    }

    @Override
    public List<String> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return this.stringToList(rs.getString(columnName));
    }

    @Override
    public List<String> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return this.stringToList(rs.getString(columnIndex));
    }

    @Override
    public List<String> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return this.stringToList(cs.getString(columnIndex));
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

