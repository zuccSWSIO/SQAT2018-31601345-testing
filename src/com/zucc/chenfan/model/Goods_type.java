package com.zucc.chenfan.model;

import java.sql.*;
/**   
*    
* ��Ŀ���ƣ�PetServiceManagementSystem   
* �����ƣ�Goods_type   
* ��������   ��Ʒ����model��
* �����ˣ�Administrator   
* ����ʱ�䣺2018��9��3�� ����10:39:36   
* �޸��ˣ�Administrator   
* �޸�ʱ�䣺2018��9��3�� ����10:39:36   
* �޸ı�ע��   
* @version    
*    
*/
public class Goods_type {
	
	private int goods_type_id;
	private String goods_type_name;
	private String goods_type_detail;
	
	
	
	public Goods_type(String goods_type_name) {
		super();
		this.goods_type_name = goods_type_name;
	}
	public Goods_type(String goods_type_name, String goods_type_detail) {
		super();
		this.goods_type_name = goods_type_name;
		this.goods_type_detail = goods_type_detail;
	}
	public Goods_type() {
		super();
		// TODO Auto-generated constructor stub
	}
	public int getGoods_type_id() {
		return goods_type_id;
	}
	public void setGoods_type_id(int goods_type_id) {
		this.goods_type_id = goods_type_id;
	}
	public String getGoods_type_name() {
		return goods_type_name;
	}
	public void setGoods_type_name(String goods_type_name) {
		this.goods_type_name = goods_type_name;
	}
	public String getGoods_type_detail() {
		return goods_type_detail;
	}
	public void setGoods_type_detail(String goods_type_detail) {
		this.goods_type_detail = goods_type_detail;
	}

}
