package com.nosliw.uiresource.tag;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.data.context.HAPContextGroup;
/**
 * Represent definition of context according to relationship with parent 
 */
@HAPEntityWithAttribute
public class HAPUITagDefinitionContext extends HAPContextGroup{

	@HAPAttribute
	public static final String INHERIT = "inherit";

	//whether interit the context from parent
	private boolean m_inherit;

	public HAPUITagDefinitionContext(){
		this.m_inherit = true;
	}

	public boolean isInherit(){  return this.m_inherit;  }
	public void setInherit(boolean inherit){  this.m_inherit = inherit;   } 

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(INHERIT, this.m_inherit+"");
		typeJsonMap.put(INHERIT, Boolean.class);
	}	
}
