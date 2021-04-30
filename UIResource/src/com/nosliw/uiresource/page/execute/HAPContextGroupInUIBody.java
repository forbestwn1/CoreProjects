package com.nosliw.uiresource.page.execute;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.structure.value.HAPStructureValueDefinitionGroup;

public class HAPContextGroupInUIBody extends HAPStructureValueDefinitionGroup{

	private HAPExecutableUIUnit m_uiUnit; 
	
	public HAPContextGroupInUIBody(HAPExecutableUIUnit uiUnit) {
		this.m_uiUnit = uiUnit;
	}
	
	public HAPContextGroupInUIBody(HAPExecutableUIUnit uiUnit, HAPStructureValueDefinitionGroup contextGroup) {
		this.m_uiUnit = uiUnit;
		this.setContext(contextGroup);
	}
	
	public void setContext(HAPStructureValueDefinitionGroup contextGroup) {
		this.empty();
		contextGroup.cloneTo(this);
	}
	
	@Override
	public HAPStructureValueDefinitionGroup getParent() {
		HAPStructureValueDefinitionGroup out = null;
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
	public void setParent(HAPStructureValueDefinitionGroup parent) {  throw new RuntimeException();  }

}
