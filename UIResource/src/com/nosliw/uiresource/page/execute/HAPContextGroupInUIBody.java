package com.nosliw.uiresource.page.execute;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.valuestructure.HAPValueStructureDefinitionGroup;

public class HAPContextGroupInUIBody extends HAPValueStructureDefinitionGroup{

	private HAPExecutableUIUnit m_uiUnit; 
	
	public HAPContextGroupInUIBody(HAPExecutableUIUnit uiUnit) {
		this.m_uiUnit = uiUnit;
	}
	
	public HAPContextGroupInUIBody(HAPExecutableUIUnit uiUnit, HAPValueStructureDefinitionGroup contextGroup) {
		this.m_uiUnit = uiUnit;
		this.setContext(contextGroup);
	}
	
	public void setContext(HAPValueStructureDefinitionGroup contextGroup) {
		this.empty();
		contextGroup.cloneTo(this);
	}
	
	@Override
	public HAPValueStructureDefinitionGroup getParent() {
		HAPValueStructureDefinitionGroup out = null;
		String unitType = this.m_uiUnit.getType();
		switch(unitType) {
		case HAPConstantShared.UIRESOURCE_TYPE_RESOURCE:
			break;
		case HAPConstantShared.UIRESOURCE_TYPE_TAG:
			out = ((HAPExecutableUIUnitTag)this.m_uiUnit).getTagContext();
			break;
		}
		return out;
	}
	
	@Override
	public void setParent(HAPValueStructureDefinitionGroup parent) {  throw new RuntimeException();  }

}
