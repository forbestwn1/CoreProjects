package com.nosliw.data.core.script.expression;

import com.nosliw.data.core.component.HAPManagerResourceDefinition;
import com.nosliw.data.core.resource.HAPContextResourceDefinition;
import com.nosliw.data.core.resource.HAPEntityWithResourceContext;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.resource.HAPResourceUtility;

//context for processing expression
//it include related suite or process
public class HAPContextScript implements HAPContextResourceDefinition{

	private HAPResourceDefinitionExpressionSuite m_suite; 
	
	private HAPManagerResourceDefinition m_resourceDefMan;
	
	public static HAPContextScript createContext(HAPResourceDefinitionExpressionSuite suite, HAPManagerResourceDefinition resourceDefMan) {
		HAPContextScript out = new HAPContextScript();
		out.m_resourceDefMan = resourceDefMan;
		out.m_suite = suite;
		return out;
	}
	
	public static HAPContextScript createContext(HAPManagerResourceDefinition resourceDefMan) {
		HAPContextScript out = new HAPContextScript();
		out.m_resourceDefMan = resourceDefMan;
		return out;
	}
	
	@Override
	public HAPEntityWithResourceContext getResourceDefinition(HAPResourceId expressionId) {
		HAPResourceDefinitionExpression processDef = (HAPResourceDefinitionExpression)HAPResourceUtility.getImpliedResourceDefinition(expressionId, this.m_suite, this.m_resourceDefMan);
		HAPEntityWithResourceContext out = new HAPEntityWithResourceContext(processDef, HAPContextScript.createContext(processDef.getSuite(), m_resourceDefMan));
		return out;
	}	
}
