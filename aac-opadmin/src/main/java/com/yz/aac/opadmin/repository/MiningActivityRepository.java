package com.yz.aac.opadmin.repository;

import com.yz.aac.opadmin.repository.domain.MiningActivity;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface MiningActivityRepository {

    String ALL_FIELDS = "id, begin_time, end_time, status";
    String STORE = "INSERT INTO mining_activity(" + ALL_FIELDS + ") VALUES(#{id}, #{beginTime}, #{endTime}, #{status})";
      String QUERY = "<script>SELECT " + ALL_FIELDS + " FROM mining_activity"
            + "<where>"
            + "<if test=\"beginTime != null\"> AND begin_time > #{beginTime}</if>"
            + "<if test=\"status != null\"> AND status = #{status}</if>"
            + "</where>"
            + " ORDER BY id"
            + "</script>";
    String DELETE = "<script>DELETE FROM mining_activity"
            + "<where>"
            + "<if test=\"ids != null\"> AND id IN <foreach collection='ids' item='id' open='(' close=')' separator=','> #{id} </foreach></if>"
            + "</where>"
            + "</script>";
    String UPDATE = "UPDATE mining_activity SET status = #{status} WHERE id = #{id}";

    @Insert(STORE)
    @Options(useGeneratedKeys = true)
    void store(MiningActivity activity);

    @Select(QUERY)
    List<MiningActivity> query(MiningActivity activity);

    @Delete(DELETE)
    void delete(MiningActivity activity);

    @Update(UPDATE)
    void update(MiningActivity activity);


}
