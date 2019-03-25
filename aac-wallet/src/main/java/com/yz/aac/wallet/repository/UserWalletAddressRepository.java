package com.yz.aac.wallet.repository;

import com.yz.aac.wallet.repository.domain.UserWalletAddress;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserWalletAddressRepository {

    String INSERT_WALLET_ADDRESS = " INSERT INTO user_wallet_address(user_name,remarks,wallet_address,currency_symbol,user_id) values (#{userName},#{remarks},#{walletAddress},#{currencySymbol},#{userId}) ";

    String DELETE_WALLET_ADDRESS = " delete from user_wallet_address where id = #{id} and user_id = #{userId} ";

    String QUERY_WALLET_ADDRESS = " <script> select id,user_name,remarks,wallet_address,currency_symbol,user_id from user_wallet_address where user_id=#{userId} <if test='currencySymbol!=null and currencySymbol!=\"\"'> and currency_symbol=#{currencySymbol} </if></script>";



    @Insert(INSERT_WALLET_ADDRESS)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int addWalletAddress(UserWalletAddress walletAddress);

    @Delete(DELETE_WALLET_ADDRESS)
    void delWalletAddressById(@Param("id") Integer id,@Param("userId") Long userId);

    @Select(QUERY_WALLET_ADDRESS)
    List<UserWalletAddress> getWalletAddress(@Param("userId") Long userId, @Param("currencySymbol") String currencySymbol);
}
