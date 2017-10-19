package com.nosliw.uiresource.definition;

import com.nosliw.common.constant.HAPAttribute;

public class HAPContextNodeRoot extends HAPContextNode{

	@HAPAttribute
	public static final String DEFAULT = "default";
	
	//default value for the root, used in runtime when no value is set
	private Object m_defaultValue;
	
	public void setDefaultValue(Object defaultValue){		this.m_defaultValue = defaultValue;	}

	public Object getDefaultValue(){   return this.m_defaultValue;  }
}
