package com.nosliw.data.core.domain;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.complex.HAPConfigureParentRelationComplex;
import com.nosliw.data.core.domain.entity.attachment.HAPReferenceAttachment;
import com.nosliw.data.core.resource.HAPFactoryResourceId;
import com.nosliw.data.core.resource.HAPResourceId;

public class HAPUtilityParserEntity {

	public static List<HAPInfoContainerElement> parseContainer(JSONObject containerObj, String eleEntityType, String containerType, HAPContextParser parserContext, HAPManagerDomainEntityDefinition domainEntityManager) {
		List<HAPInfoContainerElement> out = new ArrayList<HAPInfoContainerElement>();
		JSONArray eleArrayObj = containerObj.getJSONArray(HAPContainerEntity.ELEMENT);
		for(int i=0; i<eleArrayObj.length(); i++) {
			JSONObject eleObj = eleArrayObj.getJSONObject(i);
			
			//element entity
			HAPIdEntityInDomain eleEntityId = parseEntity(eleObj, eleEntityType, parserContext, domainEntityManager);
			
			//element info
			out.add(buildContainerElementInfo(eleObj, eleEntityId, containerType, parserContext.getDefinitionDomain()));
		}
		return out;
	}
	
	public static List<HAPInfoContainerElement> parseComplexContainer(JSONObject containerObj, String eleEntityType, String containerType, HAPIdEntityInDomain parentEntityId, HAPConfigureParentRelationComplex parentRelationConfigureDefault, HAPContextParser parserContext, HAPManagerDomainEntityDefinition domainEntityManager) {
		JSONObject parentRelationConfigureObjCustomer = containerObj.optJSONObject(HAPInfoDefinitionEntityInDomain.PARENT);
		HAPConfigureParentRelationComplex parentRelationConfigureCustomer = null;
		if(parentRelationConfigureObjCustomer!=null) {
			parentRelationConfigureCustomer = new HAPConfigureParentRelationComplex();
			parentRelationConfigureCustomer.buildObject(parentRelationConfigureObjCustomer, HAPSerializationFormat.JSON);
		}
		
		List<HAPInfoContainerElement> out = new ArrayList<HAPInfoContainerElement>();
		JSONArray eleArrayObj = containerObj.getJSONArray(HAPContainerEntity.ELEMENT);
		for(int i=0; i<eleArrayObj.length(); i++) {
			JSONObject eleObj = eleArrayObj.getJSONObject(i);
			
			//element entity
			HAPIdEntityInDomain eleEntityId = parseComplexEntity(eleObj, eleEntityType, parentEntityId, parentRelationConfigureCustomer, parentRelationConfigureDefault, parserContext, domainEntityManager);
			
			//element info
			out.add(buildContainerElementInfo(eleObj, eleEntityId, containerType, parserContext.getDefinitionDomain()));
		}
		return out;
	}
	
	private static HAPInfoContainerElement buildContainerElementInfo(JSONObject eleObj, HAPIdEntityInDomain eleEntityId, String containerType, HAPDomainDefinitionEntity definitionDomain) {
		HAPInfoContainerElement out = null;
		JSONObject eleInfoObj = eleObj.optJSONObject(HAPContainerEntity.ELEMENT_INFO);
		if(containerType.equals(HAPConstantShared.ENTITYCONTAINER_TYPE_SET)) {
			out = new HAPInfoContainerElementSet(eleEntityId);
		}
		else if(containerType.equals(HAPConstantShared.ENTITYCONTAINER_TYPE_LIST)) {
			out = new HAPInfoContainerElementList(eleEntityId);
		}
		out.buildObject(eleInfoObj, HAPSerializationFormat.JSON);
		if(out.getElementName()==null) {
			//if no name for element, use name from entity definition
			out.setElementName(definitionDomain.getEntityInfo(eleEntityId).getExtraInfo().getName());
		}
		return out;
	}
	
	//parse entity into domain
	public static HAPIdEntityInDomain parseEntity(Object obj, String entityType, HAPContextParser parserContext, HAPManagerDomainEntityDefinition domainEntityManager) {
		HAPIdEntityInDomain out = null;
		if(obj instanceof JSONObject) {
			JSONObject jsonObj = (JSONObject)obj;

			//resource id
			if(out==null) {
				Object resourceObj = jsonObj.opt(HAPInfoDefinitionEntityInDomain.RESOURCEID);
				if(resourceObj!=null) {
					HAPResourceId resourceId = HAPFactoryResourceId.newInstance(entityType, resourceObj);
					out = parserContext.getDefinitionDomain().addEntityOrReference(resourceId, entityType);
				}
			}
			//reference
			if(out==null) {
				Object referenceObj = jsonObj.opt(HAPInfoDefinitionEntityInDomain.REFERENCE);
				if(referenceObj!=null) {
					HAPReferenceAttachment reference = HAPReferenceAttachment.newInstance(referenceObj, entityType);
					out = parserContext.getDefinitionDomain().addEntityOrReference(reference, entityType);
				}
			}
			//entity
			if(out==null) {
				Object entityObj = jsonObj.opt(HAPInfoDefinitionEntityInDomain.ENTITY);
				if(entityObj==null)  entityObj = jsonObj;    //if no entity node, then using root
				out = domainEntityManager.parseDefinition(entityType, entityObj, parserContext);
			}
			
			//entity info (name, description, ...)
			HAPInfoDefinitionEntityInDomain entityInfo = parserContext.getDefinitionDomain().getEntityInfo(out);
			HAPInfoDefinitionEntityInDomainExtra entityInfoDef = entityInfo.getExtraInfo();
			JSONObject infoObj = jsonObj.optJSONObject(HAPInfoDefinitionEntityInDomain.EXTRA);
			if(infoObj==null)   infoObj = jsonObj;
			entityInfoDef.buildObject(infoObj, HAPSerializationFormat.JSON);
		}
		return out;
	}
	
	public static HAPIdEntityInDomain parseComplexEntity(Object obj, String entityType, HAPIdEntityInDomain parentEntityId, HAPConfigureParentRelationComplex parentRelationConfigureExternal, HAPConfigureParentRelationComplex parentRelationConfigureDefault, HAPContextParser parserContext, HAPManagerDomainEntityDefinition domainEntityManager) {
		//entity itself
		HAPIdEntityInDomain out = parseEntity(obj, entityType, parserContext, domainEntityManager);

		if(parentEntityId!=null) {
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
				JSONObject parentRelationConfigureObjCustomer = ((JSONObject)obj).optJSONObject(HAPInfoDefinitionEntityInDomain.PARENT);
				HAPConfigureParentRelationComplex customerConfigure = null;
				if(parentRelationConfigureObjCustomer!=null) {
					customerConfigure = new HAPConfigureParentRelationComplex();
					customerConfigure.buildObject(parentRelationConfigureObjCustomer, HAPSerializationFormat.JSON);
				}
				parentRelationConfigureDefault.mergeHard(customerConfigure);
			}
			
			parserContext.getDefinitionDomain().buildComplexParentRelation(out, parentInfo);
		}
		
		return out;
	}
	
}
