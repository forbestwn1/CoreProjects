package com.nosliw.data.core.expression;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.nosliw.data.core.common.HAPDefinitionConstant;
import com.nosliw.data.core.common.HAPWithConstantDefinition;
import com.nosliw.data.core.common.HAPWithValueStructure;
import com.nosliw.data.core.valuestructure.HAPValueStructureDefinition;

public class HAPDefinitionExpressionSuiteImp implements HAPDefinitionExpressionSuite, HAPWithValueStructure, HAPWithConstantDefinition{

	private Map<String, HAPDefinitionExpressionGroup> m_expressionGroups;

	private HAPValueStructureDefinition m_valueStructure;
	
	private Map<String, HAPDefinitionConstant> m_constantDefinitions;
	
	public HAPDefinitionExpressionSuiteImp() {
		this.m_expressionGroups = new LinkedHashMap<String, HAPDefinitionExpressionGroup>();
		this.m_constantDefinitions = new LinkedHashMap<String, HAPDefinitionConstant>();
	}
	
	@Override
	public Set<HAPDefinitionExpressionGroup> getEntityElements() {  return new HashSet<HAPDefinitionExpressionGroup>(this.m_expressionGroups.values()); }

	@Override
	public HAPDefinitionExpressionGroup getEntityElement(String id) {  return this.m_expressionGroups.get(id);  }

	@Override
	public void addEntityElement(HAPDefinitionExpressionGroup expressionGroup) {
		expressionGroup.setValueStructure((HAPValueStructureDefinition)this.m_valueStructure.cloneStructure());
		for(String id : this.m_constantDefinitions.keySet()) {
			expressionGroup.addConstantDefinition(this.m_constantDefinitions.get(id).cloneConstantDefinition());
		}
		this.m_expressionGroups.put(expressionGroup.getId(), expressionGroup); 
	}

	@Override
	public Set<HAPDefinitionConstant> getConstantDefinitions() {  return new HashSet<HAPDefinitionConstant>(this.m_constantDefinitions.values()); }

	@Override
	public HAPDefinitionConstant getConstantDefinition(String id) {    return this.m_constantDefinitions.get(id);  }

	@Override
	public void addConstantDefinition(HAPDefinitionConstant constantDef) {  
		this.m_constantDefinitions.put(constantDef.getId(), constantDef);
		for(String id : this.m_expressionGroups.keySet()) {
			this.m_expressionGroups.get(id).addConstantDefinition(constantDef);
		}
	}

	@Override
	public HAPValueStructureDefinition getValueStructure() {  return this.m_valueStructure; }

	@Override
	public void setValueStructure(HAPValueStructureDefinition context) {
		this.m_valueStructure = context;  
		for(String id : this.m_expressionGroups.keySet()) {
			this.m_expressionGroups.get(id).setValueStructure((HAPValueStructureDefinition)context.cloneStructure());
		}
	}
	
	@Override
	public HAPDefinitionExpressionSuite cloneExpressionSuiteDefinition() {
		HAPDefinitionExpressionSuiteImp out = new HAPDefinitionExpressionSuiteImp();
		if(this.m_valueStructure!=null)	out.m_valueStructure = (HAPValueStructureDefinition)this.m_valueStructure.cloneStructure();
		for(String id : this.m_expressionGroups.keySet()) {
			out.m_expressionGroups.put(id, this.m_expressionGroups.get(id).cloneExpressionGroupDefinition());
		}
		for(String id : this.m_constantDefinitions.keySet()) {
			out.m_constantDefinitions.put(id, this.m_constantDefinitions.get(id).cloneConstantDefinition());
		}
		return out;
	}

	@Override
	public void cloneToWithValueStructure(HAPWithValueStructure dataContext) {
		dataContext.setValueStructure(this.m_valueStructure);
	}
}
