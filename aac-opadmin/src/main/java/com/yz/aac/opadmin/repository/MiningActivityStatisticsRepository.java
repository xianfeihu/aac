package com.yz.aac.opadmin.repository;

import com.yz.aac.opadmin.model.request.QueryMiningActivityParticipatorRequest;
import com.yz.aac.opadmin.model.response.QueryMiningActivityParticipatorResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface MiningActivityStatisticsRepository {

    String QUERY = "<script>SELECT u.id AS userCode, u.name AS userName, ubs.value AS totalCount, mas.ad_clicked AS adClickedCount, gained AS totalBonus FROM user u"
            + " INNER JOIN user_behaviour_statistics ubs ON u.id = ubs.user_id AND ubs.key = 'MINING_EVENT'"
            + " INNER JOIN (SELECT user_id, IFNULL(SUM(gained), 0) AS gained, IFNULL(SUM(ad_clicked), 0) AS ad_clicked FROM mining_activity_statistics GROUP BY user_id) mas ON u.id = mas.user_id"
            + "<where>"
            + "<if test=\"id != null\"> AND u.id = #{id}</if>"
            + "<if test=\"name != null and name != ''\"><bind name=\"fixedName\" value=\"'%' + name + '%'\" /> AND u.name LIKE #{fixedName}</if>"
            + "</where>"
            + " ORDER BY mas.gained DESC"
            + "</script>";


    @Select(QUERY)
    List<QueryMiningActivityParticipatorResponse.Item> query(QueryMiningActivityParticipatorRequest condition);
}
