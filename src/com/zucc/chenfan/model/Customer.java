package com.zucc.chenfan.model;

/**   
*    
* 项目名称：PetServiceManagementSystem   
* 类名称：Customer   
* 类描述：   
* 创建人：Administrator   
* 创建时间：2018年9月13日 上午10:01:37   
* 修改人：Administrator   
* 修改时间：2018年9月13日 上午10:01:37   
* 修改备注：   
* @version    
*    
*/
public class Customer {

	private int customer_id;
	private String customer_name;
	private long customer_phone;
	private String customer_email;
	private String customer_other_contact;
	private String customer_createdate;
	private String customer_modifydate;
	
	public Customer() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
	
	
	
	public int getCustomer_id() {
		return customer_id;
	}
	public void setCustomer_id(int customer_id) {
		this.customer_id = customer_id;
	}
	public String getCustomer_name() {
		return customer_name;
	}
	public void setCustomer_name(String customer_name) {
		this.customer_name = customer_name;
	}
	public String getCustomer_email() {
		return customer_email;
	}
	public void setCustomer_email(String customer_email) {
		this.customer_email = customer_email;
	}
	public String getCustomer_other_contact() {
		return customer_other_contact;
	}
	public void setCustomer_other_contact(String customer_other_contact) {
		this.customer_other_contact = customer_other_contact;
	}
	public String getCustomer_createdate() {
		return customer_createdate;
	}
	public void setCustomer_createdate(String customer_createdate) {
		this.customer_createdate = customer_createdate;
	}
	public String getCustomer_modifydate() {
		return customer_modifydate;
	}
	public void setCustomer_modifydate(String customer_modifydate) {
		this.customer_modifydate = customer_modifydate;
	}
	public long getCustomer_phone() {
		return customer_phone;
	}
	public void setCustomer_phone(long customer_phone) {
		this.customer_phone = customer_phone;
	}

	
}
