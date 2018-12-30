package com.nosliw.data.core.script.context;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.utils.HAPBasicUtility;

public abstract class HAPContextDefinitionLeafVariable extends HAPContextDefinitionElement{

	@HAPAttribute
	public static final String DEFAULT = "defaultValue";

	//default value for the element, used in runtime when no value is set
	private Object m_defaultValue;

	public Object getDefaultValue(){   return this.m_defaultValue;  }

	public void setDefaultValue(Object defaultValue){		this.m_defaultValue = defaultValue;	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		
		if(this.m_defaultValue!=null){
			jsonMap.put(DEFAULT, this.m_defaultValue.toString());
			typeJsonMap.put(DEFAULT, this.m_defaultValue.getClass());
		}
	}
	
	@Override
	public void toContextDefinitionElement(HAPContextDefinitionElement out) {
		super.toContextDefinitionElement(out);
		((HAPContextDefinitionLeafVariable)out).m_defaultValue = this.m_defaultValue;
	}

	@Override
	public boolean equals(Object obj) {
		if(!super.equals(obj))  return false;

		boolean out = false;
		if(obj instanceof HAPContextDefinitionLeafVariable) {
			HAPContextDefinitionLeafVariable ele = (HAPContextDefinitionLeafVariable)obj;
			if(!HAPBasicUtility.isEquals(this.m_defaultValue, ele.m_defaultValue))  return false;
			out = true;
		}
		return out;
	}
}
