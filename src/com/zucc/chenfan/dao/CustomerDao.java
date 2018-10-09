package com.zucc.chenfan.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.zucc.chenfan.model.Customer;
import com.zucc.chenfan.model.Goods_type;
import com.zucc.chenfan.model.Customer;
import com.zucc.chenfan.util.StringUtil;

/**   
*    
* ��Ŀ���ƣ�PetServiceManagementSystem   
* �����ƣ�CustomerDao   
* ��������   
* �����ˣ�Administrator   
* ����ʱ�䣺2018��9��13�� ����10:06:10   
* �޸��ˣ�Administrator   
* �޸�ʱ�䣺2018��9��13�� ����10:06:10   
* �޸ı�ע��   
* @version    
*    
*/
public class CustomerDao {

	/*��Ӽ�¼�����ݿ������װ*/
	public int addCustomer(Connection con, Customer customer) throws SQLException {
		int result = 0;
		
		String sql = "insert into customer values(null,?,?,?,?,now(),now())";
		PreparedStatement pstmt = con.prepareStatement(sql);
		pstmt.setString(1, customer.getCustomer_name());
		pstmt.setLong(2, customer.getCustomer_phone());
		pstmt.setString(3, customer.getCustomer_email());
		pstmt.setString(4, customer.getCustomer_other_contact());
		result = pstmt.executeUpdate();
		
		return result;
	}
	/*�ж������ݿ����Ƿ�����ͬ��ϵ�绰�����ݣ�trueΪ���ظ�*/
	public boolean isEqual(Connection con, Customer customer) throws SQLException {
		boolean result = false;
		String sql = "select * from customer where customer_phone = ?";
		PreparedStatement pstmt = con.prepareStatement(sql);
		pstmt.setLong(1, customer.getCustomer_phone());
		ResultSet rs = pstmt.executeQuery();
		if(rs.next()) {
			result = true;
			if(rs.getInt("customer_id") == customer.getCustomer_id()) {
				result = false;
			}
		}
		
		return result;
	}
	
	/*�÷���ʵ���˹˿͵�ȫ�ֲ�ѯ*/
	public ResultSet customerList(Connection con, Customer customer) throws SQLException {
		ResultSet result = null;
		
		StringBuffer sbStart = new StringBuffer("select * from customer");
		StringBuffer sbEnd = new StringBuffer("");
		
		/*ע�⡰or��ǰ������пո�*//*��ʼ��customer_id��0*/
		if(customer.getCustomer_id() != -1) {//ͨ��ֵ�Ƿ�Ϊ-1�ж��Ƿ����id��ѯ
			sbEnd.append(" or cast( customer_id as char) like '%" + customer.getCustomer_id() + "%'");
		}
		if(customer.getCustomer_phone() != -1) {//ͨ��ֵ�Ƿ�Ϊ-1�ж��Ƿ������ϵ�绰��ѯ
			sbEnd.append(" or cast( customer_phone as char) like '%" + customer.getCustomer_phone() + "%'");
		}
		if(!StringUtil.isEmpty(customer.getCustomer_name())) {//ͨ���п��ж��Ƿ����name��ѯ
			sbEnd.append(" or customer_name like '%" + customer.getCustomer_name() + "%'");
		}
		if(!StringUtil.isEmpty(customer.getCustomer_email())) {//ͨ���п��ж��Ƿ����name��ѯ
			sbEnd.append(" or customer_email like '%" + customer.getCustomer_email() + "%'");
		}
		if(!StringUtil.isEmpty(customer.getCustomer_other_contact())) {
			sbEnd.append(" or customer_other_contact like '%" + customer.getCustomer_other_contact() + "%'");
		}
		
		StringBuffer temp = sbStart.append(sbEnd.toString().replaceFirst("or", "where"));
		PreparedStatement pstmt = con.prepareStatement(temp.toString());
		result = pstmt.executeQuery();
		return result;
	}
	
	/*�÷���ʵ��������customer_id�޸ĵ�������*/
	public int updateCustomer(Connection con, Customer customer) throws SQLException {
		int result = 0;
		String sql = "update customer set customer_name = ?, customer_phone = ?, customer_email = ?, customer_other_contact = ?, customer_modifydate = now() where customer_id = ?";
		PreparedStatement pstmt = con.prepareStatement(sql);
		pstmt.setString(1, customer.getCustomer_name());
		pstmt.setLong(2, customer.getCustomer_phone());
		pstmt.setString(3, customer.getCustomer_email());
		pstmt.setString(4, customer.getCustomer_other_contact());
		pstmt.setInt(5, customer.getCustomer_id());
		result = pstmt.executeUpdate();
		return result;
	}
	
	
}
