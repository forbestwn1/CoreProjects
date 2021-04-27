package com.nosliw.data.core.structure;

//current structure info
public class HAPInfoElement {

	//structure obj
	private HAPElement m_contextLeaf;
	
	//path to reach this structure
	private HAPReferenceElement m_contextPath;
	
	public HAPInfoElement(HAPElement contextLeaf, HAPReferenceElement contextPath) {
		this.m_contextLeaf = contextLeaf;
		this.m_contextPath = contextPath;
	}
	
	public HAPElement getContextElement() {   return this.m_contextLeaf;   }
	public void setContextElement(HAPElement ele) {    this.m_contextLeaf = ele;    }
	
	public HAPReferenceElement getContextPath() {    return this.m_contextPath;    }
	
}
