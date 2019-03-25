package com.yz.aac.mining.util;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.XMLOutputter;

import com.yz.aac.mining.model.WeiObj;

public class WeChatXmlUtil {

	public static WeiObj getWeiXml(InputStream in, WeiObj weiObj) throws JDOMException{
        try {
            SAXBuilder sax = new SAXBuilder();
            Document doc = sax.build(in);
            // 获得文件的根元素
            Element root = doc.getRootElement();
            // 获得根元素的第一级子节点
            List<Element> list = root.getChildren();
            for (int j = 0; j < list.size(); j++) {
                // 获得结点
                Element first = (Element) list.get(j);
                if (first.getName().equals("ToUserName")) {
                    weiObj.setToUserName(first.getValue().trim());
                } else if (first.getName().equals("FromUserName")) {
                    weiObj.setFromUserName(first.getValue().trim());
                } else if (first.getName().equals("MsgType")) {
                    weiObj.setMsgType(first.getValue().trim());
                } else if (first.getName().equals("Content")) {
                    weiObj.setContent(first.getValue().trim());
                } else if (first.getName().equals("Event")) {
                    weiObj.setEvent(first.getValue().trim());
                } else if (first.getName().equals("EventKey")) {
                    weiObj.setEventKey(first.getValue().trim());
                }
            }
        } catch (IOException e) {
            // 异常
            e.printStackTrace();
        }
        return weiObj;
    }
	
	/**
     * 编译文本信息
     *
     * @param toName
     * @param FromName
     * @param content
     * @return
     */
    public static String getBackXMLTypeText(String toName, String fromName,
            String content) {
        String returnStr = "";
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        String times = format.format(new Date());
        Element rootXML = new Element("xml");
        rootXML.addContent(new Element("ToUserName").setText(fromName));
        rootXML.addContent(new Element("FromUserName").setText(toName));
        rootXML.addContent(new Element("CreateTime").setText(times));
        rootXML.addContent(new Element("MsgType").setText("text"));
        rootXML.addContent(new Element("Content").setText(content));
        Document doc = new Document(rootXML);
        XMLOutputter XMLOut = new XMLOutputter();
        returnStr = XMLOut.outputString(doc);
        return returnStr;
    }


}
