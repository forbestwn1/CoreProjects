package com.nosliw.uiresource.page.execute;

import com.nosliw.data.core.structure.value.HAPContextStructureValueDefinitionGroup;

public class HAPContextGroupInUITag extends HAPContextStructureValueDefinitionGroup{

	private HAPExecutableUIUnitTag m_uiTag; 
	
	public HAPContextGroupInUITag(HAPExecutableUIUnitTag uiTag) {
		this.m_uiTag = uiTag;
	}

	public HAPContextGroupInUITag(HAPExecutableUIUnitTag uiTag, HAPContextStructureValueDefinitionGroup contextGroup) {
		this.m_uiTag = uiTag;
		contextGroup.cloneTo(this);
	}
	
	public void clear() {
		this.m_uiTag = null;
	}
	
	@Override
	public HAPContextStructureValueDefinitionGroup getParent() {		
		return this.m_uiTag.getParent().getBody().getContext();	
	}
	
	@Override
	public void setParent(HAPContextStructureValueDefinitionGroup parent) {  throw new RuntimeException();  }

	
}
