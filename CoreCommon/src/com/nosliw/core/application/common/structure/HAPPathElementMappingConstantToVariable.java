package com.nosliw.core.application.common.structure;

import com.nosliw.data.core.matcher.HAPMatchers;

public class HAPPathElementMappingConstantToVariable extends HAPPathElementMapping{

	private Object m_fromConstant;
	
	private HAPMatchers m_matchers;
	
	public HAPPathElementMappingConstantToVariable(Object fromConstant, String path, HAPMatchers matchers) {
		super(HAPPathElementMapping.CONSTANT2VARIABLE, path);
		this.m_fromConstant = fromConstant;
		this.m_matchers = matchers;
	}
	
	public HAPMatchers getMatcher() {   return this.m_matchers;    }
	
	public Object getFromConstant() {    return this.m_fromConstant;     }
}
