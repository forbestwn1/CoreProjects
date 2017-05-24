package com.nosliw.data.core.runtime;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.info.HAPInfo;
import com.nosliw.common.info.HAPInfoImpSimple;

/**
 * When we need to load resource according to resource id, sometimes, we need provide more information in order to control how resources are loaded
 *  
 */
public class HAPResourceIdInfo {

	@HAPAttribute
	public static String ID = "id";
	
	@HAPAttribute
	public static String INFO = "info";

	private HAPResourceId m_resourceId;
	
	private HAPInfo m_info;
	
	public HAPResourceIdInfo(HAPResourceId resourceId){
		this.m_resourceId = resourceId;
		this.m_info = new HAPInfoImpSimple();
	}
	
	public HAPResourceId getResourceId(){
		return this.m_resourceId;
	}
	
	public void setInfo(String name, String value){
		this.m_info.setValue(name, value);
	}
	
	public HAPResourceIdInfo withInfo(String name, String value){
		this.setInfo(name, value);
		return this;
	}
	
	public HAPInfo getInfo(){
		return this.m_info;
	}
	
	public String getInfoValue(String name){
		return this.m_info.getValue(name);
	}
}
