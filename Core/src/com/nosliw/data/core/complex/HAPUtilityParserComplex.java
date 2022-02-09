package com.nosliw.data.core.complex;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.common.HAPWithValueStructure;
import com.nosliw.data.core.component.HAPWithAttachment;
import com.nosliw.data.core.domain.HAPContextParser;
import com.nosliw.data.core.domain.HAPDomainDefinitionEntity;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.HAPInfoDefinitionEntityInDomain;
import com.nosliw.data.core.domain.HAPInfoDefinitionEntityInDomainComplex;
import com.nosliw.data.core.domain.HAPManagerDomainEntity;
import com.nosliw.data.core.domain.HAPUtilityParserEntity;

public class HAPUtilityParserComplex {

	//parse value structure in complex
	public static void parseValueStructureInComplex(HAPIdEntityInDomain complexEntityId, JSONObject entityJsonObj, HAPDomainDefinitionEntity definitionDomain, HAPManagerDomainEntity domainEntityManager) {
		HAPInfoDefinitionEntityInDomain complexEntityInfo = definitionDomain.getEntityInfo(complexEntityId);
		
		//value structure
		JSONObject valueStructureJsonObj = entityJsonObj.optJSONObject(HAPWithValueStructure.VALUESTRUCTURE);
		
		HAPIdEntityInDomain valueStructureEntityId = domainEntityManager.parseDefinition(HAPConstantShared.RUNTIME_RESOURCE_TYPE_VALUESTRUCTURECOMPLEX, valueStructureJsonObj, new HAPContextParser(definitionDomain, complexEntityInfo.getLocalBaseReference()));
		((HAPDefinitionEntityComplex)complexEntityInfo.getEntity()).setValueStructureComplexId(valueStructureEntityId);
	}
	
	//parse attachment in complex
	public static void parseAttachmentInComplex(HAPIdEntityInDomain complexEntityId, JSONObject entityJsonObj, HAPDomainDefinitionEntity definitionDomain, HAPManagerDomainEntity domainEntityManager) {
		HAPInfoDefinitionEntityInDomain complexEntityInfo = definitionDomain.getEntityInfo(complexEntityId);
		
		//value structure
		JSONObject attachmentJsonObj = entityJsonObj.optJSONObject(HAPWithAttachment.ATTACHMENT);
		
		HAPIdEntityInDomain attachmentEntityId = domainEntityManager.parseDefinition(HAPConstantShared.RUNTIME_RESOURCE_TYPE_ATTACHMENT, attachmentJsonObj, new HAPContextParser(definitionDomain, complexEntityInfo.getLocalBaseReference()));
		((HAPDefinitionEntityComplex)complexEntityInfo.getEntity()).setAttachmentContainerId(attachmentEntityId);
	}

	//parse content in container entity
	public static void parseContentInComplexContainerEntity(HAPIdEntityInDomain entityId, JSONObject jsonObj, String elementEntityType, HAPConfigureParentRelationComplex parentRelation, HAPContextParser parserContext) {
		HAPDefinitionEntityComplexContainer containerEntity  = (HAPDefinitionEntityComplexContainer)parserContext.getDefinitionDomain().getComplexEntityInfo(entityId).getComplexEntity();
		
		HAPManagerDomainEntity domainEntityMan = parserContext.getRuntimeEnvironment().getDomainEntityManager();
		
		//parse complex part
		parseContentInComplexEntity(entityId, jsonObj, parserContext);
		
		//parse element
		JSONArray eleArray = jsonObj.getJSONArray(HAPDefinitionEntityComplexContainer.ELEMENT);
		for(int i=0; i<eleArray.length(); i++){
			JSONObject eleObjJson = eleArray.getJSONObject(i);
			HAPIdEntityInDomain eleEntityId = HAPUtilityParserEntity.parseEntity(eleObjJson, elementEntityType, parserContext);
			containerEntity.addEntityElement(eleEntityId);
			
			//build parent relation for complex child
			HAPInfoDefinitionEntityInDomainComplex entityInfo = (HAPInfoDefinitionEntityInDomainComplex)parserContext.getDefinitionDomain().getEntityInfo(eleEntityId);
			entityInfo.setParentId(entityId);
			entityInfo.setParentRelationConfigure(parentRelation);
		}
	}
	
	
}
