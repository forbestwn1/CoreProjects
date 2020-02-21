package com.nosliw.data.core.process.resource;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.component.HAPManagerResourceDefinition;
import com.nosliw.data.core.component.HAPPluginResourceDefinition;
import com.nosliw.data.core.process.HAPDefinitionProcessSuite;
import com.nosliw.data.core.process.HAPDefinitionProcess;
import com.nosliw.data.core.resource.HAPResourceDefinition;
import com.nosliw.data.core.resource.HAPResourceIdSimple;

public class HAPResourceDefinitionPluginProcess implements HAPPluginResourceDefinition{

	private HAPManagerResourceDefinition m_resourceDefMan;
	
	public HAPResourceDefinitionPluginProcess(HAPManagerResourceDefinition resourceDefMan) {
		this.m_resourceDefMan = resourceDefMan;
	}
	
	@Override
	public String getResourceType() {	return HAPConstant.RUNTIME_RESOURCE_TYPE_PROCESS;	}

	@Override
	public HAPResourceDefinition getResource(HAPResourceIdSimple resourceId) {
		HAPResourceIdProcess processResourceId = new HAPResourceIdProcess(resourceId);
		HAPResourceIdSimple processSuiteResourceId = processResourceId.getProcessSuiteResourceId();
		HAPDefinitionProcessSuite processSuiteDef = (HAPDefinitionProcessSuite)this.m_resourceDefMan.getResourceDefinition(processSuiteResourceId);
		return new HAPDefinitionProcess(processSuiteDef, processResourceId.getProcessId().getProcessId());
	}

}
