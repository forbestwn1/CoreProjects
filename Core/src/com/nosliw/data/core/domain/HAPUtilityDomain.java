package com.nosliw.data.core.domain;

import java.util.List;

import org.json.JSONObject;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.complex.HAPConfigureComplexRelationAttachment;
import com.nosliw.data.core.complex.HAPConfigureComplexRelationInfo;
import com.nosliw.data.core.complex.HAPConfigureParentRelationComplex;
import com.nosliw.data.core.complex.HAPProcessorEntityExecutable;
import com.nosliw.data.core.component.HAPContextProcessor;
import com.nosliw.data.core.domain.container.HAPContainerEntityExecutable;
import com.nosliw.data.core.domain.container.HAPElementContainer;
import com.nosliw.data.core.domain.container.HAPElementContainerDefinitionWithId1;
import com.nosliw.data.core.domain.entity.HAPAttributeEntityExecutable;
import com.nosliw.data.core.domain.entity.HAPEmbededExecutable;
import com.nosliw.data.core.domain.entity.HAPEmbededExecutableWithId;
import com.nosliw.data.core.domain.entity.HAPExecutableEntityComplex;
import com.nosliw.data.core.resource.HAPResourceDefinition;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.resource.HAPResourceIdLocal;
import com.nosliw.data.core.resource.HAPResourceIdSimple;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPUtilityDomain {

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
			public void process(HAPInfoEntityInDomainExecutable entityInfo, Object adapter,	HAPInfoEntityInDomainExecutable parentEntityInfo, HAPContextProcessor processContext) {
				if(entityInfo.getEntity()!=null) {
					processor.process(entityInfo, adapter, parentEntityInfo, processContext);
				}
			}
		}, processContext);
	}

	public static void traverseExecutableComplexEntityTree(HAPIdEntityInDomain entityId, HAPProcessorEntityExecutable processor, HAPContextProcessor processContext) {
		traverseExecutableComplexEntityTree(processContext.getCurrentExecutableDomain().getEntityInfoExecutable(entityId), null, null, processor, processContext);
	}

	private static void traverseExecutableComplexEntityTree(HAPInfoEntityInDomainExecutable entityInfo, Object adapter, HAPInfoEntityInDomainExecutable parentEntityInfo, HAPProcessorEntityExecutable processor, HAPContextProcessor processContext) {
		//process current entity
		processor.process(entityInfo, adapter, parentEntityInfo, processContext);
		
		HAPDomainEntityExecutableResourceComplex exeDomain = processContext.getCurrentExecutableDomain(); 

		HAPExecutableEntityComplex complexEntity = entityInfo.getEntity();
		if(complexEntity!=null) {
			List<HAPAttributeEntityExecutable> attrsExe = complexEntity.getAttributes();
			for(HAPAttributeEntityExecutable attrExe : attrsExe) {
				if(attrExe.getIsComplex()) {
					Object attrObj = attrExe.getValue();
					if(attrObj instanceof HAPContainerEntityExecutable) {
						//process container complex attribute
						HAPContainerEntityExecutable containerAttrExe = (HAPContainerEntityExecutable)attrObj;
						if(containerAttrExe.getIsComplex()) {
							List<HAPElementContainer> eleInfos = containerAttrExe.getAllElements();
							for(HAPElementContainer eleInfo : eleInfos) {
								HAPEmbededExecutableWithId eleEntity = (HAPEmbededExecutableWithId)eleInfo.getEmbededElementEntity();
								HAPInfoEntityInDomainExecutable eleEntityInfo = exeDomain.getEntityInfoExecutable(eleEntity.getEntityId()); 
								traverseExecutableComplexEntityTree(eleEntityInfo, eleEntity.getAdapter(), entityInfo, processor, processContext);
							}
						}
					}
					else if(attrObj instanceof HAPEmbededExecutable) {
						HAPEmbededExecutable simpleAttrExe = (HAPEmbededExecutable)attrObj;
						if(simpleAttrExe.getIsComplex()) {
							//process complex simple attribute
							HAPEmbededExecutableWithId complexSimpleAttrExe = (HAPEmbededExecutableWithId)attrObj;
							HAPInfoEntityInDomainExecutable attrEntityInfo = exeDomain.getEntityInfoExecutable(complexSimpleAttrExe.getEntityId());
							traverseExecutableComplexEntityTree(attrEntityInfo, complexSimpleAttrExe.getAdapter(), entityInfo, processor, processContext);
						}
					}
				}
			}
		}
	}
	
	public static HAPExecutablePackage getResourceExecutableComplexEntity(HAPResourceId resourceId, HAPRuntimeEnvironment runtimeEnv) {
		HAPContextDomain domainContext = new HAPContextDomain(runtimeEnv.getDomainEntityManager());
		//build definition domain
		HAPResourceDefinition resourceDefinition = runtimeEnv.getResourceDefinitionManager().getResourceDefinition(resourceId, domainContext.getDefinitionDomain(), null);
		domainContext.getDefinitionDomain().setRootComplexEntityId(resourceDefinition.getEntityId());
		
		//process definition
		HAPContextProcessor processorContext = HAPUtilityDomain.createProcessContext(domainContext, runtimeEnv); 
		HAPIdEntityInDomain exeEntityId = runtimeEnv.getComplexEntityManager().process(resourceDefinition.getEntityId(), processorContext);
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
