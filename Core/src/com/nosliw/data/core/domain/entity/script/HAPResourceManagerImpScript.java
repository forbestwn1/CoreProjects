package com.nosliw.data.core.domain.entity.script;

import com.nosliw.data.core.domain.HAPDomainEntityDefinitionGlobal;
import com.nosliw.data.core.domain.HAPManagerDomainEntityDefinition;
import com.nosliw.data.core.resource.HAPManagerResourceDefinition;
import com.nosliw.data.core.resource.HAPResource;
import com.nosliw.data.core.resource.HAPResourceDefinition;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.resource.HAPResourceManagerImp;
import com.nosliw.data.core.resource.HAPResourceManagerRoot;
import com.nosliw.data.core.resource.HAPUtilityResource;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;

public class HAPResourceManagerImpScript extends HAPResourceManagerImp{

	private HAPManagerResourceDefinition m_resourceDefMan;
	private HAPManagerDomainEntityDefinition m_entityDefMan;
	
	public HAPResourceManagerImpScript(HAPManagerDomainEntityDefinition entityDefMan, HAPManagerResourceDefinition resourceDefMan, HAPResourceManagerRoot rootResourceMan) {
		super(rootResourceMan);
		this.m_resourceDefMan = resourceDefMan;
		this.m_entityDefMan = entityDefMan;
	}

	@Override
	public HAPResource getResource(HAPResourceId resourceId, HAPRuntimeInfo runtimeInfo) {
		HAPDomainEntityDefinitionGlobal entityDomain = new HAPDomainEntityDefinitionGlobal(m_entityDefMan, m_resourceDefMan);
		HAPResourceDefinition resourceDef = m_resourceDefMan.getResourceDefinition(resourceId, entityDomain);
		HAPDefinitionEntityScript scriptEntity = (HAPDefinitionEntityScript)entityDomain.getEntityInfoDefinition(resourceDef.getEntityId()).getEntity();
		
		HAPExecutableScript scriptExe = new HAPExecutableScript();
		scriptExe.setScript(scriptEntity.getScript());

		return new HAPResource(resourceId, scriptExe.toResourceData(runtimeInfo), HAPUtilityResource.buildResourceLoadPattern(resourceId, null));
	}
}
