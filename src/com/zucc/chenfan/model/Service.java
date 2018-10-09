package com.zucc.chenfan.model;

import java.util.Date;

/**   
*    
* 项目名称：PetServiceManagementSystem   
* 类名称：Service   
* 类描述：   
* 创建人：Administrator   
* 创建时间：2018年9月10日 上午10:53:25   
* 修改人：Administrator   
* 修改时间：2018年9月10日 上午10:53:25   
* 修改备注：   
* @version    
*    
*/
public class Service {

	private int service_id;
	private String service_name;
	private int service_type_id;
	private float service_price;
	private String service_detail;
	private boolean service_state;
	private Date service_createdate;
	private Date sercice_modifydate;
	public Service() {
		super();
		// TODO Auto-generated constructor stub
	}
	public int getService_id() {
		return service_id;
	}
	public void setService_id(int service_id) {
		this.service_id = service_id;
	}
	public String getService_name() {
		return service_name;
	}
	public void setService_name(String service_name) {
		this.service_name = service_name;
	}
	public int getService_type_id() {
		return service_type_id;
	}
	public void setService_type_id(int service_type_id) {
		this.service_type_id = service_type_id;
	}
	public float getService_price() {
		return service_price;
	}
	public void setService_price(float service_price) {
		this.service_price = service_price;
	}
	public String getService_detail() {
		return service_detail;
	}
	public void setService_detail(String service_detail) {
		this.service_detail = service_detail;
	}
	public boolean isService_state() {
		return service_state;
	}
	public void setService_state(boolean service_state) {
		this.service_state = service_state;
	}
	public Date getService_createdate() {
		return service_createdate;
	}
	public void setService_createdate(Date service_createdate) {
		this.service_createdate = service_createdate;
	}
	public Date getSercice_modifydate() {
		return sercice_modifydate;
	}
	public void setSercice_modifydate(Date sercice_modifydate) {
		this.sercice_modifydate = sercice_modifydate;
	}
	public Service(String service_name, int service_type_id, float service_price, String service_detail,
			boolean service_state) {
		super();
		this.service_name = service_name;
		this.service_type_id = service_type_id;
		this.service_price = service_price;
		this.service_detail = service_detail;
		this.service_state = service_state;
	}
	
	
}
