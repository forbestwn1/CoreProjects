package com.nosliw.uiresource.page.execute;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.script.context.HAPContextGroup;

public class HAPContextGroupInUIResource extends HAPContextGroup{

	private HAPExecutableUIUnit m_uiUnit; 
	
	public HAPContextGroupInUIResource(HAPExecutableUIUnit uiUnit) {
		this.m_uiUnit = uiUnit;
	}
	
	public HAPContextGroupInUIResource(HAPExecutableUIUnit uiUnit, HAPContextGroup contextGroup) {
		this.m_uiUnit = uiUnit;
		contextGroup.cloneTo(this);
	}
	
	public void clear() {
		this.m_uiUnit = null;
	}
	
	@Override
	public HAPContextGroup getParent() {
		HAPContextGroup out = null;
		String unitType = this.m_uiUnit.getType();
		switch(unitType) {
		case HAPConstant.UIRESOURCE_TYPE_RESOURCE:
			break;
		case HAPConstant.UIRESOURCE_TYPE_TAG:
			out = ((HAPExecutableUIUnitTag)this.m_uiUnit).getTagContext();
			break;
		}
		return out;
	}
	
	@Override
	public void setParent(HAPContextGroup parent) {  throw new RuntimeException();  }

}
