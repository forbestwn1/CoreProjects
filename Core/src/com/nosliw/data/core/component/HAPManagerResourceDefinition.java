package com.nosliw.data.core.component;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.resource.HAPResourceDefinition;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.resource.HAPResourceIdSimple;

public class HAPManagerResourceDefinition {

	private Map<String, HAPPluginResourceDefinition> m_plugins;
	
	public HAPManagerResourceDefinition() {
		this.m_plugins = new LinkedHashMap<String, HAPPluginResourceDefinition>();
	}
	
	public HAPResourceDefinition getResourceDefinition(HAPResourceId resourceId) {
		HAPResourceDefinition out = null;
		String structure = resourceId.getStructure();
		if(structure.equals(HAPConstant.RESOURCEID_TYPE_SIMPLE)) {
			HAPResourceIdSimple simpleId = (HAPResourceIdSimple)resourceId;
			String type = simpleId.getType();
			out = this.m_plugins.get(type).getResource(simpleId);
		}
		
		//set resource id
		out.setResourceId(resourceId);
		return out;
	}
	
	public void registerPlugin(HAPPluginResourceDefinition plugin) {
		this.m_plugins.put(plugin.getResourceType(), plugin);
	}
}
