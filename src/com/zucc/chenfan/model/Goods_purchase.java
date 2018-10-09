package com.zucc.chenfan.model;

import java.util.Date;

/**   
*    
* 项目名称：PetServiceManagementSystem   
* 类名称：Goods_purchase   
* 类描述：   
* 创建人：Administrator   
* 创建时间：2018年9月14日 上午2:34:16   
* 修改人：Administrator   
* 修改时间：2018年9月14日 上午2:34:16   
* 修改备注：   
* @version    
*    
*/
public class Goods_purchase {

	private int purchase_id;
	private int goods_id;
	private int customer_id;
	private int quantity;
	private float totalprice;
	private String address;
	private String state;
	private Date purchase_createdate;
	private Date purchase_updatedate;
	public Goods_purchase() {
		super();
		// TODO Auto-generated constructor stub
	}
	public int getPurchase_id() {
		return purchase_id;
	}
	public void setPurchase_id(int purchase_id) {
		this.purchase_id = purchase_id;
	}
	public int getGoods_id() {
		return goods_id;
	}
	public void setGoods_id(int goods_id) {
		this.goods_id = goods_id;
	}
	public int getCustomer_id() {
		return customer_id;
	}
	public void setCustomer_id(int customer_id) {
		this.customer_id = customer_id;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public float getTotalprice() {
		return totalprice;
	}
	public void setTotalprice(float totalprice) {
		this.totalprice = totalprice;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public Date getPurchase_createdate() {
		return purchase_createdate;
	}
	public void setPurchase_createdate(Date purchase_createdate) {
		this.purchase_createdate = purchase_createdate;
	}
	public Date getPurchase_updatedate() {
		return purchase_updatedate;
	}
	public void setPurchase_updatedate(Date purchase_updatedate) {
		this.purchase_updatedate = purchase_updatedate;
	}
	
	
	
}
