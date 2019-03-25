package com.yz.aac.opadmin.repository;

import com.yz.aac.opadmin.model.request.QueryInvitationParticipatorRequest;
import com.yz.aac.opadmin.model.request.QueryQuestionParticipatorRequest;
import com.yz.aac.opadmin.model.request.QuerySignInParticipatorRequest;
import com.yz.aac.opadmin.model.response.QueryInvitationParticipatorResponse;
import com.yz.aac.opadmin.model.response.QueryQuestionParticipatorResponse;
import com.yz.aac.opadmin.model.response.QuerySignInParticipatorResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface UserMiningRecordRepository {

    String QUERY_QUESTION_PARTICIPATORS = "<script>SELECT u.id AS userCode, u.name AS userName, ubs.value AS totalAnswers, IFNULL(COUNT(umr.id), 0) AS correctAnswers, IFNULL(SUM(umr.bonus), 0) AS powerPointBonus"
            + " FROM user u"
            + " INNER JOIN user_mining_record umr ON umr.user_id = u.id AND umr.action = 7 AND umr.bonus_type = 2"
            + " LEFT JOIN user_behaviour_statistics ubs ON u.id = ubs.user_id AND ubs.`key` = 'ANSWER'"
            + "<where>"
            + " AND ubs.value > 0"
            + "<if test=\"name != null and name != ''\"><bind name=\"fixedName\" value=\"'%' + name + '%'\" /> AND u.name LIKE #{fixedName}</if>"
            + "</where>"
            + " GROUP BY u.id"
            + "<if test=\"minCorrect != null and maxCorrect == null\"> HAVING correctAnswers &gt;= #{minCorrect}</if>"
            + "<if test=\"minCorrect == null and maxCorrect != null\"> HAVING correctAnswers &lt;= #{maxCorrect} </if>"
            + "<if test=\"minCorrect != null and maxCorrect != null\"> HAVING correctAnswers &gt;= #{minCorrect} AND correctAnswers &lt;= #{maxCorrect}</if>"
            + " ORDER BY powerPointBonus DESC"
            + "</script>";

    String QUERY_SIGN_IN_PARTICIPATORS = "<script>SELECT u.id AS userCode, u.name AS userName, up.real_name_crt_time AS crtTime, IFNULL(COUNT(umr.id), 0) AS totalSignIn, IFNULL(SUM(umr.bonus), 0) AS powerPointBonus"
            + " FROM user u"
            + " INNER JOIN user_property up ON up.user_id = u.id"
            + " INNER JOIN user_mining_record umr ON umr.user_id = u.id AND umr.action = 3 AND umr.bonus_type = 2"
            + "<where>"
            + "<if test=\"id != null\"> AND u.id = #{id}</if>"
            + "<if test=\"name != null and name != ''\"><bind name=\"fixedName\" value=\"'%' + name + '%'\" /> AND u.name LIKE #{fixedName}</if>"
            + "</where>"
            + " GROUP BY u.id"
            + " ORDER BY powerPointBonus DESC"
            + "</script>";

    String QUERY_INVITATION_PARTICIPATORS = "<script>SELECT"
            + " u.id AS userCode, u.name AS userName,"
            + " IFNULL(COUNT(umr.id), 0) AS invitingNumber,"
            + " IFNULL(SUM(umr.bonus), 0) AS totalBonus,"
            + " IFNULL((SELECT SUM(r.bonus) FROM user_mining_record r WHERE r.inviter_id = u.id AND r.bonus_type = 1), 0) AS inviteeTotalBonus"
            + " FROM user u"
            + " INNER JOIN user_mining_record umr ON umr.user_id = u.id AND umr.action = 5 AND umr.bonus_type = 1"
            + "<where>"
            + "<if test=\"id != null\"> AND u.id = #{id}</if>"
            + "<if test=\"name != null and name != ''\"><bind name=\"fixedName\" value=\"'%' + name + '%'\" /> AND u.name LIKE #{fixedName}</if>"
            + "</where>"
            + " GROUP BY u.id"
            + " ORDER BY invitingNumber DESC"
            + "</script>";

    String SUM_BONUS = "<script>SELECT IFNULL(SUM(bonus), 0) FROM user_mining_record"
            + "<where>"
            + "<if test=\"bonusType != null\"> AND bonus_type = #{bonusType}</if>"
            + "<if test=\"actions != null\"> AND action IN <foreach collection='actions' item='act' open='(' close=')' separator=','> #{act} </foreach></if>"
            + "</where>"
            + "</script>";

    @Select(QUERY_QUESTION_PARTICIPATORS)
    List<QueryQuestionParticipatorResponse.Item> queryQuestionParticipators(QueryQuestionParticipatorRequest condition);

    @Select(QUERY_SIGN_IN_PARTICIPATORS)
    List<QuerySignInParticipatorResponse.Item> querySignInParticipators(QuerySignInParticipatorRequest condition);

    @Select(QUERY_INVITATION_PARTICIPATORS)
    List<QueryInvitationParticipatorResponse.Item> queryInvitationParticipators(QueryInvitationParticipatorRequest condition);

    @Select(SUM_BONUS)
    BigDecimal sumBonus(@Param("bonusType") Integer bonusType, @Param("actions") List<Integer> actions);

}
