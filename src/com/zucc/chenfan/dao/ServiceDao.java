package com.zucc.chenfan.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.zucc.chenfan.model.Goods;
import com.zucc.chenfan.model.Service;
import com.zucc.chenfan.model.Service_type;
import com.zucc.chenfan.util.StringUtil;


/**   
*    
* 项目名称：PetServiceManagementSystem   
* 类名称：ServiceDao   
* 类描述：   
* 创建人：Administrator   
* 创建时间：2018年9月10日 上午11:17:29   
* 修改人：Administrator   
* 修改时间：2018年9月10日 上午11:17:29   
* 修改备注：   
* @version    
*    
*/
public class ServiceDao {

	public int addService(Connection con, Service service) throws SQLException {
		int result = 0;
		
		String sql = "insert into service values(null,?,?,?,?,?,NOW(),NOW())";
		PreparedStatement pstmt = con.prepareStatement(sql);		
		pstmt.setString(1, service.getService_name());
		pstmt.setInt(2, service.getService_type_id());
		pstmt.setFloat(3, service.getService_price());
		pstmt.setString(4, service.getService_detail());
		pstmt.setBoolean(5, service.isService_state());
		
		result = pstmt.executeUpdate();
		return result;
	}
	
	/*判断在数据库中是否有相同服务名称的数据，true为有重复*/
	public boolean isEqual(Connection con, Service service) throws SQLException {
		boolean result = false;
		String sql = "select * from service where service_name = ?";
		PreparedStatement pstmt = con.prepareStatement(sql);
		pstmt.setString(1, service.getService_name());
		ResultSet rs = pstmt.executeQuery();
		if(rs.next()) {
			result = true;
			if(service.getService_id() == rs.getInt("service_id")) {
				result = false;
			}
		}
		
		return result;
	}
	
	/*搜索功能*/
	public ResultSet serviceList(Connection con, Service service, boolean hasState, int[] typeIdSet) throws SQLException {
		ResultSet result = null;
		
		StringBuffer sbStart = new StringBuffer("select * from service");
		StringBuffer sbEnd = new StringBuffer("");
		
		/*注意“or”前面必须有空格*/
		if(service.getService_id() != -1) {//通过hasId条件判断是否加入id查询
			sbEnd.append(" or cast( service_id as char) like '%" + service.getService_id() + "%'");
		}
		/*初始的service_id是0*/
		if(!StringUtil.isEmpty(service.getService_name())) {//通过判空判断是否加入name查询
			sbEnd.append(" or service_name like '%" + service.getService_name() + "%'");
		}
		if(!StringUtil.isEmpty(service.getService_detail())) {//通过判空判断是否加入detail查询
			sbEnd.append(" or service_detail like '%" + service.getService_detail() + "%'");
		}
		if(service.getService_type_id() != -1) {
			sbEnd.append(" or service_brand = " + service.getService_type_id());
		}
		
		/*保证浮点数的精度
		select convert(4545.1366,decimal(10,2));
		或者SELECT CAST('4545.1366' AS DECIMAL(10,2));*/
		if(service.getService_price() > 0 ) {
			sbEnd.append(" or service_price = convert(" + service.getService_price() + ",DECIMAL(10,2))");
		}
		if(hasState) {
			sbEnd.append(" or service_state = " + service.isService_state());
		}
		if(typeIdSet != null) {
			for(int i=0; i<typeIdSet.length; i++) {
				sbEnd.append(" or service_type_id = " + typeIdSet[i]);
			}
		}
		
		StringBuffer temp = sbStart.append(sbEnd.toString().replaceFirst("or", "where"));
		PreparedStatement pstmt = con.prepareStatement(temp.toString());
		result = pstmt.executeQuery();
		return result;
	}
	
	/*该方法实现了利用service_id删除单条数据*/
	public int deleteService(Connection con, Service service) throws SQLException {
		int result = 0;
		String sql = "delete from service where service_id = ?";
		PreparedStatement pstmt = con.prepareStatement(sql);
		pstmt.setInt(1, service.getService_id());
		result = pstmt.executeUpdate();
		
		return result;
	}
	
	/*该方法实现了利用service_id修改单条数据*/
	public int updateService(Connection con, Service service) throws SQLException {
		int result = 0;
		String sql = "update service set service_name = ?, service_type_id = ?, service_price = ?, service_detail = ?, service_state = ? where service_id = ?";
		PreparedStatement pstmt = con.prepareStatement(sql);
		pstmt.setString(1, service.getService_name());
		pstmt.setInt(2, service.getService_type_id());
		pstmt.setFloat(3, service.getService_price());
		pstmt.setString(4, service.getService_detail());
		pstmt.setBoolean(5, service.isService_state());
		pstmt.setInt(6, service.getService_id());

		result = pstmt.executeUpdate();
		return result;
	}
	
}
