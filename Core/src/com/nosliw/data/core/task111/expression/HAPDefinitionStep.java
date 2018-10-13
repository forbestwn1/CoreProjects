package com.nosliw.data.core.task111.expression;

import java.util.Map;
import java.util.Set;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.data.core.task111.HAPDefinitionComponent;

public abstract class HAPDefinitionStep extends HAPDefinitionComponent {

	@HAPAttribute
	public static String TYPE = "type";
	
	abstract public String getType();
	
	abstract public Set<String> getVariableNames();
	
	abstract public Set<String> getReferenceNames();

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(TYPE, this.getType());
	}
	
}
