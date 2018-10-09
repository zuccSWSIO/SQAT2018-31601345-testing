package com.zucc.chenfan.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.zucc.chenfan.model.Goods;
import com.zucc.chenfan.model.Goods_type;
import com.zucc.chenfan.model.Goods;
import com.zucc.chenfan.util.StringUtil;

/**   
*    
* ��Ŀ���ƣ�PetServiceManagementSystem   
* �����ƣ�GoodsDao   
* ��������   Goods model������ݿ��װ
* �����ˣ�Administrator   
* ����ʱ�䣺2018��9��10�� ����9:33:10   
* �޸��ˣ�Administrator   
* �޸�ʱ�䣺2018��9��10�� ����9:33:10   
* �޸ı�ע��   
* @version    
*    
*/
public class GoodsDao {

	public int addGoods(Connection con, Goods goods) throws SQLException {
		int result = 0;
		
		String sql = "insert into goods values(null,?,?,?,?,?,?,?,NOW(),NOW())";
		PreparedStatement pstmt = con.prepareStatement(sql);		
		pstmt.setString(1, goods.getGoods_name());
		pstmt.setInt(2, goods.getGoods_type_id());
		pstmt.setString(3, goods.getGoods_brand());
		pstmt.setFloat(4, goods.getGoods_unitprice());
		pstmt.setString(5, goods.getGoods_barcode());
		pstmt.setString(6, goods.getGoods_detail());
		pstmt.setBoolean(7, goods.isGoods_state());
		
		
		result = pstmt.executeUpdate();
		return result;
	}
	
	/*�ж������ݿ����Ƿ�����ͬ��Ʒ��barcode�����ݣ�trueΪ���ظ�*/
	public boolean isEqual(Connection con, Goods goods) throws SQLException {
		boolean result = false;
		String sql = "select * from goods where goods_barcode = ?";
		PreparedStatement pstmt = con.prepareStatement(sql);
		pstmt.setString(1, goods.getGoods_barcode());
		ResultSet rs = pstmt.executeQuery();
		if(rs.next()) {
			result = true;
			if(rs.getInt("goods_id") == goods.getGoods_id()) {
				result = false;
			}
		}
		
		return result;
	}
	
	/*��������*/
	public ResultSet goodsList(Connection con, Goods goods, boolean hasState, int[] typeIdSet) throws SQLException {
		ResultSet result = null;
		
		StringBuffer sbStart = new StringBuffer("select * from goods");
		StringBuffer sbEnd = new StringBuffer("");
		
		/*ע�⡰or��ǰ������пո�*/
		if(goods.getGoods_id() != -1) {//ͨ��hasId�����ж��Ƿ����id��ѯ
			sbEnd.append(" or cast( goods_id as char) like '%" + goods.getGoods_id() + "%'");
		}
		/*��ʼ��goods_id��0*/
		if(!StringUtil.isEmpty(goods.getGoods_name())) {//ͨ���п��ж��Ƿ����name��ѯ
			sbEnd.append(" or goods_name like '%" + goods.getGoods_name() + "%'");
		}
		if(!StringUtil.isEmpty(goods.getGoods_detail())) {//ͨ���п��ж��Ƿ����detail��ѯ
			sbEnd.append(" or goods_detail like '%" + goods.getGoods_detail() + "%'");
		}
		if(!StringUtil.isEmpty(goods.getGoods_barcode())) {
			sbEnd.append(" or goods_barcode like '%" + goods.getGoods_barcode() + "%'");
		}
		if(!StringUtil.isEmpty(goods.getGoods_brand())) {
			sbEnd.append(" or goods_brand like '%" + goods.getGoods_brand() + "%'");
		}
		if(goods.getGoods_type_id() != -1) {
			sbEnd.append(" or goods_brand = " + goods.getGoods_type_id());
		}
		
		/*��֤�������ľ���
		select convert(4545.1366,decimal(10,2));
		����SELECT CAST('4545.1366' AS DECIMAL(10,2));*/
		if(goods.getGoods_unitprice() > 0 ) {
			sbEnd.append(" or goods_unitprice = convert(" + goods.getGoods_unitprice() + ",DECIMAL(10,2))");
		}
		if(hasState) {
			sbEnd.append(" or goods_state = " + goods.isGoods_state());
		}
		if(typeIdSet != null) {
			for(int i=0; i<typeIdSet.length; i++) {
				sbEnd.append(" or goods_type_id = " + typeIdSet[i]);
			}
		}
		
		StringBuffer temp = sbStart.append(sbEnd.toString().replaceFirst("or", "where"));
		PreparedStatement pstmt = con.prepareStatement(temp.toString());
		result = pstmt.executeQuery();
		return result;
	}
	
	/*�÷���ʵ��������goods_idɾ����������*/
	public int deleteGoods(Connection con, Goods goods) throws SQLException {
		int result = 0;
		String sql = "delete from goods where goods_id = ?";
		PreparedStatement pstmt = con.prepareStatement(sql);
		pstmt.setInt(1, goods.getGoods_id());
		result = pstmt.executeUpdate();
		
		return result;
	}
	
	/*�÷���ʵ��������goods_id�޸ĵ�������*/
	public int updateGoods(Connection con, Goods goods) throws SQLException {
		int result = 0;
		String sql = "update goods set goods_name = ?, goods_type_id = ?, goods_brand = ?, goods_unitprice = ?, goods_barcode = ?, goods_detail = ?, goods_state = ? where goods_id = ?";
		PreparedStatement pstmt = con.prepareStatement(sql);
		pstmt.setString(1, goods.getGoods_name());
		pstmt.setInt(2, goods.getGoods_type_id());
		pstmt.setString(3, goods.getGoods_brand());
		pstmt.setFloat(4, goods.getGoods_unitprice());
		pstmt.setString(5, goods.getGoods_barcode());
		pstmt.setString(6, goods.getGoods_detail());
		pstmt.setBoolean(7, goods.isGoods_state());
		pstmt.setInt(8, goods.getGoods_id());

		result = pstmt.executeUpdate();
		return result;
	}
	
	//�÷���ʹ��state��name����������������
	public ResultSet findGoods(Connection con, Goods goods) throws SQLException {
		ResultSet result = null;
		String sql = "select * from goods where goods_state = ? and goods_name like '%" + goods.getGoods_name() + "%'";
		PreparedStatement pstmt = con.prepareStatement(sql);
		pstmt.setBoolean(1, goods.isGoods_state());
		result = pstmt.executeQuery();
		return result;
	}
	//�÷���ʹ��state��������
		public ResultSet findAllGoods(Connection con, Goods goods) throws SQLException {
			ResultSet result = null;
			String sql = "select * from goods where goods_state = ?";
			PreparedStatement pstmt = con.prepareStatement(sql);
			pstmt.setBoolean(1, goods.isGoods_state());
			result = pstmt.executeQuery();
			return result;
		}
	
	
	
}
