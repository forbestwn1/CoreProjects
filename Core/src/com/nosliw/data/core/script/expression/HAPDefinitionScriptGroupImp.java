package com.nosliw.data.core.script.expression;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.info.HAPUtilityEntityInfo;

public class HAPDefinitionScriptGroupImp implements HAPDefinitionScriptGroup{

	private Map<String, HAPDefinitionScriptEntity> m_scriptDefs;
	
	public HAPDefinitionScriptGroupImp() {
		this.m_scriptDefs = new LinkedHashMap<String, HAPDefinitionScriptEntity>();
	}
	
	@Override
	public Set<HAPDefinitionScriptEntity> getEntityElements() {  return new HashSet<HAPDefinitionScriptEntity>(this.m_scriptDefs.values()); }

	@Override
	public HAPDefinitionScriptEntity getEntityElement(String id) {  return this.m_scriptDefs.get(id); }

	@Override
	public void addEntityElement(HAPDefinitionScriptEntity entityElement) {
		HAPUtilityEntityInfo.processEntityId(entityElement);
		this.m_scriptDefs.put(entityElement.getId(), entityElement);  
	}

}
