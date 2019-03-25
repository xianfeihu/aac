package com.yz.aac.opadmin.repository;

import com.yz.aac.opadmin.repository.domain.PlatformServiceChargeStrategy;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface PlatformServiceChargeStrategyRepository {

    String ALL_FIELDS = "id, name, trade_charge_rate, issuance_deposit, is_default, create_time, update_time";
    String QUERY_CHARGES = "<script>SELECT " + ALL_FIELDS + " FROM platform_service_charge_strategy"
            + "<where>"
            + "<if test=\"id != null\"> AND id = #{id}</if>"
            + "<if test=\"name != null and name != ''\"><bind name=\"fixedName\" value=\"'%' + name + '%'\" /> AND name LIKE #{fixedName}</if>"
            + "<if test=\"accurateName != null and accurateName != ''\"> AND name = #{accurateName}</if>"
            + "<if test=\"isDefault != null\"> AND is_default = #{isDefault}</if>"
            + "</where>"
            + "ORDER BY create_time DESC"
            + "</script>";
    String STORE_CHARGE = "INSERT INTO platform_service_charge_strategy(" + ALL_FIELDS + ") VALUES(#{id}, #{name}, #{tradeChargeRate}, #{issuanceDeposit}, #{isDefault}, #{createTime}, #{updateTime})";
    String UPDATE_CHARGE = "UPDATE platform_service_charge_strategy SET name = #{name}, trade_charge_rate = #{tradeChargeRate}, issuance_deposit = #{issuanceDeposit}, is_default = #{isDefault}, update_time = #{updateTime} WHERE id = #{id}";
    String DELETE_CHARGE = "DELETE FROM platform_service_charge_strategy WHERE id = #{id}";

    @Select(QUERY_CHARGES)
    List<PlatformServiceChargeStrategy> query(PlatformServiceChargeStrategy strategy);

    @Insert(STORE_CHARGE)
    void store(PlatformServiceChargeStrategy strategy);

    @Update(UPDATE_CHARGE)
    void update(PlatformServiceChargeStrategy strategy);

    @Delete(DELETE_CHARGE)
    void delete(Long id);

}
