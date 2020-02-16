package com.nosliw.uiresource.page.definition;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.process.plugin.HAPManagerActivityPlugin;

public class HAPDefinitionUITag extends HAPDefinitionUIUnit{

	//name of this customer tag
	private String m_tagName;

	public HAPDefinitionUITag(String tagName, String id, HAPManagerActivityPlugin activityPluginMan){
		super(id, activityPluginMan);
		this.m_tagName = tagName;
	}
	
	public String getTagName(){	return this.m_tagName;}
	
	@Override
	public String getType() {
		return HAPConstant.UIRESOURCE_TYPE_TAG;
	}

}
