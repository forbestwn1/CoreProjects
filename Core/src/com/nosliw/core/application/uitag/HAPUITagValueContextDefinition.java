package com.nosliw.core.application.uitag;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.core.application.common.structure.HAPValueContextDefinition;
import com.nosliw.core.application.common.structure22.HAPWrapperValueStructureDefinition;

public class HAPUITagValueContextDefinition extends HAPSerializableImp implements HAPValueContextDefinition{

	
	
	private List<HAPWrapperValueStructureDefinition> m_valueStructures;
	
	public HAPUITagValueContextDefinition() {
		this.m_valueStructures = new ArrayList<HAPWrapperValueStructureDefinition>();
	}
	
	@Override
	public List<HAPWrapperValueStructureDefinition> getValueStructures() {   return this.m_valueStructures;  }

	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		
	}
	
}
