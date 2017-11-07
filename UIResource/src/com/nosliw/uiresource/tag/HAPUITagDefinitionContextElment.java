package com.nosliw.uiresource.tag;

import com.nosliw.data.core.criteria.HAPDataTypeCriteria;

/**
 * Define context element under uiTag 
 */
public class HAPUITagDefinitionContextElment {

	//context element name definition
	//it can be string constant
	//it can also be script expression with attribute value as parms
	String m_nameDefinition;
	
	//context element criteria
	HAPDataTypeCriteria m_criteria;
	
	//context element can be a child of parent context
	//it can be string constant
	//it can also be script expression with attribute value as parms
	String m_parentContextPath;

	public String getNameDefinition(){
		return this.m_nameDefinition;
	}
	
	public HAPDataTypeCriteria getCriteria(){
		return this.m_criteria;
	}
	
	public String getParentContextPath(){
		return this.m_parentContextPath;
	}
	
}
