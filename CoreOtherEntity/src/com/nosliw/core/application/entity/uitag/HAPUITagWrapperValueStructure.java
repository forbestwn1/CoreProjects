package com.nosliw.core.application.entity.uitag;

import com.nosliw.common.info.HAPEntityInfoImp;
import com.nosliw.core.application.common.structure.HAPInfoStructureInWrapper;
import com.nosliw.core.application.common.structure.HAPValueStructure;
import com.nosliw.core.application.common.structure.HAPWrapperValueStructureDefinition;

public class HAPUITagWrapperValueStructure extends HAPEntityInfoImp implements HAPWrapperValueStructureDefinition{

	private HAPValueStructure m_valueStructure;
	
	private HAPInfoStructureInWrapper m_valueStructureInfo;
	
	public HAPUITagWrapperValueStructure() {
		this.m_valueStructureInfo = new HAPInfoStructureInWrapper(); 
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
