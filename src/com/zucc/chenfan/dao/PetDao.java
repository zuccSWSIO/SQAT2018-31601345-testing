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
* ��Ŀ���ƣ�PetServiceManagementSystem   
* �����ƣ�PetDao   
* ��������   
* �����ˣ�Administrator   
* ����ʱ�䣺2018��9��13�� ����4:11:09   
* �޸��ˣ�Administrator   
* �޸�ʱ�䣺2018��9��13�� ����4:11:09   
* �޸ı�ע��   
* @version    
*    
*/
public class PetDao {

	/*��Ӽ�¼�����ݿ������װ*/
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
	/*�ж������ݿ�����ͬ�������Ƿ�����ͬ�ǳƵĳ��trueΪ���ظ�*/
	public boolean isEqual(Connection con, Pet pet) throws SQLException {
		//pet����Ҫ����customer_id��pet_name
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
	
	/*�÷���ʵ���˳����ȫ�ֲ�ѯ*/
	public ResultSet petList(Connection con, Pet pet, int[] customerId, int[] petSpeciesId) throws SQLException {
		ResultSet result = null;
		
		StringBuffer sbStart = new StringBuffer("select * from pet");
		StringBuffer sbEnd = new StringBuffer("");
		
		/*ע�⡰or��ǰ������пո�*//*��ʼ��pet_id��0*/
		if(pet.getPet_id() != -1) {//ͨ��ֵ�Ƿ�Ϊ-1�ж��Ƿ�������id��ѯ
			sbEnd.append(" or cast( pet_id as char) like '%" + pet.getPet_id() + "%'");
		}
		if(pet.getCustomer_id() != -1) {//ͨ��ֵ�Ƿ�Ϊ-1�ж��Ƿ����˿�id��ѯ
			sbEnd.append(" or customer_id = " + pet.getCustomer_id() );
		}
		else {
			if(customerId != null) {//����ѯ���customer_idʱ�ߵ�·��
				for(int i=0; i<customerId.length; i++) {
					sbEnd.append(" or customer_id = " + customerId[i] );
				}
			}
		}
		if(pet.getPet_species_id() != -1) {//ͨ���Ƿ�Ϊ-1�ж��Ƿ�������Ʒ��id��ѯ
			sbEnd.append(" or pet_species_id = " + pet.getPet_species_id() );
		}
		else {
			if(petSpeciesId != null) {//����ѯ���pet_species_idʱ�ߵ�·��
				for(int i=0; i<petSpeciesId.length; i++) {
					sbEnd.append(" or pet_species_id = " + petSpeciesId[i] );
				}
			}
		}
		if(!StringUtil.isEmpty(pet.getPet_name())) {//ͨ���п��ж��Ƿ����name��ѯ
			sbEnd.append(" or pet_name like '%" + pet.getPet_name() + "%'");
		}
		
		StringBuffer temp = sbStart.append(sbEnd.toString().replaceFirst("or", "where"));
		PreparedStatement pstmt = con.prepareStatement(temp.toString());
		result = pstmt.executeQuery();
		return result;
	}
	
	/*�÷���ʵ��������pet_id�޸ĵ�������*/
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
