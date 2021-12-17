package com.nosliw.data.core.complex;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.data.core.component.HAPContextProcessor;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.HAPPluginEntityDefinitionInDomain;

public class HAPManagerComplexEntity {

	private Map<String, HAPPluginEntityDefinitionInDomain> m_processorPlugins;

	public HAPManagerComplexEntity() {
		this.m_processorPlugins = new LinkedHashMap<String, HAPPluginEntityDefinitionInDomain>();
	}
	
	public HAPIdEntityInDomain process(HAPIdEntityInDomain complexEntityDefinitionId, HAPContextProcessor processContext) {
		HAPPluginEntityDefinitionInDomain processPlugin = this.m_processorPlugins.get(complexEntityDefinitionId.getEntityType());
		HAPIdEntityInDomain out = processPlugin.processDefinition(complexEntityDefinitionId, processContext);
		return out;
	}
	
	public void registerProcessorPlugin(String complexEntityType, HAPPluginEntityDefinitionInDomain processorPlugin) {
		this.m_processorPlugins.put(complexEntityType, processorPlugin);
	}
	
}
