package com.nosliw.data.core.domain.common.script;

import com.nosliw.core.application.common.script.HAPResourceDataScript;
import com.nosliw.data.core.domain.HAPDomainEntityDefinitionGlobal;
import com.nosliw.data.core.domain.definition.HAPManagerDomainEntityDefinition;
import com.nosliw.data.core.resource.HAPManagerResourceDefinition;
import com.nosliw.data.core.resource.HAPResource;
import com.nosliw.data.core.resource.HAPResourceDefinition;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.resource.HAPResourceManagerImp;
import com.nosliw.data.core.resource.HAPManagerResource;
import com.nosliw.data.core.resource.HAPUtilityResource;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;

public class HAPResourceManagerImpScriptBased extends HAPResourceManagerImp{

	private HAPManagerResourceDefinition m_resourceDefMan;
	private HAPManagerDomainEntityDefinition m_entityDefMan;
	
	public HAPResourceManagerImpScriptBased(HAPManagerDomainEntityDefinition entityDefMan, HAPManagerResourceDefinition resourceDefMan, HAPManagerResource rootResourceMan) {
		super(rootResourceMan);
		this.m_resourceDefMan = resourceDefMan;
		this.m_entityDefMan = entityDefMan;
	}

	@Override
	public HAPResource getResource(HAPResourceId resourceId, HAPRuntimeInfo runtimeInfo) {
		HAPDomainEntityDefinitionGlobal entityDomain = new HAPDomainEntityDefinitionGlobal(m_entityDefMan, m_resourceDefMan);
		HAPResourceDefinition resourceDef = m_resourceDefMan.getResourceDefinition(resourceId, entityDomain);
		HAPDefinitionEntityScriptBased scriptEntity = (HAPDefinitionEntityScriptBased)entityDomain.getEntityInfoDefinition(resourceDef.getEntityId()).getAdapter();
		
		HAPResourceDataScript scriptExe = new HAPResourceDataScript();
		scriptExe.setScript(scriptEntity.getScript());

		return new HAPResource(resourceId, scriptExe.toResourceData(runtimeInfo), HAPUtilityResource.buildResourceLoadPattern(resourceId, null));
	}
}
