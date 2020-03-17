package com.nosliw.data.core.expression;

import com.nosliw.data.core.component.HAPManagerResourceDefinition;
import com.nosliw.data.core.resource.HAPContextResourceDefinition;
import com.nosliw.data.core.resource.HAPResourceDefinitionWithContext;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.resource.HAPResourceUtility;

//context for processing expression
//it include related suite or process
public class HAPContextExpression implements HAPContextResourceDefinition{

	private HAPDefinitionExpressionSuite m_suite; 
	
	private HAPManagerResourceDefinition m_resourceDefMan;
	
	public static HAPContextExpression createContext(HAPDefinitionExpressionSuite suite, HAPManagerResourceDefinition resourceDefMan) {
		HAPContextExpression out = new HAPContextExpression();
		out.m_resourceDefMan = resourceDefMan;
		out.m_suite = suite;
		return out;
	}
	
	public static HAPContextExpression createContext(HAPManagerResourceDefinition resourceDefMan) {
		HAPContextExpression out = new HAPContextExpression();
		out.m_resourceDefMan = resourceDefMan;
		return out;
	}
	
	@Override
	public HAPResourceDefinitionWithContext getResourceDefinition(HAPResourceId processId) {
		HAPDefinitionExpression processDef = (HAPDefinitionExpression)HAPResourceUtility.getImpliedResourceDefinition(processId, this.m_suite, this.m_resourceDefMan);
		HAPResourceDefinitionWithContext out = new HAPResourceDefinitionWithContext(processDef, HAPContextExpression.createContext(processDef.getSuite(), m_resourceDefMan));
		return out;
	}	
}
