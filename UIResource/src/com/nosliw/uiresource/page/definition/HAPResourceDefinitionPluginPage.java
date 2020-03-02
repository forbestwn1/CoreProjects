package com.nosliw.uiresource.page.definition;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPFileUtility;
import com.nosliw.data.core.component.HAPPluginResourceDefinition;
import com.nosliw.data.core.resource.HAPResourceDefinition;
import com.nosliw.data.core.resource.HAPResourceIdSimple;

public class HAPResourceDefinitionPluginPage implements HAPPluginResourceDefinition{

	private HAPParserPage m_pageParser;
	
	public HAPResourceDefinitionPluginPage(HAPParserPage pageParser) {
		this.m_pageParser = pageParser;
	}
	
	@Override
	public HAPResourceDefinition getResource(HAPResourceIdSimple resourceId) {
		//read content
		String file = HAPFileUtility.getUIPageFolder()+resourceId.getId()+".res";
		//parse content
		HAPDefinitionUIPage uiResourceDef = m_pageParser.parseFile(file);
		return uiResourceDef;
	}

	@Override
	public String getResourceType() {		return HAPConstant.RUNTIME_RESOURCE_TYPE_UIRESOURCE;	}

	@Override
	public HAPResourceDefinition parseResourceDefinition(Object content) {
		String str = (String)content;
		return this.m_pageParser.parseUIDefinition(null, str);
	}

}
