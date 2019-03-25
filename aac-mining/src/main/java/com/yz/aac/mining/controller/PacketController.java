package com.yz.aac.mining.controller;

import com.yz.aac.common.Constants.ResponseMessageInfo;
import com.yz.aac.common.controller.BaseController;
import com.yz.aac.common.exception.BusinessException;
import com.yz.aac.common.model.request.LoginInfo;
import com.yz.aac.common.model.response.RootResponse;
import com.yz.aac.common.util.ImageUtil;
import com.yz.aac.mining.aspect.targetCustom.PageControllerAspect;
import com.yz.aac.mining.model.PageResult;
import com.yz.aac.mining.model.request.*;
import com.yz.aac.mining.model.response.*;
import com.yz.aac.mining.service.PacketService;
import com.yz.aac.mining.util.RegularUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.yz.aac.common.Constants.Misc.PLACEHOLDER;
import static com.yz.aac.common.Constants.ResponseMessageInfo.MSG_INVALID_IMAGE;
import static com.yz.aac.common.model.response.RootResponse.buildSuccess;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@RestController
@RequestMapping(value = "/packet")
@Slf4j
@Api(tags = "挖矿-红信")
public class PacketController extends BaseController {

    @Autowired
    private PacketService packetService;
    
    @ApiOperation(value = "能量详情")
    @GetMapping("/getEnergyInfo")
    public RootResponse<EnergyInfoResponse> getEnergyInfo() throws Exception {
        LoginInfo userInfo = getLoginInfo();
		Long userId = userInfo.getId();
		
        return buildSuccess(packetService.getEnergyInfo(userId));
    }
    
    @ApiOperation(value = "红包首页")
    @GetMapping("/indexPage")
    public RootResponse<PackeIndexResponse> indexPage(
    		@ApiParam(name = "lng", value = "经度", required = true) @RequestParam(value = "lng", required = true) BigDecimal lng,
    		@ApiParam(name = "lat", value = "纬度", required = true) @RequestParam(value = "lat", required = true) BigDecimal lat
    		) throws Exception {
    	validateRequired("经度", lng);
    	validateRequired("纬度", lat);
    	
        LoginInfo userInfo = getLoginInfo();
		Long userId = userInfo.getId();
		
        return buildSuccess(packetService.getPacketMapDate(lng, lat, userId));
    }

    @ApiOperation(value = "发布红信")
    @PutMapping(consumes = MULTIPART_FORM_DATA_VALUE, value = "/issuancePacket")
    public RootResponse<PacketInfoResponse> issuancePacket(
    		 PacketRequest packetRequest,
    		 @ApiParam(name = "imgs", value = "图标", required = false) @RequestParam(value = "imgs", required = false) MultipartFile[] imgs
    		) throws Exception {
        validateRequired("红包个数", packetRequest.getDividingNumber());
        validateIntRange("红包个数", packetRequest.getDividingNumber(), 1, 10000);
        validateRequired("红包总金额", packetRequest.getDividingAmount());
        validateRequired("经度", packetRequest.getLng());
        validateRequired("纬度", packetRequest.getLat());
        validateRequired("公里范围", packetRequest.getRadius());
        validateIntRange("公里范围", packetRequest.getRadius(), 1, 5000);

        if (BigDecimal.valueOf(1).compareTo(packetRequest.getDividingAmount()) == 1 ) {
            throw new BusinessException(ResponseMessageInfo.MSG_CUSTOMIZED_EXCEPTION.code(), "红包金額不能小于1！");
        }

        if (!"".equals(packetRequest.getLinkTitle()) && null != packetRequest.getLinkTitle()) {
        	validateStringLength("链接标题", packetRequest.getLinkTitle(), 1, 10);
        }
        
        if (!RegularUtil.checkItude(packetRequest.getLng().toString(),  packetRequest.getLat().toString())) {
        	throw new BusinessException(ResponseMessageInfo.MSG_CUSTOMIZED_EXCEPTION.code(), "经纬度不合法,请选择国内范围小数点后六位经纬度！");
        }
        
        if ((null == imgs || imgs.length == 0) && (null == packetRequest.getImgIdList() || packetRequest.getImgIdList().size() == 0)) {
        	throw new BusinessException(ResponseMessageInfo.MSG_CUSTOMIZED_EXCEPTION.code(), "至少上传一张图片！");
        }
        
        List<UploadRequest> uploadRequestList = new ArrayList<UploadRequest>();
        for (MultipartFile img : imgs) {
        	 if (!ImageUtil.isImage(img.getInputStream())) {
             	throw new BusinessException(MSG_INVALID_IMAGE.code(), MSG_INVALID_IMAGE.message().replaceFirst(PLACEHOLDER.value(), "imgs"));
             }
        	 uploadRequestList.add(UploadRequest.createOrNull(img));
        }
        
        LoginInfo userInfo = getLoginInfo();
		Long userId = userInfo.getId();
		
        return buildSuccess(packetService.savePacket(packetRequest, uploadRequestList, userId));
    }
    
    @ApiOperation(value = "拆红包")
    @GetMapping("/openPacket")
    public RootResponse<PacketMesResponse> openPacket(
    		@ApiParam(name = "packetId", value = "红包ID", required = true) @RequestParam(value = "packetId", required = true) Long packetId
    		) throws Exception {
        validateRequired("红包ID", packetId);
        
        LoginInfo userInfo = getLoginInfo();
		Long userId = userInfo.getId();
		
        return buildSuccess(packetService.openPacket(packetId, userId));
    }
    
    @ApiOperation(value = "红包点赞")
    @PostMapping("/thumbUp")
    public RootResponse<Integer> thumbUp(@RequestBody PacketThumbUpRequest packetThumbUpRequest) throws Exception {
        validateRequired("红包ID", packetThumbUpRequest.getPacketId());
        
        LoginInfo userInfo = getLoginInfo();
		Long userId = userInfo.getId();
		
        return buildSuccess(packetService.thumbUp(packetThumbUpRequest, userId));
    }
    
    @ApiOperation(value = "评论/回复")
    @PostMapping("/comment")
    public RootResponse<List<PacketCommentResponse>> comment(@RequestBody PacketCommentRequest packetCommentRequest) throws Exception {
        validateRequired("红包ID", packetCommentRequest.getPacketId());
        validateRequired("内容", packetCommentRequest.getDescription());

        validateStringLength("内容", packetCommentRequest.getDescription(), 1, 125);

        LoginInfo userInfo = getLoginInfo();
		Long userId = userInfo.getId();
		
        return buildSuccess(packetService.comment(packetCommentRequest, userId));
    }

    @ApiOperation(value = "链接点击")
    @PostMapping("/linkClick")
    public RootResponse<Boolean> linkClick(@RequestBody PacketLinkClickRequest packetLinkClickRequest) throws Exception {
        validateRequired("红包ID", packetLinkClickRequest.getPacketId());

        LoginInfo userInfo = getLoginInfo();
        Long userId = userInfo.getId();
        return buildSuccess(packetService.linkClick(packetLinkClickRequest, userId));
    }
    
    @ApiOperation(value = "红包发布历史记录")
    @GetMapping("/getHistoricalRecordList")
    @PageControllerAspect
    public RootResponse<PageResult<PacketHistoryResponse>> getHistoricalRecordList(
    		@ApiParam(name = "pageNo", value = "当前页码", required = true) @RequestParam(value = "pageNo", required = true) Integer pageNo,
    		@ApiParam(name = "pageSize", value = "每页条数", required = true) @RequestParam(value = "pageSize", required = true) Integer pageSize
    		) throws Exception {
    	this.validateRequired("当前页码", pageNo);
		this.validateRequired("每页条数", pageSize);
        
        LoginInfo userInfo = getLoginInfo();
		Long userId = userInfo.getId();
		
        return buildSuccess(packetService.getHistoryList(pageNo, pageSize, userId));
    }
    
    @ApiOperation(value = "红包导入信息")
    @GetMapping("/getPacketImportInfo")
    public RootResponse<PacketImportInfoResponse> getPacketImportInfo(
    		@ApiParam(name = "packetId", value = "红包ID", required = true) @RequestParam(value = "packetId", required = true) Long packetId
    		) throws Exception {
    	this.validateRequired("红包ID", packetId);
        
        LoginInfo userInfo = getLoginInfo();
		Long userId = userInfo.getId();
		
        return buildSuccess(packetService.getPacketImportInfo(packetId, userId));
    }
    
    @ApiOperation(value = "红包详情")
    @GetMapping("/packetInfo")
    public RootResponse<PacketMesResponse> packetInfo(
    		@ApiParam(name = "packetId", value = "红包ID", required = true) @RequestParam(value = "packetId", required = true) Long packetId
    		) throws Exception {
    	this.validateRequired("红包ID", packetId);
        
        LoginInfo userInfo = getLoginInfo();
		Long userId = userInfo.getId();
		
        return buildSuccess(packetService.packetInfo(packetId, userId));
    }
    
    @ApiOperation(value = "抢到的红包")
    @GetMapping("/getReceiveInfo")
    public RootResponse<PacketReceiveInfoResponse> getReceiveInfo() throws Exception {
        
        LoginInfo userInfo = getLoginInfo();
		Long userId = userInfo.getId();
		
        return buildSuccess(packetService.getReceiveInfo(userId));
    }
    
    @ApiOperation("领取红包记录列表")
	@GetMapping("/getReceiveList")
	@PageControllerAspect
	public RootResponse<PageResult<PacketHistoryResponse>> getMiningRecordList(
			@ApiParam(name = "pageNo", value = "当前页码", required = true) @RequestParam(value = "pageNo", required = true) Integer pageNo,
			@ApiParam(name = "pageSize", value = "每页条数", required = true) @RequestParam(value = "pageSize", required = true) Integer pageSize
			) throws Exception{
		this.validateRequired("当前页码", pageNo);
		this.validateRequired("每页条数", pageSize);
		
		LoginInfo userInfo = getLoginInfo();
		
		PageResult<PacketHistoryResponse> pageResult = packetService.getReceiveList(pageNo, pageSize, userInfo.getId());
		return buildSuccess(pageResult);
	}
    
    @ApiOperation(value = "发布的红包")
    @GetMapping("/getIssueInfo")
    public RootResponse<PacketIssueInfoResponse> getIssueInfo() throws Exception {
        
        LoginInfo userInfo = getLoginInfo();
		Long userId = userInfo.getId();
		
        return buildSuccess(packetService.getIssueInfo(userId));
    }
    
    @ApiOperation(value = "效果统计")
    @GetMapping("/effectStatistics")
    public RootResponse<PacketStatisticesInfoResponse> effectStatistics(
    		@ApiParam(name = "packetId", value = "红包ID", required = true) @RequestParam(value = "packetId", required = true) Long packetId
    		) throws Exception {
		
        return buildSuccess(packetService.effectStatistics(packetId));
    }
    
}