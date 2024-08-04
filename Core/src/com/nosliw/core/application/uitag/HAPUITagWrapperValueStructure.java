package com.nosliw.core.application.uitag;

import com.nosliw.common.info.HAPInfo;
import com.nosliw.core.application.common.structure.HAPValueStructureDefinition;
import com.nosliw.core.application.common.structure.HAPWrapperValueStructure;

public class HAPUITagWrapperValueStructure implements HAPWrapperValueStructure{

	private HAPValueStructureDefinition m_valueStructure;
	
	private String m_name;

	//group type for value structure (public, private, protected, internal)
	private String m_groupType;
	
	private HAPInfo m_info;
	
	@Override
	public HAPInfo getInfo() {  return this.m_info;  }

	@Override
	public void setInfo(HAPInfo info) {   this.m_info = info;  }

	@Override
	public HAPValueStructureDefinition getValueStructure() {   return  this.m_valueStructure;  } 

	@Override
	public void setValueStructure(HAPValueStructureDefinition valueStructure) {   this.m_valueStructure = valueStructure;  }

	@Override
	public String getName() {   return this.m_name;   }

	@Override
	public void setName(String name) {  this.m_name = name;   }

	@Override
	public String getGroupType() {   return this.m_groupType;   }

	@Override
	public void setGroupType(String groupType) {   this.m_groupType = groupType;  }

}
