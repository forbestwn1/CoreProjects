package com.nosliw.data.context;

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
	public static final String NAME = "name";
	
	@HAPAttribute
	public static final String DESCRIPTION = "description";
	
	@HAPAttribute
	public static final String DEFAULT = "default";
	
	//name of context. it is just for display purpose
	private String m_name;
	
	//description of context, it is just for display purpose
	private String m_description;
	
	//default value for the root, used in runtime when no value is set
	private Object m_defaultValue;

	abstract String getType();
	
	public void setDefaultValue(Object defaultValue){		this.m_defaultValue = defaultValue;	}

	public Object getDefaultValue(){   return this.m_defaultValue;  }

	public void setName(String name) {  this.m_name = name;    }
	public void setDescription(String description) {   this.m_description = description;   }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(TYPE, this.getType());
		if(this.m_defaultValue!=null){
			jsonMap.put(DEFAULT, this.m_defaultValue.toString());
			typeJsonMap.put(DEFAULT, this.m_defaultValue.getClass());
		}
	}
}
