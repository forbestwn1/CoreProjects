package com.nosliw.data.core.expression;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.common.HAPDefinitionConstant;
import com.nosliw.data.core.complex.HAPDefinitionEntityComplex;
import com.nosliw.data.core.complex.valuestructure.HAPUtilityComplexValueStructure;
import com.nosliw.data.core.domain.HAPInfoContainerElementSet;

public class HAPDefinitionExpressionSuite extends HAPDefinitionEntityComplex{

	@HAPAttribute
	public static String ELEMENT = "element";

	public static final String ENTITY_TYPE = HAPConstantShared.RUNTIME_RESOURCE_TYPE_DATAEXPRESSIONSUITE;
	
	private Map<String, HAPDefinitionConstant> m_constantDefinitions;
	
	public HAPDefinitionExpressionSuite() {
		super(ENTITY_TYPE);
		this.m_constantDefinitions = new LinkedHashMap<String, HAPDefinitionConstant>();
	}

	public void addExpressionGroup(HAPInfoContainerElementSet eleInfo) {
		this.addContainerElementAttribute(ELEMENT, eleInfo);
	}
	
	
	@Override
	public void addEntityElement(HAPDefinitionExpressionGroup1 expressionGroup) {
		this.setElementParentPart(expressionGroup);
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
	public HAPDefinitionExpressionSuite1 cloneExpressionSuiteDefinition() {
		HAPDefinitionExpressionSuite out = new HAPDefinitionExpressionSuite();
		out.m_valueStructureComplex = this.m_valueStructureComplex.cloneValueStructureComplex();
		for(String id : this.m_expressionGroups.keySet()) {
			out.m_expressionGroups.put(id, this.m_expressionGroups.get(id).cloneExpressionGroupDefinition());
		}
		for(String id : this.m_constantDefinitions.keySet()) {
			out.m_constantDefinitions.put(id, this.m_constantDefinitions.get(id).cloneConstantDefinition());
		}
		return out;
	}

	@Override
	public String getValueStructureTypeIfNotDefined() {  return HAPConstantShared.STRUCTURE_TYPE_VALUEFLAT;  }

	private void setElementParentPart(HAPDefinitionExpressionGroup1 expressionGroup) {
		HAPUtilityComplexValueStructure.setParentPart(expressionGroup, this);
	}
	
}
