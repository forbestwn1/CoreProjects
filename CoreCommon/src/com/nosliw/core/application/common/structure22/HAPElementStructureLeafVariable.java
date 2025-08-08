package com.nosliw.core.application.common.structure22;

import java.util.Map;

public abstract class HAPElementStructureLeafVariable extends HAPElementStructure{

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
	}
	
	@Override
	public void toStructureElement(HAPElementStructure out) {
		super.toStructureElement(out);
	}

	@Override
	public boolean equals(Object obj) {
		if(!super.equals(obj))  return false;
		return true;
	}
}
