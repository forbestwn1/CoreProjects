package com.nosliw.uiresource.context;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;

public class HAPContextNodeRootAbsolute extends HAPContextNode implements HAPContextNodeRoot{

	@HAPAttribute
	public static final String DEFAULT = "default";
	
	//default value for the root, used in runtime when no value is set
	private Object m_defaultValue;
	
	public void setDefaultValue(Object defaultValue){		this.m_defaultValue = defaultValue;	}

	public Object getDefaultValue(){   return this.m_defaultValue;  }

	@Override
	protected void buildFullJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildFullJsonMap(jsonMap, typeJsonMap);
		if(this.m_defaultValue!=null){
			jsonMap.put(DEFAULT, this.m_defaultValue.toString());
			typeJsonMap.put(DEFAULT, this.m_defaultValue.getClass());
		}
	}
}
