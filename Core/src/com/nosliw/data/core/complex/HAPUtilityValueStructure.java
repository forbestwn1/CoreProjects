package com.nosliw.data.core.complex;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.component.HAPContextProcessor;
import com.nosliw.data.core.domain.HAPDomainEntityDefinitionGlobal;
import com.nosliw.data.core.domain.HAPDomainEntityExecutableResourceComplex;
import com.nosliw.data.core.domain.HAPDomainValueStructure;
import com.nosliw.data.core.domain.HAPExecutableBundle;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.HAPInfoEntityInDomainDefinition;
import com.nosliw.data.core.domain.HAPInfoEntityInDomainExecutable;
import com.nosliw.data.core.domain.HAPUtilityDomain;
import com.nosliw.data.core.domain.entity.HAPDefinitionEntityInDomainComplex;
import com.nosliw.data.core.domain.entity.HAPEmbededDefinitionWithId;
import com.nosliw.data.core.domain.entity.HAPExecutableEntityComplex;
import com.nosliw.data.core.domain.entity.attachment.HAPDefinitionEntityContainerAttachment;
import com.nosliw.data.core.domain.entity.valuestructure.HAPConfigureProcessorInherit;
import com.nosliw.data.core.domain.entity.valuestructure.HAPConfigureProcessorValueStructure;
import com.nosliw.data.core.domain.entity.valuestructure.HAPDefinitionEntityValueContext;
import com.nosliw.data.core.domain.entity.valuestructure.HAPDefinitionEntityValueStructure;
import com.nosliw.data.core.domain.entity.valuestructure.HAPDefinitionWrapperValueStructure;
import com.nosliw.data.core.domain.entity.valuestructure.HAPExecutableEntityValueContext;
import com.nosliw.data.core.domain.entity.valuestructure.HAPExecutablePartValueContext;
import com.nosliw.data.core.domain.entity.valuestructure.HAPInfoValueStructureSorting;
import com.nosliw.data.core.domain.entity.valuestructure.HAPUtilityProcessRelativeElement;
import com.nosliw.data.core.domain.entity.valuestructure.HAPUtilityValueContext;
import com.nosliw.data.core.domain.entity.valuestructure.HAPWrapperExecutableValueStructure;
import com.nosliw.data.core.structure.reference.HAPCandidatesValueContext;

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
				
				HAPExecutableBundle bundleExe = processContext.getCurrentBundle();
				HAPDomainEntityDefinitionGlobal definitionGlobalDomain = processContext.getCurrentDefinitionDomain();
				HAPDomainEntityExecutableResourceComplex exeDomain = processContext.getCurrentExecutableDomain();
				HAPDomainValueStructure valueStructureDomain = exeDomain.getValueStructureDomain();

				HAPIdEntityInDomain entityIdExe = entityInfo.getEntityId();
				HAPExecutableEntityComplex complexEntityExe = exeDomain.getEntityInfoExecutable(entityIdExe).getEntity();
				HAPIdEntityInDomain entityIdDef = bundleExe.getDefinitionEntityIdByExecutableEntityId(entityIdExe);
				HAPDefinitionEntityContainerAttachment attachmentContainer = HAPUtilityAttachment.getAttachmentContainerByComplexExeId(entityIdExe, processContext);

				HAPInfoEntityInDomainDefinition complexEntityInfoDef = definitionGlobalDomain.getSolidEntityInfoDefinition(entityIdDef, attachmentContainer);
				
				HAPDefinitionEntityValueContext valueContextEntityDef = null;
				HAPEmbededDefinitionWithId valueStructureComplexAttribute = ((HAPDefinitionEntityInDomainComplex)complexEntityInfoDef.getEntity()).getValueContextEntity();
				if(valueStructureComplexAttribute!=null) {
					valueContextEntityDef = (HAPDefinitionEntityValueContext)definitionGlobalDomain.getSolidEntityInfoDefinition(valueStructureComplexAttribute.getEntityId(), attachmentContainer).getEntity();
				}

				//value context
				HAPExecutableEntityValueContext valueContextExe = new HAPExecutableEntityValueContext();
				if(valueContextEntityDef!=null) {
					List<HAPWrapperExecutableValueStructure> wrappers = new ArrayList<HAPWrapperExecutableValueStructure>();
					for(HAPDefinitionWrapperValueStructure part : valueContextEntityDef.getValueStructures()) {
						HAPInfoEntityInDomainDefinition valueStructureDefInfo = definitionGlobalDomain.getSolidEntityInfoDefinition(part.getValueStructureId(), attachmentContainer);
						String valueStructureExeId = valueStructureDomain.newValueStructure(valueStructureDefInfo, part.getValueStructureId().getEntityId());
						HAPWrapperExecutableValueStructure valueStructureWrapperExe = new HAPWrapperExecutableValueStructure(valueStructureExeId);
						valueStructureWrapperExe.cloneFromDefinition(part);
						wrappers.add(valueStructureWrapperExe);
					}
					valueContextExe.addPartSimple(wrappers, HAPUtilityValueContext.createPartInfoDefault());
				}
				complexEntityExe.setValueContext(valueContextExe);
				
			}}, processContext);
	}

	//merge value structure between paren and child
	private static void mergeValueStructure(HAPIdEntityInDomain rootComplexEntityExecutableId, HAPContextProcessor processContext) {
		HAPUtilityDomain.traverseExecutableComplexEntityTreeSolidOnly(rootComplexEntityExecutableId, new HAPProcessorEntityExecutable() {
			@Override
			public void process(HAPInfoEntityInDomainExecutable entityExeInfo, Object adapter, HAPInfoEntityInDomainExecutable parentEntityExeInfo,	HAPContextProcessor processContext) {

				HAPExecutableBundle bundleExe = processContext.getCurrentBundle();
				HAPDomainEntityDefinitionGlobal definitionGlobalDomain = processContext.getCurrentDefinitionDomain();
				HAPDomainEntityExecutableResourceComplex exeDomain = processContext.getCurrentExecutableDomain();
				HAPDomainValueStructure valueStructureDomain = exeDomain.getValueStructureDomain();

				HAPIdEntityInDomain entityIdExe = entityExeInfo.getEntityId();
				HAPIdEntityInDomain entityIdDef = bundleExe.getDefinitionEntityIdByExecutableEntityId(entityIdExe);
				HAPDefinitionEntityContainerAttachment attachmentContainer = HAPUtilityAttachment.getAttachmentContainerByComplexExeId(entityIdExe, processContext);

				HAPExecutableEntityComplex entityExe = exeDomain.getEntityInfoExecutable(entityIdExe).getEntity();
				String attachmentContainerId = entityExe.getAttachmentContainerId();
				HAPExecutableEntityValueContext valueContext = entityExe.getValueContext();

				
				String parentAttachmentContainerId = null;
				HAPExecutableEntityValueContext parentValueContext = null;
				if(parentEntityExeInfo!=null) {
					HAPExecutableEntityComplex parentEntityExe = exeDomain.getEntityInfoExecutable(parentEntityExeInfo.getEntityId()).getEntity();
					parentAttachmentContainerId = parentEntityExe.getAttachmentContainerId();
					parentValueContext = parentEntityExe.getValueContext();
				}
				
				HAPConfigureProcessorValueStructure valueStructureConfig = null; 
//						definitionGlobalDomain.getComplexEntityParentInfo(entityIdDef).getParentRelationConfigure().getValueStructureRelationMode();
				
				//process static
				
				
				//process relative
				List<HAPInfoValueStructureSorting> valueStructureInfos = HAPUtilityValueContext.getAllValueStructures(valueContext);
				for(HAPInfoValueStructureSorting valueStructureInfo : valueStructureInfos) {
					HAPWrapperExecutableValueStructure valueStructureWrapper = valueStructureInfo.getValueStructure();
					HAPDefinitionEntityValueStructure valueStructure = valueStructureDomain.getValueStructureDefinitionByRuntimeId(valueStructureWrapper.getValueStructureRuntimeId());
					List<HAPServiceData> errors = new ArrayList<HAPServiceData>();
					Set<String> dependency = new HashSet<String>();
					HAPCandidatesValueContext valueContextGroup = new HAPCandidatesValueContext(valueContext, parentValueContext);
					HAPUtilityProcessRelativeElement.processRelativeInStructure(valueStructure, valueContextGroup, valueStructureDomain, valueStructureConfig==null?null:valueStructureConfig.getRelativeProcessorConfigure(), dependency, errors, processContext.getRuntimeEnvironment());
				}

				if(parentEntityExeInfo!=null) {
					//inheritance
					processInteritance(valueContext, parentValueContext, valueStructureConfig.getInheritProcessorConfigure(), valueStructureDomain);
					
				}

			}}, processContext);
	}

	private static void processInteritance(HAPExecutableEntityValueContext valueStructureComplex, HAPExecutableEntityValueContext parentValueStructureComplex, HAPConfigureProcessorInherit valueStructureInheritConfig, HAPDomainValueStructure valueStructureDomain) {
		String inheritMode = valueStructureInheritConfig.getMode();
		if(!inheritMode.equals(HAPConstantShared.INHERITMODE_NONE)) {
			List<HAPExecutablePartValueContext> newParts = new ArrayList<HAPExecutablePartValueContext>();
			for(HAPExecutablePartValueContext part : parentValueStructureComplex.getParts()) {
				HAPExecutablePartValueContext newPart = part.cloneComplexValueStructurePart(valueStructureDomain, inheritMode, valueStructureInheritConfig.getGroupTypes());
				if(!newPart.isEmpty()) newParts.add(newPart);
			}
			valueStructureComplex.addPartGroup(newParts, HAPUtilityValueContext.createPartInfoFromParent());
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

	public static List<String> getAllCategariesWithResolvePriority(){
		String[] contextTypes = {
			HAPConstantShared.UIRESOURCE_CONTEXTTYPE_PRIVATE,
			HAPConstantShared.UIRESOURCE_CONTEXTTYPE_INTERNAL,
			HAPConstantShared.UIRESOURCE_CONTEXTTYPE_PROTECTED,
			HAPConstantShared.UIRESOURCE_CONTEXTTYPE_PUBLIC,
		};
		return new ArrayList<>(Arrays.asList(contextTypes));
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
