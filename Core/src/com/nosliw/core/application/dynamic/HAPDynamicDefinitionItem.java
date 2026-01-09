package com.nosliw.core.application.dynamic;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPEntityInfoImp;

@HAPEntityWithAttribute
public abstract class HAPDynamicDefinitionItem extends HAPEntityInfoImp{

	@HAPAttribute
	public final static String TYPE = "type"; 

	public abstract String getType();

	public abstract HAPDynamicDefinitionItem getChild(String childName);
	
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(TYPE, this.getType());
	}

	
}
