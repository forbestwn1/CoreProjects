package com.nosliw.data.core.valuestructure;

public class HAPTreeNodeValueStructure {

	private HAPValueStructure m_valueStructure;
	
	private HAPTreeNodeValueStructure m_parent;
	
	public HAPValueStructure getValueStructure() {   return this.m_valueStructure;    }
	public void setValueStructure(HAPValueStructure valueStructure) {    this.m_valueStructure = valueStructure;    }
	
	public HAPTreeNodeValueStructure getParent() {   return this.m_parent;      }
	public void setParent(HAPTreeNodeValueStructure parent) {    this.m_parent = parent;      }
	
}
