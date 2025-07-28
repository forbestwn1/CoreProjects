package com.nosliw.data.core.domain.entity.configure;

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

public class HAPResourceManagerImpConfigure extends HAPResourceManagerImp{

	private HAPManagerResourceDefinition m_resourceDefMan;
	private HAPManagerDomainEntityDefinition m_entityDefMan;
	
	public HAPResourceManagerImpConfigure(HAPManagerDomainEntityDefinition entityDefMan, HAPManagerResourceDefinition resourceDefMan, HAPManagerResource rootResourceMan) {
		super(rootResourceMan);
		this.m_resourceDefMan = resourceDefMan;
		this.m_entityDefMan = entityDefMan;
	}

	@Override
	public HAPResource getResource(HAPResourceId resourceId, HAPRuntimeInfo runtimeInfo) {
		HAPDomainEntityDefinitionGlobal entityDomain = new HAPDomainEntityDefinitionGlobal(m_entityDefMan, m_resourceDefMan);
		HAPResourceDefinition resourceDef = m_resourceDefMan.getResourceDefinition(resourceId, entityDomain);
		HAPDefinitionEntityConfigure configureEntity = (HAPDefinitionEntityConfigure)entityDomain.getEntityInfoDefinition(resourceDef.getEntityId()).getAdapter();
		
		HAPExecutableConfigure configureExe = new HAPExecutableConfigure();
		configureExe.setScript(configureEntity.getScript());

		return new HAPResource(resourceId, configureExe.toResourceData(runtimeInfo), HAPUtilityResource.buildResourceLoadPattern(resourceId, null));
	}
}
