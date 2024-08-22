package com.nosliw.core.application.uitag;

import com.nosliw.common.info.HAPEntityInfoImp;
import com.nosliw.core.application.common.structure.HAPValueStructureDefinition;
import com.nosliw.core.application.common.structure.HAPWrapperValueStructure;

public class HAPUITagWrapperValueStructure extends HAPEntityInfoImp implements HAPWrapperValueStructure{

	private HAPValueStructureDefinition m_valueStructure;
	
	//group type for value structure (public, private, protected, internal)
	private String m_groupType;
	
	@Override
	public HAPValueStructureDefinition getValueStructure() {   return  this.m_valueStructure;  } 

	@Override
	public void setValueStructure(HAPValueStructureDefinition valueStructure) {   this.m_valueStructure = valueStructure;  }

	@Override
	public String getGroupType() {   return this.m_groupType;   }

	@Override
	public void setGroupType(String groupType) {   this.m_groupType = groupType;  }

}
