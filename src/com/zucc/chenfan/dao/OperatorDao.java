package com.zucc.chenfan.dao;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.zucc.chenfan.model.Operator;
import com.zucc.chenfan.util.StringUtil;


/**   
*    
* 项目名称：PetServiceManagementSystem   
* 类名称：OperatorDao   
* 类描述：   操作员登录验证的数据库操作封装
* 创建人：Administrator   
* 创建时间：2018年9月2日 上午10:38:02   
* 修改人：Administrator   
* 修改时间：2018年9月2日 上午10:38:02   
* 修改备注：   
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
		int result = 0;//result==1表示新增操作员成功
		
		String sql = "insert into operator values(null,?,?,?,NOW(),NOW())";
		PreparedStatement pstmt = con.prepareStatement(sql);		
		pstmt.setString(1, operator.getOperator_name());
		pstmt.setString(2, operator.getOperator_group());
		pstmt.setString(3, operator.getPassword());
		
		result = pstmt.executeUpdate();
		
		return result;
	}
	
	/*该方法实现了员工(全局、id、姓名、职位)查询*/
	public ResultSet operatorList(Connection con, Operator operator, boolean hasId) throws SQLException {
		ResultSet result = null;
		
		StringBuffer sbStart = new StringBuffer("select * from operator");
		StringBuffer sbEnd = new StringBuffer("");
		
		/*注意“or”前面必须有空格*/
		if(hasId) {//通过hasId条件判断是否加入id查询
			sbEnd.append(" or cast( operator_id as char) like '%" + operator.getOperator_id() + "%'");
		}
		
		/*不能确定初始的operator_id是为null还是0*/
		if(!StringUtil.isEmpty(operator.getOperator_name())) {//通过判空判断是否加入name查询
			sbEnd.append(" or operator_name like '%" + operator.getOperator_name() + "%'");
		}
		if(!StringUtil.isEmpty(operator.getOperator_group())) {//通过判空判断是否加入group查询
			sbEnd.append(" or operator_group like '%" + operator.getOperator_group() + "%'");
		}
		
		StringBuffer temp = sbStart.append(sbEnd.toString().replaceFirst("or", "where"));
		PreparedStatement pstmt = con.prepareStatement(temp.toString());
		result = pstmt.executeQuery();
		return result;
	}
	
	/*该方法实现了利用operator_id删除单条数据*/
	public int deleteOperator(Connection con, Operator operator) throws SQLException {
		int result = 0;
		String sql = "delete from operator where operator_id = ?";
		PreparedStatement pstmt = con.prepareStatement(sql);
		pstmt.setInt(1, operator.getOperator_id());
		result = pstmt.executeUpdate();
		
		return result;
	}
	
	/*该方法实现了利用operator_id修改单条数据*/
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
