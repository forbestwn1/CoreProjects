package com.nosliw.data.core.domain;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.path.HAPPath;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.complex.HAPConfigureComplexRelationAttachment;
import com.nosliw.data.core.complex.HAPConfigureComplexRelationInfo;
import com.nosliw.data.core.complex.HAPConfigureComplexRelationValueStructure;
import com.nosliw.data.core.complex.HAPConfigureParentRelationComplex;
import com.nosliw.data.core.complex.HAPExecutableEntityComplex;
import com.nosliw.data.core.complex.HAPProcessorEntityExecutable;
import com.nosliw.data.core.component.HAPContextProcessor;
import com.nosliw.data.core.resource.HAPResourceDefinition;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.resource.HAPResourceIdLocal;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPUtilityDomain {

	public static HAPInfoEntityInDomainDefinition getEntityInfoByResourceId(HAPResourceId resourceId, String currentDomainId, HAPDomainEntityDefinitionGlobal globalDomain) {
		HAPIdEntityInDomain entityId = null;
		if(resourceId.getStructure().equals(HAPConstantShared.RESOURCEID_TYPE_LOCAL)) {
			//local
			entityId = globalDomain.getResourceDomainById(currentDomainId).getLocalResourceDefinition((HAPResourceIdLocal)resourceId).getEntityId();
		}
		else {
			HAPResourceDefinition resourceDefinition = globalDomain.getResourceDefinitionByResourceId(resourceId);
			if(resourceDefinition!=null)	entityId = resourceDefinition.getEntityId();
		}
		return entityId==null? null : globalDomain.getEntityInfoDefinition(entityId);
	}
	
	public static HAPExtraInfoEntityInDomainExecutable buildExecutableExtraInfo(HAPInfoEntityInDomainDefinition defEntityInfo) {
		HAPExtraInfoEntityInDomainExecutable out = new HAPExtraInfoEntityInDomainExecutable(defEntityInfo.getEntityId());
		defEntityInfo.getExtraInfo().cloneToEntityInfo(out);
		return out;
	}
	
	public static void traverseExecutableComplexEntityTree(HAPIdEntityInDomain entityId, HAPProcessorEntityExecutable processor, HAPContextProcessor processContext) {
		traverseExecutableComplexEntityTree(processContext.getCurrentExecutableDomain().getEntityInfoExecutable(entityId), null, null, processor, processContext);
	}

	private static void traverseExecutableComplexEntityTree(HAPInfoEntityInDomainExecutable entityInfo, Object adapter, HAPInfoEntityInDomainExecutable parentEntityInfo, HAPProcessorEntityExecutable processor, HAPContextProcessor processContext) {
		//process current entity
		processor.process(entityInfo, adapter, parentEntityInfo, processContext);
		
		HAPDomainEntityExecutableResourceComplex exeDomain = processContext.getCurrentExecutableDomain(); 

		HAPExecutableEntityComplex complexEntity = entityInfo.getEntity();
				
		//process attribute entity
		Map<String, HAPEmbededEntity> simpleAttributes = complexEntity.getNormalAttributes();
		for(String attrName : simpleAttributes.keySet()) {
			HAPEmbededEntity attributeEntity = simpleAttributes.get(attrName);
			HAPInfoEntityInDomainExecutable attrEntityInfo = exeDomain.getEntityInfoExecutable(attributeEntity.getEntityId());
			traverseExecutableComplexEntityTree(attrEntityInfo, attributeEntity.getAdapter(), entityInfo, processor, processContext);
		}

		//process container attribute entity
		Map<String, HAPContainerEntity> containerAttributes = complexEntity.getContainerAttributes();
		for(String attrName : containerAttributes.keySet()) {
			List<HAPInfoContainerElement> eleInfos = containerAttributes.get(attrName).getAllElementsInfo();
			for(HAPInfoContainerElement eleInfo : eleInfos) {
				HAPEmbededEntity eleEntity = eleInfo.getEmbededElementEntity();
				HAPInfoEntityInDomainExecutable eleEntityInfo = exeDomain.getEntityInfoExecutable(eleEntity.getEntityId()); 
				traverseExecutableComplexEntityTree(eleEntityInfo, eleEntity.getAdapter(), entityInfo, processor, processContext);
			}
		}
	}
	
	public static HAPPackageExecutable getResourceExecutableComplexEntity(HAPResourceId resourceId, HAPRuntimeEnvironment runtimeEnv) {
		HAPContextDomain domainContext = new HAPContextDomain(runtimeEnv.getDomainEntityManager());
		//build definition domain
		HAPResourceDefinition resourceDefinition = runtimeEnv.getResourceDefinitionManager().getResourceDefinition(resourceId, domainContext.getDefinitionDomain(), null);
		domainContext.getDefinitionDomain().setRootComplexEntityId(resourceDefinition.getEntityId());
		
		//process definition
		HAPContextProcessor processorContext = HAPUtilityDomain.createProcessContext(domainContext, runtimeEnv); 
		HAPIdEntityInDomain exeEntityId = runtimeEnv.getComplexEntityManager().process(resourceDefinition.getEntityId(), processorContext);
		return new HAPResultExecutableEntityInDomain(exeEntityId, domainContext);
	}

	
	public static HAPIdEntityInDomain getEntityDescent(HAPIdEntityInDomain entityId, HAPPath p, HAPDomainEntityDefinitionGlobal globalDomain) {
		if(p.isEmpty())  return entityId;
		else return null;
//		HAPPath p = new HAPPath(path);
//		HAPIdEntityInDomain currentEntityId = entityId;
//		HAPInfoEntityInDomainDefinition currentEntityInfo = definitionDomain.getEntityInfoDefinition(currentEntityId);
//		HAPDefinitionEntityInDomain currentEntityDef = currentEntityInfo.getEntity();
//		for(String seg : p.getPathSegments()) {
//			HAPEntityOrReference child = currentEntityDef.getChild(seg);
//			if(HAPConstantShared.REFERENCE.equals(child.getEntityOrReferenceType())) {
//				//resource id
//				out = this.getResourceDefinition((HAPResourceId)defOrId, entityDomain, parentResourceDef.getLocalReferenceBase());
//			}
//			else {
//				//resource def
//				currentEntityDef = (HAPDefinitionEntityInDomain)child;
//				out.setLocalReferenceBase(parentResourceDef.getLocalReferenceBase());
//			}
//
//		}
	}
	
	public static HAPContextParser getContextParse(HAPDomainEntityDefinitionGlobal globalDomain, String currentDomainId) {
		return new HAPContextParser(globalDomain, currentDomainId);
	}
	
	public static HAPContextProcessor createProcessContext(HAPContextDomain domainContext, HAPRuntimeEnvironment runtimeEnv) {
		HAPContextProcessor out = new HAPContextProcessor(domainContext, runtimeEnv);
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
	
	public static String getEntityExpandedJsonString(HAPIdEntityInDomain entityId, HAPDomainEntity entityDomain) {
		return entityDomain.getEntityInfo(entityId).toExpandedJsonString(entityDomain);
	}
	
	public static HAPInfoEntityInDomainDefinition newEntityDefinitionInfoInDomain(String entityType, HAPManagerDomainEntityDefinition entityDefMan) {
		HAPInfoEntityInDomainDefinition out = new HAPInfoEntityInDomainDefinition(entityType);
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
	
	public static HAPContainerEntityImp buildContainer(String containerType) {
		HAPContainerEntityImp out = null;
		if(HAPConstantShared.ENTITYCONTAINER_TYPE_SET.equals(containerType)) {
			out = new HAPContainerEntitySet();
		}
		else if(HAPConstantShared.ENTITYCONTAINER_TYPE_LIST.equals(containerType)) {
			out = new HAPContainerEntityList();
		}
		return out;
	}
}
