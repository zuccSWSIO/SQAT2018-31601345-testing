package com.zucc.chenfan.dao;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.zucc.chenfan.model.Operator;
import com.zucc.chenfan.util.StringUtil;


/**   
*    
* ��Ŀ���ƣ�PetServiceManagementSystem   
* �����ƣ�OperatorDao   
* ��������   ����Ա��¼��֤�����ݿ������װ
* �����ˣ�Administrator   
* ����ʱ�䣺2018��9��2�� ����10:38:02   
* �޸��ˣ�Administrator   
* �޸�ʱ�䣺2018��9��2�� ����10:38:02   
* �޸ı�ע��   
* @version    
*    
*/
public class OperatorDao {

	public Operator login(Connection con, Operator operator) throws SQLException {
		Operator operatorResult = null;
		
		String sql = "select* from operator where operator_id = ? and password = ?";
		PreparedStatement pstmt = con.prepareStatement(sql);
		pstmt.setInt(1, operator.getOperator_id());
		pstmt.setString(2, operator.getPassword());
		ResultSet rs = pstmt.executeQuery();
		if(rs.next()) {
			operatorResult = new Operator();
			operatorResult.setOperator_id(rs.getInt("operator_id"));
			operatorResult.setOperator_group(rs.getString(3));
			operatorResult.setPassword(rs.getString("password"));
		}
		
		return operatorResult; 
	}
	
	public int addOperator(Connection con, Operator operator) throws SQLException {
		int result = 0;//result==1��ʾ��������Ա�ɹ�
		
		String sql = "insert into operator values(null,?,?,?,NOW(),NOW())";
		PreparedStatement pstmt = con.prepareStatement(sql);		
		pstmt.setString(1, operator.getOperator_name());
		pstmt.setString(2, operator.getOperator_group());
		pstmt.setString(3, operator.getPassword());
		
		result = pstmt.executeUpdate();
		
		return result;
	}
	
	/*�÷���ʵ����Ա��(ȫ�֡�id��������ְλ)��ѯ*/
	public ResultSet operatorList(Connection con, Operator operator, boolean hasId) throws SQLException {
		ResultSet result = null;
		
		StringBuffer sbStart = new StringBuffer("select * from operator");
		StringBuffer sbEnd = new StringBuffer("");
		
		/*ע�⡰or��ǰ������пո�*/
		if(hasId) {//ͨ��hasId�����ж��Ƿ����id��ѯ
			sbEnd.append(" or cast( operator_id as char) like '%" + operator.getOperator_id() + "%'");
		}
		
		/*����ȷ����ʼ��operator_id��Ϊnull����0*/
		if(!StringUtil.isEmpty(operator.getOperator_name())) {//ͨ���п��ж��Ƿ����name��ѯ
			sbEnd.append(" or operator_name like '%" + operator.getOperator_name() + "%'");
		}
		if(!StringUtil.isEmpty(operator.getOperator_group())) {//ͨ���п��ж��Ƿ����group��ѯ
			sbEnd.append(" or operator_group like '%" + operator.getOperator_group() + "%'");
		}
		
		StringBuffer temp = sbStart.append(sbEnd.toString().replaceFirst("or", "where"));
		PreparedStatement pstmt = con.prepareStatement(temp.toString());
		result = pstmt.executeQuery();
		return result;
	}
	
	/*�÷���ʵ��������operator_idɾ����������*/
	public int deleteOperator(Connection con, Operator operator) throws SQLException {
		int result = 0;
		String sql = "delete from operator where operator_id = ?";
		PreparedStatement pstmt = con.prepareStatement(sql);
		pstmt.setInt(1, operator.getOperator_id());
		result = pstmt.executeUpdate();
		
		return result;
	}
	
	/*�÷���ʵ��������operator_id�޸ĵ�������*/
	public int updateOperator(Connection con, Operator operator) throws SQLException {
		int result = 0;
		String sql = "update operator set operator_group = ? , password = ? , operator_modifydate = now() where operator_id = ?";
		PreparedStatement pstmt = con.prepareStatement(sql);
		pstmt.setString(1, operator.getOperator_group());
		pstmt.setString(2, operator.getPassword());
		pstmt.setInt(3, operator.getOperator_id());
		result = pstmt.executeUpdate();
		return result;
	}
	
}
