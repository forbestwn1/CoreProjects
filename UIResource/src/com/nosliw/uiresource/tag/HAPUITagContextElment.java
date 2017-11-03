package com.nosliw.uiresource.tag;

import com.nosliw.data.core.criteria.HAPDataTypeCriteria;

public class HAPUITagContextElment {

	//name of context
	String m_name;
	
	//
	HAPDataTypeCriteria m_criteria;
	
	String m_mapFrom;

	public String getName(){
		return this.m_name;
	}
	
	public HAPDataTypeCriteria getCriteria(){
		return this.m_criteria;
	}
	
	public String getMapFrom(){
		return this.m_mapFrom;
	}
	
}
