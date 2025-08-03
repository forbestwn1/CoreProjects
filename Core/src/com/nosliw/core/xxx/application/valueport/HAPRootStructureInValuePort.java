package com.nosliw.core.xxx.application.valueport;

import java.util.Map;

import com.nosliw.common.info.HAPEntityInfoWritableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.core.application.common.structure.HAPElementStructure;

public class HAPRootStructureInValuePort extends HAPEntityInfoWritableImp{

	public static final String DEFINITION = "definition";

	//context element definition
	private HAPElementStructure m_definition;

	public HAPRootStructureInValuePort(HAPElementStructure definition) {
		this.m_definition = definition;
	}
	
	public HAPElementStructure getDefinition() {   return this.m_definition;   }

	public void setDefinition(HAPElementStructure definition) {   this.m_definition = definition;  }

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(DEFINITION, this.m_definition.toStringValue(HAPSerializationFormat.JSON));
	}

}
