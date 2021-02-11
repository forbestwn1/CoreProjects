package com.nosliw.data.core.template;

import org.json.JSONObject;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.resource.HAPPluginResourceDefinition;
import com.nosliw.data.core.resource.HAPResourceDefinition;
import com.nosliw.data.core.resource.HAPResourceIdSimple;
import com.nosliw.data.core.system.HAPSystemFolderUtility;

public class HAPResourceDefinitionPluginTemplate implements HAPPluginResourceDefinition{

	
	public HAPResourceDefinitionPluginTemplate() {
	}
	
	@Override
	public HAPResourceDefinition getResource(HAPResourceIdSimple resourceId) {
		//read content
		String file = HAPSystemFolderUtility.getTemplateFolder()+resourceId.getId()+".template";
		//parse content
		HAPResourceDefinitionTemplate out = HAPParserTemplate.parseFile(file);
		return out;
	}

	@Override
	public String getResourceType() {		return HAPConstantShared.RUNTIME_RESOURCE_TYPE_TEMPLATE;	}

	@Override
	public HAPResourceDefinition parseResourceDefinition(Object content) {
		JSONObject jsonObj = (JSONObject)content;
		return HAPParserTemplate.parseTemplateDefinition(jsonObj);
	}

}
