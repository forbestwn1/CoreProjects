package com.nosliw.data.core.valuestructure;

//wrapper for value structure
//so that as long as different component share same wrapper, we can update value structure instance without lose syc between components
public class HAPWrapperValueStructure {

	private HAPValueStructure m_valueStructure;
	
	public HAPWrapperValueStructure(HAPValueStructure valueStructure) {
		this.m_valueStructure = valueStructure;
	}
	
	public HAPValueStructure getValueStructure() {
		return this.m_valueStructure;
	}
	
	public HAPValueStructure setValueStructure(HAPValueStructure valueStructure) {
		this.m_valueStructure = valueStructure;
		return this.m_valueStructure;
	}
	
	public HAPWrapperValueStructure cloneValueStructureWrapper() {
		return new HAPWrapperValueStructure((HAPValueStructure)this.getValueStructure().cloneStructure());
	}
}
