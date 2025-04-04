package com.nosliw.data.core.domain.definition;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.info.HAPUtilityEntityInfo;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.division.manual.common.attachment.HAPReferenceAttachment;
import com.nosliw.data.core.domain.HAPContextParser;
import com.nosliw.data.core.domain.HAPDomainEntityDefinitionLocalComplex;
import com.nosliw.data.core.domain.HAPExtraInfoEntityInDomainDefinition;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.HAPInfoEntityInDomainDefinition;
import com.nosliw.data.core.domain.HAPInfoParentComplex;
import com.nosliw.data.core.domain.HAPUtilityParserEntity;
import com.nosliw.data.core.domain.entity.HAPConfigureParentRelationComplex;
import com.nosliw.data.core.domain.entity.HAPEmbeded;
import com.nosliw.data.core.domain.entity.HAPEmbededDefinition;
import com.nosliw.data.core.domain.entity.HAPInfoAdapterDefinition;
import com.nosliw.data.core.resource.HAPFactoryResourceId;
import com.nosliw.data.core.resource.HAPManagerResourceDefinition;
import com.nosliw.data.core.resource.HAPResourceId;

public class HAPUtilityParserEntityFormatJson {

	public static HAPEmbededDefinition parseEmbededEntity(Object obj, String entityType, HAPContextParser parserContext, HAPManagerDomainEntityDefinition domainEntityManager, HAPManagerResourceDefinition resourceDefinitionManager) {
		return parseEmbededEntity(obj, entityType, null, parserContext, domainEntityManager, resourceDefinitionManager);
	}

	public static HAPEmbededDefinition parseEmbededEntity(Object obj, String entityType, String adapterType, HAPContextParser parserContext, HAPManagerDomainEntityDefinition domainEntityManager, HAPManagerResourceDefinition resourceDefinitionManager) {
		HAPEmbededDefinition out = null;
		if(obj instanceof JSONObject) {
			JSONObject jsonObj = (JSONObject)obj;
			
			//embeded entity
			HAPIdEntityInDomain entityId = parseEntity(getEntityObject(jsonObj), entityType, parserContext, domainEntityManager, resourceDefinitionManager); 

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

	private static List<HAPInfoAdapterDefinition> parseAdapter(Object adaptersObj, String adatperType, HAPContextParser parserContext, HAPManagerDomainEntityDefinition domainEntityManager, HAPManagerResourceDefinition resourceDefinitionManager){
		List<HAPInfoAdapterDefinition> out = new ArrayList<HAPInfoAdapterDefinition>();
		
		if(adaptersObj instanceof JSONArray) {
			JSONArray adaptersArray = (JSONArray)adaptersObj;
			for(int i=0; i<adaptersArray.length(); i++) {
				JSONObject adapterObj = adaptersArray.getJSONObject(i);
				if(HAPUtilityEntityInfo.isEnabled(adapterObj)) {
					HAPIdEntityInDomain adpaterEntityId = parseEntity(adapterObj, adatperType, parserContext, domainEntityManager, resourceDefinitionManager);
					HAPInfoAdapterDefinition adapterInfo = new HAPInfoAdapterDefinition(adpaterEntityId.getEntityType(), adpaterEntityId);
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
				HAPIdEntityInDomain adpaterEntityId = parseEntity(adapterObj, adatperType, parserContext, domainEntityManager, resourceDefinitionManager);
				HAPInfoAdapterDefinition adapterInfo = new HAPInfoAdapterDefinition(adpaterEntityId.getEntityType(), adpaterEntityId);
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

	
	//parse entity into domain
	public static HAPIdEntityInDomain parseEntity(JSONObject jsonObj, String entityTypeIfNotProvided, HAPContextParser parserContext, HAPManagerDomainEntityDefinition domainEntityManager, HAPManagerResourceDefinition resourceDefinitionManager) {
		HAPIdEntityInDomain out = null;

		String entityType = (String)jsonObj.opt(HAPInfoEntityInDomainDefinition.ENTITYTYPE);   //if entity type is defined in entity, then override provided
		if(entityType==null) {
			entityType = entityTypeIfNotProvided;
		}
		
		JSONObject infoObj = jsonObj.optJSONObject(HAPInfoEntityInDomainDefinition.INFO);
		if(infoObj==null) {
			infoObj = jsonObj;
		}

		if(HAPUtilityEntityInfo.isEnabled(infoObj)) {
			//resource id
			if(out==null) {
				Object resourceObj = jsonObj.opt(HAPInfoEntityInDomainDefinition.RESOURCEID);
				if(resourceObj!=null) {
					HAPResourceId resourceId = HAPFactoryResourceId.tryNewInstance(entityType, resourceObj);
					out = HAPUtilityParserEntity.parseReferenceResource(resourceId, parserContext, resourceDefinitionManager);
				}
			}
			//reference
			if(out==null) {
				Object referenceObj = jsonObj.opt(HAPInfoEntityInDomainDefinition.REFERENCE);
				if(referenceObj!=null) {
					HAPReferenceAttachment reference = HAPReferenceAttachment.newInstance(referenceObj, entityType);
					out = parserContext.getCurrentDomain().addEntityOrReference(reference);
				}
			}
			//entity
			if(out==null) {
				Object entityObj = jsonObj.opt(HAPInfoEntityInDomainDefinition.ENTITY);
				if(entityObj==null)
				 {
					entityObj = jsonObj;    //if no entity node, then using root
				}
				out = domainEntityManager.parseDefinition(entityType, entityObj, HAPSerializationFormat.JSON, parserContext);
			}
			
			//entity info (name, description, ...)
			HAPInfoEntityInDomainDefinition entityInfo = parserContext.getCurrentDomain().getEntityInfoDefinition(out);
			HAPExtraInfoEntityInDomainDefinition entityInfoDef = entityInfo.getExtraInfo();
			entityInfoDef.buildObject(infoObj, HAPSerializationFormat.JSON);
		}
		
		return out;
	}

	private static HAPIdEntityInDomain parseComplexEntity(JSONObject jsonObj, String entityType, HAPIdEntityInDomain parentEntityId, HAPConfigureParentRelationComplex parentRelationConfigureExternal, HAPConfigureParentRelationComplex parentRelationConfigureDefault, HAPContextParser parserContext, HAPManagerDomainEntityDefinition domainEntityManager, HAPManagerResourceDefinition resourceDefinitionManager) {
		//entity itself
		HAPIdEntityInDomain out = parseEntity(jsonObj, entityType, parserContext, domainEntityManager, resourceDefinitionManager);

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
	
	public static JSONObject getEntityObject(JSONObject embededEntityObject) {
		JSONObject out = embededEntityObject.optJSONObject(HAPEmbeded.EMBEDED);
		if(out==null) {
			out = embededEntityObject;
		}
		return out;
	}
}
