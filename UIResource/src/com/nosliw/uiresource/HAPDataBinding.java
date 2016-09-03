package com.nosliw.uiresource;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.serialization.HAPStringable;
import com.nosliw.common.utils.HAPJsonUtility;

/*
 * store data binding information
 */
public class HAPDataBinding implements HAPStringable{

	//name of data binding
	private String m_name;
	
	//path regarding the context
	private HAPContextVariable m_variable;

	public HAPDataBinding(String name, String dataPath){
		this.m_name = name;
		this.m_variable = new HAPContextVariable(dataPath);
	}

	public String getName(){ return this.m_name; }
	public HAPContextVariable getContextVariablePath(){ return this.m_variable; }
	
	@Override
	public String toStringValue(String format) {
		Map<String, String> jsonMap = new LinkedHashMap<String, String>();
		
		jsonMap.put(HAPAttributeConstant.DATABINDING_VARIABLE, this.m_variable.toStringValue(format));
		jsonMap.put(HAPAttributeConstant.CONTEXTVARIABLE_NAME, this.m_name);
		
		return HAPJsonUtility.getMapJson(jsonMap);
	}
}
