package com.yz.aac.mining.util;


public final class RedPacketUtil {

	public static double[] allocateRedPacket(int n, double money) {
		if (n*1.0/100>money) {    //若每个人不能分至少一分钱，则显示金额太少，结束程序
			return null;
		}
		money = 100*money;
		
		double[] result = new double[n];
		double[] randNumber = new double[n];
		double randSum = 0;   //随机数和
		double allocateSum = money-n;    //按每人一分钱的计划扣出来，剩下的再分配
		for (int i = 0; i < randNumber.length; i++) {
			randNumber[i]=Math.random()*allocateSum;    //生成随机数
			randSum+=randNumber[i];
		}
		double left = allocateSum;
		for (int i = 0; i < result.length-1; i++) {			
			result[i]=Math.round(randNumber[i]/randSum*allocateSum);
			left -= result[i];
			result[i]=(result[i]+1)*1.0/100;       //把一分钱加回去
		}
		result[n-1] = (left+1)*1.0/100;
		
		return result;
	}
	
	/**
	 * 判断是否平均最低0.01
	 * @param n
	 * @param money
	 * @return
	 */
	public static boolean isFloorPrice(int n, double money) {
		if (n*1.0/100>money) {    //若每个人不能分至少一分钱，则显示金额太少，结束程序
			return false;
		}
		
		return true;
	}
}
