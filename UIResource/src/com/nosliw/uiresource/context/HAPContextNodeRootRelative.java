package com.nosliw.uiresource.context;

import java.util.Map;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.expression.HAPMatchers;

/**
 * Context element that based on context element on parent
 * When tag has its own variable definition which is differnt from defintion from parent, 
 * we should treat through two variables as different variables 
 * And matcher is needed to do convert between these two variables  
 */
public class HAPContextNodeRootRelative extends HAPContextNode implements HAPContextNodeRoot{

	//relative path from parent context
	private HAPContextPath m_path;

	//variable full name --- matchers
	//used to convert data from parent to data within uiTag
	private Map<String, HAPMatchers> m_matchers;
	
	@Override
	public String getType() {		return HAPConstant.UIRESOURCE_ROOTTYPE_RELATIVE;	}

	public void setPath(HAPContextPath path){
		this.m_path = path;
	}
	
}
