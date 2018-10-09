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
* 项目名称：PetServiceManagementSystem   
* 类名称：CustomerDao   
* 类描述：   
* 创建人：Administrator   
* 创建时间：2018年9月13日 上午10:06:10   
* 修改人：Administrator   
* 修改时间：2018年9月13日 上午10:06:10   
* 修改备注：   
* @version    
*    
*/
public class CustomerDao {

	/*添加记录的数据库操作封装*/
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
	/*判断在数据库中是否有相同联系电话的数据，true为有重复*/
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
	
	/*该方法实现了顾客的全局查询*/
	public ResultSet customerList(Connection con, Customer customer) throws SQLException {
		ResultSet result = null;
		
		StringBuffer sbStart = new StringBuffer("select * from customer");
		StringBuffer sbEnd = new StringBuffer("");
		
		/*注意“or”前面必须有空格*//*初始的customer_id是0*/
		if(customer.getCustomer_id() != -1) {//通过值是否为-1判断是否加入id查询
			sbEnd.append(" or cast( customer_id as char) like '%" + customer.getCustomer_id() + "%'");
		}
		if(customer.getCustomer_phone() != -1) {//通过值是否为-1判断是否加入联系电话查询
			sbEnd.append(" or cast( customer_phone as char) like '%" + customer.getCustomer_phone() + "%'");
		}
		if(!StringUtil.isEmpty(customer.getCustomer_name())) {//通过判空判断是否加入name查询
			sbEnd.append(" or customer_name like '%" + customer.getCustomer_name() + "%'");
		}
		if(!StringUtil.isEmpty(customer.getCustomer_email())) {//通过判空判断是否加入name查询
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
	
	/*该方法实现了利用customer_id修改单条数据*/
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
