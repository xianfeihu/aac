package com.yz.aac.mining.repository;

import com.yz.aac.mining.repository.domian.Operator;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface OperatorRepository {

    String QUERY_OPERATOR_BY_ID = "SELECT * FROM operator WHERE id = #{id}";

    @Select(QUERY_OPERATOR_BY_ID)
    Operator getOperatorById(@Param("id") Long id);

}
