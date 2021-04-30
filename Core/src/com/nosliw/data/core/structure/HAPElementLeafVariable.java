package com.nosliw.data.core.structure;

import java.util.Map;

public abstract class HAPElementLeafVariable extends HAPElement{

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
	}
	
	@Override
	public void toStructureElement(HAPElement out) {
		super.toStructureElement(out);
	}

	@Override
	public boolean equals(Object obj) {
		if(!super.equals(obj))  return false;
		return true;
	}
}
