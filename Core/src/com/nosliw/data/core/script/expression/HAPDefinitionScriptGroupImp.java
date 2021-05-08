package com.nosliw.data.core.script.expression;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.info.HAPEntityInfoWritableImp;
import com.nosliw.common.info.HAPUtilityEntityInfo;
import com.nosliw.data.core.common.HAPDefinitionConstant;
import com.nosliw.data.core.common.HAPWithValueStructure;
import com.nosliw.data.core.structure.value.HAPStructureValueDefinition;

public class HAPDefinitionScriptGroupImp extends HAPEntityInfoWritableImp implements HAPDefinitionScriptGroup{

	private Map<String, HAPDefinitionScriptEntity> m_scriptDefs;
	
	private Map<String, HAPDefinitionConstant> m_constantDef;
	
	private HAPStructureValueDefinition m_context;
	
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
	public HAPStructureValueDefinition getValueStructure() {   return this.m_context;  }

	@Override
	public void setValueContext(HAPStructureValueDefinition context) {   this.m_context = context;  }

	@Override
	public void cloneToWithValueStructure(HAPWithValueStructure dataContext) {
		dataContext.setValueStructure(this.m_context.cloneStructure());
	}

	@Override
	public Set<HAPDefinitionConstant> getConstantDefinitions() {  return new HashSet<HAPDefinitionConstant>(this.m_constantDef.values());  }

	@Override
	public HAPDefinitionConstant getConstantDefinition(String id) {  return this.m_constantDef.get(id);  }

	@Override
	public void addConstantDefinition(HAPDefinitionConstant constantDef) {   this.m_constantDef.put(constantDef.getName(), constantDef);  }

}
