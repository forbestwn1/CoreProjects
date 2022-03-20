package com.nosliw.data.core.domain;

import java.lang.reflect.Field;

import org.json.JSONObject;

import com.nosliw.common.interfac.HAPEntityOrReference;
import com.nosliw.common.path.HAPPath;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.complex.HAPConfigureComplexRelationAttachment;
import com.nosliw.data.core.complex.HAPConfigureComplexRelationInfo;
import com.nosliw.data.core.complex.HAPConfigureComplexRelationValueStructure;
import com.nosliw.data.core.complex.HAPConfigureParentRelationComplex;
import com.nosliw.data.core.component.HAPContextProcessor;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPUtilityDomain {

	public static HAPIdEntityInDomain getEntityDescent(HAPIdEntityInDomain entityId, String path, HAPDomainDefinitionEntity definitionDomain) {
		HAPPath p = new HAPPath(path);
		HAPIdEntityInDomain currentEntityId = entityId;
		HAPInfoDefinitionEntityInDomain currentEntityInfo = definitionDomain.getEntityInfo(currentEntityId);
		HAPDefinitionEntityInDomain currentEntityDef = currentEntityInfo.getEntity();
		for(String seg : p.getPathSegments()) {
			HAPEntityOrReference child = currentEntityDef.getChild(seg);
			if(HAPConstantShared.REFERENCE.equals(child.getEntityOrReferenceType())) {
				//resource id
				out = this.getResourceDefinition((HAPResourceId)defOrId, entityDomain, parentResourceDef.getLocalReferenceBase());
			}
			else {
				//resource def
				currentEntityDef = (HAPDefinitionEntityInDomain)child;
				out.setLocalReferenceBase(parentResourceDef.getLocalReferenceBase());
			}

		}
	}
	
	public static HAPContextParser getContextParse(HAPIdEntityInDomain entityId, HAPDomainDefinitionEntity definitionDomain) {
		return new HAPContextParser(definitionDomain, definitionDomain.getEntityInfo(entityId).getBaseLocationPath());
	}
	
	public static HAPContextProcessor createProcessContext(HAPContextDomain domainContext, HAPIdEntityInDomain entityId, HAPRuntimeEnvironment runtimeEnv) {
		HAPInfoDefinitionEntityInDomain entityInfo = domainContext.getDefinitionDomain().getEntityInfo(entityId);
		HAPContextProcessor out = new HAPContextProcessor(domainContext, entityInfo.getBaseLocationPath(), runtimeEnv);
		return out;
	}

	public static void preProcess(HAPContextDomain domainContext, HAPIdEntityInDomain entityId, HAPRuntimeEnvironment runtimeEnv) {
		//build executable
	}
	
	public static HAPInfoContainerElementSet newInfoContainerElementSet(HAPIdEntityInDomain entityId, JSONObject jsonObj) {
		HAPInfoContainerElementSet out = new HAPInfoContainerElementSet(entityId);
		out.buildEntityInfoByJson(jsonObj);
		return out;
	}

	public static HAPConfigureParentRelationComplex createDefaultParentRelationConfigure() {
		HAPConfigureParentRelationComplex parentRelationConfigure = new HAPConfigureParentRelationComplex();
		parentRelationConfigure.setValueStructureRelationMode(HAPUtilityDomain.createDefaultValueStructureRelationConfigure());
		parentRelationConfigure.setAttachmentRelationMode(HAPUtilityDomain.createDefaultAttachmentRelationConfigure());
		parentRelationConfigure.setInfoRelationMode(HAPUtilityDomain.createDefaultInfoRelationConfigure());
		return parentRelationConfigure;
	}
	
	public static HAPConfigureComplexRelationAttachment createDefaultAttachmentRelationConfigure() {      
		return new HAPConfigureComplexRelationAttachment();
	}
	
	public static HAPConfigureComplexRelationValueStructure createDefaultValueStructureRelationConfigure() {
		return new HAPConfigureComplexRelationValueStructure();
	}

	public static HAPConfigureComplexRelationInfo createDefaultInfoRelationConfigure() {
		return new HAPConfigureComplexRelationInfo();
	}
	
	public static String getEntityExpandedJsonString(HAPIdEntityInDomain entityId, HAPDomainDefinitionEntity definitionDomain) {
		return definitionDomain.getEntityInfo(entityId).toExpandedJsonString(definitionDomain);
	}
	
	public static HAPInfoDefinitionEntityInDomain newEntityDefinitionInfoInDomain(String entityType, HAPManagerDomainEntityDefinition entityDefMan) {
		HAPInfoDefinitionEntityInDomain out = new HAPInfoDefinitionEntityInDomain(entityType);
		out.setIsComplexEntity(entityDefMan.isComplexEntity(entityType));
		return out;
	}
	
	//get entity type from class
	public static String getEntityTypeFromEntityClass(Class<? extends HAPDefinitionEntityInDomain> entityClass) {
		String out = null;
		try {
			Field f = entityClass.getField("ENTITY_TYPE");
			out = (String)f.get(null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return out;
	}
	
}
