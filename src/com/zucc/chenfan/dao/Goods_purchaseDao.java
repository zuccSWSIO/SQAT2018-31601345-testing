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
* ��Ŀ���ƣ�PetServiceManagementSystem   
* �����ƣ�Goods_purchaseDao   
* ��������   
* �����ˣ�Administrator   
* ����ʱ�䣺2018��9��14�� ����2:39:39   
* �޸��ˣ�Administrator   
* �޸�ʱ�䣺2018��9��14�� ����2:39:39   
* �޸ı�ע��   
* @version    
*    
*/
public class Goods_purchaseDao {

	//���purchase_id
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
	
	//��Ӽ�¼
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
	
	/*�÷���ʵ������Ʒ������ȫ�ֲ�ѯ*/
	public ResultSet goods_purchaseList(Connection con, Goods_purchase goods_purchase) throws SQLException {
		ResultSet result = null;
		
		StringBuffer sbStart = new StringBuffer("select * from goods_purchase");
		StringBuffer sbEnd = new StringBuffer("");
		
		/*ע�⡰or��ǰ������пո�*//*��ʼ��goods_purchase_id��0*/
		if(goods_purchase.getPurchase_id() != -1) {//ͨ��ֵ�Ƿ�Ϊ-1�ж��Ƿ����id��ѯ
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
