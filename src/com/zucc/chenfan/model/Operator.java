package com.zucc.chenfan.model;

import java.util.Date;

/**   
*    
* 项目名称：PetServiceManagementSystem   
* 类名称：Operator   
* 类描述：   操作员（店员）model类
* 创建人：Administrator   
* 创建时间：2018年9月2日 上午8:58:17   
* 修改人：Administrator   
* 修改时间：2018年9月2日 上午8:58:17   
* 修改备注：   
* @version    
*    
*/
public class Operator {

	private int operator_id;
	private String operator_name;
	private String operator_group;
	private String password;
	private Date operator_createdate;
	private Date operator_modifydate;
	
	
	public Operator() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Operator(int operator_id, String password) {
		super();
		this.operator_id = operator_id;
		this.password = password;
	}

	public Operator(String operator_name, String operator_group, String password) {
		super();
		this.operator_name = operator_name;
		this.operator_group = operator_group;
		this.password = password;
	}
	public int getOperator_id() {
		return operator_id;
	}
	public void setOperator_id(int operator_id) {
		this.operator_id = operator_id;
	}
	public String getOperator_name() {
		return operator_name;
	}
	public void setOperator_name(String operator_name) {
		this.operator_name = operator_name;
	}
	public String getOperator_group() {
		return operator_group;
	}
	public void setOperator_group(String operator_group) {
		this.operator_group = operator_group;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Date getOperator_createdate() {
		return operator_createdate;
	}
	public void setOperator_createdate(Date operator_createdate) {
		this.operator_createdate = operator_createdate;
	}
	public Date getOperator_modifydate() {
		return operator_modifydate;
	}
	public void setOperator_modifydate(Date operator_modifydate) {
		this.operator_modifydate = operator_modifydate;
	}
	
	/*getter&setter*/
	
	
	
	
}
