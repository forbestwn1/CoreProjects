package com.nosliw.core.application.common.structure.reference;

import com.nosliw.core.application.common.valueport.HAPResultReferenceResolve;

public class HAPExceptionResolveReference extends Exception{

	private HAPResultReferenceResolve m_resolveInfo;
	
	public HAPExceptionResolveReference(HAPResultReferenceResolve resolveInfo) {
		this.m_resolveInfo = resolveInfo;
	}
	
	public HAPResultReferenceResolve getResolveInfo() {    return this.m_resolveInfo;     }
	
}
