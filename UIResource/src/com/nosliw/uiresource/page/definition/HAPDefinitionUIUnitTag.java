package com.nosliw.uiresource.page.definition;

import com.nosliw.common.utils.HAPConstant;

public class HAPDefinitionUIUnitTag extends HAPDefinitionUIUnit{

	//name of this customer tag
	private String m_tagName;

	public HAPDefinitionUIUnitTag(String tagName, String id){
		super(id);
		this.m_tagName = tagName;
	}
	
	public String getTagName(){	return this.m_tagName;}
	
	@Override
	public String getType() {
		return HAPConstant.UIRESOURCE_TYPE_TAG;
	}
}
