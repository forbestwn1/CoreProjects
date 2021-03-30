package com.nosliw.data.core.script.context;

public class HAPInfoContextNode {

	private HAPContextDefinitionElement m_contextLeaf;
	
	private HAPContextPath m_contextPath;
	
	public HAPInfoContextNode(HAPContextDefinitionElement contextLeaf, HAPContextPath contextPath) {
		this.m_contextLeaf = contextLeaf;
		this.m_contextPath = contextPath;
	}
	
	public HAPContextDefinitionElement getContextElement() {   return this.m_contextLeaf;   }
	public void setContextElement(HAPContextDefinitionElement ele) {    this.m_contextLeaf = ele;    }
	
	public HAPContextPath getContextPath() {    return this.m_contextPath;    }
	
}
