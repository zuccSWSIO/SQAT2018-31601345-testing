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
* ��Ŀ���ƣ�PetServiceManagementSystem   
* �����ƣ�Goods_typeDao   
* ��������   ����Ʒ������Ϣ������ɾ�����ݿ����Ӳ����ķ�װ
* �����ˣ�Administrator   
* ����ʱ�䣺2018��9��3�� ����10:43:07   
* �޸��ˣ�Administrator   
* �޸�ʱ�䣺2018��9��3�� ����10:43:07   
* �޸ı�ע��   
* @version    
*    
*/
public class Goods_typeDao {
	/*��Ӽ�¼�����ݿ������װ*/
	public int addGoodsType(Connection con, Goods_type goodsType) throws SQLException {
		int result = 0;
		
		String sql = "insert into goods_type values(null,?,?)";
		PreparedStatement pstmt = con.prepareStatement(sql);
		pstmt.setString(1, goodsType.getGoods_type_name());
		pstmt.setString(2, goodsType.getGoods_type_detail());
		result = pstmt.executeUpdate();
		
		return result;
	}
	/*�ж������ݿ����Ƿ�����ͬ���Ƶ����ݣ�trueΪ���ظ�*/
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
	
	/*�÷���ʵ������Ʒ����(ȫ�֡�id�����ơ���ϸ��Ϣ)��ѯ*/
	public ResultSet goodsTypeList(Connection con, Goods_type goodsType, boolean hasId) throws SQLException {
		ResultSet result = null;
		
		StringBuffer sbStart = new StringBuffer("select * from goods_type");
		StringBuffer sbEnd = new StringBuffer("");
		
		/*ע�⡰or��ǰ������пո�*/
		if(hasId) {//ͨ��hasId�����ж��Ƿ����id��ѯ
			sbEnd.append(" or cast( goods_type_id as char) like '%" + goodsType.getGoods_type_id() + "%'");
		}
		
		/*��ʼ��goods_type_id��0*/
		if(!StringUtil.isEmpty(goodsType.getGoods_type_name())) {//ͨ���п��ж��Ƿ����name��ѯ
			sbEnd.append(" or goods_type_name like '%" + goodsType.getGoods_type_name() + "%'");
		}
		if(!StringUtil.isEmpty(goodsType.getGoods_type_detail())) {//ͨ���п��ж��Ƿ����detail��ѯ
			sbEnd.append(" or goods_type_detail like '%" + goodsType.getGoods_type_detail() + "%'");
		}
		
		StringBuffer temp = sbStart.append(sbEnd.toString().replaceFirst("or", "where"));
		PreparedStatement pstmt = con.prepareStatement(temp.toString());
		result = pstmt.executeQuery();
		return result;
	}
	
	/*�÷���ʵ��������goods_type_id�޸ĵ�������*/
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
	
	/*�÷���ʵ��������goods_type_idɾ����������*/
	public int deleteGoodsType(Connection con, Goods_type goodsType) throws SQLException {
		int result = 0;
		String sql = "delete from goods_type where goods_type_id = ?";
		PreparedStatement pstmt = con.prepareStatement(sql);
		pstmt.setInt(1, goodsType.getGoods_type_id());
		result = pstmt.executeUpdate();
		
		return result;
	}

}
