package com.zucc.chenfan.model;

import java.io.FileInputStream;

/**   
*    
* 项目名称：PetServiceManagementSystem   
* 类名称：Pet   
* 类描述：   
* 创建人：Administrator   
* 创建时间：2018年9月13日 下午4:01:26   
* 修改人：Administrator   
* 修改时间：2018年9月13日 下午4:01:26   
* 修改备注：   
* @version    
*    
*/
public class Pet {

	private int pet_id;
	private int customer_id;
	private int pet_species_id;
	private String pet_name;
	private FileInputStream pet_image;
	private String pet_createdate;
	private String pet_modifydate;
	public int getPet_id() {
		return pet_id;
	}
	public void setPet_id(int pet_id) {
		this.pet_id = pet_id;
	}
	public int getCustomer_id() {
		return customer_id;
	}
	public void setCustomer_id(int customer_id) {
		this.customer_id = customer_id;
	}
	public int getPet_species_id() {
		return pet_species_id;
	}
	public void setPet_species_id(int pet_species_id) {
		this.pet_species_id = pet_species_id;
	}
	public String getPet_name() {
		return pet_name;
	}
	public void setPet_name(String pet_name) {
		this.pet_name = pet_name;
	}
	public FileInputStream getPet_image() {
		return pet_image;
	}
	public void setPet_image(FileInputStream pet_image) {
		this.pet_image = pet_image;
	}
	public String getPet_createdate() {
		return pet_createdate;
	}
	public void setPet_createdate(String pet_createdate) {
		this.pet_createdate = pet_createdate;
	}
	public String getPet_modifydate() {
		return pet_modifydate;
	}
	public void setPet_modifydate(String pet_modifydate) {
		this.pet_modifydate = pet_modifydate;
	}
	public Pet() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
}
