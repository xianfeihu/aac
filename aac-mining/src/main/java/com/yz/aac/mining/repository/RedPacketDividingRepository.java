package com.yz.aac.mining.repository;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.yz.aac.mining.repository.domian.RedPacketDividing;

@Mapper
public interface RedPacketDividingRepository {

	String SAVE_RED_PACKET_DIVIDING = "<script> "
    		+ "INSERT INTO red_packet_dividing(red_packet_id, dividing_amount) "
    		+ "VALUES "
    		+ "<if test=\"dividings != null and dividings.size() > 0\"> "
    		+ "<foreach collection='dividings' item='di' open='' close='' separator=','>"
    		+ "(#{di.redPacketId}, #{di.dividingAmount}) "
    		+ "</foreach>"
    		+ "</if> "
    		+ "</script>";
	
//	String SAVE_RED_PACKET_DIVIDING = "INSERT INTO red_packet_dividing(red_packet_id, dividing_amount)"
//    		+ "VALUES(#{redPacketId}, #{dividingAmount})";
    
	String QUERY_PACKET_DIVIDING_BY_RAND = "select * from red_packet_dividing where red_packet_id = #{redPacketId} and id not in ( "
			+ "select dividing_id from red_packet_dividing_grabbing where red_packet_id = #{redPacketId} "
			+ ") ORDER BY RAND() LIMIT 1";
	
	String QUERY_PACKET_DIVIDING_SURPLUS = "select * from red_packet_dividing where red_packet_id = #{redPacketId} and id not in ( "
			+ "select dividing_id from red_packet_dividing_grabbing where red_packet_id = #{redPacketId} "
			+ ") ";
	
	String QUERY_PACKET_DIVIDING_BY_ID = "select * from red_packet_dividing where id = #{id}";
	
	String QUERY_PACKET_TOTAL_NUM = "select * from red_packet_dividing where red_packet_id = #{redPacketId}";
	
	/**
	 * 随机获取红包金额
	 * @param redPacketId
	 * @param grabberId
	 * @return
	 */
	@Select(QUERY_PACKET_DIVIDING_BY_RAND)
	RedPacketDividing getPacketDividingByRand(@Param("redPacketId") Long redPacketId);
	
	/**
	 * 剩余红包
	 * @param redPacketId
	 * @return
	 */
	@Select(QUERY_PACKET_DIVIDING_SURPLUS)
	List<RedPacketDividing> getPacketDividingSurplus(@Param("redPacketId") Long redPacketId);
	
	@Select(QUERY_PACKET_DIVIDING_BY_ID)
	RedPacketDividing getPacketDividingById(@Param("id") Long id);
	
	@Insert(SAVE_RED_PACKET_DIVIDING)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int saveRedPacketDividing(@Param("dividings") List<RedPacketDividing> dividings);
	
	/**
	 * 红包总份子
	 * @param redPacketId
	 * @return
	 */
	@Select(QUERY_PACKET_TOTAL_NUM)
	List<RedPacketDividing> getPacketTotalNum(@Param("redPacketId") Long redPacketId);
	
}
