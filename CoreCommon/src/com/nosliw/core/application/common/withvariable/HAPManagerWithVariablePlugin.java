package com.nosliw.core.application.common.withvariable;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HAPManagerWithVariablePlugin {

	private Map<String, HAPPluginProcessorEntityWithVariable<? extends HAPWithVariable>> m_withVariableProcessorPlugin;
	
	public HAPManagerWithVariablePlugin() {
		this.m_withVariableProcessorPlugin = new LinkedHashMap<String, HAPPluginProcessorEntityWithVariable<? extends HAPWithVariable>>();
	}

	@Autowired
	private void setWithVariableProcessPlugins(List<HAPPluginProcessorEntityWithVariable<? extends HAPWithVariable>> plugins) {
		for(HAPPluginProcessorEntityWithVariable<?> plugin : plugins) {
			this.m_withVariableProcessorPlugin.put(plugin.getEntityType(), plugin);
		}
	}
	
	public HAPPluginProcessorEntityWithVariable<? extends HAPWithVariable> getWithVariableEntityProcessPlugin(String entityType) {
		return this.m_withVariableProcessorPlugin.get(entityType);
	}
	
	public void registerWithVariableEntityProcessPlugin(HAPPluginProcessorEntityWithVariable<? extends HAPWithVariable> plugin) {    
		this.m_withVariableProcessorPlugin.put(plugin.getEntityType(), plugin);     
	}
	
}
