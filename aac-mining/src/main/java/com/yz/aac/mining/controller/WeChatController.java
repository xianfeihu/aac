package com.yz.aac.mining.controller;

import com.yz.aac.common.controller.BaseController;
import com.yz.aac.mining.service.WeChatService;
import com.yz.aac.mining.util.WeChatSignUtil;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;

@Api(tags = "微信公众号")
@RestController
public class WeChatController extends BaseController {
	
	@Autowired
	private WeChatService weChatService;
	
	@GetMapping("/getCheck")
    public String appointment(String signature,
                                            String timestamp, String nonce, String echostr) {
        // 通过检验 signature 对请求进行校验，若校验成功则原样返回 echostr，表示接入成功，否则接入失败
        if (WeChatSignUtil.checkSignature(signature, timestamp, nonce)) {
          return echostr;
        }
        return null;
    }
	
	 /**
     * 验证url和token
     *
     * @param signature
     *            微信加密签名
     * @param timestamp
     *            时间戮
     * @param nonce
     *            随机数
     * @param echostr
     *            随机字符串
     * @return
     */
    @PostMapping("/getCheck")
    public String appointments(HttpServletRequest request, String signature,
                               String timestamp, String nonce, String echostr,
            HttpServletResponse response) {
        try {
            response.setCharacterEncoding("UTF-8");
            request.setCharacterEncoding("UTF-8");
            InputStream is = request.getInputStream();
            
            return weChatService.PushManageXml(is, request); //实现具体业务逻辑
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
	
}
