package com.nosliw.uiresource.context;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;

/**
 * Root node in context definition 
 */
@HAPEntityWithAttribute
public abstract class HAPContextNodeRoot extends HAPContextNode{

	@HAPAttribute
	public static final String TYPE = "type";

	@HAPAttribute
	public static final String DEFAULT = "default";
	
	//default value for the root, used in runtime when no value is set
	private Object m_defaultValue;

	abstract String getType();
	
	public void setDefaultValue(Object defaultValue){		this.m_defaultValue = defaultValue;	}

	public Object getDefaultValue(){   return this.m_defaultValue;  }

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		if(this.m_defaultValue!=null){
			jsonMap.put(DEFAULT, this.m_defaultValue.toString());
			typeJsonMap.put(DEFAULT, this.m_defaultValue.getClass());
		}
	}
}
