package com.nosliw.core.application.division.manual;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.info.HAPEntityInfo;
import com.nosliw.common.info.HAPUtilityEntityInfo;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPUtilityFile;
import com.nosliw.core.application.HAPIdBrick;
import com.nosliw.core.application.HAPIdBrickType;
import com.nosliw.core.application.HAPManagerApplicationBrick;
import com.nosliw.core.application.HAPUtilityBrick;
import com.nosliw.data.core.domain.HAPContextParser;
import com.nosliw.data.core.domain.HAPDomainEntityDefinitionLocalComplex;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.HAPInfoEntityInDomainDefinition;
import com.nosliw.data.core.domain.HAPInfoParentComplex;
import com.nosliw.data.core.domain.attachment.HAPReferenceAttachment;
import com.nosliw.data.core.domain.definition.HAPManagerDomainEntityDefinition;
import com.nosliw.data.core.domain.entity.HAPConfigureParentRelationComplex;
import com.nosliw.data.core.domain.entity.HAPEmbeded;
import com.nosliw.data.core.domain.entity.HAPEmbededDefinition;
import com.nosliw.data.core.domain.entity.HAPInfoAdapterDefinition;
import com.nosliw.data.core.resource.HAPFactoryResourceId;
import com.nosliw.data.core.resource.HAPManagerResourceDefinition;
import com.nosliw.data.core.resource.HAPResourceId;

public class HAPUtilityParserBrickFormatJson {

	public static HAPManualWrapperBrick parseEntityInfo(JSONObject jsonObj, HAPIdBrickType entityTypeIfNotProvided, HAPManualContextParse parseContext, HAPManualManagerBrick manualDivisionEntityMan, HAPManagerApplicationBrick entityManager) {
		HAPManualWrapperBrick out = new HAPManualWrapperBrick();
		
		//try with definition
		Object entityTypeObj = jsonObj.opt(HAPManualWithBrick.BRICKTYPEID);   //if entity type is defined in entity, then override provided
		HAPIdBrickType entityTypeId = parseEntityTypeId(entityTypeObj, entityTypeIfNotProvided, entityManager);
		
		Object entityObj = jsonObj.opt(HAPManualWithBrick.BRICK);
		if(entityObj==null)
		{
			entityObj = jsonObj;    //if no entity node, then using root
		}
		HAPManualBrick entityDef = manualDivisionEntityMan.parseEntityDefinition(entityObj, entityTypeId, HAPSerializationFormat.JSON, parseContext);
		out.setEntity(entityDef);
		
		Object infoObj = jsonObj.opt(HAPManualWrapperBrick.INFO);
		out.buildEntityInfoByJson(infoObj);
		
		return out;
	}
	
	public static HAPManualAttribute parseAttribute(String attrName, JSONObject jsonObj, HAPIdBrickType entityTypeIfNotProvided, HAPIdBrickType adapterTypeId, HAPManualContextParse parseContext, HAPManualManagerBrick manualDivisionEntityMan, HAPManagerApplicationBrick entityManager) {
		HAPManualAttribute out = null;
		
		HAPEntityInfo info = HAPUtilityEntityInfo.buildEntityInfoFromJson(jsonObj, HAPManualAttribute.INFO);
		
		if(HAPUtilityEntityInfo.isEnabled(info)) {
			out = new HAPManualAttribute();
			
			//parse attribute value
			HAPManualWrapperValueInAttribute attrValueInfo = parseAttributeValueInfo(jsonObj, entityTypeIfNotProvided, parseContext, manualDivisionEntityMan, entityManager);
			out.setValueInfo(attrValueInfo);
			
			//parse info
			info.cloneToEntityInfo(out);
			if(attrName!=null) {
				out.setName(attrName);
			}
			
			//parse adapter
			Object adapterObjs = jsonObj.opt(HAPManualAttribute.ADAPTER);
			if(adapterObjs!=null) {
				List<HAPManualInfoAdapter> adaptersInfo = parseAdapter(adapterObjs, adapterTypeId, manualDivisionEntityMan, entityManager);
				for(HAPManualInfoAdapter adapterInfo : adaptersInfo) {
					out.addAdapter(adapterInfo);
				}
			}
			
			//parse relation
			Object relationObjs = jsonObj.opt(HAPManualAttribute.RELATION);
			if(relationObjs!=null) {
				JSONArray relationArray = (JSONArray)relationObjs;
				for(int i=0; i<relationArray.length(); i++) {
					out.addRelation(parseRelation(relationArray.getJSONObject(i)));
				}
			}
		}
		
		return out;
	}
	
	//parse entity as attribute value (value may be entity or reference(resource, attachment, local))
	public static HAPManualWrapperValueInAttribute parseAttributeValueInfo(JSONObject jsonObj, HAPIdBrickType entityTypeIfNotProvided, HAPManualContextParse parseContext, HAPManualManagerBrick manualDivisionEntityMan, HAPManagerApplicationBrick entityManager) {
		HAPManualWrapperValueInAttribute out = null;

		//try with definition
		Object entityTypeObj = jsonObj.opt(HAPManualWithBrick.BRICKTYPEID);   //if entity type is defined in entity, then override provided
		HAPIdBrickType entityTypeId = parseEntityTypeId(entityTypeObj, entityTypeIfNotProvided, entityManager);
		
		//local entity reference
		if(out==null) {
			Object entityRefObj = jsonObj.opt(HAPManualWrapperValueInAttributeReferenceBrick.BRICKREFERENCE);
			if(entityRefObj!=null) {
				HAPIdBrick entityId = HAPUtilityBrick.parseBrickIdAgressive(entityRefObj, parseContext.getBrickDivision(), entityManager); 
				out = new HAPManualWrapperValueInAttributeReferenceBrick(entityId);
				HAPManualBrick refEntity = parseLocalValue(parseContext.getBasePath(), entityId, manualDivisionEntityMan);
				((HAPManualWrapperValueInAttributeReferenceBrick)out).setBrick(refEntity);
			}
		}
		
		//resource id
		if(out==null) {
			Object resourceObj = jsonObj.opt(HAPManualWrapperValueInAttributeReferenceResource.RESOURCEID);
			if(resourceObj!=null) {
				HAPResourceId resourceId = HAPFactoryResourceId.tryNewInstance(entityTypeId.getBrickType(), entityTypeId.getVersion(), resourceObj);
				out = new HAPManualWrapperValueInAttributeReferenceResource(resourceId);
			}
		}
		
		//reference
		if(out==null) {
			Object referenceObj = jsonObj.opt(HAPManualWrapperValueInAttributeReferenceAttachment.REFERENCE);
			if(referenceObj!=null) {
				HAPReferenceAttachment reference = HAPReferenceAttachment.newInstance(referenceObj, entityTypeId.getBrickType());
				out = new HAPManualWrapperValueInAttributeReferenceAttachment(reference);
			}
		}

		//value
		if(out==null) {
			Object valueObj = jsonObj.opt(HAPManualWrapperValueInAttributeValue.VALUE);
			if(valueObj!=null) {
				out = new HAPManualWrapperValueInAttributeValue(valueObj);
			}
		}
		
		//entity
		if(out==null) {
			Object entityObj = jsonObj.opt(HAPManualWrapperValueInAttributeBrick.BRICK);
			if(entityObj==null)
			{
				entityObj = jsonObj;    //if no entity node, then using root
			}
			HAPManualBrick entityDef = manualDivisionEntityMan.parseEntityDefinition(entityObj, entityTypeId, HAPSerializationFormat.JSON, parseContext);
			out = new HAPManualWrapperValueInAttributeBrick(entityDef);
		}

		return out;
	}

	private static HAPIdBrickType parseEntityTypeId(Object entityTypeObj, HAPIdBrickType entityTypeIfNotProvided, HAPManagerApplicationBrick entityManager) {
		String entityType = null;
		String entityTypeVersion = null;
		if(entityTypeObj!=null) {
			HAPIdBrickType entityTypeId1 = HAPUtilityDefinitionBrick.parseEntityTypeId(entityTypeObj);
			entityType = entityTypeId1.getBrickType();
			entityTypeVersion = entityTypeId1.getVersion();
		}
		//try with entityTypeIfNotProvided
		if(entityTypeIfNotProvided!=null) {
			if(entityType==null) {
				entityType = entityTypeIfNotProvided.getBrickType();
			}
			if(entityTypeVersion==null) {
				entityTypeVersion = entityTypeIfNotProvided.getVersion();
			}
		}
		//if version not provided, then use latest version
		if(entityTypeVersion==null) {
			entityTypeVersion = entityManager.getLatestVersion(entityType).getVersion();
		}
		return new HAPIdBrickType(entityType, entityTypeVersion);
	}
	
	private static HAPManualBrick parseLocalValue(String basePath, HAPIdBrick entityId, HAPManualManagerBrick manualDivisionEntityMan) {
		HAPManualInfoBrickLocation entityLocationInfo = HAPUtilityBrickLocation.getEntityLocationInfo(entityId);
		String content = HAPUtilityFile.readFile(entityLocationInfo.getFiile());
		return manualDivisionEntityMan.parseEntityDefinition(content, entityId.getBrickTypeId(), entityLocationInfo.getFormat());
	}
	
	private static HAPManualBrickRelation parseRelation(JSONObject jsonObj) {
		HAPManualBrickRelation out = null;
		String type = jsonObj.getString(HAPManualBrickRelation.TYPE);
		if(type.equals(HAPConstantShared.MANUAL_RELATION_TYPE_VALUECONTEXT)) {
			out = new HAPManualBrickRelationValueContext();
		}
		else if(type.equals(HAPConstantShared.MANUAL_RELATION_TYPE_ATTACHMENT)) {
			out = new HAPManualBrickRelationAttachment();
		}
		else if(type.equals(HAPConstantShared.MANUAL_RELATION_TYPE_AUTOPROCESS)) {
			out = new HAPManualBrickRelationAutoProcess();
		}
		out.buildObject(jsonObj, HAPSerializationFormat.JSON);
		return out;
	}
	
	private static List<HAPManualInfoAdapter> parseAdapter(Object adaptersObj, HAPIdBrickType adatperTypeId, HAPManualManagerBrick textDivisionEntityMan, HAPManagerApplicationBrick entityManager){
		List<HAPManualInfoAdapter> out = new ArrayList<HAPManualInfoAdapter>();
		
		if(adaptersObj instanceof JSONArray) {
			JSONArray adaptersArray = (JSONArray)adaptersObj;
			for(int i=0; i<adaptersArray.length(); i++) {
				JSONObject adapterObj = adaptersArray.getJSONObject(i);
				if(HAPUtilityEntityInfo.isEnabled(adapterObj)) {
					HAPManualWrapperValueInAttribute adpaterEntityDefInfo = parseEntity(adapterObj, adatperTypeId, textDivisionEntityMan, entityManager);
					HAPManualInfoAdapter adapterInfo = new HAPManualInfoAdapter(adpaterEntityDefInfo);
					adapterInfo.buildEntityInfoByJson(adapterObj);
					if(adapterInfo.getName()==null) {
						adapterInfo.setName(HAPConstantShared.NAME_DEFAULT);
					}
					out.add(adapterInfo);
				}
			}
		}
		else if(adaptersObj instanceof JSONObject) {
			JSONObject adapterObj = (JSONObject)adaptersObj;
			if(HAPUtilityEntityInfo.isEnabled(adapterObj)) {
				HAPManualWrapperValueInAttribute adpaterEntityDefInfo = parseEntity(adapterObj, adatperTypeId, textDivisionEntityMan, entityManager);
				HAPManualInfoAdapter adapterInfo = new HAPManualInfoAdapter(adpaterEntityDefInfo);
				adapterInfo.buildEntityInfoByJson(adapterObj);
				if(adapterInfo.getName()==null) {
					adapterInfo.setName(HAPConstantShared.NAME_DEFAULT);
				}
				out.add(adapterInfo);
			}
		}
		
		return out;
	}
	
	private static String figureoutAdatperType(String entityType, String adapterType, HAPManagerDomainEntityDefinition domainEntityManager) {
		if(adapterType!=null) {
			return adapterType;
		} else {
			return domainEntityManager.getDefaultAdapterByEntity(entityType);
		}
	}


	
	
	public static HAPEmbededDefinition parseEmbededEntity(Object obj, HAPIdBrickType entityType, HAPContextParser parserContext, HAPManagerDomainEntityDefinition domainEntityManager, HAPManagerResourceDefinition resourceDefinitionManager) {
		return parseEmbededEntity(obj, entityType, null, parserContext, domainEntityManager, resourceDefinitionManager);
	}

	public static HAPEmbededDefinition parseEmbededEntity(Object obj, HAPIdBrickType entityType, HAPIdBrickType adapterType, HAPContextParser parserContext, HAPManagerDomainEntityDefinition domainEntityManager, HAPManagerResourceDefinition resourceDefinitionManager) {
		HAPEmbededDefinition out = null;
		if(obj instanceof JSONObject) {
			JSONObject jsonObj = (JSONObject)obj;
			
			//embeded entity
			HAPIdEntityInDomain entityId = parseAttributeValueInfo(getEntityObject(jsonObj), entityType, parserContext, domainEntityManager, resourceDefinitionManager); 

			if(entityId!=null) {
				out = new HAPEmbededDefinition(entityId); 
				//adapter
				Object adapterObjs = jsonObj.opt(HAPEmbeded.ADAPTER);
				if(adapterObjs!=null) {
					List<HAPInfoAdapterDefinition> adapters = parseAdapter(adapterObjs, figureoutAdatperType(entityId.getEntityType(), adapterType,domainEntityManager), parserContext, domainEntityManager, resourceDefinitionManager);
					for(HAPInfoAdapterDefinition adapter : adapters) {
						out.addAdapter(adapter);
					}
				}
			}
		}
		return out;
	}

	public static HAPEmbededDefinition parseEmbededComplexEntity(JSONObject jsonObj, String entityType, HAPIdEntityInDomain parentEntityId, HAPConfigureParentRelationComplex parentRelationConfigureDefault, HAPContextParser parserContext, HAPManagerDomainEntityDefinition domainEntityManager, HAPManagerResourceDefinition resourceDefinitionManager) {
		return parseEmbededComplexEntity(jsonObj, entityType, null, parentEntityId, parentRelationConfigureDefault, parserContext, domainEntityManager, resourceDefinitionManager);		
	}
	
	public static HAPEmbededDefinition parseEmbededComplexEntity(JSONObject jsonObj, String entityType, String adapterType, HAPIdEntityInDomain parentEntityId, HAPConfigureParentRelationComplex parentRelationConfigureDefault, HAPContextParser parserContext, HAPManagerDomainEntityDefinition domainEntityManager, HAPManagerResourceDefinition resourceDefinitionManager) {
		return parseEmbededComplexEntity(jsonObj, entityType, adapterType, parentEntityId, null, parentRelationConfigureDefault, parserContext, domainEntityManager, resourceDefinitionManager);		
	}
	
	public static HAPEmbededDefinition parseEmbededComplexEntity(JSONObject jsonObj, String entityType, String adapterType, HAPIdEntityInDomain parentEntityId, HAPConfigureParentRelationComplex parentRelationConfigureExternal, HAPConfigureParentRelationComplex parentRelationConfigureDefault, HAPContextParser parserContext, HAPManagerDomainEntityDefinition domainEntityManager, HAPManagerResourceDefinition resourceDefinitionManager) {
		HAPEmbededDefinition out = null;
		//embeded entity
		HAPIdEntityInDomain entityId = parseComplexEntity(getEntityObject(jsonObj), entityType, parentEntityId, parentRelationConfigureExternal, parentRelationConfigureDefault, parserContext, domainEntityManager, resourceDefinitionManager); 
		
		if(entityId!=null) {
			out = new HAPEmbededDefinition(entityId);
			//adapter
			Object adapterObjs = jsonObj.opt(HAPEmbeded.ADAPTER);
			if(adapterObjs!=null) {
				List<HAPInfoAdapterDefinition> adapters = parseAdapter(adapterObjs, figureoutAdatperType(entityId.getEntityType(), adapterType, domainEntityManager), parserContext, domainEntityManager, resourceDefinitionManager);
				for(HAPInfoAdapterDefinition adapter : adapters) {
					out.addAdapter(adapter);
				}
			}
		}
		return out;
	}


	private static HAPIdEntityInDomain parseComplexEntity(JSONObject jsonObj, String entityType, HAPIdEntityInDomain parentEntityId, HAPConfigureParentRelationComplex parentRelationConfigureExternal, HAPConfigureParentRelationComplex parentRelationConfigureDefault, HAPContextParser parserContext, HAPManagerDomainEntityDefinition domainEntityManager, HAPManagerResourceDefinition resourceDefinitionManager) {
		//entity itself
		HAPIdEntityInDomain out = parseAttributeValueInfo(jsonObj, entityType, parserContext, domainEntityManager, resourceDefinitionManager);

		if(out!=null && parentEntityId!=null) {
			//parent relation
			HAPInfoParentComplex parentInfo = new HAPInfoParentComplex();

			//parent id
			parentInfo.setParentId(parentEntityId);

			//parent relation configure
			HAPConfigureParentRelationComplex parentRelationConfigure = parentInfo.getParentRelationConfigure();
			
			//default configure
			parentRelationConfigure.mergeHard(parentRelationConfigureDefault);

			//external configure
			parentRelationConfigure.mergeHard(parentRelationConfigureExternal);

			//customer configure
			JSONObject parentRelationConfigureObjCustomer = jsonObj.optJSONObject(HAPInfoEntityInDomainDefinition.PARENT);
			HAPConfigureParentRelationComplex customerConfigure = null;
			if(parentRelationConfigureObjCustomer!=null) {
				customerConfigure = new HAPConfigureParentRelationComplex();
				customerConfigure.buildObject(parentRelationConfigureObjCustomer, HAPSerializationFormat.JSON);
			}
			parentRelationConfigure.mergeHard(customerConfigure);
			
			((HAPDomainEntityDefinitionLocalComplex)parserContext.getCurrentDomain()).buildComplexParentRelation(out, parentInfo);
		}
		
		return out;
	}

}
