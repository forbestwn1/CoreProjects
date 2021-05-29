package com.nosliw.uiresource.page.execute;

import com.nosliw.data.core.valuestructure.HAPValueStructureDefinitionGroup;

public class HAPValueStructureGroupInUITag extends HAPValueStructureDefinitionGroup{

	private HAPExecutableUIUnitTag m_uiTag; 
	
	public HAPValueStructureGroupInUITag(HAPExecutableUIUnitTag uiTag) {
		this.m_uiTag = uiTag;
	}

	public HAPValueStructureGroupInUITag(HAPExecutableUIUnitTag uiTag, HAPValueStructureDefinitionGroup contextGroup) {
		this.m_uiTag = uiTag;
		contextGroup.cloneTo(this);
	}
	
	public void clear() {
		this.m_uiTag = null;
	}
	
	@Override
	public HAPValueStructureDefinitionGroup getParent() {		
		return this.m_uiTag.getParent().getBody().getValueStructureDefinition();	
	}
	
	@Override
	public void setParent(HAPValueStructureDefinitionGroup parent) {  throw new RuntimeException();  }

	
}
