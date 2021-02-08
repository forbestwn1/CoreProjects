package com.nosliw.uiresource.resource;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.component.HAPPluginResourceDefinition;
import com.nosliw.data.core.resource.HAPResourceDefinition;
import com.nosliw.data.core.resource.HAPResourceIdSimple;
import com.nosliw.data.core.resource.HAPResourceUtility;
import com.nosliw.data.core.system.HAPSystemFolderUtility;
import com.nosliw.uiresource.page.definition.HAPDefinitionUIPage;
import com.nosliw.uiresource.page.definition.HAPParserPage;

public class HAPResourceDefinitionPluginPage implements HAPPluginResourceDefinition{

	private HAPParserPage m_pageParser;
	
	public HAPResourceDefinitionPluginPage(HAPParserPage pageParser) {
		this.m_pageParser = pageParser;
	}
	
	@Override
	public HAPResourceDefinition getResource(HAPResourceIdSimple resourceId) {
		//read content
		String file = HAPResourceUtility.isFileBased(resourceId);
		if(file==null) {
			file = HAPSystemFolderUtility.getUIPageFolder()+resourceId.getId()+".res";
		}
		
		//parse content
		HAPDefinitionUIPage uiResourceDef = m_pageParser.parseFile(file);
		return uiResourceDef;
	}

	@Override
	public String getResourceType() {		return HAPConstantShared.RUNTIME_RESOURCE_TYPE_UIRESOURCE;	}

	@Override
	public HAPResourceDefinition parseResourceDefinition(Object content) {
		String str = (String)content;
		return this.m_pageParser.parseUIDefinition(null, str);
	}

}
