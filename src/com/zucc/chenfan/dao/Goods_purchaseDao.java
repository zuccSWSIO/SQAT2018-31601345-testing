package com.zucc.chenfan.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.zucc.chenfan.model.Customer;
import com.zucc.chenfan.model.Goods_purchase;
import com.zucc.chenfan.util.StringUtil;

/**   
*    
* 项目名称：PetServiceManagementSystem   
* 类名称：Goods_purchaseDao   
* 类描述：   
* 创建人：Administrator   
* 创建时间：2018年9月14日 上午2:39:39   
* 修改人：Administrator   
* 修改时间：2018年9月14日 上午2:39:39   
* 修改备注：   
* @version    
*    
*/
public class Goods_purchaseDao {

	//获得purchase_id
	public int getPurchaseId(Connection con) throws SQLException {
		int result = 1;
		String sql = "select MAX(purchase_id) from goods_purchase";
		PreparedStatement pstmt = con.prepareStatement(sql);
		ResultSet rs = pstmt.executeQuery();
		if(rs.next()) {
			result = rs.getInt(1) + 1;
		}
		
		return result;
	}
	
	//添加记录
	public int addPurchase(Connection con, Goods_purchase temp) throws SQLException {
		int result = 0;
		
		String sql = "insert into goods_purchase values(?,?,?,?,?,?,?,now(),now())";
		PreparedStatement pstmt = con.prepareStatement(sql);
		pstmt.setInt(1, temp.getPurchase_id());
		pstmt.setInt(2, temp.getGoods_id());
		pstmt.setInt(3, temp.getCustomer_id());
		pstmt.setInt(4, temp.getQuantity());
		pstmt.setFloat(5, temp.getTotalprice());
		pstmt.setString(6, temp.getAddress());
		pstmt.setString(7, temp.getState());
		result = pstmt.executeUpdate();
		
		return result;
	}
	
	/*该方法实现了商品订单的全局查询*/
	public ResultSet goods_purchaseList(Connection con, Goods_purchase goods_purchase) throws SQLException {
		ResultSet result = null;
		
		StringBuffer sbStart = new StringBuffer("select * from goods_purchase");
		StringBuffer sbEnd = new StringBuffer("");
		
		/*注意“or”前面必须有空格*//*初始的goods_purchase_id是0*/
		if(goods_purchase.getPurchase_id() != -1) {//通过值是否为-1判断是否加入id查询
			sbEnd.append(" or purchase_id = " + goods_purchase.getPurchase_id() );
		}
		
		if(!StringUtil.isEmpty(goods_purchase.getState())) {
			sbEnd.append(" or state like '%" + goods_purchase.getState() + "%'");
		}
		
		StringBuffer temp = sbStart.append(sbEnd.toString().replaceFirst("or", "where"));
		PreparedStatement pstmt = con.prepareStatement(temp.toString());
		result = pstmt.executeQuery();
		return result;
	}
	
	
}
