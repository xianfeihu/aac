package com.yz.aac.opadmin.repository;

import com.yz.aac.opadmin.repository.domain.OperatorActionLog;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface OperatorActionLogRepository {

    String ALL_FIELDS = "id, operator_id, module, action, additional_info, action_time";
    String QUERY_LOGS = "<script>SELECT " + ALL_FIELDS + " FROM operator_action_log"
            + "<where>"
            + "<if test=\"operatorId != null\"> AND operator_id = #{operatorId}</if>"
            + "<if test=\"beginActionTime != null\"> AND action_time &gt;= #{beginActionTime}</if>"
            + "<if test=\"endActionTime != null\"> AND action_time &lt;= #{endActionTime}</if>"
            + "</where>"
            + "ORDER BY action_time DESC"
            + "</script>";
    String STORE_LOG = "INSERT INTO operator_action_log(" + ALL_FIELDS + ") VALUES(#{id}, #{operatorId}, #{module}, #{action}, #{additionalInfo}, #{actionTime})";

    @Select(QUERY_LOGS)
    List<OperatorActionLog> query(OperatorActionLog log);

    @Insert(STORE_LOG)
    void store(OperatorActionLog log);


}
