package com.zucc.chenfan.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


/**   
*    
* ��Ŀ���ƣ�PetServiceManagementSystem   
* �����ƣ�DbUtil   
* ��������   jdbc����Сģ��
* �����ˣ�Administrator   
* ����ʱ�䣺2018��9��1�� ����2:28:45   
* �޸��ˣ�Administrator   
* �޸�ʱ�䣺2018��9��1�� ����2:28:45   
* �޸ı�ע��   ����
* @version    
*    
*/
public class DbUtil {
	
	private String dbUrl = "jdbc:mysql://localhost:3333/db_petsystem?useUnicode=true&characterEncoding=utf8";
	private String dbUserName = "root";
	private String dbPassword = "123456";
	private String jdbcName = "com.mysql.jdbc.Driver";
	

	public Connection getCon() throws Exception {
		Class.forName(jdbcName);
		Connection con = DriverManager.getConnection(dbUrl,dbUserName,dbPassword);
		return con;
		
	}
	
	public void closeCon(Connection con) throws SQLException {
		if(con!=null) {
			con.close();
		}
		
	}
	
	/*����*/
	public static void main(String[] args) {
		DbUtil dbUtil = new DbUtil();
		
		try {
			dbUtil.getCon();
			System.out.println("���ݿ����ӳɹ�");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
