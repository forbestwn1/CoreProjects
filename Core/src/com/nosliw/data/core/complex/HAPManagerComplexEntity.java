package com.nosliw.data.core.complex;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.data.core.component.HAPContextProcessor;
import com.nosliw.data.core.domain.HAPContainerEntity;
import com.nosliw.data.core.domain.HAPContextDomain;
import com.nosliw.data.core.domain.HAPDomainEntityDefinition;
import com.nosliw.data.core.domain.HAPDomainEntityExecutable;
import com.nosliw.data.core.domain.HAPDomainValueStructure;
import com.nosliw.data.core.domain.HAPEmbededEntity;
import com.nosliw.data.core.domain.HAPExtraInfoEntityInDomainExecutable;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.HAPInfoContainerElement;
import com.nosliw.data.core.domain.HAPInfoEntityInDomainDefinition;
import com.nosliw.data.core.domain.HAPUtilityDomain;
import com.nosliw.data.core.domain.entity.valuestructure.HAPDefinitionEntityComplexValueStructure;
import com.nosliw.data.core.domain.entity.valuestructure.HAPInfoPartSimple;
import com.nosliw.data.core.domain.entity.valuestructure.HAPProcessorValueStructureInComponent;
import com.nosliw.data.core.domain.entity.valuestructure.HAPUtilityComplexValueStructure;
import com.nosliw.data.core.domain.entity.valuestructure.HAPValueStructureInComplex;

public class HAPManagerComplexEntity {

	private Map<String, HAPPluginComplexEntityProcessor> m_processorPlugins;

	public HAPManagerComplexEntity() {
		this.m_processorPlugins = new LinkedHashMap<String, HAPPluginComplexEntityProcessor>();
	}

	
	public HAPIdEntityInDomain process(HAPIdEntityInDomain complexEntityDefinitionId, HAPContextProcessor processContext) {
		HAPIdEntityInDomain mainExeEntityId = null;
		
		HAPContextDomain domainContext = processContext.getDomainContext();
		HAPDomainEntityDefinition defDomain = domainContext.getDefinitionDomain();
		HAPDomainEntityExecutable exeDomain = domainContext.getExecutableDomain();
		
		//build executable complexe entity
		HAPIdEntityInDomain rootIdDef = defDomain.getRootComplexEntity();
		HAPIdEntityInDomain rootIdExe = buildExecutableTree(rootIdDef, complexEntityDefinitionId, processContext);
		exeDomain.setRootEntityId(rootIdExe);
		mainExeEntityId = exeDomain.getMainEntityId();
		
		//process attachment
		HAPUtilityAttachment.buildAttachmentDomain(rootIdExe, processContext);

		//expand reference in value strucutre
//		expandValueStructure(rootId, processContext);

		//process value structure
//		processValueStructureTree(rootId, processContext);
		
		HAPPluginComplexEntityProcessor processPlugin = this.m_processorPlugins.get(complexEntityDefinitionId.getEntityType());
		processPlugin.process(mainExeEntityId, processContext);
		return mainExeEntityId;
	}

	private HAPIdEntityInDomain buildExecutableTree(HAPIdEntityInDomain complexEntityDefinitionId, HAPIdEntityInDomain mainComplexEntityDefinitionId, HAPContextProcessor processContext) {
		HAPContextDomain domainContext = processContext.getDomainContext();
		HAPDomainEntityDefinition defDomain = domainContext.getDefinitionDomain();
		HAPDomainEntityExecutable exeDomain = domainContext.getExecutableDomain();
		
		//create executable and add to domain
		HAPInfoEntityInDomainDefinition entityDefInfo = defDomain.getSolidEntityInfoDefinition(complexEntityDefinitionId);
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
					HAPDomainEntityExecutable exeDomain = domainContext.getExecutableDomain();
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
					HAPDomainEntityExecutable exeDomain = domainContext.getExecutableDomain();

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
