package com.nosliw.uiresource.tag;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.data.context.HAPConfigureContextProcessor;
import com.nosliw.data.context.HAPContextGroup;
/**
 * Represent definition of context according to relationship with parent 
 */
@HAPEntityWithAttribute
public class HAPUITagDefinitionContext extends HAPContextGroup{

	@HAPAttribute
	public static final String INHERIT = "inherit";

	public HAPUITagDefinitionContext(){
	}

	public String getInheritMode() {  
		String out = HAPConfigureContextProcessor.VALUE_INHERITMODE_CHILD;
		if("false".equals(this.getInfo().getValue(INHERIT)))  out = HAPConfigureContextProcessor.VALUE_INHERITMODE_NONE;
		return out;				
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
	}	
}
