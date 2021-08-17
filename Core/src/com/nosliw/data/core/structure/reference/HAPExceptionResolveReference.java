package com.nosliw.data.core.structure.reference;

public class HAPExceptionResolveReference extends Exception{

	private HAPInfoReferenceResolve m_resolveInfo;
	
	public HAPExceptionResolveReference(HAPInfoReferenceResolve resolveInfo) {
		this.m_resolveInfo = resolveInfo;
	}
	
	public HAPInfoReferenceResolve getResolveInfo() {    return this.m_resolveInfo;     }
	
}
