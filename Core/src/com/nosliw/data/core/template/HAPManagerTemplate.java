package com.nosliw.data.core.template;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.resource.HAPResourceDefinition;

public class HAPManagerTemplate {

	private Map<String, HAPBuilderResourceDefinition> m_builders;
	
	public HAPManagerTemplate() {
		this.m_builders = new LinkedHashMap<String, HAPBuilderResourceDefinition>();
	}
	
	private HAPBuilderResourceDefinition getResourceBuilder(String builderId) {		return this.m_builders.get(builderId);	}
	
	private HAPDefinitionTemplate getTemplateDefinition(String templateId) {
		return null;
	}
	
	public HAPResourceDefinition build(String builderId, Map<String, HAPData> parms) {
		HAPDefinitionTemplate templateDef = this.getTemplateDefinition(builderId);
		HAPBuilderResourceDefinition builder = this.getResourceBuilder(templateDef.getBuilderId());
		HAPBuilderOutput buildOutput = builder.build(parms);
		return out;
	}
	
}
