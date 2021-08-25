package com.nosliw.uiresource.page.definition;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.component.HAPDefinitionComponent;

public class HAPDefinitionUITag extends HAPDefinitionUIUnit{

	//name of this customer tag
	private String m_tagName;

	public HAPDefinitionUITag(String tagName, String id){
		super(id);
		this.m_tagName = tagName;
	}
	
	public String getTagName(){	return this.m_tagName;}
	
	@Override
	public String getType() {
		return HAPConstantShared.UIRESOURCE_TYPE_TAG;
	}

	@Override
	public HAPDefinitionComponent cloneComponent() {
		// TODO Auto-generated method stub
		return null;
	}

}
