package com.zucc.chenfan.model;

import java.sql.*;
/**   
*    
* 项目名称：PetServiceManagementSystem   
* 类名称：Goods_type   
* 类描述：   商品类别的model类
* 创建人：Administrator   
* 创建时间：2018年9月3日 上午10:39:36   
* 修改人：Administrator   
* 修改时间：2018年9月3日 上午10:39:36   
* 修改备注：   
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
