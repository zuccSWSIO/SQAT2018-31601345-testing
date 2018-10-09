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
* 项目名称：PetServiceManagementSystem   
* 类名称：GoodsDao   
* 类描述：   Goods model类的数据库封装
* 创建人：Administrator   
* 创建时间：2018年9月10日 上午9:33:10   
* 修改人：Administrator   
* 修改时间：2018年9月10日 上午9:33:10   
* 修改备注：   
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
	
	/*判断在数据库中是否有相同商品码barcode的数据，true为有重复*/
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
	
	/*搜索功能*/
	public ResultSet goodsList(Connection con, Goods goods, boolean hasState, int[] typeIdSet) throws SQLException {
		ResultSet result = null;
		
		StringBuffer sbStart = new StringBuffer("select * from goods");
		StringBuffer sbEnd = new StringBuffer("");
		
		/*注意“or”前面必须有空格*/
		if(goods.getGoods_id() != -1) {//通过hasId条件判断是否加入id查询
			sbEnd.append(" or cast( goods_id as char) like '%" + goods.getGoods_id() + "%'");
		}
		/*初始的goods_id是0*/
		if(!StringUtil.isEmpty(goods.getGoods_name())) {//通过判空判断是否加入name查询
			sbEnd.append(" or goods_name like '%" + goods.getGoods_name() + "%'");
		}
		if(!StringUtil.isEmpty(goods.getGoods_detail())) {//通过判空判断是否加入detail查询
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
		
		/*保证浮点数的精度
		select convert(4545.1366,decimal(10,2));
		或者SELECT CAST('4545.1366' AS DECIMAL(10,2));*/
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
	
	/*该方法实现了利用goods_id删除单条数据*/
	public int deleteGoods(Connection con, Goods goods) throws SQLException {
		int result = 0;
		String sql = "delete from goods where goods_id = ?";
		PreparedStatement pstmt = con.prepareStatement(sql);
		pstmt.setInt(1, goods.getGoods_id());
		result = pstmt.executeUpdate();
		
		return result;
	}
	
	/*该方法实现了利用goods_id修改单条数据*/
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
	
	//该方法使用state和name进行两级联合搜索
	public ResultSet findGoods(Connection con, Goods goods) throws SQLException {
		ResultSet result = null;
		String sql = "select * from goods where goods_state = ? and goods_name like '%" + goods.getGoods_name() + "%'";
		PreparedStatement pstmt = con.prepareStatement(sql);
		pstmt.setBoolean(1, goods.isGoods_state());
		result = pstmt.executeQuery();
		return result;
	}
	//该方法使用state进行搜索
		public ResultSet findAllGoods(Connection con, Goods goods) throws SQLException {
			ResultSet result = null;
			String sql = "select * from goods where goods_state = ?";
			PreparedStatement pstmt = con.prepareStatement(sql);
			pstmt.setBoolean(1, goods.isGoods_state());
			result = pstmt.executeQuery();
			return result;
		}
	
	
	
}
