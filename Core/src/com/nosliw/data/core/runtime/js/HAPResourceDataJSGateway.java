package com.nosliw.data.core.runtime.js;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.data.core.runtime.HAPResourceData;

public class HAPResourceDataJSGateway extends HAPSerializableImp implements HAPResourceData, HAPResourceDataJSValue{

	@HAPAttribute
	public static String GATEWAY = "gateway";

	private Object m_gatewayObj;
	private String m_name;
	
	public HAPResourceDataJSGateway(Object gatewayObj, String name){
		this.m_gatewayObj = gatewayObj;
		this.m_name = name;
	}
	
	public Object getGateway(){  return this.m_gatewayObj;  }
	
	protected void buildFullJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
	}
	
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		this.buildFullJsonMap(jsonMap, typeJsonMap);
	}

	@Override
	public String getValue() {
		return "nosliw.getNodeData(\""+this.m_name+"\")";
	}

}
