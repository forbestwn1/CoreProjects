package com.nosliw.data.core.template;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class HAPManagerTemplate {

	private Map<String, HAPBuilderResourceDefinition> m_builders;
	
	public HAPManagerTemplate() {
		this.m_builders = new LinkedHashMap<String, HAPBuilderResourceDefinition>();
	}
	
	public HAPOutputBuilder build(String builderId, Set<HAPParmDefinition> parms) {
		HAPBuilderResourceDefinition builder = this.getResourceBuilder(builderId);
		HAPOutputBuilder out = builder.build(parms);
		return out;
	}

	private HAPBuilderResourceDefinition getResourceBuilder(String builderId) {		return this.m_builders.get(builderId);	}
	
}
