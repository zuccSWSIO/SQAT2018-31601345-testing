package com.zucc.chenfan.util;

/**   
*    
* ��Ŀ���ƣ�PetServiceManagementSystem   
* �����ƣ�IntegerUtil   
* ��������   ʵ�����ж��ַ����Ƿ�Ϊ���������Ĺ���
* �����ˣ�Administrator   
* ����ʱ�䣺2018��9��4�� ����5:51:50   
* �޸��ˣ�Administrator   
* �޸�ʱ�䣺2018��9��4�� ����5:51:50   
* �޸ı�ע��   
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
