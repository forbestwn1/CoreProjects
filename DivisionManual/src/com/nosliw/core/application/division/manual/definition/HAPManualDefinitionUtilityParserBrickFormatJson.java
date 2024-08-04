package com.nosliw.core.application.division.manual.definition;

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
import com.nosliw.core.application.HAPUtilityBrickId;
import com.nosliw.core.application.common.parentrelation.HAPManualDefinitionBrickRelation;
import com.nosliw.core.application.division.manual.HAPManualManagerBrick;
import com.nosliw.core.application.division.manual.HAPManualWithBrick;
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

public class HAPManualDefinitionUtilityParserBrickFormatJson {

	public static HAPManualDefinitionWrapperBrick parseBrickWrapper(JSONObject jsonObj, HAPIdBrickType entityTypeIfNotProvided, HAPManualDefinitionContextParse parseContext, HAPManualManagerBrick manualDivisionEntityMan, HAPManagerApplicationBrick entityManager) {
		HAPManualDefinitionWrapperBrick out = new HAPManualDefinitionWrapperBrick();
		
		//try with definition
		Object brickTypeObj = jsonObj.opt(HAPManualWithBrick.BRICKTYPEID);   //if entity type is defined in entity, then override provided
		HAPIdBrickType brickTypeId = HAPUtilityBrickId.parseBrickTypeId(brickTypeObj, entityTypeIfNotProvided, entityManager);
		
		Object brickObj = jsonObj.opt(HAPManualWithBrick.BRICK);
		if(brickObj==null)
		{
			brickObj = jsonObj;    //if no entity node, then using root
		}
		HAPManualDefinitionBrick brickDef = manualDivisionEntityMan.parseBrickDefinition(brickObj, brickTypeId, HAPSerializationFormat.JSON, parseContext);
		out.setBrick(brickDef);
		
		Object infoObj = jsonObj.opt(HAPManualDefinitionWrapperBrick.INFO);
		out.buildEntityInfoByJson(infoObj);
		
		return out;
	}
	
	public static HAPManualDefinitionAttributeInBrick parseAttribute(String attrName, JSONObject jsonObj, HAPIdBrickType entityTypeIfNotProvided, HAPIdBrickType adapterTypeId, HAPManualDefinitionContextParse parseContext, HAPManualManagerBrick manualDivisionEntityMan, HAPManagerApplicationBrick entityManager) {
		HAPManualDefinitionAttributeInBrick out = null;
		
		HAPEntityInfo info = HAPUtilityEntityInfo.buildEntityInfoFromJson(jsonObj, HAPManualDefinitionAttributeInBrick.INFO);
		
		if(HAPUtilityEntityInfo.isEnabled(info)) {
			out = new HAPManualDefinitionAttributeInBrick();
			
			//parse attribute value
			HAPManualDefinitionWrapperValue attrValueInfo = parseWrapperValue(jsonObj, entityTypeIfNotProvided, parseContext, manualDivisionEntityMan, entityManager);
			out.setValueWrapper(attrValueInfo);
			
			//parse info
			info.cloneToEntityInfo(out);
			if(attrName!=null) {
				out.setName(attrName);
			}
			
			//parse adapter
			Object adaptersObj = jsonObj.opt(HAPManualDefinitionAttributeInBrick.ADAPTER);
			if(adaptersObj!=null) {
				if(adaptersObj instanceof JSONArray) {
					JSONArray adaptersArray = (JSONArray)adaptersObj;
					for(int i=0; i<adaptersArray.length(); i++) {
						HAPManualDefinitionAdapter adapterInfo = parseAdapter(adaptersArray.getJSONObject(i), adapterTypeId, parseContext, manualDivisionEntityMan, entityManager);
						if(adapterInfo!=null) {
							out.addAdapter(adapterInfo);
						}
					}
				}
				else if(adaptersObj instanceof JSONObject) {
					HAPManualDefinitionAdapter adapterInfo = parseAdapter((JSONObject)adaptersObj, adapterTypeId, parseContext, manualDivisionEntityMan, entityManager);
					if(adapterInfo!=null) {
						out.addAdapter(adapterInfo);
					}
				}
			}
			
			//parse relation
			Object relationObjs = jsonObj.opt(HAPManualDefinitionAttributeInBrick.RELATION);
			if(relationObjs!=null) {
				JSONArray relationArray = (JSONArray)relationObjs;
				for(int i=0; i<relationArray.length(); i++) {
					out.addRelation(HAPManualDefinitionBrickRelation.parseRelation(relationArray.getJSONObject(i)));
				}
			}
		}
		
		return out;
	}
	
	//parse entity as attribute value (value may be entity or reference(resource, attachment, local))
	public static HAPManualDefinitionWrapperValue parseWrapperValue(JSONObject jsonObj, HAPIdBrickType brickTypeIfNotProvided, HAPManualDefinitionContextParse parseContext, HAPManualManagerBrick manualDivisionEntityMan, HAPManagerApplicationBrick entityManager) {
		HAPManualDefinitionWrapperValue out = null;

		//try with definition
		Object entityTypeObj = jsonObj.opt(HAPManualWithBrick.BRICKTYPEID);   //if entity type is defined in entity, then override provided
		HAPIdBrickType entityTypeId = HAPUtilityBrickId.parseBrickTypeId(entityTypeObj, brickTypeIfNotProvided, entityManager);
		
		//local entity reference
		if(out==null) {
			Object entityRefObj = jsonObj.opt(HAPManualDefinitionWrapperValueReferenceBrick.BRICKREFERENCE);
			if(entityRefObj!=null) {
				HAPIdBrick entityId = HAPUtilityBrickId.parseBrickIdAgressive(entityRefObj, brickTypeIfNotProvided, parseContext.getBrickDivision(), entityManager); 
				HAPManualDefinitionBrick refBrick = parseLocalValue(entityId, parseContext, manualDivisionEntityMan);
				out = new HAPManualDefinitionWrapperValueBrick(refBrick);
			}
		}
		
		//resource id
		if(out==null) {
			Object resourceObj = jsonObj.opt(HAPManualDefinitionWrapperValueReferenceResource.RESOURCEID);
			if(resourceObj!=null) {
				HAPResourceId resourceId = HAPFactoryResourceId.tryNewInstance(entityTypeId.getBrickType(), entityTypeId.getVersion(), resourceObj);
				out = new HAPManualDefinitionWrapperValueReferenceResource(resourceId);
			}
		}
		
		//reference
		if(out==null) {
			Object referenceObj = jsonObj.opt(HAPManualDefinitionWrapperValueReferenceAttachment.REFERENCE);
			if(referenceObj!=null) {
				HAPReferenceAttachment reference = HAPReferenceAttachment.newInstance(referenceObj, entityTypeId.getBrickType());
				out = new HAPManualDefinitionWrapperValueReferenceAttachment(reference);
			}
		}

		//value
		if(out==null) {
			Object valueObj = jsonObj.opt(HAPManualDefinitionWrapperValueValue.VALUE);
			if(valueObj!=null) {
				out = new HAPManualDefinitionWrapperValueValue(valueObj);
			}
		}
		
		//entity
		if(out==null) {
			Object entityObj = jsonObj.opt(HAPManualDefinitionWrapperValueBrick.BRICK);
			if(entityObj==null)
			{
				entityObj = jsonObj;    //if no entity node, then using root
			}
			HAPManualDefinitionBrick entityDef = manualDivisionEntityMan.parseBrickDefinition(entityObj, entityTypeId, HAPSerializationFormat.JSON, parseContext);
			out = new HAPManualDefinitionWrapperValueBrick(entityDef);
		}

		return out;
	}

	private static HAPManualDefinitionBrick parseLocalValue(HAPIdBrick entityId, HAPManualDefinitionContextParse parseContext, HAPManualManagerBrick manualDivisionEntityMan) {
		HAPManualDefinitionInfoBrickLocation entityLocationInfo = HAPManualDefinitionUtilityBrickLocation.getLocalEntityLocationInfo(parseContext.getBasePath(), entityId);
		String content = HAPUtilityFile.readFile(entityLocationInfo.getFiile());
		return manualDivisionEntityMan.parseBrickDefinitionWrapper(content, entityId.getBrickTypeId(), entityLocationInfo.getFormat(), parseContext).getBrick();
	}
	
	private static HAPManualDefinitionAdapter parseAdapter(JSONObject adapterObj, HAPIdBrickType adatperTypeId, HAPManualDefinitionContextParse parseContext, HAPManualManagerBrick textDivisionEntityMan, HAPManagerApplicationBrick entityManager) {
		HAPManualDefinitionAdapter adapterInfo = null;
		if(HAPUtilityEntityInfo.isEnabled(adapterObj)) {
			HAPManualDefinitionWrapperValue adpaterEntityDefInfo = parseWrapperValue(adapterObj, adatperTypeId, parseContext, textDivisionEntityMan, entityManager);
			adapterInfo = new HAPManualDefinitionAdapter(adpaterEntityDefInfo);
			adapterInfo.buildEntityInfoByJson(adapterObj);
			if(adapterInfo.getName()==null) {
				adapterInfo.setName(HAPConstantShared.NAME_DEFAULT);
			}
		}
		return adapterInfo;
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
			HAPIdEntityInDomain entityId = parseWrapperValue(getEntityObject(jsonObj), entityType, parserContext, domainEntityManager, resourceDefinitionManager); 

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
		HAPIdEntityInDomain out = parseWrapperValue(jsonObj, entityType, parserContext, domainEntityManager, resourceDefinitionManager);

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
