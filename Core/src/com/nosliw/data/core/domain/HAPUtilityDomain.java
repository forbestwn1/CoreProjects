package com.nosliw.data.core.domain;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;
import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializable;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.component.HAPContextProcessor;
import com.nosliw.data.core.domain.attachment.HAPConfigureComplexRelationAttachment;
import com.nosliw.data.core.domain.container.HAPContainerEntityExecutable;
import com.nosliw.data.core.domain.container.HAPElementContainer;
import com.nosliw.data.core.domain.entity.HAPAttributeEntityDefinition;
import com.nosliw.data.core.domain.entity.HAPAttributeEntityExecutable;
import com.nosliw.data.core.domain.entity.HAPConfigureComplexRelationInfo;
import com.nosliw.data.core.domain.entity.HAPConfigureParentRelationComplex;
import com.nosliw.data.core.domain.entity.HAPDefinitionEntityInDomainComplex;
import com.nosliw.data.core.domain.entity.HAPEmbededDefinition;
import com.nosliw.data.core.domain.entity.HAPEmbededExecutable;
import com.nosliw.data.core.domain.entity.HAPExecutableEntity;
import com.nosliw.data.core.domain.entity.HAPExecutableEntityComplex;
import com.nosliw.data.core.domain.entity.HAPInfoAdapter;
import com.nosliw.data.core.domain.entity.HAPProcessorEntityDefinition;
import com.nosliw.data.core.domain.entity.HAPProcessorEntityExecutable;
import com.nosliw.data.core.resource.HAPResourceDefinition;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.resource.HAPResourceIdLocal;
import com.nosliw.data.core.resource.HAPResourceIdSimple;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPUtilityDomain {

	public static Pair<HAPExecutableEntity, HAPContextProcessor> resolveExecutableEntityId(HAPIdEntityInDomain exeEntityId, HAPContextProcessor processContext){
		HAPExecutableEntity outExeEntity;
		HAPContextProcessor outProcessorContext;
		HAPInfoEntityInDomainExecutable entityInfo = processContext.getCurrentExecutableDomain().getEntityInfoExecutable(exeEntityId);	
		if(entityInfo.isLocalEntity()) {
			outExeEntity = entityInfo.getEntity();
			outProcessorContext = processContext;
		}
		else {
			HAPIdComplexEntityInGlobal globalEntityId = processContext.getCurrentExecutableDomain().getExternalEntityGlobalId(exeEntityId);
			HAPExecutableBundle childBundle = processContext.getRuntimeEnvironment().getDomainEntityExecutableManager().getComplexEntityResourceBundle(globalEntityId.getResourceInfo().getRootResourceIdSimple());
			outExeEntity = childBundle.getExecutableDomain().getEntityInfoExecutable(globalEntityId.getEntityIdInDomain()).getEntity();
			outProcessorContext = new HAPContextProcessor(childBundle, processContext.getRuntimeEnvironment());
		}
		return Pair.of(outExeEntity, outProcessorContext);
	}
	
	public static <T> T getEntity(Object entityObj, HAPContextProcessor processContext, Class<T> entityClass) {
		Object out = null;
		if(entityClass.isInstance(entityObj))  out = entityObj;    
		else if(entityObj instanceof HAPIdEntityInDomain) {
			HAPIdEntityInDomain entityId = (HAPIdEntityInDomain)entityObj;
			out = processContext.getCurrentDefinitionDomain().getEntityInfoDefinition(entityId).getEntity();
		}
		else if(entityObj instanceof JSONObject) {
			if(HAPSerializable.class.isAssignableFrom(entityClass)) {
				try {
					HAPSerializable serializableEntity = (HAPSerializable)entityClass.newInstance();
					out = serializableEntity.buildObject(entityObj, HAPSerializationFormat.JSON);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return (T)out;
	}
	
	public static void buildExpandedJsonMap(Object value, String valueName, Map<String, String> jsonMap, HAPDomainEntity entityDomain) {
		if(value!=null) {
			if(value instanceof HAPExpandable)   jsonMap.put(valueName, ((HAPExpandable) value).toExpandedJsonString(entityDomain));
			else if(value instanceof HAPIdEntityInDomain) jsonMap.put(valueName, entityDomain.getEntityInfo((HAPIdEntityInDomain)value).toExpandedJsonString(entityDomain));
		}
	}
	
	public static HAPContextProcessor buildNewProcessorContext(HAPContextProcessor currentProcessContext, HAPResourceIdSimple newResourceId) {
		return new HAPContextProcessor(currentProcessContext.getComplexResourcePackageGroup(), newResourceId, currentProcessContext.getRuntimeEnvironment());
	}
	
	public static HAPInfoEntityInDomainDefinition getEntityInfoByResourceId(HAPResourceId resourceId, String currentDomainId, HAPDomainEntityDefinitionGlobal globalDomain) {
		HAPIdEntityInDomain entityId = null;
		if(resourceId.getStructure().equals(HAPConstantShared.RESOURCEID_TYPE_LOCAL)) {
			//local
			entityId = globalDomain.getLocalDomainById(currentDomainId).getLocalResourceDefinition((HAPResourceIdLocal)resourceId).getEntityId();
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

	public static void traverseExecutableComplexEntityTreeSolidOnly(HAPIdEntityInDomain entityId, HAPProcessorEntityExecutable processor, HAPContextProcessor processContext) {
		traverseExecutableComplexEntityTree(entityId, new HAPProcessorEntityExecutable() {
			@Override
			public void process(HAPInfoEntityInDomainExecutable entityInfo, Set<HAPInfoAdapter> adapters,	HAPInfoEntityInDomainExecutable parentEntityInfo, HAPContextProcessor processContext) {
				if(entityInfo.getEntity()!=null) {
					processor.process(entityInfo, adapters, parentEntityInfo, processContext);
				}
			}
		}, processContext);
	}

	public static void traverseExecutableComplexEntityTree(HAPIdEntityInDomain entityId, HAPProcessorEntityExecutable processor, HAPContextProcessor processContext) {
		traverseExecutableComplexEntityTree(processContext.getCurrentExecutableDomain().getEntityInfoExecutable(entityId), null, null, processor, processContext);
	}

	private static void traverseExecutableComplexEntityTree(HAPInfoEntityInDomainExecutable entityInfo, Set<HAPInfoAdapter> adapter, HAPInfoEntityInDomainExecutable parentEntityInfo, HAPProcessorEntityExecutable processor, HAPContextProcessor processContext) {
		//process current entity
		processor.process(entityInfo, adapter, parentEntityInfo, processContext);
		
		HAPDomainEntityExecutableResourceComplex exeDomain = processContext.getCurrentExecutableDomain(); 

		HAPExecutableEntityComplex complexEntity = entityInfo.getEntity();
		if(complexEntity!=null) {
			List<HAPAttributeEntityExecutable> attrsExe = complexEntity.getAttributes();
			for(HAPAttributeEntityExecutable attrExe : attrsExe) {
				if(attrExe.getValueTypeInfo().getIsComplex()) {
					Object attrObj = attrExe.getValue();
					if(attrObj instanceof HAPContainerEntityExecutable) {
						//process container complex attribute
						HAPContainerEntityExecutable containerAttrExe = (HAPContainerEntityExecutable)attrObj;
						if(attrExe.getValueTypeInfo().getIsComplex()) {
							List<HAPElementContainer> eleInfos = containerAttrExe.getAllElements();
							for(HAPElementContainer eleInfo : eleInfos) {
								HAPEmbededExecutable eleEntity = (HAPEmbededExecutable)eleInfo.getEmbededElementEntity();
								HAPInfoEntityInDomainExecutable eleEntityInfo = exeDomain.getEntityInfoExecutable((HAPIdEntityInDomain)eleEntity.getValue()); 
								traverseExecutableComplexEntityTree(eleEntityInfo, eleEntity.getAdapters(), entityInfo, processor, processContext);
							}
						}
					}
					else if(attrObj instanceof HAPEmbededExecutable) {
						//process complex normal attribute
						HAPEmbededExecutable complexSimpleAttrExe = (HAPEmbededExecutable)attrObj;
						HAPInfoEntityInDomainExecutable attrEntityInfo = exeDomain.getEntityInfoExecutable((HAPIdEntityInDomain)complexSimpleAttrExe.getValue());
						traverseExecutableComplexEntityTree(attrEntityInfo, complexSimpleAttrExe.getAdapters(), entityInfo, processor, processContext);
					}
				}
			}
		}
	}
	

	public static void traverseDefinitionComplexEntityTree(HAPIdEntityInDomain entityId, HAPProcessorEntityDefinition processor, HAPDomainEntityDefinitionGlobal globalDomain) {
		traverseDefinitionComplexEntityTree(globalDomain.getEntityInfoDefinition(entityId), null, null, processor, globalDomain);
	}

	private static void traverseDefinitionComplexEntityTree(HAPInfoEntityInDomainDefinition entityInfo, Set<HAPInfoAdapter> adapter, HAPInfoEntityInDomainDefinition parentEntityInfo, HAPProcessorEntityDefinition processor, HAPDomainEntityDefinitionGlobal globalDomain) {
		//process current entity
		processor.process(entityInfo, adapter, parentEntityInfo, globalDomain);

		HAPDefinitionEntityInDomainComplex complexEntity = (HAPDefinitionEntityInDomainComplex)entityInfo.getEntity();
		if(complexEntity!=null) {
			List<HAPAttributeEntityDefinition> attrsDef = complexEntity.getAttributes();
			for(HAPAttributeEntityDefinition attrDef : attrsDef) {
				if(attrDef.getValueTypeInfo().getIsComplex()) {
					Object attrObj = attrDef.getValue();
					if(attrObj instanceof HAPEmbededDefinition) {
						//process complex normal attribute
						HAPEmbededDefinition complexSimpleAttrDef = (HAPEmbededDefinition)attrObj;
						HAPInfoEntityInDomainDefinition attrEntityInfo = globalDomain.getEntityInfoDefinition((HAPIdEntityInDomain)complexSimpleAttrDef.getValue());
						traverseDefinitionComplexEntityTree(attrEntityInfo, complexSimpleAttrDef.getAdapters(), entityInfo, processor, globalDomain);
					}
				}
			}
		}
	}
	
	
	
	

	
	public static HAPExecutablePackage getResourceExecutableComplexEntity(HAPResourceId resourceId, HAPRuntimeEnvironment runtimeEnv) {
		HAPContextDomain domainContext = new HAPContextDomain(runtimeEnv.getDomainEntityDefinitionManager());
		//build definition domain
		HAPResourceDefinition resourceDefinition = runtimeEnv.getResourceDefinitionManager().getResourceDefinition(resourceId, domainContext.getDefinitionDomain(), null);
		domainContext.getDefinitionDomain().setRootComplexEntityId(resourceDefinition.getEntityId());
		
		//process definition
		HAPContextProcessor processorContext = HAPUtilityDomain.createProcessContext(domainContext, runtimeEnv); 
		HAPIdEntityInDomain exeEntityId = runtimeEnv.getDomainEntityExecutableManager().postProcess(resourceDefinition.getEntityId(), processorContext);
		return new HAPResultExecutableEntityInDomain(exeEntityId, domainContext);
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
	
	public static HAPElementContainerDefinitionWithId1 newInfoContainerElementSet(HAPIdEntityInDomain entityId, JSONObject jsonObj) {
		HAPElementContainerDefinitionWithId1 out = new HAPElementContainerDefinitionWithId1(entityId);
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
	
}
