package com.nosliw.data.core.domain;

import org.json.JSONObject;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.common.HAPWithValueStructure;
import com.nosliw.data.core.component.HAPWithAttachment;
import com.nosliw.data.core.domain.entity.HAPDefinitionEntityInDomain;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public abstract class HAPPluginEntityDefinitionInDomainImpComplex extends HAPPluginEntityDefinitionInDomainImp{

	public HAPPluginEntityDefinitionInDomainImpComplex(Class<? extends HAPDefinitionEntityInDomain> entityClass, HAPRuntimeEnvironment runtimeEnv) {
		super(entityClass, runtimeEnv);
	}

	@Override
	public boolean isComplexEntity() {    return true;   }
	
	@Override
	protected void parseDefinitionContent(HAPIdEntityInDomain entityId, Object obj, HAPDomainEntityDefinitionLocal definitionDomain) {
		JSONObject jsonObj = (JSONObject)obj;
		this.parseNormalSimpleEntityAttribute(jsonObj, entityId, HAPWithAttachment.ATTACHMENT, HAPConstantShared.RUNTIME_RESOURCE_TYPE_ATTACHMENT, null, definitionDomain);
		this.parseNormalSimpleEntityAttribute(jsonObj, entityId, HAPWithValueStructure.VALUESTRUCTURE, HAPConstantShared.RUNTIME_RESOURCE_TYPE_VALUESTRUCTURE, null, definitionDomain);

//		HAPUtilityParserComplex.parseValueStructureInComplex(entityId, jsonObj, definitionDomain, this.getRuntimeEnvironment().getDomainEntityManager());
//		HAPUtilityParserComplex.parseAttachmentInComplex(entityId, jsonObj, definitionDomain, this.getRuntimeEnvironment().getDomainEntityManager());

		this.parseComplexDefinitionContent(entityId, jsonObj, definitionDomain);
	}
	
	abstract protected void parseComplexDefinitionContent(HAPIdEntityInDomain entityId, JSONObject jsonObj, HAPDomainEntityDefinitionLocal definitionDomain);

}
