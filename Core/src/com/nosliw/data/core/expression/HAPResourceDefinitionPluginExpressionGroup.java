package com.nosliw.data.core.expression;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.component.HAPManagerResourceDefinition;
import com.nosliw.data.core.component.HAPPluginResourceDefinition;
import com.nosliw.data.core.resource.HAPResourceDefinition;
import com.nosliw.data.core.resource.HAPResourceIdSimple;

public class HAPResourceDefinitionPluginExpressionGroup implements HAPPluginResourceDefinition{

	private HAPManagerResourceDefinition m_resourceDefMan;
	
	public HAPResourceDefinitionPluginExpressionGroup(HAPManagerResourceDefinition resourceDefMan) {
		this.m_resourceDefMan = resourceDefMan;
	}
	
	@Override
	public String getResourceType() {	return HAPConstant.RUNTIME_RESOURCE_TYPE_EXPRESSION;	}

	@Override
	public HAPResourceDefinition getResource(HAPResourceIdSimple resourceId) {
		HAPResourceIdExpression expressionResourceId = new HAPResourceIdExpression(resourceId);
		HAPResourceIdSimple expressionSuiteResourceId = expressionResourceId.getExpressionSuiteResourceId();
		HAPResourceDefinitionExpressionSuite expressionSuiteDef = (HAPResourceDefinitionExpressionSuite)this.m_resourceDefMan.getResourceDefinition(expressionSuiteResourceId);
		return new HAPResourceDefinitionExpressionGroup(expressionSuiteDef, expressionResourceId.getExpressionId().getExpressionGroupId());
	}

	@Override
	public HAPResourceDefinition parseResourceDefinition(Object content) {
		// TODO Auto-generated method stub
		return null;
	}

}
