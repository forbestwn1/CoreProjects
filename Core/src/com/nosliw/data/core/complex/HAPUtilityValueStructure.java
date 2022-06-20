package com.nosliw.data.core.complex;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.component.HAPContextProcessor;
import com.nosliw.data.core.domain.HAPDomainEntityDefinitionGlobal;
import com.nosliw.data.core.domain.HAPDomainEntityExecutableResourceComplex;
import com.nosliw.data.core.domain.HAPDomainValueStructure;
import com.nosliw.data.core.domain.HAPEmbededEntity;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.HAPInfoEntityInDomainDefinition;
import com.nosliw.data.core.domain.HAPInfoEntityInDomainExecutable;
import com.nosliw.data.core.domain.HAPBundleComplexResource;
import com.nosliw.data.core.domain.HAPUtilityDomain;
import com.nosliw.data.core.domain.entity.attachment.HAPDefinitionEntityContainerAttachment;
import com.nosliw.data.core.domain.entity.valuestructure.HAPConfigureProcessorInherit;
import com.nosliw.data.core.domain.entity.valuestructure.HAPConfigureProcessorValueStructure;
import com.nosliw.data.core.domain.entity.valuestructure.HAPDefinitionEntityComplexValueStructure;
import com.nosliw.data.core.domain.entity.valuestructure.HAPDefinitionEntityValueStructure;
import com.nosliw.data.core.domain.entity.valuestructure.HAPExecutableEntityComplexValueStructure;
import com.nosliw.data.core.domain.entity.valuestructure.HAPInfoPartSimple;
import com.nosliw.data.core.domain.entity.valuestructure.HAPPartComplexValueStructure;
import com.nosliw.data.core.domain.entity.valuestructure.HAPUtilityComplexValueStructure;
import com.nosliw.data.core.domain.entity.valuestructure.HAPUtilityProcessRelativeElement;
import com.nosliw.data.core.domain.entity.valuestructure.HAPWrapperValueStructureDefinition;
import com.nosliw.data.core.domain.entity.valuestructure.HAPWrapperValueStructureExecutable;
import com.nosliw.data.core.structure.reference.HAPCandidatesValueStructureComplex;

public class HAPUtilityValueStructure {

	public static void buildValueStructureDomain(HAPIdEntityInDomain rootComplexEntityExecutableId, HAPContextProcessor processContext) {
		
		buildValueStructureComplexTree(rootComplexEntityExecutableId, processContext);
		
		mergeValueStructure(rootComplexEntityExecutableId, processContext);
	}

	//build value structure in complex tree and add to value structure domain
	private static void buildValueStructureComplexTree(HAPIdEntityInDomain rootComplexEntityExecutableId, HAPContextProcessor processContext) {
		HAPUtilityDomain.traverseExecutableComplexEntityTreeSolidOnly(rootComplexEntityExecutableId, new HAPProcessorEntityExecutable() {
			@Override
			public void process(HAPInfoEntityInDomainExecutable entityInfo, Object adapter, HAPInfoEntityInDomainExecutable parentEntityInfo, HAPContextProcessor processContext) {
				
				HAPBundleComplexResource complexEntityPackage = processContext.getCurrentComplexResourcePackage();
				HAPDomainEntityDefinitionGlobal definitionGlobalDomain = processContext.getCurrentDefinitionDomain();
				HAPDomainEntityExecutableResourceComplex exeDomain = processContext.getCurrentExecutableDomain();
				HAPDomainValueStructure valueStructureDomain = exeDomain.getValueStructureDomain();

				HAPIdEntityInDomain entityIdExe = entityInfo.getEntityId();
				HAPExecutableEntityComplex complexEntityExe = exeDomain.getEntityInfoExecutable(entityIdExe).getEntity();
				HAPIdEntityInDomain entityIdDef = complexEntityPackage.getDefinitionEntityIdByExecutableEntityId(entityIdExe);
				HAPDefinitionEntityContainerAttachment attachmentContainer = HAPUtilityAttachment.getAttachmentContainerByComplexExeId(entityIdExe, processContext);

				HAPInfoEntityInDomainDefinition complexEntityInfoDef = definitionGlobalDomain.getSolidEntityInfoDefinition(entityIdDef, attachmentContainer);
				
				HAPDefinitionEntityComplexValueStructure valueStructureComplexEntityDef = null;
				HAPEmbededEntity valueStructureComplexAttribute = ((HAPDefinitionEntityInDomainComplex)complexEntityInfoDef.getEntity()).getValueStructureComplexEntity();
				if(valueStructureComplexAttribute!=null) {
					valueStructureComplexEntityDef = (HAPDefinitionEntityComplexValueStructure)definitionGlobalDomain.getSolidEntityInfoDefinition(valueStructureComplexAttribute.getEntityId(), attachmentContainer).getEntity();
				}

				//extra value structure
				HAPExecutableEntityComplexValueStructure valueStructureComplexExe = new HAPExecutableEntityComplexValueStructure();
				if(valueStructureComplexEntityDef!=null) {
					List<HAPWrapperValueStructureExecutable> wrappers = new ArrayList<HAPWrapperValueStructureExecutable>();
					for(HAPWrapperValueStructureDefinition part : valueStructureComplexEntityDef.getParts()) {
						HAPInfoEntityInDomainDefinition valueStructureDefInfo = definitionGlobalDomain.getSolidEntityInfoDefinition(part.getValueStructureId(), attachmentContainer);
						String valueStructureExeId = valueStructureDomain.newValueStructure(valueStructureDefInfo, part.getValueStructureId().getEntityId());
						HAPWrapperValueStructureExecutable valueStructureWrapperExe = new HAPWrapperValueStructureExecutable(valueStructureExeId);
						valueStructureWrapperExe.cloneFromDefinition(part);
						wrappers.add(valueStructureWrapperExe);
					}
					valueStructureComplexExe.addPartSimple(wrappers, HAPUtilityComplexValueStructure.createPartInfoDefault());
				}
				complexEntityExe.setValueStructureComplex(valueStructureComplexExe);
				
			}}, processContext);
	}

	//merge value structure between paren and child
	private static void mergeValueStructure(HAPIdEntityInDomain rootComplexEntityExecutableId, HAPContextProcessor processContext) {
		HAPUtilityDomain.traverseExecutableComplexEntityTreeSolidOnly(rootComplexEntityExecutableId, new HAPProcessorEntityExecutable() {
			@Override
			public void process(HAPInfoEntityInDomainExecutable entityExeInfo, Object adapter, HAPInfoEntityInDomainExecutable parentEntityExeInfo,
					HAPContextProcessor processContext) {
				if(parentEntityExeInfo!=null) {

					HAPBundleComplexResource complexEntityPackage = processContext.getCurrentComplexResourcePackage();
					HAPDomainEntityDefinitionGlobal definitionGlobalDomain = processContext.getCurrentDefinitionDomain();
					HAPDomainEntityExecutableResourceComplex exeDomain = processContext.getCurrentExecutableDomain();
					HAPDomainValueStructure valueStructureDomain = exeDomain.getValueStructureDomain();

					HAPIdEntityInDomain entityIdExe = entityExeInfo.getEntityId();
					HAPIdEntityInDomain entityIdDef = complexEntityPackage.getDefinitionEntityIdByExecutableEntityId(entityIdExe);
					HAPDefinitionEntityContainerAttachment attachmentContainer = HAPUtilityAttachment.getAttachmentContainerByComplexExeId(entityIdExe, processContext);

					HAPExecutableEntityComplex entityExe = exeDomain.getEntityInfoExecutable(entityIdExe).getEntity();
					String attachmentContainerId = entityExe.getAttachmentContainerId();
					HAPExecutableEntityComplexValueStructure valueStructureComplex = entityExe.getValueStructureComplex();

					HAPIdEntityInDomain parentEntityIdExe = parentEntityExeInfo.getEntityId();
					HAPIdEntityInDomain parentEntityIdDef = complexEntityPackage.getDefinitionEntityIdByExecutableEntityId(parentEntityIdExe);

					HAPExecutableEntityComplex parentEntityExe = exeDomain.getEntityInfoExecutable(parentEntityIdExe).getEntity();
					String parentAttachmentContainerId = parentEntityExe.getAttachmentContainerId();
					HAPExecutableEntityComplexValueStructure parentValueStructureComplex = parentEntityExe.getValueStructureComplex();
					
					HAPConfigureProcessorValueStructure valueStructureConfig = definitionGlobalDomain.getComplexEntityParentInfo(entityIdDef).getParentRelationConfigure().getValueStructureRelationMode();
					
					//process static
					
					
					//process relative
					List<HAPInfoPartSimple> simpleValueStructureParts = HAPUtilityComplexValueStructure.getAllSimpleParts(valueStructureComplex);
					for(HAPInfoPartSimple simplePart : simpleValueStructureParts) {
						for(HAPWrapperValueStructureExecutable valueStructureWrapper : simplePart.getSimpleValueStructurePart().getValueStructures()) {
							HAPDefinitionEntityValueStructure valueStructure = valueStructureDomain.getValueStructureDefinitionByRuntimeId(valueStructureWrapper.getValueStructureRuntimeId());
							List<HAPServiceData> errors = new ArrayList<HAPServiceData>();
							Set<String> dependency = new HashSet<String>();
							HAPCandidatesValueStructureComplex valueStructureComplexGroup = new HAPCandidatesValueStructureComplex(valueStructureComplex, parentValueStructureComplex);
							HAPUtilityProcessRelativeElement.processRelativeInStructure(valueStructure, valueStructureComplexGroup, valueStructureDomain, valueStructureConfig.getRelativeProcessorConfigure(), dependency, errors, processContext.getRuntimeEnvironment());
						}
					}
					
					//inheritance
					processInteritance(valueStructureComplex, parentValueStructureComplex, valueStructureConfig.getInheritProcessorConfigure(), valueStructureDomain);
					
				}

			}}, processContext);
	}

	private static void processInteritance(HAPExecutableEntityComplexValueStructure valueStructureComplex, HAPExecutableEntityComplexValueStructure parentValueStructureComplex, HAPConfigureProcessorInherit valueStructureInheritConfig, HAPDomainValueStructure valueStructureDomain) {
		String inheritMode = valueStructureInheritConfig.getMode();
		if(!inheritMode.equals(HAPConstantShared.INHERITMODE_NONE)) {
			List<HAPPartComplexValueStructure> newParts = new ArrayList<HAPPartComplexValueStructure>();
			for(HAPPartComplexValueStructure part : parentValueStructureComplex.getParts()) {
				HAPPartComplexValueStructure newPart = part.cloneComplexValueStructurePart(valueStructureDomain, inheritMode, valueStructureInheritConfig.getGroupTypes());
				if(!newPart.isEmpty()) newParts.add(newPart);
			}
			valueStructureComplex.addPartGroup(newParts, HAPUtilityComplexValueStructure.createPartInfoFromParent());
		}
	}
	
	public static String[] getAllCategaries(){
		String[] contextTypes = {
			HAPConstantShared.UIRESOURCE_CONTEXTTYPE_PUBLIC,
			HAPConstantShared.UIRESOURCE_CONTEXTTYPE_PROTECTED,
			HAPConstantShared.UIRESOURCE_CONTEXTTYPE_INTERNAL,
			HAPConstantShared.UIRESOURCE_CONTEXTTYPE_PRIVATE,
		};
		return contextTypes;
	}

	public static String[] getAllCategariesWithPriority(){
		String[] contextTypes = {
			HAPConstantShared.UIRESOURCE_CONTEXTTYPE_PRIVATE,
			HAPConstantShared.UIRESOURCE_CONTEXTTYPE_INTERNAL,
			HAPConstantShared.UIRESOURCE_CONTEXTTYPE_PROTECTED,
			HAPConstantShared.UIRESOURCE_CONTEXTTYPE_PUBLIC,
		};
		return contextTypes;
	}

	//context type that can be inherited by child
	public static String[] getInheritableCategaries(){
		String[] contextTypes = {
			HAPConstantShared.UIRESOURCE_CONTEXTTYPE_PUBLIC,
			HAPConstantShared.UIRESOURCE_CONTEXTTYPE_PROTECTED,
		};
		return contextTypes;
	}

	//visible to child
	public static String[] getVisibleCategaries(){
		String[] contextTypes = {
			HAPConstantShared.UIRESOURCE_CONTEXTTYPE_INTERNAL,
			HAPConstantShared.UIRESOURCE_CONTEXTTYPE_PROTECTED,
			HAPConstantShared.UIRESOURCE_CONTEXTTYPE_PUBLIC,
		};
		return contextTypes;
	}

}
