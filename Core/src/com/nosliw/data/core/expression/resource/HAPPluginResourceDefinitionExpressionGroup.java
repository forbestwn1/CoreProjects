package com.nosliw.data.core.expression.resource;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.expression.HAPIdExpressionGroup;
import com.nosliw.data.core.resource.HAPManagerResourceDefinition;
import com.nosliw.data.core.resource.HAPPluginResourceDefinition;
import com.nosliw.data.core.resource.HAPResourceDefinition;
import com.nosliw.data.core.resource.HAPResourceIdLocal;
import com.nosliw.data.core.resource.HAPResourceIdSimple;

public class HAPPluginResourceDefinitionExpressionGroup implements HAPPluginResourceDefinition{

	private HAPManagerResourceDefinition m_resourceDefMan;
	
	public HAPPluginResourceDefinitionExpressionGroup(HAPManagerResourceDefinition resourceDefMan) {
		this.m_resourceDefMan = resourceDefMan;
	}
	
	@Override
	public String getResourceType() {	return HAPConstantShared.RUNTIME_RESOURCE_TYPE_EXPRESSION;	}

	@Override
	public HAPResourceDefinition getResourceDefinitionBySimpleResourceId(HAPResourceIdSimple resourceId) {
		HAPResourceIdExpression expressionResourceId = new HAPResourceIdExpression(resourceId);
		HAPResourceIdSimple expressionSuiteResourceId = expressionResourceId.getExpressionSuiteResourceId();
		HAPResourceDefinitionExpressionSuite expressionSuiteDef = (HAPResourceDefinitionExpressionSuite)this.m_resourceDefMan.getResourceDefinition(expressionSuiteResourceId);
		return new HAPResourceDefinitionExpressionGroup(expressionSuiteDef, expressionResourceId.getExpressionId().getExpressionGroupId());
	}

	@Override
	public HAPResourceDefinition getResourceDefinitionByLocalResourceId(HAPResourceIdLocal resourceId) {
		HAPIdExpressionGroup expressionGroupId = new HAPIdExpressionGroup(resourceId.getName());
		
		HAPResourceIdLocal suiteResourceId = new HAPResourceIdLocal(HAPConstantShared.RUNTIME_RESOURCE_TYPE_EXPRESSIONSUITE);
		suiteResourceId.setName(expressionGroupId.getSuiteId());
		suiteResourceId.setBasePath(resourceId.getBasePath());
		suiteResourceId.setSupplement(resourceId.getSupplement());
		
		HAPResourceDefinitionExpressionSuite expressionSuiteDef = (HAPResourceDefinitionExpressionSuite)this.m_resourceDefMan.getResourceDefinition(suiteResourceId);
		return new HAPResourceDefinitionExpressionGroup(expressionSuiteDef, expressionGroupId.getExpressionGroupId());
	}

	@Override
	public HAPResourceDefinition parseResourceDefinition(Object content) {
		// TODO Auto-generated method stub
		return null;
	}

}
