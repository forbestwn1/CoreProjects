package com.nosliw.core.application.common.structure222.reference;

import com.nosliw.core.data.matcher.HAPMatchers;

public class HAPPathElementMappingVariableToVariable extends HAPPathElementMapping{

	private HAPMatchers m_matchers;
	
	public HAPPathElementMappingVariableToVariable(String path, HAPMatchers matchers) {
		super(HAPPathElementMapping.VARIABLE2VARIABLE, path);
		this.m_matchers = matchers;
	}
	
	public HAPMatchers getMatcher() {   return this.m_matchers;    }
}
