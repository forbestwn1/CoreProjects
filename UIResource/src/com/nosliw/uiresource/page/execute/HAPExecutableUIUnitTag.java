package com.nosliw.uiresource.page.execute;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.uiresource.page.definition.HAPDefinitionUIUnitTag;

public class HAPExecutableUIUnitTag extends HAPExecutableUIUnit{

	@HAPAttribute
	public static final String TAGNAME = "tagName";
	
	public HAPExecutableUIUnitTag(HAPDefinitionUIUnitTag uiTagDefinition) {
		super(uiTagDefinition);
	}

	private HAPDefinitionUIUnitTag getUIUnitTagDefinition() {   return (HAPDefinitionUIUnitTag)this.getUIUnitDefinition();  }
	
	@Override
	protected void buildFullJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildFullJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(TAGNAME, this.getUIUnitTagDefinition().getTagName());
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(TAGNAME, this.getUIUnitTagDefinition().getTagName());
	}
}
