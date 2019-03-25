package com.yz.aac.wallet.repository;

import com.yz.aac.wallet.repository.domain.UserAddressBook;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserAddressBookRepository {

    String INSERT_WALLET_ADDRESS = " INSERT INTO user_address_book (nike_name,remarks,wallet_address,currency_symbol,user_id) values (#{nikeName},#{remarks},#{walletAddress},#{currencySymbol},#{userId}) ";

    String UPDATE_WALLET_ADDRESS = "UPDATE user_address_book SET nike_name=#{nikeName},remarks=#{remarks},wallet_address=#{walletAddress},currency_symbol=#{currencySymbol} WHERE id=#{id}";

    String DELETE_WALLET_ADDRESS = " delete from user_address_book where id = #{id} and user_id = #{userId} ";

    String QUERY_WALLET_ADDRESS = " <script> select id,nike_name,remarks,wallet_address,currency_symbol,user_id from user_address_book where user_id=#{userId} <if test='currencySymbol!=null and currencySymbol!=\"\"'> and currency_symbol=#{currencySymbol} </if></script>";


    @Insert(INSERT_WALLET_ADDRESS)
    @Options(useGeneratedKeys = true)
    Long addWalletAddress(UserAddressBook walletAddress);

    @Update(UPDATE_WALLET_ADDRESS)
    void modifyWalletAddress(UserAddressBook walletAddress);

    @Delete(DELETE_WALLET_ADDRESS)
    void delWalletAddressById(@Param("id") Integer id,@Param("userId") Long userId);

    @Select(QUERY_WALLET_ADDRESS)
    List<UserAddressBook> getWalletAddress(@Param("userId") Long userId, @Param("currencySymbol") String currencySymbol);

}
