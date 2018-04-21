package com.zhenhui.demo.toptop.follow.dal.repository;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.zhenhui.demo.toptop.follow.api.domain.TargetType;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

@MappedTypes(TargetType.class)
@MappedJdbcTypes(JdbcType.INTEGER)
public class TargetTypeHandler extends BaseTypeHandler<TargetType> {

    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, TargetType targetType,
                                    JdbcType jdbcType) throws SQLException {
        preparedStatement.setInt(i, targetType.code);
    }

    @Override
    public TargetType getNullableResult(ResultSet resultSet, String s) throws SQLException {

        return TargetType.valueOf(resultSet.getInt(s));
    }

    @Override
    public TargetType getNullableResult(ResultSet resultSet, int i) throws SQLException {
        return TargetType.valueOf(resultSet.getInt(i));
    }

    @Override
    public TargetType getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        return TargetType.valueOf(callableStatement.getInt(i));

    }
}
