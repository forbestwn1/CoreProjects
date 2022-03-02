package com.nosliw.data.core.domain;

import org.json.JSONObject;

import com.nosliw.data.core.complex.HAPUtilityParserComplex;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public abstract class HAPPluginEntityDefinitionInDomainComplex extends HAPPluginEntityDefinitionInDomainImp{

	public HAPPluginEntityDefinitionInDomainComplex(Class<? extends HAPDefinitionEntityInDomain> entityClass, HAPRuntimeEnvironment runtimeEnv) {
		super(entityClass, runtimeEnv);
	}

	@Override
	protected void parseDefinitionContent(HAPIdEntityInDomain entityId, Object obj, HAPDomainDefinitionEntity definitionDomain) {
		JSONObject jsonObj = (JSONObject)obj;
		HAPUtilityParserComplex.parseValueStructureInComplex(entityId, jsonObj, definitionDomain, this.getRuntimeEnvironment().getDomainEntityManager());
		HAPUtilityParserComplex.parseAttachmentInComplex(entityId, jsonObj, definitionDomain, this.getRuntimeEnvironment().getDomainEntityManager());
		this.parseComplexDefinitionContent(entityId, jsonObj, definitionDomain);
	}
	
	abstract protected void parseComplexDefinitionContent(HAPIdEntityInDomain entityId, JSONObject jsonObj, HAPDomainDefinitionEntity definitionDomain);

}
