package com.zucc.chenfan.model;

import java.util.Date;
import java.sql.*;

/**   
*    
* 项目名称：PetServiceManagementSystem   
* 类名称：Goods   
* 类描述：   商品的model类
* 创建人：Administrator   
* 创建时间：2018年9月3日 上午10:29:34   
* 修改人：Administrator   
* 修改时间：2018年9月3日 上午10:29:34   
* 修改备注：   
* @version    
*    
*/
public class Goods {

	private int goods_id;
	private String goods_name;
	private int goods_type_id;
	private String goods_brand;
	private float goods_unitprice;
	private String goods_barcode;
	private boolean goods_state;
	private Date goods_createdate;
	private Date goods_modifydate;
	private String goods_detail;
	
	
	
	
	
	
	public Goods() {
		super();
		// TODO Auto-generated constructor stub
	}
	public int getGoods_id() {
		return goods_id;
	}
	public String getGoods_name() {
		return goods_name;
	}
	public void setGoods_name(String goods_name) {
		this.goods_name = goods_name;
	}
	public int getGoods_type_id() {
		return goods_type_id;
	}
	public void setGoods_type_id(int goods_type_id) {
		this.goods_type_id = goods_type_id;
	}
	public String getGoods_brand() {
		return goods_brand;
	}
	public void setGoods_brand(String goods_brand) {
		this.goods_brand = goods_brand;
	}
	public float getGoods_unitprice() {
		return goods_unitprice;
	}
	public void setGoods_unitprice(float goods_unitprice) {
		this.goods_unitprice = goods_unitprice;
	}
	public String getGoods_barcode() {
		return goods_barcode;
	}
	public void setGoods_barcode(String goods_barcode) {
		this.goods_barcode = goods_barcode;
	}
	
	public Date getGoods_createdate() {
		return goods_createdate;
	}
	public void setGoods_createdate(Date goods_createdate) {
		this.goods_createdate = goods_createdate;
	}
	public Date getGoods_modifydate() {
		return goods_modifydate;
	}
	public void setGoods_modifydate(Date goods_modifydate) {
		this.goods_modifydate = goods_modifydate;
	}
	public void setGoods_id(int goods_id) {
		this.goods_id = goods_id;
	}
	public String getGoods_detail() {
		return goods_detail;
	}
	public void setGoods_detail(String goods_detail) {
		this.goods_detail = goods_detail;
	}
	public Goods(String goods_name, int goods_type_id, String goods_brand, float goods_unitprice, String goods_barcode,
			boolean goods_state, String goods_detail) {
		super();
		this.goods_name = goods_name;
		this.goods_type_id = goods_type_id;
		this.goods_brand = goods_brand;
		this.goods_unitprice = goods_unitprice;
		this.goods_barcode = goods_barcode;
		this.goods_state = goods_state;
		this.goods_detail = goods_detail;
	}
	public boolean isGoods_state() {
		return goods_state;
	}
	public void setGoods_state(boolean goods_state) {
		this.goods_state = goods_state;
	}
	
	

	
}
