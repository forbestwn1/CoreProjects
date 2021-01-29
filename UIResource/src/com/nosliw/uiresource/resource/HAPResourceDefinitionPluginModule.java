package com.nosliw.uiresource.resource;

import java.io.File;

import org.json.JSONObject;

import com.nosliw.common.exception.HAPErrorUtility;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.component.HAPLocalReferenceBase;
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
		String basePath = null;
		File file;
		//check single file first
		basePath = HAPSystemFolderUtility.getUIModuleFolder();
		file = new File(basePath+resourceId.getId()+".res");
		if(!file.exists()) {
			basePath = HAPSystemFolderUtility.getUIModuleFolder()+resourceId.getId()+"/";
			file = new File(basePath+"/main.res");
			if(!file.exists()) {
				HAPErrorUtility.invalid("Cannot find module resource " + resourceId.getId());
			}
		}
		
		HAPDefinitionModule moduleDef = m_moduleParser.parseFile(file);
		moduleDef.setLocalReferenceBase(new HAPLocalReferenceBase(basePath));
		return moduleDef;
	}

	@Override
	public HAPResourceDefinition parseResourceDefinition(Object content) {
		JSONObject jsonObj = (JSONObject)content;
		return this.m_moduleParser.parseModuleDefinition(jsonObj);
	}

}
