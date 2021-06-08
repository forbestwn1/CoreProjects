package com.nosliw.data.core.valuestructure;

public class HAPTreeNodeValueStructure {

	private HAPWrapperValueStructure m_valueStructureWrapper;
	
	private HAPTreeNodeValueStructure m_parent;
	
	public HAPWrapperValueStructure getValueStructureWrapper() {   return this.m_valueStructureWrapper;    }
	public void setValueStructureWrapper(HAPWrapperValueStructure valueStructureWrapper) {    this.m_valueStructureWrapper = valueStructureWrapper;    }
	
	public HAPTreeNodeValueStructure getParent() {   return this.m_parent;      }
	public void setParent(HAPTreeNodeValueStructure parent) {    this.m_parent = parent;      }
	
}
