package com.nosliw.uiresource.tag;

import com.nosliw.uiresource.context.HAPContextNode;

/**
 * Define context element under uiTag 
 */
public class HAPUITagDefinitionContextElmentRelative extends HAPContextNode implements HAPUITagDefinitionContextElment{

	//context element name definition
	//it can be string constant
	//it can also be script expression with attribute value as parms
	String m_nameDefinition;
	
	//context element can be a child of parent context
	//it can be string constant
	//it can also be script expression with attribute value as parms
	String m_parentContextPath;

	
	public String getNameDefinition(){
		return this.m_nameDefinition;
	}
	
	public String getParentContextPath(){
		return this.m_parentContextPath;
	}
	
}
