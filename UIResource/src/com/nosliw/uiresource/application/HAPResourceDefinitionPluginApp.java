package com.nosliw.uiresource.application;

import org.json.JSONObject;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPFileUtility;
import com.nosliw.data.core.component.HAPPluginResourceDefinition;
import com.nosliw.data.core.resource.HAPResourceDefinition;
import com.nosliw.data.core.resource.HAPResourceIdSimple;

public class HAPResourceDefinitionPluginApp implements HAPPluginResourceDefinition{

	private HAPParseMiniApp m_miniAppParser;
	
	public HAPResourceDefinitionPluginApp(HAPParseMiniApp miniAppParser) {
		this.m_miniAppParser = miniAppParser;
	}
	
	@Override
	public String getResourceType() {   return HAPConstant.RUNTIME_RESOURCE_TYPE_UIAPP;  }

	@Override
	public HAPResourceDefinition getResource(HAPResourceIdSimple resourceId) {
		String file = HAPFileUtility.getMiniAppFolder()+resourceId.getId()+".res";
		HAPDefinitionApp miniAppDef = m_miniAppParser.parseFile(file);
		return miniAppDef;
	}

	@Override
	public HAPResourceDefinition parseResourceDefinition(Object content) {
		JSONObject jsonObj = (JSONObject)content;
		return this.m_miniAppParser.parseMiniApp(jsonObj);
	}
}
