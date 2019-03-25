package com.yz.aac.opadmin.repository;

import com.yz.aac.opadmin.model.request.QueryArticleParticipatorRequest;
import com.yz.aac.opadmin.model.response.QueryArticleParticipatorResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ArticleInteractionRepository {

    String QUERY_PARTICIPATORS = "<script>SELECT t.user_id AS userCode, u.name AS userName, t.p_cnt AS publishCount, t.c_cnt AS commentCount, t.r_cnt AS readingCount, IFNULL(umr.bonus, 0) AS bonus"
            + " FROM ("
            + "   SELECT a.user_id, SUM(a.p_cnt) AS p_cnt, SUM(a.c_cnt) AS c_cnt, SUM(a.r_cnt) AS r_cnt"
            + "   FROM ("
            + "     SELECT author_id AS user_id, COUNT(id) AS p_cnt, 0 AS c_cnt, 0 AS r_cnt FROM article GROUP BY author_id"
            + "     UNION"
            + "     SELECT action_user_id AS user_id, 0 AS p_cnt, COUNT(id) AS c_cnt, 0 AS r_cnt FROM article_interaction WHERE action = 1 GROUP BY action_user_id"
            + "     UNION"
            + "     SELECT action_user_id AS user_id, 0 AS p_cnt, 0 AS c_cnt, COUNT(id) AS r_cnt FROM article_interaction WHERE action = 3 GROUP BY action_user_id"
            + "   ) AS a"
            + "   GROUP BY a.user_id"
            + " ) AS t"
            + " INNER JOIN user u ON u.id = t.user_id"
            + " LEFT JOIN ("
            + "   SELECT user_id, SUM(bonus) AS bonus FROM user_mining_record WHERE action IN (1, 11) AND bonus_type = 1 GROUP BY user_id"
            + " ) AS umr ON umr.user_id = t.user_id"
            + "<where>"
            + "<if test=\"id != null\"> AND u.id = #{id}</if>"
            + "<if test=\"name != null and name != ''\"><bind name=\"fixedName\" value=\"'%' + name + '%'\" /> AND u.name LIKE #{fixedName}</if>"
            + "</where>"
            + " ORDER BY bonus DESC"
            + "</script>";

    @Select(QUERY_PARTICIPATORS)
    List<QueryArticleParticipatorResponse.Item> queryParticipators(QueryArticleParticipatorRequest condition);
}