package com.nosliw.uiresource.resource;

import org.json.JSONObject;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.resource.HAPPluginResourceDefinition;
import com.nosliw.core.resource.HAPResourceDefinition1;
import com.nosliw.core.resource.HAPResourceIdSimple;
import com.nosliw.core.system.HAPSystemFolderUtility;
import com.nosliw.uiresource.application.HAPDefinitionApp;
import com.nosliw.uiresource.application.HAPParseMiniApp;

public class HAPResourceDefinitionPluginApp implements HAPPluginResourceDefinition{

	private HAPParseMiniApp m_miniAppParser;
	
	public HAPResourceDefinitionPluginApp(HAPParseMiniApp miniAppParser) {
		this.m_miniAppParser = miniAppParser;
	}
	
	@Override
	public String getResourceType() {   return HAPConstantShared.RUNTIME_RESOURCE_TYPE_UIAPP;  }

	@Override
	public HAPResourceDefinition1 getResourceEntityBySimpleResourceId(HAPResourceIdSimple resourceId) {
		String file = HAPSystemFolderUtility.getMiniAppFolder()+resourceId.getId()+".res";
		HAPDefinitionApp miniAppDef = m_miniAppParser.parseFile(file);
		return miniAppDef;
	}

	@Override
	public HAPResourceDefinition1 parseResourceEntity(Object content) {
		JSONObject jsonObj = (JSONObject)content;
		return this.m_miniAppParser.parseMiniApp(jsonObj);
	}
}
