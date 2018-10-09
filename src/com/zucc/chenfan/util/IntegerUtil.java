package com.zucc.chenfan.util;

/**   
*    
* 项目名称：PetServiceManagementSystem   
* 类名称：IntegerUtil   
* 类描述：   实现了判断字符串是否为纯正整数的功能
* 创建人：Administrator   
* 创建时间：2018年9月4日 下午5:51:50   
* 修改人：Administrator   
* 修改时间：2018年9月4日 下午5:51:50   
* 修改备注：   
* @version    
*    
*/
public class IntegerUtil {

	public static boolean isNumeric(String str) {
		for (int i = 0; i < str.length(); i++) {
			if (!Character.isDigit(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}
}
