package com.nosliw.data.core.structure;

import com.nosliw.data.core.matcher.HAPMatchers;

public class HAPPathElementMappingVariableToVariable extends HAPPathElementMapping{

	private HAPMatchers m_matchers;
	
	public HAPPathElementMappingVariableToVariable(String path, HAPMatchers matchers) {
		super(HAPPathElementMapping.VARIABLE2VARIABLE, path);
		this.m_matchers = matchers;
	}
	
	public HAPMatchers getMatcher() {   return this.m_matchers;    }
}
