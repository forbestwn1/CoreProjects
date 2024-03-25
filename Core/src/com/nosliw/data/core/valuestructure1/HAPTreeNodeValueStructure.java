package com.nosliw.data.core.valuestructure1;

import com.nosliw.core.application.division.manual.brick.valuestructure.HAPManualBrickValueContext;

public class HAPTreeNodeValueStructure {

	private HAPManualBrickValueContext m_valueStructureComplex;
	
	private HAPTreeNodeValueStructure m_parent;
	
	public HAPManualBrickValueContext getValueStructureComplex() {   return this.m_valueStructureComplex;    }
	public void setValueStructureComplex(HAPManualBrickValueContext valueStructureComplex) {    this.m_valueStructureComplex = valueStructureComplex;    }
	
	public HAPTreeNodeValueStructure getParent() {   return this.m_parent;      }
	public void setParent(HAPTreeNodeValueStructure parent) {    this.m_parent = parent;      }
	
}
