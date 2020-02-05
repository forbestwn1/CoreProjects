package com.nosliw.uiresource.module;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPFileUtility;
import com.nosliw.data.core.component.HAPPluginResourceDefinition;
import com.nosliw.data.core.resource.HAPResourceDefinition;
import com.nosliw.data.core.resource.HAPResourceIdSimple;

public class HAPResourceDefinitionPluginModule implements HAPPluginResourceDefinition{

	private HAPParserModule m_moduleParser;
	
	public HAPResourceDefinitionPluginModule(HAPParserModule moduleParser) {
		this.m_moduleParser = moduleParser;
	}
	
	@Override
	public String getResourceType() {	return HAPConstant.RUNTIME_RESOURCE_TYPE_UIMODULE;	}

	@Override
	public HAPResourceDefinition getResource(HAPResourceIdSimple resourceId) {
		String file = HAPFileUtility.getUIModuleFolder()+resourceId.getId()+".res";
		HAPDefinitionModule moduleDef = m_moduleParser.parseFile(file);
		return moduleDef;
	}

}