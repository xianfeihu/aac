package com.yz.aac.opadmin.repository;

import com.yz.aac.opadmin.repository.domain.IncreaseStrategy;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface AlgorithmRepository {

    String ALL_FIELDS = "id, name, increased_power_point, consumed_ad, platform_currency, is_default, create_time, update_time";
    String QUERY_ALGORITHMS = "<script>SELECT " + ALL_FIELDS + " FROM increase_strategy"
            + "<where>"
            + "<if test=\"id != null\"> AND id = #{id}</if>"
            + "<if test=\"name != null and name != ''\"><bind name=\"fixedName\" value=\"'%' + name + '%'\" /> AND name LIKE #{fixedName}</if>"
            + "<if test=\"accurateName != null and accurateName != ''\"> AND name = #{accurateName}</if>"
            + "<if test=\"isDefault != null\"> AND is_default = #{isDefault}</if>"
            + "</where>"
            + "ORDER BY create_time DESC"
            + "</script>";
    String STORE_ALGORITHM = "INSERT INTO increase_strategy(" + ALL_FIELDS + ") VALUES(#{id}, #{name}, #{increasedPowerPoint}, #{consumedAd}, #{platformCurrency}, #{isDefault}, #{createTime}, #{updateTime})";
    String UPDATE_ALGORITHM = "UPDATE increase_strategy SET name = #{name}, increased_power_point = #{increasedPowerPoint}, consumed_ad = #{consumedAd}, platform_currency = #{platformCurrency}, is_default = #{isDefault}, update_time = #{updateTime} WHERE id = #{id}";
    String DELETE_ALGORITHM = "DELETE FROM increase_strategy WHERE id = #{id}";

    @Select(QUERY_ALGORITHMS)
    List<IncreaseStrategy> query(IncreaseStrategy strategy);

    @Insert(STORE_ALGORITHM)
    void store(IncreaseStrategy strategy);

    @Update(UPDATE_ALGORITHM)
    void update(IncreaseStrategy strategy);

    @Delete(DELETE_ALGORITHM)
    void delete(Long id);

}
