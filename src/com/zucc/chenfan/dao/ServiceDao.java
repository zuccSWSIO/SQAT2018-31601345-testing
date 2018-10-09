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
* ��Ŀ���ƣ�PetServiceManagementSystem   
* �����ƣ�ServiceDao   
* ��������   
* �����ˣ�Administrator   
* ����ʱ�䣺2018��9��10�� ����11:17:29   
* �޸��ˣ�Administrator   
* �޸�ʱ�䣺2018��9��10�� ����11:17:29   
* �޸ı�ע��   
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
	
	/*�ж������ݿ����Ƿ�����ͬ�������Ƶ����ݣ�trueΪ���ظ�*/
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
	
	/*��������*/
	public ResultSet serviceList(Connection con, Service service, boolean hasState, int[] typeIdSet) throws SQLException {
		ResultSet result = null;
		
		StringBuffer sbStart = new StringBuffer("select * from service");
		StringBuffer sbEnd = new StringBuffer("");
		
		/*ע�⡰or��ǰ������пո�*/
		if(service.getService_id() != -1) {//ͨ��hasId�����ж��Ƿ����id��ѯ
			sbEnd.append(" or cast( service_id as char) like '%" + service.getService_id() + "%'");
		}
		/*��ʼ��service_id��0*/
		if(!StringUtil.isEmpty(service.getService_name())) {//ͨ���п��ж��Ƿ����name��ѯ
			sbEnd.append(" or service_name like '%" + service.getService_name() + "%'");
		}
		if(!StringUtil.isEmpty(service.getService_detail())) {//ͨ���п��ж��Ƿ����detail��ѯ
			sbEnd.append(" or service_detail like '%" + service.getService_detail() + "%'");
		}
		if(service.getService_type_id() != -1) {
			sbEnd.append(" or service_brand = " + service.getService_type_id());
		}
		
		/*��֤�������ľ���
		select convert(4545.1366,decimal(10,2));
		����SELECT CAST('4545.1366' AS DECIMAL(10,2));*/
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
	
	/*�÷���ʵ��������service_idɾ����������*/
	public int deleteService(Connection con, Service service) throws SQLException {
		int result = 0;
		String sql = "delete from service where service_id = ?";
		PreparedStatement pstmt = con.prepareStatement(sql);
		pstmt.setInt(1, service.getService_id());
		result = pstmt.executeUpdate();
		
		return result;
	}
	
	/*�÷���ʵ��������service_id�޸ĵ�������*/
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
