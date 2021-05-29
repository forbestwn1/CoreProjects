package com.nosliw.uiresource.page.tag;

import java.util.Map;

import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.data.core.valuestructure.HAPValueStructureDefinitionGroup;
/**
 * Represent definition of context according to relationship with parent 
 */
@HAPEntityWithAttribute
public class HAPValueStructureDefinitionInUITag extends HAPValueStructureDefinitionGroup{


	public HAPValueStructureDefinitionInUITag(){
	}
 
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
	}	
}
