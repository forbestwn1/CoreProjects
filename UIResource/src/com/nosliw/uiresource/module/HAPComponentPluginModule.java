package com.nosliw.uiresource.module;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPFileUtility;
import com.nosliw.data.core.component.HAPComponent;
import com.nosliw.data.core.component.HAPPluginComponent;
import com.nosliw.data.core.resource.HAPResourceIdSimple;

public class HAPComponentPluginModule implements HAPPluginComponent{

	private HAPParserModule m_moduleParser;
	
	public HAPComponentPluginModule(HAPParserModule moduleParser) {
		this.m_moduleParser = moduleParser;
	}
	
	@Override
	public String getComponentType() {	return HAPConstant.RUNTIME_RESOURCE_TYPE_UIMODULE;	}

	@Override
	public HAPComponent getComponent(HAPResourceIdSimple resourceId) {
		String file = HAPFileUtility.getUIModuleFolder()+resourceId.getId()+".res";
		HAPDefinitionModule moduleDef = m_moduleParser.parseFile(file);
		return moduleDef;
	}

}
