package com.nosliw.core.application.entity.uitag;

import com.nosliw.common.info.HAPEntityInfoImp;
import com.nosliw.core.application.common.structure.HAPValueStructure;
import com.nosliw.core.application.common.structure.HAPWrapperValueStructureDefinition;

public class HAPUITagWrapperValueStructure extends HAPEntityInfoImp implements HAPWrapperValueStructureDefinition{

	private HAPValueStructure m_valueStructure;
	
	//group type for value structure (public, private, protected, internal)
	private String m_groupType;

	private String m_inheritMode;
	
	@Override
	public HAPValueStructure getValueStructure() {   return  this.m_valueStructure;  } 

	@Override
	public void setValueStructure(HAPValueStructure valueStructure) {   this.m_valueStructure = valueStructure;  }

	@Override
	public String getGroupType() {   return this.m_groupType;   }

	@Override
	public void setGroupType(String groupType) {   this.m_groupType = groupType;  }

	@Override
	public String getInheritMode() {    return this.m_inheritMode;    }
	
	@Override
	public void setInheritMode(String mode) {     this.m_inheritMode = mode;       }

}
