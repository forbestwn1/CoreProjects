package com.nosliw.data.core.script.expression;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.nosliw.data.core.common.HAPDefinitionConstant;
import com.nosliw.data.core.common.HAPWithConstantDefinition;
import com.nosliw.data.core.common.HAPWithValueContext;
import com.nosliw.data.core.domain.entity.expression.data.HAPDefinitionExpressionSuite1;
import com.nosliw.data.core.domain.entity.valuestructure.HAPDefinitionWrapperValueStructure;

public class HAPContextProcessExpressionScript implements HAPWithValueContext, HAPWithConstantDefinition{

	//all expression container
	private HAPDefinitionExpressionSuite1 m_expressionSuiteDefinition;

	//all constant
	private Map<String, HAPDefinitionConstant> m_constantsDefinition;

	//context for expression
	private HAPDefinitionWrapperValueStructure m_valueStructureWrapper;
	
	public HAPContextProcessExpressionScript() {
		this.m_constantsDefinition = new LinkedHashMap<String, HAPDefinitionConstant>();
	}
	
	public HAPDefinitionExpressionSuite1 getExpressionDefinitionSuite(){		return this.m_expressionSuiteDefinition;	}
	public void setExpressionDefinitionSuite(HAPDefinitionExpressionSuite1 expressionSuite) {   this.m_expressionSuiteDefinition = expressionSuite;    }

	@Override
	public Set<HAPDefinitionConstant> getConstantDefinitions() {  return new HashSet<HAPDefinitionConstant>(this.m_constantsDefinition.values()); }

	@Override
	public HAPDefinitionConstant getConstantDefinition(String id) {  return this.m_constantsDefinition.get(id);  }

	@Override
	public void addConstantDefinition(HAPDefinitionConstant constantDef) {  this.m_constantsDefinition.put(constantDef.getId(), constantDef);  }

	@Override
	public HAPDefinitionWrapperValueStructure getValueStructureWrapper() {   return this.m_valueStructureWrapper;  }

	@Override
	public void setValueStructureWrapper(HAPDefinitionWrapperValueStructure valueStructureWrapper) {   this.m_valueStructureWrapper = valueStructureWrapper;  }

}
