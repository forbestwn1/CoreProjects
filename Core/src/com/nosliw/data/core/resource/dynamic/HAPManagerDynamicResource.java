package com.nosliw.data.core.resource.dynamic;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.nosliw.data.core.resource.HAPResourceDefinition;

public class HAPManagerDynamicResource {

	private Map<String, HAPBuilderResourceDefinition> m_builders;
	
	public HAPManagerDynamicResource() {
		this.m_builders = new LinkedHashMap<String, HAPBuilderResourceDefinition>();
	}

	public HAPResourceDefinition buildResource(String builderId, Set<HAPParmDefinition> parms) {
		HAPOutputBuilder output = this.tryBuildResource(builderId, parms);
		return output.getResourceDefinition();
	}
	
	public HAPOutputBuilder tryBuildResource(String builderId, Set<HAPParmDefinition> parms) {
		HAPBuilderResourceDefinition builder = this.getResourceBuilder(builderId);
		HAPOutputBuilder out = builder.build(parms);
		HAPUtility.exportDynamicResourceDefinition(builderId, builder.getResourceType(), out.getParmsInfo(), out.getResourceDefinition());
		return out;
	}
	
	public void registerDynamicResourceBuilder(String id, HAPBuilderResourceDefinition builder) {
		this.m_builders.put(id, builder);
	}

	private HAPBuilderResourceDefinition getResourceBuilder(String builderId) {		return this.m_builders.get(builderId);	}
}
