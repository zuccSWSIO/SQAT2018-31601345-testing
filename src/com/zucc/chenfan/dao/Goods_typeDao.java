package com.zucc.chenfan.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.zucc.chenfan.model.Goods_type;
import com.zucc.chenfan.model.Operator;
import com.zucc.chenfan.util.StringUtil;

/**   
*    
* 项目名称：PetServiceManagementSystem   
* 类名称：Goods_typeDao   
* 类描述：   对商品类型信息进行增删改数据库连接操作的封装
* 创建人：Administrator   
* 创建时间：2018年9月3日 上午10:43:07   
* 修改人：Administrator   
* 修改时间：2018年9月3日 上午10:43:07   
* 修改备注：   
* @version    
*    
*/
public class Goods_typeDao {
	/*添加记录的数据库操作封装*/
	public int addGoodsType(Connection con, Goods_type goodsType) throws SQLException {
		int result = 0;
		
		String sql = "insert into goods_type values(null,?,?)";
		PreparedStatement pstmt = con.prepareStatement(sql);
		pstmt.setString(1, goodsType.getGoods_type_name());
		pstmt.setString(2, goodsType.getGoods_type_detail());
		result = pstmt.executeUpdate();
		
		return result;
	}
	/*判断在数据库中是否有相同名称的数据，true为有重复*/
	public boolean isEqual(Connection con, Goods_type goodsType) throws SQLException {
		boolean result = false;
		String sql = "select * from goods_type where goods_type_name = ?";
		PreparedStatement pstmt = con.prepareStatement(sql);
		pstmt.setString(1, goodsType.getGoods_type_name());
		ResultSet rs = pstmt.executeQuery();
		if(rs.next()) {
			result = true;
		}
		
		return result;
	}
	
	/*该方法实现了商品类别的(全局、id、名称、详细信息)查询*/
	public ResultSet goodsTypeList(Connection con, Goods_type goodsType, boolean hasId) throws SQLException {
		ResultSet result = null;
		
		StringBuffer sbStart = new StringBuffer("select * from goods_type");
		StringBuffer sbEnd = new StringBuffer("");
		
		/*注意“or”前面必须有空格*/
		if(hasId) {//通过hasId条件判断是否加入id查询
			sbEnd.append(" or cast( goods_type_id as char) like '%" + goodsType.getGoods_type_id() + "%'");
		}
		
		/*初始的goods_type_id是0*/
		if(!StringUtil.isEmpty(goodsType.getGoods_type_name())) {//通过判空判断是否加入name查询
			sbEnd.append(" or goods_type_name like '%" + goodsType.getGoods_type_name() + "%'");
		}
		if(!StringUtil.isEmpty(goodsType.getGoods_type_detail())) {//通过判空判断是否加入detail查询
			sbEnd.append(" or goods_type_detail like '%" + goodsType.getGoods_type_detail() + "%'");
		}
		
		StringBuffer temp = sbStart.append(sbEnd.toString().replaceFirst("or", "where"));
		PreparedStatement pstmt = con.prepareStatement(temp.toString());
		result = pstmt.executeQuery();
		return result;
	}
	
	/*该方法实现了利用goods_type_id修改单条数据*/
	public int updateGoodsType(Connection con, Goods_type goodsType) throws SQLException {
		int result = 0;
		String sql = "update goods_type set goods_type_name = ? , goods_type_detail = ? where goods_type_id = ?";
		PreparedStatement pstmt = con.prepareStatement(sql);
		pstmt.setString(1, goodsType.getGoods_type_name());
		pstmt.setString(2, goodsType.getGoods_type_detail());
		pstmt.setInt(3, goodsType.getGoods_type_id());
		result = pstmt.executeUpdate();
		return result;
	}
	
	/*该方法实现了利用goods_type_id删除单条数据*/
	public int deleteGoodsType(Connection con, Goods_type goodsType) throws SQLException {
		int result = 0;
		String sql = "delete from goods_type where goods_type_id = ?";
		PreparedStatement pstmt = con.prepareStatement(sql);
		pstmt.setInt(1, goodsType.getGoods_type_id());
		result = pstmt.executeUpdate();
		
		return result;
	}

}
