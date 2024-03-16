package com.nosliw.data.core.domain;

import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;
import org.json.JSONObject;

import com.nosliw.common.path.HAPPath;
import com.nosliw.common.serialization.HAPSerializable;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.domain.attachment.HAPConfigureComplexRelationAttachment;
import com.nosliw.data.core.domain.entity.HAPConfigureComplexRelationInfo;
import com.nosliw.data.core.domain.entity.HAPConfigureParentRelationComplex;
import com.nosliw.data.core.domain.entity.HAPContextProcessor;
import com.nosliw.data.core.domain.entity.HAPExecutableEntity;
import com.nosliw.data.core.domain.entity.HAPExecutableEntityComplex;
import com.nosliw.data.core.domain.entity.HAPProcessorEntityExecutableDownwardImpAttribute;
import com.nosliw.data.core.domain.entity.HAPReferenceExternal;
import com.nosliw.data.core.resource.HAPResourceDefinition;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.resource.HAPResourceIdLocal;
import com.nosliw.data.core.resource.HAPResourceIdSimple;
import com.nosliw.data.core.runtime.HAPExecutable;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPUtilityDomain {

	public static void buildExternalResourceDependency(HAPDomainEntityExecutableResourceComplex executableDomain, HAPContextProcessor processContext) {
		HAPExecutableEntityComplex rootEntity = executableDomain.getRootEntity();
		HAPUtilityEntityExecutable.traverseExecutableEntityTree(rootEntity, new HAPProcessorEntityExecutableDownwardImpAttribute() {

			@Override
			public void processRootEntity(HAPExecutableEntity entity, HAPContextProcessor processContext) {}

			@Override
			public boolean processAttribute(HAPExecutableEntity parentEntity, String attribute,	HAPContextProcessor processContext) {
				String attrValueType = parentEntity.getAttributeEmbeded(attribute).getValueType();
				if(HAPConstantShared.EMBEDEDVALUE_TYPE_EXTERNALREFERENCE.equals(attrValueType)) {
					executableDomain.addExternalResourceDependency(parentEntity.getAttributeReferenceExternal(attribute).getNormalizedResourceId());
				}
				return true;
			}}, processContext);
	}
	
	public static Pair<HAPExecutable, HAPContextProcessor> resolveAttributeExecutableEntity(HAPExecutableEntity exeEntity, String attribute, HAPContextProcessor processContext){
		HAPExecutable outExe = null;
		HAPContextProcessor outContextProcessor = processContext;

		String attrEntityValueType = exeEntity.getAttributeValueType(attribute);
		if(attrEntityValueType.equals(HAPConstantShared.EMBEDEDVALUE_TYPE_ENTITY)) {
			outExe = exeEntity.getAttributeValueEntity(attribute);
		}
		else if(attrEntityValueType.equals(HAPConstantShared.EMBEDEDVALUE_TYPE_EXTERNALREFERENCE)) {
			HAPReferenceExternal externalRef = exeEntity.getAttributeReferenceExternal(attribute);
			HAPExecutableBundle childBundle = processContext.getRuntimeEnvironment().getDomainEntityExecutableManager().getComplexEntityResourceBundle(externalRef.getNormalizedResourceId().getRootResourceIdSimple());

			HAPExecutableEntityComplex rootEntity = childBundle.getExecutableDomain().getRootEntity();
			HAPPath childEntityPath = externalRef.getNormalizedResourceId().getPath();
			if(childEntityPath.isEmpty()) {
				outExe = rootEntity;
			}
			else {
				HAPExecutableEntity entity = rootEntity;
				for(String seg : childEntityPath.getPathSegments()) {
					Pair<HAPExecutable, HAPContextProcessor> result = resolveAttributeExecutableEntity(entity, seg, outContextProcessor);
					entity = (HAPExecutableEntity)result.getLeft();
					outContextProcessor = result.getRight();
				}
			}
		}
		else if(attrEntityValueType.equals(HAPConstantShared.EMBEDEDVALUE_TYPE_VALUE)) {
			outExe = (HAPExecutable)exeEntity.getAttributeValue(attribute);
		}
		return Pair.of(outExe, outContextProcessor);
	}
	
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
			outExeEntity = childBundle.getExecutableDomain().getEntityInfoExecutable(globalEntityId.getEntityIdInDomain()).getBrick();
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

	
	
	
	

	
	public static HAPExecutablePackage getResourceExecutableComplexEntity(HAPResourceId resourceId, HAPRuntimeEnvironment runtimeEnv) {
		HAPContextDomain domainContext = new HAPContextDomain(runtimeEnv.getDomainEntityDefinitionManager());
		//build definition domain
		HAPResourceDefinition resourceDefinition = runtimeEnv.getResourceDefinitionManager().getResourceDefinition(resourceId, domainContext.getDefinitionDomain(), null);
		domainContext.getDefinitionDomain().setRootComplexEntityId(resourceDefinition.getEntityId());
		
		//process definition
		HAPContextProcessor processorContext = HAPUtilityDomain.createProcessContext(domainContext, runtimeEnv); 
		HAPIdEntityInDomain exeEntityId = runtimeEnv.getDomainEntityExecutableManager().processTreeNode(resourceDefinition.getEntityId(), processorContext);
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
