package com.nosliw.data.core.complex.valuestructure;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.valuestructure.HAPValueStructure;

//wrapper for value structure
//so that as long as different component share same wrapper, we can update value structure instance without lose syc between components
public class HAPWrapperValueStructure{

	//id for value structure definition
	private String m_id;

	private HAPValueStructure m_valueStructure;
	
	public HAPWrapperValueStructure(HAPValueStructure valueStructure) {
		this.m_valueStructure = valueStructure;
	}
	
	public HAPValueStructure getValueStructure() {
		return this.m_valueStructure;
	}
	
	public String getId() {   return this.m_id;    }
	public void setId(String id) {   this.m_id = id;   }
	
	public HAPValueStructure setValueStructure(HAPValueStructure valueStructure) {
		this.m_valueStructure = valueStructure;
		return this.m_valueStructure;
	}

	public boolean isEmpty() {
		if(this.m_valueStructure==null)  return true;
		return this.m_valueStructure.getStructureType().equals(HAPConstantShared.STRUCTURE_TYPE_VALUEEMPTY);
	}
	
	public HAPWrapperValueStructure cloneValueStructureWrapper() {
		return new HAPWrapperValueStructure((HAPValueStructure)this.getValueStructure().cloneStructure());
	}
}
