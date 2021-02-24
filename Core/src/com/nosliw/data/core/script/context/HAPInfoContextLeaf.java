package com.nosliw.data.core.script.context;

public class HAPInfoContextLeaf {

	private HAPContextDefinitionElement m_contextLeaf;
	
	private HAPContextPath m_contextPath;
	
	public HAPInfoContextLeaf(HAPContextDefinitionElement contextLeaf, HAPContextPath contextPath) {
		this.m_contextLeaf = contextLeaf;
		this.m_contextPath = contextPath;
	}
	
	public HAPContextDefinitionElement getContextElement() {   return this.m_contextLeaf;   }
	
	public HAPContextPath getContextPath() {    return this.m_contextPath;    }
	
}
