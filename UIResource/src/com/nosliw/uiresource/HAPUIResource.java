package com.nosliw.uiresource;

import com.nosliw.data.core.expression.HAPExpressionDefinitionSuite;

/**
 * UIResource that is result of processing UI Definition
 */
public class HAPUIResource {

	private HAPUIDefinitionUnitResource m_uiDefinitionResource;
	
	private HAPExpressionDefinitionSuite m_expressionDefinitionSuite;
	
	public HAPUIResource(HAPUIDefinitionUnitResource uiDefinitionUnitResource){
		this.m_uiDefinitionResource = uiDefinitionUnitResource; 
	}
	
	public void process(){
		
	}
}
