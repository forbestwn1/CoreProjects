package com.nosliw.data.core.expression;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.info.HAPEntityInfoImp;
import com.nosliw.common.info.HAPUtilityEntityInfo;
import com.nosliw.data.core.common.HAPDefinitionConstant;
import com.nosliw.data.core.common.HAPWithValueStructure;
import com.nosliw.data.core.structure.value.HAPStructureValueDefinition;

public class HAPDefinitionExpressionGroupImp extends HAPEntityInfoImp implements HAPDefinitionExpressionGroup, HAPWithValueStructure{

	private Map<String, HAPDefinitionExpression> m_elements;
	
	private HAPStructureValueDefinition m_valueStructure;
	
	private Map<String, HAPDefinitionConstant> m_constantDefinitions;
	
	public HAPDefinitionExpressionGroupImp() {
		this.m_elements = new LinkedHashMap<String, HAPDefinitionExpression>();
		this.m_constantDefinitions = new LinkedHashMap<String, HAPDefinitionConstant>();
	}
	
	public void addExpression(HAPDefinitionExpression element) {
		this.addEntityElement(element);
	}

	@Override
	public HAPStructureValueDefinition getValueStructure() {   return this.m_valueStructure;  }

	@Override
	public void setValueStructure(HAPStructureValueDefinition context) {  this.m_valueStructure = context;  }

	@Override
	public void cloneToWithValueStructure(HAPWithValueStructure withValueStructure) {   withValueStructure.setValueStructure((HAPStructureValueDefinition)this.m_valueStructure.cloneStructure());  }

	@Override
	public Set<HAPDefinitionExpression> getEntityElements() {  return new HashSet<HAPDefinitionExpression>(this.m_elements.values()); }

	@Override
	public HAPDefinitionExpression getEntityElement(String id) {  return this.m_elements.get(id);  }

	@Override
	public void addEntityElement(HAPDefinitionExpression expression) {  
		HAPUtilityEntityInfo.processEntityId(expression);
		this.m_elements.put(expression.getId(), expression);  
	}

	@Override
	public Set<HAPDefinitionConstant> getConstantDefinitions() {  return new HashSet(this.m_constantDefinitions.values());  }  

	@Override
	public HAPDefinitionConstant getConstantDefinition(String id) {  return this.m_constantDefinitions.get(id);  }

	@Override
	public void addConstantDefinition(HAPDefinitionConstant constantDef) {  this.m_constantDefinitions.put(constantDef.getId(), constantDef);  }

	@Override
	public HAPDefinitionExpressionGroup cloneExpressionGroupDefinition() {
		HAPDefinitionExpressionGroupImp out = new HAPDefinitionExpressionGroupImp();
		if(this.m_valueStructure!=null)	out.m_valueStructure = (HAPStructureValueDefinition)this.m_valueStructure.cloneStructure();
		for(String id : this.m_elements.keySet()) {
			out.m_elements.put(id, this.m_elements.get(id).cloneDefinitionExpression());
		}
		for(String id : this.m_constantDefinitions.keySet()) {
			out.m_constantDefinitions.put(id, this.m_constantDefinitions.get(id).cloneConstantDefinition());
		}
		return out;
	}
}
