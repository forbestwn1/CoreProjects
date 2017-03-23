package com.nosliw.uiresource;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPJsonUtility;

/*
 * class for input data info
 */
public class HAPUIResourceContextInfo extends HAPSerializableImp{

	//name of the context input data
	private String m_name;
	
	//data type of the context input data 
	private String m_type;
	
	public HAPUIResourceContextInfo(String name, String type){
		this.m_name = name;
		this.m_type = type;
	}
	
	public HAPUIResourceContextInfo(String name){
		this.m_name = name;
	}
	
	public String getName(){return this.m_name;	}
	
	public String getType(){	return this.m_type;	}

	@Override
	protected void buildFullJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(HAPAttributeConstant.UIRESOURCECONTEXTINFO_NAME, this.m_name);
		jsonMap.put(HAPAttributeConstant.UIRESOURCECONTEXTINFO_TYPE, this.m_type);
		jsonMap.put(HAPAttributeConstant.UIRESOURCECONTEXTINFO_CONFIGURE, HAPJsonUtility.buildMapJson(new LinkedHashMap<String, String>()));
	}
	
	@Override
	public String toString(){
		return this.toStringValue(HAPSerializationFormat.JSON);
	}
}
