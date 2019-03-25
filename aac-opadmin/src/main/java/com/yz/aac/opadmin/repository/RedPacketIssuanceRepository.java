package com.yz.aac.opadmin.repository;

import com.yz.aac.opadmin.model.response.QueryRedPacketDetailResponse;
import com.yz.aac.opadmin.model.response.QueryRedPacketResponse;
import com.yz.aac.opadmin.repository.domain.Operator;
import com.yz.aac.opadmin.repository.domain.RedPacketIssuance;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface RedPacketIssuanceRepository {

    String QUERY_RED_PACKETS = "<script>SELECT i.id, u.name, i.issuance_time, i.location, i.radius, i.dividing_number,"
            + " IFNULL(SUM(d.dividing_amount), 0) AS currencyAmount,"
            + " (SELECT COUNT(id) FROM red_packet_dividing_grabbing g WHERE g.red_packet_id = i.id) AS grabberCount"
            + " FROM red_packet_issuance i"
            + " INNER JOIN user u ON u.id = i.issuer_id"
            + " INNER JOIN red_packet_dividing d ON d.red_packet_id = i.id"
            + "<where>"
            + "<if test=\"name != null and name != ''\"><bind name=\"fixedName\" value=\"'%' + name + '%'\" /> AND u.name LIKE #{fixedName}</if>"
            + "</where>"
            + " GROUP BY d.red_packet_id"
            + " ORDER BY issuance_time DESC"
            + "</script>";

    String QUERY_RED_PACKET = "SELECT i.id, u.name, i.issuance_time, i.location, i.radius, i.dividing_number, i.grabbing_limit, i.description, i.link_title, i.link_url,"
            + " IFNULL(SUM(d.dividing_amount), 0) AS currencyAmount,"
            + " (SELECT COUNT(g.id) FROM red_packet_dividing_grabbing g WHERE g.red_packet_id = i.id) AS grabberCount,"
            + " (SELECT COUNT(act.id) FROM red_packet_interaction act WHERE act.red_packet_id = i.id AND act.parent_id IS NULL AND act.action = 1) AS commentCount,"
            + " (SELECT COUNT(act.id) FROM red_packet_interaction act WHERE act.red_packet_id = i.id AND act.parent_id IS NULL AND act.action = 2) AS likeCount,"
            + " (SELECT COUNT(act.id) FROM red_packet_interaction act WHERE act.red_packet_id = i.id AND act.parent_id IS NULL AND act.action = 3) AS clickLinkCount,"
            + " (SELECT img.image_url FROM red_packet_image img WHERE img.red_packet_id = i.id ORDER BY img.order_number LIMIT 1) AS primaryImageUrl"
            + " FROM red_packet_issuance i"
            + " INNER JOIN user u ON u.id = i.issuer_id"
            + " INNER JOIN red_packet_dividing d ON d.red_packet_id = i.id"
            + " WHERE i.id = #{id}"
            + " GROUP BY d.red_packet_id";

    @Select(QUERY_RED_PACKETS)
    List<QueryRedPacketResponse.Item> query(RedPacketIssuance condition);

    @Select(QUERY_RED_PACKET)
    QueryRedPacketDetailResponse queryDetail(@Param("id") Long id);


}
