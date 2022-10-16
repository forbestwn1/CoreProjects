package com.nosliw.data.core.complex;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.common.HAPWithValueStructure;
import com.nosliw.data.core.component.HAPWithAttachment;
import com.nosliw.data.core.domain.HAPContextParser;
import com.nosliw.data.core.domain.HAPDomainEntityDefinitionLocal;
import com.nosliw.data.core.domain.HAPEmbededWithIdDefinition;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.HAPInfoEntityInDomainDefinition;
import com.nosliw.data.core.domain.HAPManagerDomainEntityDefinition;
import com.nosliw.data.core.domain.HAPUtilityParserEntity;

public class HAPUtilityParserComplex {

	//parse value structure in complex
	public static void parseValueStructureInComplex(HAPIdEntityInDomain complexEntityId, JSONObject entityJsonObj, HAPDomainEntityDefinitionLocal definitionDomain, HAPManagerDomainEntityDefinition domainEntityManager) {
		HAPInfoEntityInDomainDefinition complexEntityInfo = definitionDomain.getEntityInfoDefinition(complexEntityId);
		
		//parse value structure
		JSONObject valueStructureJsonObj = entityJsonObj.optJSONObject(HAPWithValueStructure.VALUESTRUCTURE);
		HAPEmbededWithIdDefinition valueStructureEntity = HAPUtilityParserEntity.parseEmbededEntity(valueStructureJsonObj, HAPConstantShared.RUNTIME_RESOURCE_TYPE_VALUESTRUCTURECOMPLEX, new HAPContextParser(definitionDomain, complexEntityInfo.getBaseLocationPath()), domainEntityManager);
		
		((HAPDefinitionEntityInDomainComplex)complexEntityInfo.getEntity()).setValueStructureComplexEntity(valueStructureEntity);
	}
	
	//parse attachment in complex
	public static void parseAttachmentInComplex(HAPIdEntityInDomain complexEntityId, JSONObject entityJsonObj, HAPDomainEntityDefinitionLocal definitionDomain, HAPManagerDomainEntityDefinition domainEntityManager) {
		HAPInfoEntityInDomainDefinition complexEntityInfo = definitionDomain.getEntityInfoDefinition(complexEntityId);
		
		//parse attachment
		JSONObject attachmentJsonObj = entityJsonObj.optJSONObject(HAPWithAttachment.ATTACHMENT);
		HAPEmbededWithIdDefinition attachmentEntity = HAPUtilityParserEntity.parseEmbededEntity(attachmentJsonObj, HAPConstantShared.RUNTIME_RESOURCE_TYPE_ATTACHMENT, new HAPContextParser(definitionDomain, complexEntityInfo.getBaseLocationPath()), domainEntityManager);

		((HAPDefinitionEntityInDomainComplex)complexEntityInfo.getEntity()).setAttachmentContainerEntity(attachmentEntity);
	}

	//parse content in container entity
	public static void parseContentInComplexContainerEntity(HAPIdEntityInDomain entityId, JSONObject jsonObj, String elementEntityType, HAPConfigureParentRelationComplex parentRelation, HAPContextParser parserContext) {
		HAPDefinitionEntityComplexContainer containerEntity  = (HAPDefinitionEntityComplexContainer)parserContext.getDefinitionDomain().getComplexEntityInfo(entityId).getComplexEntity();
		
		HAPManagerDomainEntityDefinition domainEntityMan = parserContext.getRuntimeEnvironment().getDomainEntityManager();
		
		//parse complex part
		parseContentInComplexEntity(entityId, jsonObj, parserContext);
		
		//parse element
		JSONArray eleArray = jsonObj.getJSONArray(HAPDefinitionEntityComplexContainer.ELEMENT);
		for(int i=0; i<eleArray.length(); i++){
			JSONObject eleObjJson = eleArray.getJSONObject(i);
			HAPIdEntityInDomain eleEntityId = HAPUtilityParserEntity.parseEntity(eleObjJson, elementEntityType, parserContext);
			containerEntity.addEntityElement(eleEntityId);
			
			//build parent relation for complex child
			HAPConfigureEntityInDomainComplex entityInfo = (HAPConfigureEntityInDomainComplex)parserContext.getDefinitionDomain().getEntityInfoDefinition(eleEntityId);
			entityInfo.setParentId(entityId);
			entityInfo.setParentRelationConfigure(parentRelation);
		}
	}
	
	
}
