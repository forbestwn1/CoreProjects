package com.nosliw.uiresource.context;

import java.util.Map;

import com.nosliw.data.core.expression.HAPMatchers;

public class HAPUITagContextNodeRootRelative implements HAPUITagContextNodeRoot{

	//relative path from parent context
	private String m_parentPath;

	//variable full name --- matchers
	//used to convert data from parent to data within uiTag
	private Map<String, HAPMatchers> m_matchers;
	
}
