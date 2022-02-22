package com.nosliw.data.core.domain;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.complex.HAPConfigureParentRelationComplex;

public class HAPUtilityParserEntity {

	public static List<HAPInfoContainerElement> parseContainer(JSONObject containerObj, String eleEntityType, String containerType, HAPContextParser parserContext, HAPManagerDomainEntity domainEntityManager) {
		List<HAPInfoContainerElement> out = new ArrayList<HAPInfoContainerElement>();
		JSONArray eleArrayObj = containerObj.getJSONArray(HAPContainerEntity.ELEMENT);
		for(int i=0; i<eleArrayObj.length(); i++) {
			JSONObject eleObj = eleArrayObj.getJSONObject(i);
			
			//element entity
			HAPIdEntityInDomain eleEntityId = parseEntity(eleObj, eleEntityType, parserContext, domainEntityManager);
			
			//element info
			JSONObject eleInfoObj = eleObj.optJSONObject("eleInfo");
			if(containerType.equals("set")) {
				HAPInfoContainerElementSet eleInfo = new HAPInfoContainerElementSet(eleEntityId);
				eleInfo.buildObject(eleInfoObj, HAPSerializationFormat.JSON);
				out.add(eleInfo);
			}
			else if(containerType.equals("list")) {
				HAPInfoContainerElementList eleInfo = new HAPInfoContainerElementList(eleEntityId);
				eleInfo.buildObject(eleInfoObj, HAPSerializationFormat.JSON);
				out.add(eleInfo);
			}
		}
		return out;
	}
	
	public static List<HAPInfoContainerElement> parseComplexContainer(JSONObject containerObj, String eleEntityType, String containerType, HAPIdEntityInDomain parentEntityId, HAPConfigureParentRelationComplex parentRelationConfigureDefault, HAPContextParser parserContext, HAPManagerDomainEntity domainEntityManager) {
		JSONObject parentRelationConfigureObjCustomer = containerObj.optJSONObject(HAPInfoDefinitionEntityInDomain.PARENT);

		List<HAPInfoContainerElement> out = new ArrayList<HAPInfoContainerElement>();
		JSONArray eleArrayObj = containerObj.getJSONArray(HAPContainerEntity.ELEMENT);
		for(int i=0; i<eleArrayObj.length(); i++) {
			JSONObject eleObj = eleArrayObj.getJSONObject(i);
			
			//element entity
			HAPIdEntityInDomain eleEntityId = parseComplexEntity(eleObj, eleEntityType, parentEntityId, parentRelationConfigureObjCustomer, parentRelationConfigureDefault, parserContext, domainEntityManager);
			
			//element info
			JSONObject eleInfoObj = eleObj.optJSONObject("eleInfo");
			if(containerType.equals("set")) {
				HAPInfoContainerElementSet eleInfo = new HAPInfoContainerElementSet(eleEntityId);
				eleInfo.buildObject(eleInfoObj, HAPSerializationFormat.JSON);
				out.add(eleInfo);
			}
			else if(containerType.equals("list")) {
				HAPInfoContainerElementList eleInfo = new HAPInfoContainerElementList(eleEntityId);
				eleInfo.buildObject(eleInfoObj, HAPSerializationFormat.JSON);
				out.add(eleInfo);
			}
		}
		return out;
	}
	

	
	//parse entity into domain
	public static HAPIdEntityInDomain parseEntity(Object obj, String entityType, HAPContextParser parserContext, HAPManagerDomainEntity domainEntityManager) {
		HAPIdEntityInDomain out = null;
		if(obj instanceof JSONObject) {
			JSONObject jsonObj = (JSONObject)obj;
			
			//entity
			JSONObject entityObj = jsonObj.getJSONObject(HAPInfoDefinitionEntityInDomain.ENTITY);
			out = domainEntityManager.parseDefinition(entityType, entityObj, parserContext);
			
			//entity info defined
			HAPInfoDefinitionEntityInDomain entityInfo = parserContext.getDefinitionDomain().getEntityInfo(out);
			HAPInfoDefinitionEntityInDomainExtra entityInfoDef = entityInfo.getExtraInfo();
			JSONObject infoObj = jsonObj.optJSONObject(HAPInfoDefinitionEntityInDomain.EXTRA);
			entityInfoDef.buildObject(infoObj, HAPSerializationFormat.JSON);
		}
		return out;
	}
	
	public static HAPIdEntityInDomain parseComplexEntity(Object obj, String entityType, HAPIdEntityInDomain parentEntityId, JSONObject parentRelationConfigureObjExternal, HAPConfigureParentRelationComplex parentRelationConfigureDefault, HAPContextParser parserContext, HAPManagerDomainEntity domainEntityManager) {
		//entity itself
		HAPIdEntityInDomain out = parseEntity(obj, entityType, parserContext, domainEntityManager);
		HAPInfoDefinitionEntityInDomain entityInfo = parserContext.getDefinitionDomain().getEntityInfo(out);

		//parent relation
		HAPInfoParentComplex parentInfo = new HAPInfoParentComplex();

		//external configure
		HAPConfigureParentRelationComplex externalConfigure = null;
		if(parentRelationConfigureObjExternal!=null) {
			externalConfigure = new HAPConfigureParentRelationComplex();
			externalConfigure.buildObject(parentRelationConfigureObjExternal, HAPSerializationFormat.JSON);
		}
		parentRelationConfigureDefault.mergeHard(externalConfigure);

		//customer
		if(obj instanceof JSONObject) {
			JSONObject jsonObj = (JSONObject)obj;
			JSONObject parentRelationConfigureObjCustomer = ((JSONObject)obj).optJSONObject(HAPInfoDefinitionEntityInDomain.PARENT);
			HAPConfigureParentRelationComplex customerConfigure = null;
			if(parentRelationConfigureObjCustomer!=null) {
				customerConfigure = new HAPConfigureParentRelationComplex();
				customerConfigure.buildObject(parentRelationConfigureObjExternal, HAPSerializationFormat.JSON);
			}
			parentRelationConfigureDefault.mergeHard(customerConfigure);
		}
		
		parentInfo.setParentRelationConfigure(parentRelationConfigureDefault);
		
		parentInfo.setParentId(parentEntityId);
		parserContext.getDefinitionDomain().buildComplexParentRelation(out, parentInfo);
		
		return out;
	}
	
}
