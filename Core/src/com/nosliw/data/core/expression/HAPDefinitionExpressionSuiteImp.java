package com.nosliw.data.core.expression;

import java.util.LinkedHashMap;
import java.util.Map;

public class HAPDefinitionExpressionSuiteImp implements HAPDefinitionExpressionSuite{

	private Map<String, HAPDefinitionExpressionGroup> m_expressionGroups;

	public HAPDefinitionExpressionSuiteImp() {
		this.m_expressionGroups = new LinkedHashMap<String, HAPDefinitionExpressionGroup>();
	}
	
	@Override
	public Map<String, HAPDefinitionExpressionGroup> getExpressionGroups() {   return this.m_expressionGroups;  }

	@Override
	public void addExpressionGroup(HAPDefinitionExpressionGroup expressionGroup) {
		this.m_expressionGroups.put(expressionGroup.getName(), expressionGroup);
	}

}
