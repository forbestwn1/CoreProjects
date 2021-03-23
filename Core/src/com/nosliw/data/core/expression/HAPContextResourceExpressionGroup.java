package com.nosliw.data.core.expression;

import com.nosliw.data.core.expression.resource.HAPResourceDefinitionExpressionGroup;
import com.nosliw.data.core.resource.HAPContextResourceDefinition;
import com.nosliw.data.core.resource.HAPEntityWithResourceContext;
import com.nosliw.data.core.resource.HAPManagerResourceDefinition;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.resource.HAPUtilityResource;

//context for processing expression
//it include related suite or process
public class HAPContextResourceExpressionGroup implements HAPContextResourceDefinition{

	private HAPDefinitionExpressionSuite m_suite; 
	
	private HAPManagerResourceDefinition m_resourceDefMan;
	
	public static HAPContextResourceExpressionGroup createContext(HAPDefinitionExpressionSuite suite, HAPManagerResourceDefinition resourceDefMan) {
		HAPContextResourceExpressionGroup out = new HAPContextResourceExpressionGroup();
		out.m_resourceDefMan = resourceDefMan;
		out.m_suite = suite;
		return out;
	}
	
	public static HAPContextResourceExpressionGroup createContext(HAPManagerResourceDefinition resourceDefMan) {
		HAPContextResourceExpressionGroup out = new HAPContextResourceExpressionGroup();
		out.m_resourceDefMan = resourceDefMan;
		return out;
	}
	
	@Override
	public HAPEntityWithResourceContext getResourceDefinition(HAPResourceId expressionId) {
		HAPDefinitionExpressionGroup expressionGroup = (HAPDefinitionExpressionGroup)HAPUtilityResource.getImpliedEntity(expressionId, this.m_suite, this.m_resourceDefMan);
		HAPDefinitionExpressionSuite suite = this.m_suite;
		if(expressionGroup instanceof HAPResourceDefinitionExpressionGroup) {
			suite = ((HAPResourceDefinitionExpressionGroup)expressionGroup).getSuite();
		}
		HAPEntityWithResourceContext out = new HAPEntityWithResourceContext(expressionGroup, HAPContextResourceExpressionGroup.createContext(suite, m_resourceDefMan));
		return out;
	}	
}
