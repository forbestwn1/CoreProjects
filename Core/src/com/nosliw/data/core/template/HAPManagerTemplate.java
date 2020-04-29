package com.nosliw.data.core.template;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.nosliw.data.core.component.HAPManagerResourceDefinition;
import com.nosliw.data.core.resource.HAPResourceDefinition;
import com.nosliw.data.core.template.resource.HAPResourceDefinitionTemplate;
import com.nosliw.data.core.template.resource.HAPResourceIdTemplate;
import com.nosliw.data.core.template.resource.HAPTemplateId;

public class HAPManagerTemplate {

	private HAPManagerResourceDefinition m_resourceDefManager;
	
	private Map<String, HAPBuilderResourceDefinition> m_builders;
	
	public HAPManagerTemplate(HAPManagerResourceDefinition resourceDefManager) {
		this.m_resourceDefManager = resourceDefManager;
		this.m_builders = new LinkedHashMap<String, HAPBuilderResourceDefinition>();
	}

	public HAPResourceDefinition buildResource(String builderId, Set<HAPParmDefinition> parms) {
		HAPOutputBuilder output = this.tryBuildResource(builderId, parms);
		return output.getResourceDefinition();
	}
	
	public HAPOutputBuilder tryBuildResource(String builderId, Set<HAPParmDefinition> parms) {
		HAPBuilderResourceDefinition builder = this.getResourceBuilder(builderId);
		HAPOutputBuilder out = builder.build(parms);
		HAPUtility.exportTemplateResourceDefinition(builderId, builder.getResourceType(), out.getParmsInfo(), out.getResourceDefinition());
		return out;
	}

	public HAPResourceDefinitionTemplate getTemplate(String id) {
		return (HAPResourceDefinitionTemplate)this.m_resourceDefManager.getResourceDefinition(new HAPResourceIdTemplate(new HAPTemplateId(id)));
	}
	
	private HAPBuilderResourceDefinition getResourceBuilder(String builderId) {		return this.m_builders.get(builderId);	}
}
