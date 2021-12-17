package com.nosliw.data.core.complex;

import com.nosliw.data.core.common.HAPWithValueStructure;
import com.nosliw.data.core.runtime.HAPExecutableImpEntityInfo;

public abstract class HAPExecutableEntityComplex extends HAPExecutableImpEntityInfo implements HAPWithValueStructure{

	private String m_valueStructureComplexId;
	
	public abstract String getEntityType();
	
	@Override
	public String getValueStructureComplexId() {  return this.m_valueStructureComplexId;  }

	public void setValueStructureComplexId(String valueStructureComplexId) {    this.m_valueStructureComplexId = valueStructureComplexId;     }
	
	@Override
	public String getValueStructureTypeIfNotDefined() {
		// TODO Auto-generated method stub
		return null;
	}
}
