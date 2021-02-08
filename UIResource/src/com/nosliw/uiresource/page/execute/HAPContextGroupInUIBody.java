package com.nosliw.uiresource.page.execute;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.script.context.HAPContextGroup;

public class HAPContextGroupInUIBody extends HAPContextGroup{

	private HAPExecutableUIUnit m_uiUnit; 
	
	public HAPContextGroupInUIBody(HAPExecutableUIUnit uiUnit) {
		this.m_uiUnit = uiUnit;
	}
	
	public HAPContextGroupInUIBody(HAPExecutableUIUnit uiUnit, HAPContextGroup contextGroup) {
		this.m_uiUnit = uiUnit;
		this.setContext(contextGroup);
	}
	
	public void setContext(HAPContextGroup contextGroup) {
		this.empty();
		contextGroup.cloneTo(this);
	}
	
	@Override
	public HAPContextGroup getParent() {
		HAPContextGroup out = null;
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
	public void setParent(HAPContextGroup parent) {  throw new RuntimeException();  }

}
