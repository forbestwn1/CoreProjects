package com.nosliw.data.core.component;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.resource.HAPResourceIdSimple;

public class HAPManagerComponent {

	private Map<String, HAPPluginComponent> m_plugins;
	
	public HAPManagerComponent() {
		this.m_plugins = new LinkedHashMap<String, HAPPluginComponent>();
	}
	
	public HAPComponent getComponent(HAPResourceId resourceId) {
		HAPComponent out = null;
		String structure = resourceId.getStructure();
		if(structure.equals(HAPConstant.RESOURCEID_TYPE_SIMPLE)) {
			HAPResourceIdSimple simpleId = (HAPResourceIdSimple)resourceId;
			String type = simpleId.getType();
			out = this.m_plugins.get(type).getComponent(simpleId);
		}
		return out;
	}
	
	public void registerPlugin(HAPPluginComponent plugin) {
		this.m_plugins.put(plugin.getComponentType(), plugin);
	}
}
