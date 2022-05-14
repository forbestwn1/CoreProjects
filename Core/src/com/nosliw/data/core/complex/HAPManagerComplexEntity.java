package com.nosliw.data.core.complex;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.common.utils.HAPGeneratorId;
import com.nosliw.data.core.component.HAPContextProcessor;
import com.nosliw.data.core.domain.HAPContainerEntity;
import com.nosliw.data.core.domain.HAPContextDomain;
import com.nosliw.data.core.domain.HAPDomainEntityDefinitionGlobal;
import com.nosliw.data.core.domain.HAPDomainEntityExecutableGlobal;
import com.nosliw.data.core.domain.HAPDomainEntityExecutableResourceComplex;
import com.nosliw.data.core.domain.HAPDomainValueStructure;
import com.nosliw.data.core.domain.HAPEmbededEntity;
import com.nosliw.data.core.domain.HAPExtraInfoEntityInDomainExecutable;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.HAPInfoContainerElement;
import com.nosliw.data.core.domain.HAPInfoEntityInDomainDefinition;
import com.nosliw.data.core.domain.HAPManagerDomainEntityDefinition;
import com.nosliw.data.core.domain.HAPPackageComplexResource;
import com.nosliw.data.core.domain.HAPPackageExecutable;
import com.nosliw.data.core.domain.HAPUtilityDomain;
import com.nosliw.data.core.domain.entity.valuestructure.HAPDefinitionEntityComplexValueStructure;
import com.nosliw.data.core.domain.entity.valuestructure.HAPInfoPartSimple;
import com.nosliw.data.core.domain.entity.valuestructure.HAPProcessorValueStructureInComponent;
import com.nosliw.data.core.domain.entity.valuestructure.HAPUtilityComplexValueStructure;
import com.nosliw.data.core.domain.entity.valuestructure.HAPValueStructureInComplex;
import com.nosliw.data.core.resource.HAPInfoResourceIdNormalize;
import com.nosliw.data.core.resource.HAPManagerResourceDefinition;
import com.nosliw.data.core.resource.HAPResourceIdSimple;

public class HAPManagerComplexEntity {

	private HAPManagerResourceDefinition m_resourceDefinitionManager;
	
	private HAPManagerDomainEntityDefinition m_entityDefManager;

	private HAPGeneratorId m_idGenerator;

	//pulug in for processing complex entity
	private Map<String, HAPPluginComplexEntityProcessor> m_processorPlugins;

	private Map<HAPResourceIdSimple, HAPPackageComplexResource> m_complexResourcePackages;
	
	public HAPManagerComplexEntity(HAPManagerDomainEntityDefinition entityDefManager, HAPManagerResourceDefinition resourceDefinitionManager) {
		this.m_entityDefManager = entityDefManager;
		this.m_resourceDefinitionManager = resourceDefinitionManager;
		this.m_processorPlugins = new LinkedHashMap<String, HAPPluginComplexEntityProcessor>();
		this.m_complexResourcePackages = new LinkedHashMap<HAPResourceIdSimple, HAPPackageComplexResource>();
		this.m_idGenerator = new HAPGeneratorId();
	}

	public HAPPackageExecutable process(HAPResourceIdSimple resourceId) {
		HAPPackageExecutable out = new HAPPackageExecutable();

		HAPInfoResourceIdNormalize normalizedResourceInfo = this.m_resourceDefinitionManager.normalizeResourceId(resourceId);
		buildComplexEntityResourcePackage(new HAPContextProcessor(out.getComplexResourcePackageGroup(), (HAPResourceIdSimple)normalizedResourceInfo.getRootResourceId()));
		
//		HAPResourceIdSimple mainResourceId = (HAPResourceIdSimple)normalizedResourceInfo.getRootResourceId();
//		HAPPackageComplexResource mainComplexResourcePackage = out.getComplexResourcePackage(mainResourceId);
//		mainComplexResourcePackage.getDefinitionDomain().getResourceDefinitionByResourceId(resourc);
//		
//		HAPUtilityDomain.getEntityDescent(entityId, p, globalDomain)
		
		return out;
	}
	
	//build complex resource package within executable package
	private HAPPackageComplexResource buildComplexEntityResourcePackage(HAPContextProcessor processContext) {
		HAPResourceIdSimple complexEntityResourceId = processContext.getCurrentComplexResourceId();
		HAPPackageComplexResource out = processContext.getCurrentComplexResourcePackage();
		if(out==null) {
			//if not load in package
			out = this.m_complexResourcePackages.get(complexEntityResourceId);
			if(out==null) {
				//build definition
				HAPDomainEntityDefinitionGlobal globalDefDomain = new HAPDomainEntityDefinitionGlobal(this.m_idGenerator, this.m_entityDefManager, this.m_resourceDefinitionManager);
				globalDefDomain.setRootResourceId(complexEntityResourceId);
				this.m_resourceDefinitionManager.getResourceDefinition(complexEntityResourceId, globalDefDomain);
				out = new HAPPackageComplexResource(globalDefDomain, this.m_idGenerator);
				processContext.addComplexResourcePackage(out);
				//build executable
				this.process(processContext);
			}
			else {
				//if already generated
				processContext.addComplexResourcePackage(out);
			}
		}
		return out;
	}
	
	private void process(HAPContextProcessor processContext) {
		HAPPackageComplexResource complexResourcePackage = processContext.getCurrentComplexResourcePackage();
		HAPDomainEntityDefinitionGlobal defDomain = complexResourcePackage.getDefinitionDomain();
		HAPDomainEntityExecutableResourceComplex exeDomain = complexResourcePackage.getExecutableDomain();
		
		//build complex entity tree
		HAPIdEntityInDomain rootEntityIdExe = buildExecutableTree(defDomain.getRootEntityId(), processContext);
		
		//build attachment domain(build attachment tree, merge attachment value)
		HAPUtilityAttachment.buildAttachmentDomain(rootEntityIdExe, processContext);
		
		//process value structure
//		HAPUtilityValueStructure.buildValueStructureDomain(rootIdExe, processContext);
//		processValueStructureTree(rootId, processContext);
	}
	
	public HAPIdEntityInDomain process(HAPIdEntityInDomain complexEntityDefinitionId, HAPContextProcessor processContext) {
		HAPIdEntityInDomain mainExeEntityId = null;
		
		HAPContextDomain domainContext = processContext.getDomainContext();
		HAPDomainEntityDefinitionGlobal defDomain = domainContext.getDefinitionDomain();
		HAPDomainEntityExecutableGlobal exeDomain = domainContext.getExecutableDomain();
		
		//build executable complexe entity
		HAPIdEntityInDomain rootIdDef = defDomain.getRootComplexEntity();
		HAPIdEntityInDomain rootIdExe = buildExecutableTree(rootIdDef, complexEntityDefinitionId, processContext);
		exeDomain.setRootEntityId(rootIdExe);
		mainExeEntityId = exeDomain.getMainEntityId();
		
		//build attachment domain(build attachment tree, merge attachment value)
		HAPUtilityAttachment.buildAttachmentDomain(rootIdExe, processContext);

		//expand reference in value strucutre
//		expandValueStructure(rootId, processContext);

		//process value structure
		HAPUtilityValueStructure.buildValueStructureDomain(rootIdExe, processContext);
//		processValueStructureTree(rootId, processContext);
		
		HAPPluginComplexEntityProcessor processPlugin = this.m_processorPlugins.get(complexEntityDefinitionId.getEntityType());
		processPlugin.process(mainExeEntityId, processContext);
		return mainExeEntityId;
	}

	private HAPIdEntityInDomain buildExecutableTree(HAPIdEntityInDomain complexEntityDefinitionId, HAPContextProcessor processContext) {
		HAPPackageComplexResource complexResourcePackage = processContext.getCurrentComplexResourcePackage();
		HAPDomainEntityDefinitionGlobal defDomain = complexResourcePackage.getDefinitionDomain();
		HAPDomainEntityExecutableResourceComplex exeDomain = complexResourcePackage.getExecutableDomain();
		
		//create executable and add to domain
		HAPIdEntityInDomain out = null;
		HAPInfoEntityInDomainDefinition entityDefInfo = defDomain.getSolidEntityInfoDefinition(complexEntityDefinitionId, null);
		if(entityDefInfo.isSolid()) {
			HAPDefinitionEntityInDomainComplex complexEntityDef = (HAPDefinitionEntityInDomainComplex)entityDefInfo.getEntity();
			String entityType = complexEntityDef.getEntityType();
			HAPExecutableEntityComplex exeEntity = this.m_processorPlugins.get(entityType).newExecutable();
			HAPExtraInfoEntityInDomainExecutable exeExtraInfo = HAPUtilityDomain.buildExecutableExtraInfo(entityDefInfo);
			out = complexResourcePackage.addExecutableEntity(complexEntityDefinitionId, exeEntity, exeExtraInfo);
			HAPExecutableEntityComplex complexEntityExe = exeDomain.getEntityInfoExecutable(out).getEntity();
			
			//build executable for simple complex attribute
			Map<String, HAPEmbededEntity> simpleAttributes = complexEntityDef.getSimpleAttributes();
			for(String attrName : simpleAttributes.keySet()) {
				HAPIdEntityInDomain attrEntityDefId = simpleAttributes.get(attrName).getEntityId();
				HAPInfoEntityInDomainDefinition entityInfo = defDomain.getEntityInfoDefinition(attrEntityDefId);
				if(entityInfo.isComplexEntity()) {
					HAPIdEntityInDomain attrEntityExeId = buildExecutableTree(attrEntityDefId, processContext);
					complexEntityExe.setNormalComplexAttribute(attrName, attrEntityExeId);
				}
			}
			
			//build executable for container complex attribute
			Map<String, HAPContainerEntity> containerAttributes = complexEntityDef.getContainerAttributes();
			for(String attrName : containerAttributes.keySet()) {
				HAPContainerEntity defContainer = containerAttributes.get(attrName);
				HAPContainerEntity exeContainer = HAPUtilityDomain.buildContainer(defContainer.getContainerType());
				List<HAPInfoContainerElement> defEleInfos = defContainer.getAllElementsInfo();
				for(HAPInfoContainerElement defEleInfo : defEleInfos) {
					HAPIdEntityInDomain defEleId = defEleInfo.getEmbededElementEntity().getEntityId();
					HAPInfoEntityInDomainDefinition eleEntityInfo = defDomain.getEntityInfoDefinition(defEleId);
					if(eleEntityInfo.isComplexEntity()) {
						HAPIdEntityInDomain eleEntityExeId = buildExecutableTree(defEleId, processContext);
						HAPInfoContainerElement exeEleInfo = defEleInfo.cloneContainerElementInfo();
						exeEleInfo.setEmbededElementEntity(new HAPEmbededEntity(eleEntityExeId));
						exeContainer.addEntityElement(exeEleInfo);
					}
				}
				complexEntityExe.setContainerAttributeElementComplex(attrName, exeContainer);
			}
		}
		else {
			HAPInfoResourceIdNormalize normalizedResourceId = this.m_resourceDefinitionManager.normalizeResourceId(entityDefInfo.getResourceId());
			buildComplexEntityResourcePackage(new HAPContextProcessor(processContext.getComplexResourcePackageGroup(), (HAPResourceIdSimple)normalizedResourceId.getRootResourceId()));
			HAPExtraInfoEntityInDomainExecutable exeExtraInfo = HAPUtilityDomain.buildExecutableExtraInfo(entityDefInfo);
			out = complexResourcePackage.addExecutableEntity(complexEntityDefinitionId, normalizedResourceId, exeExtraInfo);
		}
		
		return out;
	}

	
	private HAPIdEntityInDomain buildExecutableTree(HAPIdEntityInDomain complexEntityDefinitionId, HAPIdEntityInDomain mainComplexEntityDefinitionId, HAPContextProcessor processContext) {
		HAPContextDomain domainContext = processContext.getDomainContext();
		HAPDomainEntityDefinitionGlobal defDomain = domainContext.getDefinitionDomain();
		HAPDomainEntityExecutableGlobal exeDomain = domainContext.getExecutableDomain();
		
		//create executable and add to domain
		HAPInfoEntityInDomainDefinition entityDefInfo = defDomain.getSolidEntityInfoDefinition(complexEntityDefinitionId, null);
		HAPDefinitionEntityInDomainComplex complexEntityDef = (HAPDefinitionEntityInDomainComplex)entityDefInfo.getEntity();
		String entityType = complexEntityDef.getEntityType();
		HAPExecutableEntityComplex exeEntity = this.m_processorPlugins.get(entityType).newExecutable();
		HAPExtraInfoEntityInDomainExecutable exeExtraInfo = HAPUtilityDomain.buildExecutableExtraInfo(entityDefInfo);
		HAPIdEntityInDomain complexeEntityExeId = domainContext.addExecutableEntity(exeEntity, exeExtraInfo);
		HAPExecutableEntityComplex complexEntityExe = exeDomain.getEntityInfoExecutable(complexeEntityExeId).getEntity();
		
		if(complexEntityDefinitionId.equals(mainComplexEntityDefinitionId)) {
			//main entity
			exeDomain.setMainEntityId(complexeEntityExeId);
		}
		
		//build executable for simple complex attribute
		Map<String, HAPEmbededEntity> simpleAttributes = complexEntityDef.getSimpleAttributes();
		for(String attrName : simpleAttributes.keySet()) {
			HAPIdEntityInDomain attrEntityDefId = simpleAttributes.get(attrName).getEntityId();
			HAPInfoEntityInDomainDefinition entityInfo = defDomain.getEntityInfoDefinition(attrEntityDefId);
			if(entityInfo.isComplexEntity()) {
				HAPIdEntityInDomain attrEntityExeId = buildExecutableTree(attrEntityDefId, mainComplexEntityDefinitionId, processContext);
				complexEntityExe.setNormalComplexAttribute(attrName, attrEntityExeId);
			}
		}
		
		//build executable for container complex attribute
		Map<String, HAPContainerEntity> containerAttributes = complexEntityDef.getContainerAttributes();
		for(String attrName : containerAttributes.keySet()) {
			HAPContainerEntity defContainer = containerAttributes.get(attrName);
			HAPContainerEntity exeContainer = HAPUtilityDomain.buildContainer(defContainer.getContainerType());
			List<HAPInfoContainerElement> defEleInfos = defContainer.getAllElementsInfo();
			for(HAPInfoContainerElement defEleInfo : defEleInfos) {
				HAPIdEntityInDomain defEleId = defEleInfo.getEmbededElementEntity().getEntityId();
				HAPInfoEntityInDomainDefinition eleEntityInfo = defDomain.getEntityInfoDefinition(defEleId);
				if(eleEntityInfo.isComplexEntity()) {
					HAPIdEntityInDomain eleEntityExeId = buildExecutableTree(defEleId, mainComplexEntityDefinitionId, processContext);
					HAPInfoContainerElement exeEleInfo = defEleInfo.cloneContainerElementInfo();
					exeEleInfo.setEmbededElementEntity(new HAPEmbededEntity(eleEntityExeId));
					exeContainer.addEntityElement(exeEleInfo);
				}
			}
			complexEntityExe.setContainerAttributeElementComplex(attrName, exeContainer);
		}
		
		return complexeEntityExeId;
	}
	
	private void expandValueStructure(HAPIdEntityInDomain complexEntityDefinitionId, HAPContextProcessor processContext) {
		HAPUtilityComplexEntity.traversComplexEntityTree(complexEntityDefinitionId, new HAPProcessorEntityDefinition() {
			@Override
			public void process(HAPInfoEntityInDomainDefinition complexEntityInfo, Object adapter, HAPInfoEntityInDomainDefinition parentComplexEntityInfo,
					HAPContextProcessor processContext) {
				if(parentComplexEntityInfo!=null) {
					HAPContextDomain domainContext = processContext.getDomainContext();
					HAPDomainEntityExecutableResource exeDomain = domainContext.getExecutableDomain();
					HAPDomainValueStructure valueStructureDomain = exeDomain.getValueStructureDomain();

					HAPIdEntityInDomain complexEntityExeId = domainContext.getExecutableIdByDefinitionId(complexEntityDefinitionId);
					HAPExecutableEntityComplex complexEntityExe = domainContext.getExecutableEntityByDefinitionId(complexEntityDefinitionId);

					//expand reference
					HAPDefinitionEntityComplexValueStructure valueStructureComplex = valueStructureDomain.getValueStructureComplex(complexEntityExe.getValueStructureComplexId());
					List<HAPInfoPartSimple> simpleValueStructureParts = HAPUtilityComplexValueStructure.getAllSimpleParts(valueStructureComplex);
					for(HAPInfoPartSimple simpleValueStructurePart : simpleValueStructureParts) {
						HAPProcessorValueStructureInComponent.expandReference((HAPValueStructureInComplex)simpleValueStructurePart.getSimpleValueStructurePart().getValueStructure(), processContext);
					}
				}

			}}, processContext);
	}
	
	private void processValueStructureTree(HAPIdEntityInDomain complexEntityDefinitionId, HAPContextProcessor processContext) {
		HAPUtilityComplexEntity.traversComplexEntityTree(complexEntityDefinitionId, new HAPProcessorEntityDefinition() {
			@Override
			public void process(HAPInfoEntityInDomainDefinition complexEntityInfo, Object adapter, HAPInfoEntityInDomainDefinition parentComplexEntityInfo,
					HAPContextProcessor processContext) {
				if(parentComplexEntityInfo!=null) {
					HAPContextDomain domainContext = processContext.getDomainContext();
					HAPDomainEntityExecutableResource exeDomain = domainContext.getExecutableDomain();

					HAPIdEntityInDomain complexEntityExeId = domainContext.getExecutableIdByDefinitionId(complexEntityDefinitionId);
					HAPExecutableEntityComplex complexEntityExe = domainContext.getExecutableEntityByDefinitionId(complexEntityExeId);

					HAPIdEntityInDomain parentComplexEntityExeId = domainContext.getExecutableIdByDefinitionId(parentComplexEntityInfo.getEntityId());
					HAPExecutableEntityComplex parentComplexEntityExe = domainContext.getExecutableEntityByDefinitionId(parentComplexEntityExeId);

					//process inheritance
					HAPUtilityComplexValueStructure.processValueStructureInheritance(complexEntityExe.getValueStructureComplexId(), parentComplexEntityExe.getValueStructureComplexId(), parentComplexEntityInfo.getParentRelationConfigure().getValueStructureRelationMode(), exeDomain.getValueStructureDomain());
				}

			}}, processContext);
	}
	
	
	public void registerProcessorPlugin(HAPPluginComplexEntityProcessor processorPlugin) {
		this.m_processorPlugins.put(processorPlugin.getEntityType(), processorPlugin);
	}
	
}
