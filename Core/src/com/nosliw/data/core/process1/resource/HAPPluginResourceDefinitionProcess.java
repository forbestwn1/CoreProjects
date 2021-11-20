package com.nosliw.data.core.process1.resource;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.resource.HAPManagerResourceDefinition;
import com.nosliw.data.core.resource.HAPPluginResourceDefinition;
import com.nosliw.data.core.resource.HAPResourceDefinition1;
import com.nosliw.data.core.resource.HAPResourceIdLocal;
import com.nosliw.data.core.resource.HAPResourceIdSimple;

public class HAPPluginResourceDefinitionProcess implements HAPPluginResourceDefinition{

	private HAPManagerResourceDefinition m_resourceDefMan;
	
	public HAPPluginResourceDefinitionProcess(HAPManagerResourceDefinition resourceDefMan) {
		this.m_resourceDefMan = resourceDefMan;
	}
	
	@Override
	public String getResourceType() {	return HAPConstantShared.RUNTIME_RESOURCE_TYPE_PROCESS;	}

	@Override
	public HAPResourceDefinition1 getResourceEntityBySimpleResourceId(HAPResourceIdSimple resourceId) {
		HAPResourceIdProcess processResourceId = new HAPResourceIdProcess(resourceId);
		HAPResourceIdSimple processSuiteResourceId = processResourceId.getProcessSuiteResourceId();
		HAPResourceDefinitionProcessSuite processSuiteDef = (HAPResourceDefinitionProcessSuite)this.m_resourceDefMan.getResourceDefinition(processSuiteResourceId);
		return new HAPResourceDefinitionProcess(processSuiteDef, processResourceId.getProcessId().getProcessId());
	}

	@Override
	public HAPResourceDefinition1 getResourceEntityByLocalResourceId(HAPResourceIdLocal resourceId, HAPResourceDefinition1 relatedResource) {
		HAPIdProcess processId = new HAPIdProcess(resourceId.getName());
		
		HAPResourceIdLocal suiteResourceId = new HAPResourceIdLocal(HAPConstantShared.RUNTIME_RESOURCE_TYPE_PROCESSSUITE);
		suiteResourceId.setName(processId.getSuiteId());
		suiteResourceId.setBasePath(resourceId.getBasePath());
		suiteResourceId.setSupplement(resourceId.getSupplement());
		
		HAPResourceDefinitionProcessSuite processSuiteDef = (HAPResourceDefinitionProcessSuite)this.m_resourceDefMan.getResourceDefinition(suiteResourceId);
		return new HAPResourceDefinitionProcess(processSuiteDef, processId.getProcessId());
	}

	@Override
	public HAPResourceDefinition1 parseResourceEntity(Object content) {
		// TODO Auto-generated method stub
		return null;
	}

}
