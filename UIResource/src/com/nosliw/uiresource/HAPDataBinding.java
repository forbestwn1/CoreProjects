package com.nosliw.uiresource;

import java.util.Map;

import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;

/*
 * store data binding information
 */
public class HAPDataBinding extends HAPSerializableImp{

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
	protected void buildFullJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(HAPAttributeConstant.DATABINDING_VARIABLE, this.m_variable.toStringValue(HAPSerializationFormat.JSON_FULL));
		jsonMap.put(HAPAttributeConstant.CONTEXTVARIABLE_NAME, this.m_name);
	}
}
