package com.nosliw.core.application.common.structure;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.nosliw.common.serialization.HAPSerializableImp;

public class HAPValueContextDefinitionImp extends HAPSerializableImp implements HAPValueContextDefinition{

	private List<HAPWrapperValueStructureDefinition> m_valueStructures;
	
	public HAPValueContextDefinitionImp() {
		this.m_valueStructures = new ArrayList<HAPWrapperValueStructureDefinition>();
	}
	
	@Override
	public List<HAPWrapperValueStructureDefinition> getValueStructures() {   return this.m_valueStructures;  }

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		
	}
	
}
