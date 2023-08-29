package com.nosliw.data.core.domain;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.info.HAPUtilityEntityInfo;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.domain.attachment.HAPReferenceAttachment;
import com.nosliw.data.core.domain.entity.HAPConfigureParentRelationComplex;
import com.nosliw.data.core.domain.entity.HAPEmbeded;
import com.nosliw.data.core.domain.entity.HAPEmbededDefinition;
import com.nosliw.data.core.domain.entity.HAPInfoAdapterDefinition;
import com.nosliw.data.core.resource.HAPFactoryResourceId;
import com.nosliw.data.core.resource.HAPManagerResourceDefinition;
import com.nosliw.data.core.resource.HAPResourceId;

public class HAPUtilityParserEntity {

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
					List<HAPInfoAdapterDefinition> adapters = parseAdapter(adapterObjs, entityId.getEntityType(), parserContext, domainEntityManager, resourceDefinitionManager);
					for(HAPInfoAdapterDefinition adapter : adapters)   out.addAdapter(adapter);
				}
			}
		}
		return out;
	}

	public static HAPEmbededDefinition parseEmbededComplexEntity(Object obj, String entityType, HAPIdEntityInDomain parentEntityId, HAPConfigureParentRelationComplex parentRelationConfigureDefault, HAPContextParser parserContext, HAPManagerDomainEntityDefinition domainEntityManager, HAPManagerResourceDefinition resourceDefinitionManager) {
		return parseEmbededComplexEntity(obj, entityType, null, parentEntityId, parentRelationConfigureDefault, parserContext, domainEntityManager, resourceDefinitionManager);		
	}
	
	public static HAPEmbededDefinition parseEmbededComplexEntity(Object obj, String entityType, String adapterType, HAPIdEntityInDomain parentEntityId, HAPConfigureParentRelationComplex parentRelationConfigureDefault, HAPContextParser parserContext, HAPManagerDomainEntityDefinition domainEntityManager, HAPManagerResourceDefinition resourceDefinitionManager) {
		return parseEmbededComplexEntity(obj, entityType, adapterType, parentEntityId, null, parentRelationConfigureDefault, parserContext, domainEntityManager, resourceDefinitionManager);		
	}
	
	public static HAPEmbededDefinition parseEmbededComplexEntity(Object obj, String entityType, String adapterType, HAPIdEntityInDomain parentEntityId, HAPConfigureParentRelationComplex parentRelationConfigureExternal, HAPConfigureParentRelationComplex parentRelationConfigureDefault, HAPContextParser parserContext, HAPManagerDomainEntityDefinition domainEntityManager, HAPManagerResourceDefinition resourceDefinitionManager) {
		HAPEmbededDefinition out = null;
		if(obj instanceof JSONObject) {
			JSONObject jsonObj = (JSONObject)obj;

			//embeded entity
			HAPIdEntityInDomain entityId = parseComplexEntity(getEntityObject(jsonObj), entityType, parentEntityId, parentRelationConfigureExternal, parentRelationConfigureDefault, parserContext, domainEntityManager, resourceDefinitionManager); 
			
			if(entityId!=null) {
				out = new HAPEmbededDefinition(entityId);
				//adapter
				Object adapterObjs = jsonObj.opt(HAPEmbeded.ADAPTER);
				if(adapterObjs!=null) {
					List<HAPInfoAdapterDefinition> adapters = parseAdapter(adapterObjs, entityId.getEntityType(), parserContext, domainEntityManager, resourceDefinitionManager);
					for(HAPInfoAdapterDefinition adapter : adapters)   out.addAdapter(adapter);
				}
			}
		}
		else {
			HAPIdEntityInDomain entityId = parseComplexEntity(obj, entityType, parentEntityId, parentRelationConfigureExternal, parentRelationConfigureDefault, parserContext, domainEntityManager, resourceDefinitionManager); 
			out = new HAPEmbededDefinition(entityId);
		}
		return out;
	}

	private static List<HAPInfoAdapterDefinition> parseAdapter(Object adaptersObj, String embededEntityType, HAPContextParser parserContext, HAPManagerDomainEntityDefinition domainEntityManager, HAPManagerResourceDefinition resourceDefinitionManager){
		List<HAPInfoAdapterDefinition> out = new ArrayList<HAPInfoAdapterDefinition>();
		
		if(adaptersObj instanceof JSONArray) {
			JSONArray adaptersArray = (JSONArray)adaptersObj;
			for(int i=0; i<adaptersArray.length(); i++) {
				JSONObject adapterObj = adaptersArray.getJSONObject(i);
				if(HAPUtilityEntityInfo.isEnabled(adapterObj)) {
					HAPIdEntityInDomain adpaterEntityId = parseEntity(adapterObj, domainEntityManager.getDefaultAdapterByEntity(embededEntityType), parserContext, domainEntityManager, resourceDefinitionManager);
					HAPInfoAdapterDefinition adapterInfo = new HAPInfoAdapterDefinition(adpaterEntityId.getEntityType(), adpaterEntityId);
					adapterInfo.buildEntityInfoByJson(adapterObj);
					if(adapterInfo.getName()==null)   adapterInfo.setName(HAPConstantShared.NAME_DEFAULT);
					out.add(adapterInfo);
				}
			}
		}
		else if(adaptersObj instanceof JSONObject) {
			JSONObject adapterObj = (JSONObject)adaptersObj;
			if(HAPUtilityEntityInfo.isEnabled(adapterObj)) {
				HAPIdEntityInDomain adpaterEntityId = parseEntity(adapterObj, domainEntityManager.getDefaultAdapterByEntity(embededEntityType), parserContext, domainEntityManager, resourceDefinitionManager);
				HAPInfoAdapterDefinition adapterInfo = new HAPInfoAdapterDefinition(adpaterEntityId.getEntityType(), adpaterEntityId);
				adapterInfo.buildEntityInfoByJson(adapterObj);
				if(adapterInfo.getName()==null)   adapterInfo.setName(HAPConstantShared.NAME_DEFAULT);
				out.add(adapterInfo);
			}
		}
		
		return out;
	}
	
	//parse entity into domain
	public static HAPIdEntityInDomain parseEntity(Object obj, String entityTypeIfNotProvided, HAPContextParser parserContext, HAPManagerDomainEntityDefinition domainEntityManager, HAPManagerResourceDefinition resourceDefinitionManager) {
		HAPIdEntityInDomain out = null;
		if(obj instanceof JSONObject) {
			JSONObject jsonObj = (JSONObject)obj;

			String entityType = (String)jsonObj.opt(HAPInfoEntityInDomainDefinition.ENTITYTYPE);   //if entity type is defined in entity, then override provided
			if(entityType==null)  entityType = entityTypeIfNotProvided;
			
			JSONObject infoObj = jsonObj.optJSONObject(HAPInfoEntityInDomainDefinition.INFO);
			if(infoObj==null)   infoObj = jsonObj;

			if(HAPUtilityEntityInfo.isEnabled(infoObj)) {
				//resource id
				if(out==null) {
					Object resourceObj = jsonObj.opt(HAPInfoEntityInDomainDefinition.RESOURCEID);
					if(resourceObj!=null) {
						HAPResourceId resourceId = HAPFactoryResourceId.tryNewInstance(entityType, resourceObj);
						out = parseReferenceResource(resourceId, parserContext, resourceDefinitionManager);
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
					if(entityObj==null)  entityObj = jsonObj;    //if no entity node, then using root
					out = domainEntityManager.parseDefinition(entityType, entityObj, parserContext);
				}
				
				//entity info (name, description, ...)
				HAPInfoEntityInDomainDefinition entityInfo = parserContext.getCurrentDomain().getEntityInfoDefinition(out);
				HAPExtraInfoEntityInDomainDefinition entityInfoDef = entityInfo.getExtraInfo();
				entityInfoDef.buildObject(infoObj, HAPSerializationFormat.JSON);
			}
		}
		else if(obj instanceof HAPResourceId) {
			out = parseReferenceResource((HAPResourceId)obj, parserContext, resourceDefinitionManager);
		}
		else if(obj instanceof HAPReferenceAttachment) {
			out = parserContext.getCurrentDomain().addEntityOrReference((HAPReferenceAttachment)obj);
		}
		else {
			out = domainEntityManager.parseDefinition(entityTypeIfNotProvided, obj, parserContext);
		}
		
		return out;
	}
	
	public static HAPIdEntityInDomain parseReferenceResource(HAPResourceId resourceId, HAPContextParser parserContext, HAPManagerResourceDefinition resourceDefinitionManager) {
		HAPIdEntityInDomain out = parserContext.getCurrentDomain().addEntityOrReference(resourceId);
		HAPInfoEntityInDomainDefinition entityInfo = parserContext.getCurrentDomain().getEntityInfoDefinition(out);
		if(!entityInfo.isGlobalComplexResourceReference()) {
			//load resource into global domain except for global complex entity resource
			 resourceDefinitionManager.getResourceDefinition(resourceId, parserContext.getGlobalDomain(), parserContext.getCurrentDomainId());  //kkkk
		}
		return out;
	}
	
	private static HAPIdEntityInDomain parseComplexEntity(Object obj, String entityType, HAPIdEntityInDomain parentEntityId, HAPConfigureParentRelationComplex parentRelationConfigureExternal, HAPConfigureParentRelationComplex parentRelationConfigureDefault, HAPContextParser parserContext, HAPManagerDomainEntityDefinition domainEntityManager, HAPManagerResourceDefinition resourceDefinitionManager) {
		//entity itself
		HAPIdEntityInDomain out = parseEntity(obj, entityType, parserContext, domainEntityManager, resourceDefinitionManager);

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
			if(obj instanceof JSONObject) {
				JSONObject parentRelationConfigureObjCustomer = ((JSONObject)obj).optJSONObject(HAPInfoEntityInDomainDefinition.PARENT);
				HAPConfigureParentRelationComplex customerConfigure = null;
				if(parentRelationConfigureObjCustomer!=null) {
					customerConfigure = new HAPConfigureParentRelationComplex();
					customerConfigure.buildObject(parentRelationConfigureObjCustomer, HAPSerializationFormat.JSON);
				}
				parentRelationConfigure.mergeHard(customerConfigure);
			}
			
			((HAPDomainEntityDefinitionLocalComplex)parserContext.getCurrentDomain()).buildComplexParentRelation(out, parentInfo);
		}
		
		return out;
	}
	
	public static Object getEntityObject(JSONObject embededEntityObject) {
		Object out = embededEntityObject.opt(HAPEmbeded.EMBEDED);
		if(out==null)  out = embededEntityObject;
		return out;
	}
}
