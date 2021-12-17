package com.nosliw.data.core.complex;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.common.HAPWithValueStructure;
import com.nosliw.data.core.component.HAPWithAttachment;
import com.nosliw.data.core.domain.HAPContextParser;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.HAPInfoDefinitionEntityInDomainComplex;
import com.nosliw.data.core.domain.HAPManagerDomainEntity;
import com.nosliw.data.core.domain.HAPUtilityParserEntity;

public class HAPUtilityComplexParser {

	//parse content in complex entity (value structure, attachment)
	public static void parseContentInComplexEntity(HAPIdEntityInDomain entityId, JSONObject jsonObj, HAPContextParser parserContext) {
		
		HAPDefinitionEntityComplex complexEntity = parserContext.getDefinitionDomain().getComplexEntityInfo(entityId).getComplexEntity();
		
		HAPManagerDomainEntity domainEntityMan = parserContext.getRuntimeEnvironment().getDomainEntityManager();
		
		HAPIdEntityInDomain valueStructureEntityId = HAPUtilityParserEntity.parseEntity(jsonObj.optJSONObject(HAPWithValueStructure.VALUESTRUCTURE), HAPConstantShared.RUNTIME_RESOURCE_TYPE_VALUESTRUCTURECOMPLEX, parserContext);
		complexEntity.setValueStructureComplexId(valueStructureEntityId);
		
		HAPIdEntityInDomain attachmentEntityId = HAPUtilityParserEntity.parseEntity(jsonObj.optJSONObject(HAPWithAttachment.ATTACHMENT), HAPConstantShared.RUNTIME_RESOURCE_TYPE_ATTACHMENT, parserContext);
		complexEntity.setValueStructureComplexId(attachmentEntityId);
		
	}
	
	//parse content in container entity
	public static void parseContentInComplexContainerEntity(HAPIdEntityInDomain entityId, JSONObject jsonObj, String elementEntityType, HAPConfigureParentRelationComplex parentRelation, HAPContextParser parserContext) {
		HAPDefinitionEntityComplexContainer containerEntity  = (HAPDefinitionEntityComplexContainer)parserContext.getDefinitionDomain().getComplexEntityInfo(entityId).getComplexEntity();;
		
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
