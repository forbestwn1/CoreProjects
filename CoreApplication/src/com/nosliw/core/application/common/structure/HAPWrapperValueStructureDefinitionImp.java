package com.nosliw.core.application.common.structure;

import com.nosliw.common.info.HAPEntityInfoImp;

public class HAPWrapperValueStructureDefinitionImp extends HAPEntityInfoImp implements HAPWrapperValueStructureDefinition{

	private HAPValueStructure m_valueStructure;
	
	private HAPInfoStructureInWrapper m_valueStructureInfo;
	
	public HAPWrapperValueStructureDefinitionImp() {
		this.m_valueStructureInfo = new HAPInfoStructureInWrapper(); 
	}
	
	public HAPWrapperValueStructureDefinitionImp(HAPValueStructure valueStructure) {
		this();
		this.m_valueStructure = valueStructure;
	}
	
	@Override
	public HAPValueStructure getValueStructure() {   return  this.m_valueStructure;  } 

	@Override
	public void setValueStructure(HAPValueStructure valueStructure) {   this.m_valueStructure = valueStructure;  }

	@Override
	public HAPInfoStructureInWrapper getStructureInfo() {   return this.m_valueStructureInfo;   }

	@Override
	public void setStructureInfo(HAPInfoStructureInWrapper info) {   this.m_valueStructureInfo = info;   }
}
