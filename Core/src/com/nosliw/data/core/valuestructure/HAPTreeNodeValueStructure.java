package com.nosliw.data.core.valuestructure;

import com.nosliw.data.core.complex.valuestructure.HAPComplexValueStructure;

public class HAPTreeNodeValueStructure {

	private HAPComplexValueStructure m_valueStructureComplex;
	
	private HAPTreeNodeValueStructure m_parent;
	
	public HAPComplexValueStructure getValueStructureComplex() {   return this.m_valueStructureComplex;    }
	public void setValueStructureComplex(HAPComplexValueStructure valueStructureComplex) {    this.m_valueStructureComplex = valueStructureComplex;    }
	
	public HAPTreeNodeValueStructure getParent() {   return this.m_parent;      }
	public void setParent(HAPTreeNodeValueStructure parent) {    this.m_parent = parent;      }
	
}
