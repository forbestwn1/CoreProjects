package com.nosliw.data.core.domain;

import org.json.JSONObject;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.common.HAPWithValueStructure;
import com.nosliw.data.core.complex.HAPDefinitionEntityComplex;
import com.nosliw.data.core.component.HAPWithAttachment;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public abstract class HAPPluginEntityDefinitionInDomainComplex extends HAPPluginEntityDefinitionInDomainImp{

	public HAPPluginEntityDefinitionInDomainComplex(Class<? extends HAPDefinitionEntityInDomain> entityClass, HAPRuntimeEnvironment runtimeEnv) {
		super(entityClass, runtimeEnv);
	}

	@Override
	protected void parseDefinitionContent(HAPIdEntityInDomain entityId, JSONObject jsonObj, HAPDomainDefinitionEntity definitionDomain) {
		this.parseValueStructure(entityId, jsonObj, definitionDomain);
		this.parseAttachment(entityId, jsonObj, definitionDomain);
		this.parseComplexDefinitionContent(entityId, jsonObj, definitionDomain);
	}
	
	protected void parseValueStructure(HAPIdEntityInDomain complexEntityId, JSONObject entityJsonObj, HAPDomainDefinitionEntity definitionDomain) {
		HAPInfoDefinitionEntityInDomain complexEntityInfo = definitionDomain.getEntityInfo(complexEntityId);
		
		//value structure
		JSONObject valueStructureJsonObj = entityJsonObj.optJSONObject(HAPWithValueStructure.VALUESTRUCTURE);
		
		HAPIdEntityInDomain valueStructureEntityId = this.getRuntimeEnvironment().getDomainEntityManager().parseDefinition(HAPConstantShared.RUNTIME_RESOURCE_TYPE_VALUESTRUCTURECOMPLEX, valueStructureJsonObj, new HAPContextParser(definitionDomain, complexEntityInfo.getLocalBaseReference()));
		((HAPDefinitionEntityComplex)complexEntityInfo.getEntity()).setValueStructureComplexId(valueStructureEntityId);
	}
	
	protected void parseAttachment(HAPIdEntityInDomain complexEntityId, JSONObject entityJsonObj, HAPDomainDefinitionEntity definitionDomain) {
		HAPInfoDefinitionEntityInDomain complexEntityInfo = definitionDomain.getEntityInfo(complexEntityId);
		
		//value structure
		JSONObject valueStructureJsonObj = entityJsonObj.optJSONObject(HAPWithAttachment.ATTACHMENT);
		
		HAPIdEntityInDomain attachmentEntityId = this.getRuntimeEnvironment().getDomainEntityManager().parseDefinition(HAPConstantShared.RUNTIME_RESOURCE_TYPE_ATTACHMENT, valueStructureJsonObj, new HAPContextParser(definitionDomain, complexEntityInfo.getLocalBaseReference()));
		((HAPDefinitionEntityComplex)complexEntityInfo.getEntity()).setAttachmentContainerId(attachmentEntityId);
	}

	abstract protected void parseComplexDefinitionContent(HAPIdEntityInDomain entityId, JSONObject jsonObj, HAPDomainDefinitionEntity definitionDomain);

}
