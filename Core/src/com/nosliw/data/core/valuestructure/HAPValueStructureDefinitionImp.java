package com.nosliw.data.core.valuestructure;

import java.util.Map;

import com.nosliw.common.info.HAPInfo;
import com.nosliw.common.info.HAPInfoImpSimple;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;

public abstract class HAPValueStructureDefinitionImp extends HAPSerializableImp implements HAPValueStructure{

	private HAPInfo m_info;
	
	public HAPValueStructureDefinitionImp() {
		this.m_info = new HAPInfoImpSimple(); 

	}

	@Override
	public HAPInfo getInfo() {  return this.m_info;  }
	
	@Override
	public void setInfo(HAPInfo info) {  this.m_info = info.cloneInfo();   }
	
	public void cloneBaseToValueStructureDefinition(HAPValueStructure valueStructure) {
		valueStructure.setInfo(this.getInfo());
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(TYPE, this.getStructureType());
		jsonMap.put(INFO, HAPUtilityJson.buildJson(this.m_info, HAPSerializationFormat.JSON));
	}

}
