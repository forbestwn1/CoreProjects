package com.nosliw.uiresource.page.definition;

import java.util.Map;

import com.nosliw.data.core.component.HAPDefinitionEmbededComponent;

public class HAPDefinitionUITag extends HAPDefinitionEmbededComponent{

	//name of this customer tag
	private String m_tagName;

	private HAPDefinitionUIUnit m_uiUnit;
	
	private Map<String, String> m_attributes;
	
	public HAPDefinitionUITag(String tagName, String id){
		this.m_tagName = tagName;
	}
	
	public String getTagName(){	return this.m_tagName;  }
	

}
