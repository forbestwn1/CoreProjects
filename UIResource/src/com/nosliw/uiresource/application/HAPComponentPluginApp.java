package com.nosliw.uiresource.application;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPFileUtility;
import com.nosliw.data.core.component.HAPComponent;
import com.nosliw.data.core.component.HAPPluginComponent;
import com.nosliw.data.core.resource.HAPResourceIdSimple;

public class HAPComponentPluginApp implements HAPPluginComponent{

	private HAPParseMiniApp m_miniAppParser;
	
	public HAPComponentPluginApp(HAPParseMiniApp miniAppParser) {
		this.m_miniAppParser = miniAppParser;
	}
	
	@Override
	public String getComponentType() {   return HAPConstant.RUNTIME_RESOURCE_TYPE_UIAPP;  }

	@Override
	public HAPComponent getComponent(HAPResourceIdSimple resourceId) {
		String file = HAPFileUtility.getMiniAppFolder()+resourceId.getId()+".res";
		HAPDefinitionApp miniAppDef = m_miniAppParser.parseFile(file);
		return miniAppDef;
	}
}
