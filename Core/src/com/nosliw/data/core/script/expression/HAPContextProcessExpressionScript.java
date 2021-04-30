package com.nosliw.data.core.script.expression;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.nosliw.data.core.common.HAPDefinitionConstant;
import com.nosliw.data.core.common.HAPWithConstantDefinition;
import com.nosliw.data.core.common.HAPWithValueContext;
import com.nosliw.data.core.expression.HAPDefinitionExpressionSuite;
import com.nosliw.data.core.structure.value.HAPStructureValueDefinition;
import com.nosliw.data.core.structure.value.HAPStructureValueDefinitionFlat;

public class HAPContextProcessExpressionScript implements HAPWithValueContext, HAPWithConstantDefinition{

	//all expression container
	private HAPDefinitionExpressionSuite m_expressionSuiteDefinition;

	//all constant
	private Map<String, HAPDefinitionConstant> m_constantsDefinition;

	//context for expression
	private HAPStructureValueDefinitionFlat m_context;
	
	public HAPContextProcessExpressionScript() {
		this.m_constantsDefinition = new LinkedHashMap<String, HAPDefinitionConstant>();
	}
	
	public HAPDefinitionExpressionSuite getExpressionDefinitionSuite(){		return this.m_expressionSuiteDefinition;	}
	public void setExpressionDefinitionSuite(HAPDefinitionExpressionSuite expressionSuite) {   this.m_expressionSuiteDefinition = expressionSuite;    }

	@Override
	public Set<HAPDefinitionConstant> getConstantDefinitions() {  return new HashSet<HAPDefinitionConstant>(this.m_constantsDefinition.values()); }

	@Override
	public HAPDefinitionConstant getConstantDefinition(String id) {  return this.m_constantsDefinition.get(id);  }

	@Override
	public void addConstantDefinition(HAPDefinitionConstant constantDef) {  this.m_constantsDefinition.put(constantDef.getId(), constantDef);  }

	@Override
	public HAPStructureValueDefinition getValueContext() {   return this.m_context;  }

	@Override
	public void setValueContext(HAPStructureValueDefinition context) {   this.m_context = (HAPStructureValueDefinitionFlat)context;  }

	@Override
	public void cloneToValueContext(HAPWithValueContext withDataContext) {   withDataContext.setValueContext(m_context);  }  
}
