package com.zucc.chenfan.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


/**   
*    
* 项目名称：PetServiceManagementSystem   
* 类名称：DbUtil   
* 类描述：   jdbc连接小模块
* 创建人：Administrator   
* 创建时间：2018年9月1日 下午2:28:45   
* 修改人：Administrator   
* 修改时间：2018年9月1日 下午2:28:45   
* 修改备注：   创建
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
	
	/*测试*/
	public static void main(String[] args) {
		DbUtil dbUtil = new DbUtil();
		
		try {
			dbUtil.getCon();
			System.out.println("数据库连接成功");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
