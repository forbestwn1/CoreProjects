package com.nosliw.data.core.script.expression;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPProcessTracker;
import com.nosliw.data.core.component.HAPContextProcessor;
import com.nosliw.data.core.domain.entity.attachment.HAPDefinitionEntityContainerAttachment;
import com.nosliw.data.core.resource.HAPManagerResourceDefinition;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.script.expression.resource.HAPResourceDefinitionScriptGroup;


public class HAPManagerScript {
	
	private HAPRuntimeEnvironment m_runtimeEnv;

	public HAPManagerScript(HAPRuntimeEnvironment runtimeEnv) {
		this.m_runtimeEnv = runtimeEnv;
	}
	
	public HAPResourceDefinitionScriptGroup getScriptDefinition(HAPResourceId resourceId, HAPDefinitionEntityContainerAttachment parentAttachment) {
		return (HAPResourceDefinitionScriptGroup)this.getResourceDefinitionManager().getAdjustedComplextResourceDefinition(resourceId, parentAttachment);
	}

	public HAPExecutableScriptGroup getScript(HAPResourceId resourceId, Map<String, String> configure) {
		HAPResourceDefinitionScriptGroup scriptGroupResourceDef = (HAPResourceDefinitionScriptGroup)this.m_runtimeEnv.getResourceDefinitionManager().getLocalResourceDefinition(resourceId);
		HAPContextProcessor contextProcess = new HAPContextProcessor(scriptGroupResourceDef, this.m_runtimeEnv);

		if(configure==null) {
			//build configure from definition info
			configure = new LinkedHashMap<String, String>();
			for(String n : scriptGroupResourceDef.getInfo().getNames()) {
				configure.put(n, (String)scriptGroupResourceDef.getInfo().getValue(n)); 
			}
		}
		
		HAPExecutableScriptGroup out = HAPProcessorScript.processScript(
						resourceId.toStringValue(HAPSerializationFormat.LITERATE), 
						scriptGroupResourceDef, 
						contextProcess, 
						configure, this.m_runtimeEnv, 
						new HAPProcessTracker()
		);
		return out;
	}
	
	private HAPManagerResourceDefinition getResourceDefinitionManager() {     return this.m_runtimeEnv.getResourceDefinitionManager();   }
	
}
