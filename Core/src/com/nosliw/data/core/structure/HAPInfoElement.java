package com.nosliw.data.core.structure;

import com.nosliw.common.path.HAPComplexPath;

//current structure info
public class HAPInfoElement {

	//structure element
	private HAPElement m_element;
	
	//path to reach this structure (root local id + path)
	private HAPComplexPath m_elementPath;
	
	public HAPInfoElement(HAPElement element, HAPComplexPath elementPath) {
		this.m_element = element;
		this.m_elementPath = elementPath;
	}
	
	public HAPElement getElement() {   return this.m_element;   }
	public void setElement(HAPElement ele) {    this.m_element = ele;    }
	
	public HAPComplexPath getElementPath() {    return this.m_elementPath;    }
	
}
