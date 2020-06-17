package com.yyt.axios.mybatis.typeHandler;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@MappedJdbcTypes(JdbcType.VARCHAR)
@MappedTypes(value = List.class)
public class StringListTypeHandler extends BaseTypeHandler<List<Integer>> {
    private static final Joiner joiner = Joiner.on(",").skipNulls();
    private static final Splitter splitter = Splitter.on(",").omitEmptyStrings();


    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, List<Integer> integers, JdbcType jdbcType) throws SQLException {
        preparedStatement.setString(i, list2String(integers));
    }

    @Override
    public List<Integer> getNullableResult(ResultSet resultSet, String s) throws SQLException {
        String str = resultSet.getString(s);
        return string2List(str);
    }

    @Override
    public List<Integer> getNullableResult(ResultSet resultSet, int i) throws SQLException {
        String str = resultSet.getString(i);
        return string2List(str);
    }

    @Override
    public List<Integer> getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        String string = callableStatement.getString(i);
        return string2List(string);
    }

    private static String list2String(List<Integer> integers) {
        if(integers == null) {
            return null;
        }
        return joiner.join(integers);
    }

    private static List<Integer> string2List(String string) {
        if (string == null) {
            return null;
        }
        List<String> strings = splitter.splitToList(string);
        List<Integer> res = new ArrayList<>();
        strings.forEach(str -> {
            res.add(Integer.parseInt(str));
        });
        return res;
    }
}
