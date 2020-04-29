package com.nosliw.data.core.template.resource;

import org.json.JSONObject;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.component.HAPPluginResourceDefinition;
import com.nosliw.data.core.resource.HAPResourceDefinition;
import com.nosliw.data.core.resource.HAPResourceIdSimple;
import com.nosliw.data.core.system.HAPSystemFolderUtility;

public class HAPResourceDefinitionPluginTemplate implements HAPPluginResourceDefinition{

	private HAPParserTemplate m_templateParser;
	
	public HAPResourceDefinitionPluginTemplate(HAPParserTemplate pageParser) {
		this.m_templateParser = pageParser;
	}
	
	@Override
	public HAPResourceDefinition getResource(HAPResourceIdSimple resourceId) {
		//read content
		String file = HAPSystemFolderUtility.getTemplateFolder()+resourceId.getId()+".template";
		//parse content
		HAPResourceDefinitionTemplate out = m_templateParser.parseFile(file);
		return out;
	}

	@Override
	public String getResourceType() {		return HAPConstant.RUNTIME_RESOURCE_TYPE_TEMPLATE;	}

	@Override
	public HAPResourceDefinition parseResourceDefinition(Object content) {
		JSONObject jsonObj = (JSONObject)content;
		return this.m_templateParser.parseTemplateDefinition(jsonObj);
	}

}
