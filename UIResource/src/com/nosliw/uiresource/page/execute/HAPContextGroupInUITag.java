package com.nosliw.uiresource.page.execute;

import com.nosliw.data.core.structure.value.HAPStructureValueDefinitionGroup;

public class HAPContextGroupInUITag extends HAPStructureValueDefinitionGroup{

	private HAPExecutableUIUnitTag m_uiTag; 
	
	public HAPContextGroupInUITag(HAPExecutableUIUnitTag uiTag) {
		this.m_uiTag = uiTag;
	}

	public HAPContextGroupInUITag(HAPExecutableUIUnitTag uiTag, HAPStructureValueDefinitionGroup contextGroup) {
		this.m_uiTag = uiTag;
		contextGroup.cloneTo(this);
	}
	
	public void clear() {
		this.m_uiTag = null;
	}
	
	@Override
	public HAPStructureValueDefinitionGroup getParent() {		
		return this.m_uiTag.getParent().getBody().getContext();	
	}
	
	@Override
	public void setParent(HAPStructureValueDefinitionGroup parent) {  throw new RuntimeException();  }

	
}
