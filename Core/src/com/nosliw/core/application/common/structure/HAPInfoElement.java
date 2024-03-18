package com.nosliw.core.application.common.structure;

import com.nosliw.common.path.HAPComplexPath;

//current structure info
public class HAPInfoElement {

	//structure element
	private HAPElementStructure m_element;
	
	//path to reach this structure (root local id + path)
	private HAPComplexPath m_elementPath;
	
	public HAPInfoElement(HAPElementStructure element, HAPComplexPath elementPath) {
		this.m_element = element;
		this.m_elementPath = elementPath;
	}
	
	public HAPElementStructure getElement() {   return this.m_element;   }
	public void setElement(HAPElementStructure ele) {    this.m_element = ele;    }
	
	public HAPComplexPath getElementPath() {    return this.m_elementPath;    }
	
}
