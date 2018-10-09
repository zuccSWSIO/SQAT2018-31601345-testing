package com.zucc.chenfan.model;                                          
                                                                         
import java.sql.*;                                                       
/**                                                                      
*                                                                        
* 项目名称：PetServiceManagementSystem                                        
* 类名称：Service_type                                                         
* 类描述：   服务类别的model类                                                     
* 创建人：Administrator                                                      
* 创建时间：2018年9月3日 上午10:39:36                                              
* 修改人：Administrator                                                      
* 修改时间：2018年9月3日 上午10:39:36                                              
* 修改备注：                                                                  
* @version                                                               
*                                                                        
*/                                                                       
public class Service_type {                                                
	                                                                     
	private int service_type_id;                                           
	private String service_type_name;                                      
	private String service_type_detail;                                    
	                                                                     
	                                                                     
	                                                                     
	public Service_type(String service_type_name) {                          
		super();                                                         
		this.service_type_name = service_type_name;                          
	}                                                                    
	public Service_type(String service_type_name, String service_type_detail) {
		super();                                                         
		this.service_type_name = service_type_name;                          
		this.service_type_detail = service_type_detail;                      
	}                                                                    
	public Service_type() {                                                
		super();                                                         
		// TODO Auto-generated constructor stub                          
	}                                                                    
	public int getService_type_id() {                                      
		return service_type_id;                                            
	}                                                                    
	public void setService_type_id(int service_type_id) {                    
		this.service_type_id = service_type_id;                              
	}                                                                    
	public String getService_type_name() {                                 
		return service_type_name;                                          
	}                                                                    
	public void setService_type_name(String service_type_name) {             
		this.service_type_name = service_type_name;                          
	}                                                                    
	public String getService_type_detail() {                               
		return service_type_detail;                                        
	}                                                                    
	public void setService_type_detail(String service_type_detail) {         
		this.service_type_detail = service_type_detail;                      
	}                                                                    
                                                                         
}                                                                        
                                                                         