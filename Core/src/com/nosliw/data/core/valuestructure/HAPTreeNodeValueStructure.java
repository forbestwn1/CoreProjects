package com.nosliw.data.core.valuestructure;

import com.nosliw.data.core.domain.entity.valuestructure.HAPDefinitionEntityComplexValueStructure;

public class HAPTreeNodeValueStructure {

	private HAPDefinitionEntityComplexValueStructure m_valueStructureComplex;
	
	private HAPTreeNodeValueStructure m_parent;
	
	public HAPDefinitionEntityComplexValueStructure getValueStructureComplex() {   return this.m_valueStructureComplex;    }
	public void setValueStructureComplex(HAPDefinitionEntityComplexValueStructure valueStructureComplex) {    this.m_valueStructureComplex = valueStructureComplex;    }
	
	public HAPTreeNodeValueStructure getParent() {   return this.m_parent;      }
	public void setParent(HAPTreeNodeValueStructure parent) {    this.m_parent = parent;      }
	
}
