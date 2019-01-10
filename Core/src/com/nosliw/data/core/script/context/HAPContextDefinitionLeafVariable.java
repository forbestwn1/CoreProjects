package com.nosliw.data.core.script.context;

import java.util.Map;

public abstract class HAPContextDefinitionLeafVariable extends HAPContextDefinitionElement{

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
	}
	
	@Override
	public void toContextDefinitionElement(HAPContextDefinitionElement out) {
		super.toContextDefinitionElement(out);
	}

	@Override
	public boolean equals(Object obj) {
		if(!super.equals(obj))  return false;
		return true;
	}
}
