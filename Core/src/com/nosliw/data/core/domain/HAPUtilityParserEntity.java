package com.nosliw.data.core.domain;

import org.json.JSONObject;

import com.nosliw.common.info.HAPUtilityEntityInfo;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.complex.HAPConfigureParentRelationComplex;
import com.nosliw.data.core.domain.entity.attachment.HAPReferenceAttachment;
import com.nosliw.data.core.resource.HAPFactoryResourceId;
import com.nosliw.data.core.resource.HAPManagerResourceDefinition;
import com.nosliw.data.core.resource.HAPResourceId;

public class HAPUtilityParserEntity {

//	public static List<HAPInfoContainerElement> parseContainer(Object containerObj, String eleEntityType, String containerType, HAPContextParser parserContext, HAPManagerDomainEntityDefinition domainEntityManager, HAPManagerResourceDefinition resourceDefinitionManager) {
//		return parseContainer(containerObj, eleEntityType, null, containerType, parserContext, domainEntityManager, resourceDefinitionManager);
//	}
//	
//	public static List<HAPInfoContainerElement> parseContainer(Object containerObj, String eleEntityType, String adapterType, String containerType, HAPContextParser parserContext, HAPManagerDomainEntityDefinition domainEntityManager, HAPManagerResourceDefinition resourceDefinitionManager) {
//		List<HAPInfoContainerElement> out = new ArrayList<HAPInfoContainerElement>();
//		JSONArray eleArrayObj = null;
//		if(containerObj instanceof JSONArray)  eleArrayObj = (JSONArray)containerObj;
//		else if(containerObj instanceof JSONObject)   eleArrayObj = ((JSONObject)containerObj).getJSONArray(HAPContainerEntity.ELEMENT);
//		for(int i=0; i<eleArrayObj.length(); i++) {
//			JSONObject eleObj = eleArrayObj.getJSONObject(i);
//			
//			//element entity
//			HAPEmbededEntity embededEntity = parseEmbededEntity(eleObj, eleEntityType, adapterType, parserContext, domainEntityManager, resourceDefinitionManager);
//			
//			//element info
//			out.add(buildContainerElementInfo(eleObj, embededEntity, containerType, parserContext.getDefinitionDomain()));
//		}
//		return out;
//	}
//
//	public static List<HAPInfoContainerElement> parseComplexContainer(Object containerObj, String eleEntityType, String containerType, HAPIdEntityInDomain parentEntityId, HAPConfigureParentRelationComplex parentRelationConfigureDefault, HAPContextParser parserContext, HAPManagerDomainEntityDefinition domainEntityManager, HAPManagerResourceDefinition resourceDefinitionManager) {
//		return parseComplexContainer(containerObj, eleEntityType, null, containerType, parentEntityId, parentRelationConfigureDefault, parserContext, domainEntityManager, resourceDefinitionManager);
//	}
//	
//	public static List<HAPInfoContainerElement> parseComplexContainer(Object containerObj, String eleEntityType, String adapterType, String containerType, HAPIdEntityInDomain parentEntityId, HAPConfigureParentRelationComplex parentRelationConfigureDefault, HAPContextParser parserContext, HAPManagerDomainEntityDefinition domainEntityManager, HAPManagerResourceDefinition resourceDefinitionManager) {
//		List<HAPInfoContainerElement> out = new ArrayList<HAPInfoContainerElement>();
//
//		HAPConfigureParentRelationComplex parentRelationConfigureCustomer = null;
//		JSONArray eleArrayObj = null;
//		if(containerObj instanceof JSONObject) {
//			JSONObject containerJsonObj = (JSONObject)containerObj;
//			JSONObject parentRelationConfigureObjCustomer = containerJsonObj.optJSONObject(HAPInfoDefinitionEntityInDomain.PARENT);
//			if(parentRelationConfigureObjCustomer!=null) {
//				parentRelationConfigureCustomer = new HAPConfigureParentRelationComplex();
//				parentRelationConfigureCustomer.buildObject(parentRelationConfigureObjCustomer, HAPSerializationFormat.JSON);
//			}
//			eleArrayObj = containerJsonObj.getJSONArray(HAPContainerEntity.ELEMENT);
//		}
//		else if(containerObj instanceof JSONArray) {
//			eleArrayObj = (JSONArray)containerObj;
//		}
//
//		for(int i=0; i<eleArrayObj.length(); i++) {
//			JSONObject eleObj = eleArrayObj.getJSONObject(i);
//			
//			//element entity
//			HAPEmbededEntity embededEntity = parseEmbededComplexEntity(eleObj, eleEntityType, adapterType, parentEntityId, parentRelationConfigureCustomer, parentRelationConfigureDefault, parserContext, domainEntityManager, resourceDefinitionManager);
//			
//			//element info
//			out.add(buildContainerElementInfo(eleObj, embededEntity, containerType, parserContext.getDefinitionDomain()));
//		}
//		return out;
//	}
//	
//	private static HAPInfoContainerElement buildContainerElementInfo(JSONObject eleObj, HAPEmbededEntity embededEntity, String containerType, HAPDomainDefinitionEntity definitionDomain) {
//		HAPInfoContainerElement out = null;
//		JSONObject eleInfoObj = eleObj.optJSONObject(HAPContainerEntity.ELEMENT_INFO);
//		if(containerType.equals(HAPConstantShared.ENTITYCONTAINER_TYPE_SET)) {
//			out = new HAPInfoContainerElementSet(embededEntity);
//		}
//		else if(containerType.equals(HAPConstantShared.ENTITYCONTAINER_TYPE_LIST)) {
//			out = new HAPInfoContainerElementList(embededEntity);
//		}
//		out.buildObject(eleInfoObj, HAPSerializationFormat.JSON);
//		if(out.getElementName()==null) {
//			//if no name for element, use name from entity definition
//			out.setElementName(definitionDomain.getEntityInfo(embededEntity.getEntityId()).getExtraInfo().getName());
//		}
//		return out;
//	}

	public static HAPEmbededDefinitionWithId parseEmbededEntity(Object obj, String entityType, HAPContextParser parserContext, HAPManagerDomainEntityDefinition domainEntityManager, HAPManagerResourceDefinition resourceDefinitionManager) {
		return parseEmbededEntity(obj, entityType, null, parserContext, domainEntityManager, resourceDefinitionManager);
	}

	public static HAPEmbededDefinitionWithId parseEmbededEntity(Object obj, String entityType, String adapterType, HAPContextParser parserContext, HAPManagerDomainEntityDefinition domainEntityManager, HAPManagerResourceDefinition resourceDefinitionManager) {
		HAPEmbededDefinitionWithId out = null;
		if(obj instanceof JSONObject) {
			JSONObject jsonObj = (JSONObject)obj;
			//adapter
			Object adapterObj = null;
			JSONObject adapterJsonObj = jsonObj.optJSONObject(HAPEmbeded.ADAPTER);
			if(adapterJsonObj!=null) {
				if(adapterType==null)  adapterType = domainEntityManager.getDefaultAdapterByEntity(entityType);
				if(adapterType!=null)	adapterObj = domainEntityManager.parseAdapter(adapterType, adapterJsonObj);
			}
			
			//embeded entity
			HAPIdEntityInDomain entityId = parseEntity(adapterJsonObj==null?jsonObj:jsonObj.opt(HAPEmbededDefinitionWithId.EMBEDED), entityType, parserContext, domainEntityManager, resourceDefinitionManager); 
			
			if(entityId!=null)  out = new HAPEmbededDefinitionWithId(entityId);
		}
		return out;
	}

	public static HAPEmbededDefinitionWithId parseEmbededComplexEntity(Object obj, String entityType, HAPIdEntityInDomain parentEntityId, HAPConfigureParentRelationComplex parentRelationConfigureDefault, HAPContextParser parserContext, HAPManagerDomainEntityDefinition domainEntityManager, HAPManagerResourceDefinition resourceDefinitionManager) {
		return parseEmbededComplexEntity(obj, entityType, null, parentEntityId, parentRelationConfigureDefault, parserContext, domainEntityManager, resourceDefinitionManager);		
	}
	
	public static HAPEmbededDefinitionWithId parseEmbededComplexEntity(Object obj, String entityType, String adapterType, HAPIdEntityInDomain parentEntityId, HAPConfigureParentRelationComplex parentRelationConfigureDefault, HAPContextParser parserContext, HAPManagerDomainEntityDefinition domainEntityManager, HAPManagerResourceDefinition resourceDefinitionManager) {
		return parseEmbededComplexEntity(obj, entityType, adapterType, parentEntityId, null, parentRelationConfigureDefault, parserContext, domainEntityManager, resourceDefinitionManager);		
	}
	
	public static HAPEmbededDefinitionWithId parseEmbededComplexEntity(Object obj, String entityType, String adapterType, HAPIdEntityInDomain parentEntityId, HAPConfigureParentRelationComplex parentRelationConfigureExternal, HAPConfigureParentRelationComplex parentRelationConfigureDefault, HAPContextParser parserContext, HAPManagerDomainEntityDefinition domainEntityManager, HAPManagerResourceDefinition resourceDefinitionManager) {
		HAPEmbededDefinitionWithId out = null;
		if(obj instanceof JSONObject) {
			JSONObject jsonObj = (JSONObject)obj;
			//adapter
			Object adapterObj = null;
			JSONObject adapterJsonObj = jsonObj.optJSONObject(HAPEmbeded.ADAPTER);
			if(adapterJsonObj!=null) {
				if(adapterType==null)  adapterType = domainEntityManager.getDefaultAdapterByEntity(entityType);
				if(adapterType!=null)  adapterObj = domainEntityManager.parseAdapter(adapterType, adapterJsonObj);
			}
			
			//embeded entity
			HAPIdEntityInDomain entityId = parseComplexEntity(adapterJsonObj==null?jsonObj:jsonObj.opt(HAPEmbededDefinitionWithId.EMBEDED), entityType, parentEntityId, parentRelationConfigureExternal, parentRelationConfigureDefault, parserContext, domainEntityManager, resourceDefinitionManager); 
			
			if(entityId!=null)  out = new HAPEmbededDefinitionWithId(entityId);
		}
		return out;
	}
	
	//parse entity into domain
	public static HAPIdEntityInDomain parseEntity(Object obj, String entityType, HAPContextParser parserContext, HAPManagerDomainEntityDefinition domainEntityManager, HAPManagerResourceDefinition resourceDefinitionManager) {
		HAPIdEntityInDomain out = null;
		if(obj instanceof JSONObject) {
			JSONObject jsonObj = (JSONObject)obj;

			JSONObject infoObj = jsonObj.optJSONObject(HAPInfoEntityInDomainDefinition.EXTRA);
			if(infoObj==null)   infoObj = jsonObj;

			if(HAPUtilityEntityInfo.isEnabled(infoObj)) {
				//resource id
				if(out==null) {
					Object resourceObj = jsonObj.opt(HAPInfoEntityInDomainDefinition.RESOURCEID);
					if(resourceObj!=null) {
						HAPResourceId resourceId = HAPFactoryResourceId.tryNewInstance(entityType, resourceObj);
						out = parserContext.getCurrentDomain().addEntityOrReference(resourceId, entityType);
						HAPInfoEntityInDomainDefinition entityInfo = parserContext.getCurrentDomain().getEntityInfoDefinition(out);
						if(!entityInfo.isGlobalComplexResourceReference()) {
							//load resource into global domain except for global complex entity resource
							 resourceDefinitionManager.getResourceDefinition(resourceId, parserContext.getGlobalDomain(), parserContext.getCurrentDomainId());  //kkkk
						}
					}
				}
				//reference
				if(out==null) {
					Object referenceObj = jsonObj.opt(HAPInfoEntityInDomainDefinition.REFERENCE);
					if(referenceObj!=null) {
						HAPReferenceAttachment reference = HAPReferenceAttachment.newInstance(referenceObj, entityType);
						out = parserContext.getCurrentDomain().addEntityOrReference(reference, entityType);
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
}
