package com.nosliw.uiresource.page.execute;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.valuestructure.HAPValueStructure;
import com.nosliw.data.core.valuestructure.HAPValueStructureDefinitionGroup;

public class HAPValueStructureGroupInUIBody extends HAPValueStructureDefinitionGroup{

	private HAPExecutableUIUnit m_uiUnit; 
	
	public HAPValueStructureGroupInUIBody(HAPExecutableUIUnit uiUnit) {
		this.m_uiUnit = uiUnit;
	}
	
	public HAPValueStructureGroupInUIBody(HAPExecutableUIUnit uiUnit, HAPValueStructureDefinitionGroup valueStructureGroup) {
		this.m_uiUnit = uiUnit;
		this.setValueStructure(valueStructureGroup);
	}
	
	public void setValueStructure(HAPValueStructureDefinitionGroup contextGroup) {
		this.empty();
		contextGroup.cloneToValueStructureDefinitionGroup(this);
	}
	
	@Override
	public HAPValueStructureDefinitionGroup getParent() {
		HAPValueStructureDefinitionGroup out = null;
		String unitType = this.m_uiUnit.getType();
		switch(unitType) {
		case HAPConstantShared.UIRESOURCE_TYPE_RESOURCE:
			break;
		case HAPConstantShared.UIRESOURCE_TYPE_TAG:
			out = ((HAPExecutableUIUnitTag)this.m_uiUnit).getTagValueStructureDefinition();
			break;
		}
		return out;
	}
	
	@Override
	public void setParent(HAPValueStructure parent) {  throw new RuntimeException();  }

}
