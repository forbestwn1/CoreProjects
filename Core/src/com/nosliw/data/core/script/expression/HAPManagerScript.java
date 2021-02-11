package com.nosliw.data.core.script.expression;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.utils.HAPProcessTracker;
import com.nosliw.data.core.component.attachment.HAPContainerAttachment;
import com.nosliw.data.core.resource.HAPManagerResourceDefinition;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.script.expression.resource.HAPResourceDefinitionScriptGroup;


public class HAPManagerScript {
	
	private HAPRuntimeEnvironment m_runtimeEnv;

	public HAPManagerScript(HAPRuntimeEnvironment runtimeEnv) {
		this.m_runtimeEnv = runtimeEnv;
	}
	
	public HAPResourceDefinitionScriptGroup getScriptDefinition(HAPResourceId resourceId, HAPContainerAttachment parentAttachment) {
		return (HAPResourceDefinitionScriptGroup)this.getResourceDefinitionManager().getAdjustedComplextResourceDefinition(resourceId, parentAttachment);
	}

	public HAPExecutableScriptGroup getScript(HAPResourceId resourceId, Map<String, String> configure) {
		HAPResourceDefinitionScriptGroup scriptGroupDef = this.getScriptDefinition(resourceId, null);
		
		if(configure==null) {
			//build configure from definition info
			configure = new LinkedHashMap<String, String>();
			for(String n : scriptGroupDef.getInfo().getNames()) {
				configure.put(n, (String)scriptGroupDef.getInfo().getValue(n)); 
			}
		}
		
		HAPExecutableScriptGroup out = HAPProcessorScript.processScript(scriptGroupDef, null, this.m_runtimeEnv.getExpressionManager(), configure, this.m_runtimeEnv, new HAPProcessTracker());
		return out;
	}
	
	private HAPManagerResourceDefinition getResourceDefinitionManager() {     return this.m_runtimeEnv.getResourceDefinitionManager();   }
	
}
