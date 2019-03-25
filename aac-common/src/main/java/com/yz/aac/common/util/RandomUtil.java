package com.yz.aac.common.util;

import java.util.Random;
import java.util.UUID;

public class RandomUtil {

    public static String genUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
    }
    
    /** 
     * 获得随机数
     * @param len 长度 
     * @return 
     */  
    public static String createNumber(int len){  
        int random = createRandomInt();  
        return createNumber(random, len);  
    }  
      
    public static String createNumber(int random,int len){  
        Random rd = new Random(random);  
        final int  maxNum = 62;  
        StringBuffer sb = new StringBuffer();  
        int rdGet;//取得随机数  
        char[] str = { '1', '2', '3', '4', '5', '6', '7', '8', '9' };  
          
        int count=0;  
        while(count < len){  
            rdGet = Math.abs(rd.nextInt(maxNum));//生成的数最大为62-1  
            if (rdGet >= 0 && rdGet < str.length) {  
                sb.append(str[rdGet]);  
                count ++;  
            }  
        }  
        return sb.toString();  
    }  
      
    public static int createRandomInt(){  
        //得到0.0到1.0之间的数字，并扩大100000倍  
        double temp = Math.random()*100000;  
        //如果数据等于100000，则减少1  
        if(temp>=100000){  
            temp = 99999;  
        }  
        int tempint = (int)Math.ceil(temp);  
        return tempint;  
    } 
}
