package com.zucc.chenfan.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.zucc.chenfan.model.Customer;
import com.zucc.chenfan.model.Pet;
import com.zucc.chenfan.util.StringUtil;

/**   
*    
* 项目名称：PetServiceManagementSystem   
* 类名称：PetDao   
* 类描述：   
* 创建人：Administrator   
* 创建时间：2018年9月13日 下午4:11:09   
* 修改人：Administrator   
* 修改时间：2018年9月13日 下午4:11:09   
* 修改备注：   
* @version    
*    
*/
public class PetDao {

	/*添加记录的数据库操作封装*/
	public int addPet(Connection con, Pet pet) throws SQLException {
		int result = 0;
		
		String sql = "insert into pet values(null,?,?,?,?,now(),now())";
		PreparedStatement pstmt = con.prepareStatement(sql);
		pstmt.setInt(1, pet.getCustomer_id());
		pstmt.setInt(2, pet.getPet_species_id());
		pstmt.setString(3, pet.getPet_name());
		pstmt.setBlob(4, pet.getPet_image());
		result = pstmt.executeUpdate();
		
		return result;
	}
	/*判断在数据库中相同的主人是否有相同昵称的宠物，true为有重复*/
	public boolean isEqual(Connection con, Pet pet) throws SQLException {
		//pet中需要包含customer_id和pet_name
		boolean result = false;
		String sql = "select pet_name,pet_id from pet where customer_id = ?";
		PreparedStatement pstmt = con.prepareStatement(sql);
		pstmt.setInt(1, pet.getCustomer_id());
		ResultSet rs = pstmt.executeQuery();
		while(rs.next()) {
			if(rs.getString("pet_name").equals(pet.getPet_name())) {
				result = true;
				if(pet.getPet_id() == rs.getInt("pet_id")) {
					result = false;
				}
				break;
			}
		}
		
		return result;
	}
	
	/*该方法实现了宠物的全局查询*/
	public ResultSet petList(Connection con, Pet pet, int[] customerId, int[] petSpeciesId) throws SQLException {
		ResultSet result = null;
		
		StringBuffer sbStart = new StringBuffer("select * from pet");
		StringBuffer sbEnd = new StringBuffer("");
		
		/*注意“or”前面必须有空格*//*初始的pet_id是0*/
		if(pet.getPet_id() != -1) {//通过值是否为-1判断是否加入宠物id查询
			sbEnd.append(" or cast( pet_id as char) like '%" + pet.getPet_id() + "%'");
		}
		if(pet.getCustomer_id() != -1) {//通过值是否为-1判断是否加入顾客id查询
			sbEnd.append(" or customer_id = " + pet.getCustomer_id() );
		}
		else {
			if(customerId != null) {//当查询多个customer_id时走的路径
				for(int i=0; i<customerId.length; i++) {
					sbEnd.append(" or customer_id = " + customerId[i] );
				}
			}
		}
		if(pet.getPet_species_id() != -1) {//通过是否为-1判断是否加入宠物品种id查询
			sbEnd.append(" or pet_species_id = " + pet.getPet_species_id() );
		}
		else {
			if(petSpeciesId != null) {//当查询多个pet_species_id时走的路径
				for(int i=0; i<petSpeciesId.length; i++) {
					sbEnd.append(" or pet_species_id = " + petSpeciesId[i] );
				}
			}
		}
		if(!StringUtil.isEmpty(pet.getPet_name())) {//通过判空判断是否加入name查询
			sbEnd.append(" or pet_name like '%" + pet.getPet_name() + "%'");
		}
		
		StringBuffer temp = sbStart.append(sbEnd.toString().replaceFirst("or", "where"));
		PreparedStatement pstmt = con.prepareStatement(temp.toString());
		result = pstmt.executeQuery();
		return result;
	}
	
	/*该方法实现了利用pet_id修改单条数据*/
	public int updatePet(Connection con, Pet pet) throws SQLException {
		int result = 0;
		
		StringBuffer StrStart = new StringBuffer("update pet ");
		StringBuffer StrEnd = new StringBuffer("");
		if(!StringUtil.isEmpty(pet.getPet_name())) {
			StrEnd.append(" , pet_name = '" + pet.getPet_name() + "'");
		}
		if(pet.getPet_image() != null) {
			StrEnd.append(" , pet_image = ? ");
		}
		StrEnd.append(" , pet_modifydate = now() where pet_id = " + pet.getPet_id());
		
		String sql = (StrStart.append(StrEnd.toString().replaceFirst(",", "set"))).toString();
		PreparedStatement pstmt = con.prepareStatement(sql);
		if(sql.contains("?")) {
			pstmt.setBlob(1, pet.getPet_image());
		}
		result = pstmt.executeUpdate();
		return result;
	}
	
}
