package com.yz.aac.mining.repository;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.yz.aac.mining.repository.domian.RedPacketImage;

@Mapper
public interface RedPacketImageRepository {

	String SAVE_RED_PACKET_IMAGE = "INSERT INTO red_packet_image(red_packet_id, image_url, order_number)"
    		+ "VALUES(#{redPacketId}, #{imageUrl}, #{orderNumber})";
	
	String QUERY_RED_PACKET_IMAGE = "SELECT * FROM red_packet_image WHERE red_packet_id = #{redPacketId} ORDER BY order_number";
	
	String QUERY_RED_PACKET_IMAGE_BY_ID = "SELECT * FROM red_packet_image WHERE id = #{id}";
    
	@Insert(SAVE_RED_PACKET_IMAGE)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int saveRedPacketImage(RedPacketImage redPacketImage);
	
	@Select(QUERY_RED_PACKET_IMAGE)
	List<RedPacketImage> getRedPacketImage(@Param("redPacketId") Long redPacketId);
	
	@Select(QUERY_RED_PACKET_IMAGE_BY_ID)
	RedPacketImage getRedPacketImageById(@Param("id") Long id);
}
