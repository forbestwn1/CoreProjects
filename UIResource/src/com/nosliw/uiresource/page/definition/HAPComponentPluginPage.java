package com.nosliw.uiresource.page.definition;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPFileUtility;
import com.nosliw.data.core.component.HAPComponent;
import com.nosliw.data.core.component.HAPPluginComponent;
import com.nosliw.data.core.resource.HAPResourceIdSimple;

public class HAPComponentPluginPage implements HAPPluginComponent{

	private HAPParserPage m_pageParser;
	
	public HAPComponentPluginPage(HAPParserPage pageParser) {
		this.m_pageParser = pageParser;
	}
	
	@Override
	public HAPComponent getComponent(HAPResourceIdSimple resourceId) {
		String file = HAPFileUtility.getUIPageFolder()+resourceId.getId()+".res";
		HAPDefinitionUIPage uiResourceDef = m_pageParser.parseFile(file);
		return uiResourceDef;
	}

	@Override
	public String getComponentType() {		return HAPConstant.RUNTIME_RESOURCE_TYPE_UIRESOURCE;	}

}
