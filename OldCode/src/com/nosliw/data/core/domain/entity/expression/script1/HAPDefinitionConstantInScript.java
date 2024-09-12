package com.nosliw.data.core.domain.entity.expression.script1;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;

public class HAPDefinitionConstantInScript extends HAPSerializableImp{

	@HAPAttribute
	public static String CONSTANTNAME = "constantName";

	private String m_constantName; 

	public HAPDefinitionConstantInScript(String name){
		this.m_constantName = name;
	}
	
	public String getConstantName(){	return this.m_constantName;	}
	
	public HAPDefinitionConstantInScript cloneConstantInScript() {
		HAPDefinitionConstantInScript out = new HAPDefinitionConstantInScript(this.m_constantName);
		return out;
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(CONSTANTNAME, this.m_constantName);
	}
}
