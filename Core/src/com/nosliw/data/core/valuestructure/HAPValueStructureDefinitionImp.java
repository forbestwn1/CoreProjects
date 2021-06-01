package com.nosliw.data.core.valuestructure;

import java.util.Map;

import com.nosliw.common.info.HAPInfo;
import com.nosliw.common.info.HAPInfoImpSimple;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;

public abstract class HAPValueStructureDefinitionImp extends HAPSerializableImp implements HAPValueStructureDefinition{

	private HAPInfo m_info;
	
	private HAPValueStructure m_parent;
	
	public HAPValueStructureDefinitionImp() {
		this.m_info = new HAPInfoImpSimple(); 

	}

	@Override
	public HAPInfo getInfo() {  return this.m_info;  }
	
	@Override
	public void setInfo(HAPInfo info) {  this.m_info = info.cloneInfo();   }
	
	@Override
	public HAPValueStructure getParent() {   return this.m_parent;   }
	@Override
	public void setParent(HAPValueStructure parent) {  this.m_parent = parent;   }
	 
	public void cloneBaseToValueStructureDefinition(HAPValueStructureDefinition valueStructureDefinition) {
		valueStructureDefinition.setInfo(this.getInfo());
		valueStructureDefinition.setParent(this.getParent());
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(TYPE, this.getStructureType());
		jsonMap.put(INFO, HAPJsonUtility.buildJson(this.m_info, HAPSerializationFormat.JSON));
	}

}
