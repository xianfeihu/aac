package com.yz.aac.opadmin.repository;

import com.yz.aac.opadmin.model.request.QueryExchangeRecordRequest;
import com.yz.aac.opadmin.model.response.QueryExchangeRecordResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface ExchangeRecordRepository {

    String QUERY = "<script>SELECT r.id, r.user_id, u.name AS userName, r.exchange_id, e.name AS serviceName, r.charging_number, r.rmb_amount, r.platform_amount, r.record_time, r.status"
            + " FROM exchange_record r"
            + " INNER JOIN user u ON r.user_id = u.id"
            + " INNER JOIN exchange e ON r.exchange_id = e.id"
            + "<where>"
            + "<if test=\"id != null\"> AND r.id = #{id}</if>"
            + "<if test=\"exchangeId != null\"> AND r.exchange_id = #{exchangeId}</if>"
            + "<if test=\"userName != null\"><bind name=\"fixedName\" value=\"'%' + userName + '%'\" /> AND u.name LIKE #{fixedName}</if>"
            + "</where>"
            + " ORDER BY r.status, record_time"
            + "</script>";

    String COUNT = "<script>SELECT r.user_id, r.exchange_id, COUNT(r.id) AS exchangedTimesInMonth, (e.limit_in_month - COUNT(r.id)) AS restTimesInMonth"
            + " FROM exchange_record r"
            + " INNER JOIN exchange e ON r.exchange_id = e.id"
            + "<where>"
            + "<if test=\"userIds != null\"> AND r.user_id IN <foreach collection='userIds' item='userId' open='(' close=')' separator=','> #{userId} </foreach></if>"
            + "<if test=\"exchangeIds != null\"> AND r.exchange_id IN <foreach collection='exchangeIds' item='exchangeId' open='(' close=')' separator=','> #{exchangeId} </foreach></if>"
            + "<if test=\"beginTime != null\"> AND r.record_time >= #{beginTime}</if>"
            + "</where>"
            + " GROUP BY r.user_id, r.exchange_id"
            + "</script>";

    String UPDATE = "UPDATE exchange_record SET status = #{status} WHERE id = #{id}";

    @Select(QUERY)
    List<QueryExchangeRecordResponse.Item> query(QueryExchangeRecordRequest condition);

    @Select(COUNT)
    List<QueryExchangeRecordResponse.Item> count(QueryExchangeRecordRequest condition);

    @Update(UPDATE)
    void update(QueryExchangeRecordResponse.Item request);

}
