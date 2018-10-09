package com.zucc.chenfan.dao;                                                          
                                                                                       
import java.sql.Connection;                                                            
import java.sql.PreparedStatement;                                                     
import java.sql.ResultSet;                                                             
import java.sql.SQLException;

import com.zucc.chenfan.model.Service_type;
import com.zucc.chenfan.model.Service_type;
import com.zucc.chenfan.util.StringUtil;                                              
                                                                                       
/**                                                                                    
*                                                                                      
* ��Ŀ���ƣ�PetServiceManagementSystem                                                      
* �����ƣ�Service_typeDao                                                                    
* ��������   �Է���������Ϣ������ɾ�����ݿ����Ӳ����ķ�װ                                                        
* �����ˣ�Administrator                                                                    
* ����ʱ�䣺2018��9��3�� ����10:43:07                                                            
* �޸��ˣ�Administrator                                                                    
* �޸�ʱ�䣺2018��9��3�� ����10:43:07                                                            
* �޸ı�ע��                                                                                
* @version                                                                             
*                                                                                      
*/                                                                                     
public class Service_typeDao {                                                           
	/*��Ӽ�¼�����ݿ������װ*/                                                                   
	public int addServiceType(Connection con, Service_type serviceType) throws SQLException {
		int result = 0;                                                                
		                                                                               
		String sql = "insert into service_type values(null,?,?)";                        
		PreparedStatement pstmt = con.prepareStatement(sql);                           
		pstmt.setString(1, serviceType.getService_type_name());                            
		pstmt.setString(2, serviceType.getService_type_detail());                          
		result = pstmt.executeUpdate();                                                
		                                                                               
		return result;                                                                 
	}                                                                                  
	/*�ж������ݿ����Ƿ�����ͬ���Ƶ����ݣ�trueΪ���ظ�*/                                                     
	public boolean isEqual(Connection con, Service_type serviceType) throws SQLException { 
		boolean result = false;                                                        
		String sql = "select * from service_type where service_type_name = ?";             
		PreparedStatement pstmt = con.prepareStatement(sql);                           
		pstmt.setString(1, serviceType.getService_type_name());                            
		ResultSet rs = pstmt.executeQuery();                                           
		if(rs.next()) {                                                                
			result = true;                                                             
		}                                                                              
		                                                                               
		return result;                                                                 
	}                                                                                  
	                     
	/*�÷���ʵ���˷�������(ȫ�֡�id�����ơ���ϸ��Ϣ)��ѯ*/
	public ResultSet serviceTypeList(Connection con, Service_type serviceType, boolean hasId) throws SQLException {
		ResultSet result = null;
		
		StringBuffer sbStart = new StringBuffer("select * from service_type");
		StringBuffer sbEnd = new StringBuffer("");
		
		/*ע�⡰or��ǰ������пո�*/
		if(hasId) {//ͨ��hasId�����ж��Ƿ����id��ѯ
			sbEnd.append(" or cast( service_type_id as char) like '%" + serviceType.getService_type_id() + "%'");
		}
		
		/*��ʼ��service_type_id��0*/
		if(!StringUtil.isEmpty(serviceType.getService_type_name())) {//ͨ���п��ж��Ƿ����name��ѯ
			sbEnd.append(" or service_type_name like '%" + serviceType.getService_type_name() + "%'");
		}
		if(!StringUtil.isEmpty(serviceType.getService_type_detail())) {//ͨ���п��ж��Ƿ����detail��ѯ
			sbEnd.append(" or service_type_detail like '%" + serviceType.getService_type_detail() + "%'");
		}
		
		StringBuffer temp = sbStart.append(sbEnd.toString().replaceFirst("or", "where"));
		PreparedStatement pstmt = con.prepareStatement(temp.toString());
		result = pstmt.executeQuery();
		return result;
	}
	
	/*�÷���ʵ��������service_type_id�޸ĵ�������*/
	public int updateServiceType(Connection con, Service_type serviceType) throws SQLException {
		int result = 0;
		String sql = "update service_type set service_type_name = ? , service_type_detail = ? where service_type_id = ?";
		PreparedStatement pstmt = con.prepareStatement(sql);
		pstmt.setString(1, serviceType.getService_type_name());
		pstmt.setString(2, serviceType.getService_type_detail());
		pstmt.setInt(3, serviceType.getService_type_id());
		result = pstmt.executeUpdate();
		return result;
	}
	
	/*�÷���ʵ��������service_type_idɾ����������*/
	public int deleteServiceType(Connection con, Service_type serviceType) throws SQLException {
		int result = 0;
		String sql = "delete from service_type where service_type_id = ?";
		PreparedStatement pstmt = con.prepareStatement(sql);
		pstmt.setInt(1, serviceType.getService_type_id());
		result = pstmt.executeUpdate();
		
		return result;
	}
	
                                                                                       
}                                                                                      
                                                                                       