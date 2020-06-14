package com.nosliw.uiresource.page.execute;

import com.nosliw.data.core.script.context.HAPContextGroup;

public class HAPContextGroupInUITag extends HAPContextGroup{

	private HAPExecutableUIUnitTag m_uiTag; 
	
	public HAPContextGroupInUITag(HAPExecutableUIUnitTag uiTag) {
		this.m_uiTag = uiTag;
	}

	public HAPContextGroupInUITag(HAPExecutableUIUnitTag uiTag, HAPContextGroup contextGroup) {
		this.m_uiTag = uiTag;
		contextGroup.cloneTo(this);
	}
	
	public void clear() {
		this.m_uiTag = null;
	}
	
	@Override
	public HAPContextGroup getParent() {		
		return this.m_uiTag.getParent().getBody().getContext();	
	}
	
	@Override
	public void setParent(HAPContextGroup parent) {  throw new RuntimeException();  }

	
}
