package com.nosliw.data.core.script.expression1;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.info.HAPEntityInfoWritableImp;
import com.nosliw.common.info.HAPUtilityEntityInfo;
import com.nosliw.core.application.division.manual.brick.valuestructure.HAPManualBrickWrapperValueStructure;
import com.nosliw.data.core.common.HAPDefinitionConstant;

public class HAPDefinitionScriptGroupImp extends HAPEntityInfoWritableImp implements HAPDefinitionScriptGroup{

	private Map<String, HAPDefinitionScriptEntity> m_scriptDefs;
	
	private Map<String, HAPDefinitionConstant> m_constantDef;
	
	private HAPManualBrickWrapperValueStructure m_valueStructureWrapper;
	
	public HAPDefinitionScriptGroupImp() {
		this.m_scriptDefs = new LinkedHashMap<String, HAPDefinitionScriptEntity>();
		this.m_constantDef = new LinkedHashMap<String, HAPDefinitionConstant>();
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

	@Override
	public HAPManualBrickWrapperValueStructure getValueStructureWrapper() {   return this.m_valueStructureWrapper;  }

	@Override
	public void setValueStructureWrapper(HAPManualBrickWrapperValueStructure valueStructureWrapper) {   this.m_valueStructureWrapper = valueStructureWrapper;  }

	@Override
	public Set<HAPDefinitionConstant> getConstantDefinitions() {  return new HashSet<HAPDefinitionConstant>(this.m_constantDef.values());  }

	@Override
	public HAPDefinitionConstant getConstantDefinition(String id) {  return this.m_constantDef.get(id);  }

	@Override
	public void addConstantDefinition(HAPDefinitionConstant constantDef) {   this.m_constantDef.put(constantDef.getName(), constantDef);  }

}
