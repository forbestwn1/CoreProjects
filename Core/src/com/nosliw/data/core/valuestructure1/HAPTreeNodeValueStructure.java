package com.nosliw.data.core.valuestructure1;

import com.nosliw.data.core.entity.division.manual.valuestructure.HAPDefinitionEntityValueContext;

public class HAPTreeNodeValueStructure {

	private HAPDefinitionEntityValueContext m_valueStructureComplex;
	
	private HAPTreeNodeValueStructure m_parent;
	
	public HAPDefinitionEntityValueContext getValueStructureComplex() {   return this.m_valueStructureComplex;    }
	public void setValueStructureComplex(HAPDefinitionEntityValueContext valueStructureComplex) {    this.m_valueStructureComplex = valueStructureComplex;    }
	
	public HAPTreeNodeValueStructure getParent() {   return this.m_parent;      }
	public void setParent(HAPTreeNodeValueStructure parent) {    this.m_parent = parent;      }
	
}
