package com.nosliw.uiresource.resource;

import org.json.JSONObject;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.component.HAPPluginResourceDefinition;
import com.nosliw.data.core.resource.HAPResourceDefinition;
import com.nosliw.data.core.resource.HAPResourceIdSimple;
import com.nosliw.data.core.system.HAPSystemFolderUtility;
import com.nosliw.uiresource.module.HAPDefinitionModule;
import com.nosliw.uiresource.module.HAPParserModule;

public class HAPResourceDefinitionPluginModule implements HAPPluginResourceDefinition{

	private HAPParserModule m_moduleParser;
	
	public HAPResourceDefinitionPluginModule(HAPParserModule moduleParser) {
		this.m_moduleParser = moduleParser;
	}
	
	@Override
	public String getResourceType() {	return HAPConstant.RUNTIME_RESOURCE_TYPE_UIMODULE;	}

	@Override
	public HAPResourceDefinition getResource(HAPResourceIdSimple resourceId) {
		String file = HAPSystemFolderUtility.getUIModuleFolder()+resourceId.getId()+".res";
		HAPDefinitionModule moduleDef = m_moduleParser.parseFile(file);
		return moduleDef;
	}

	@Override
	public HAPResourceDefinition parseResourceDefinition(Object content) {
		JSONObject jsonObj = (JSONObject)content;
		return this.m_moduleParser.parseModuleDefinition(jsonObj);
	}

}
