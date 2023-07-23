package com.nosliw.data.core.domain.valuecontext;

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
import com.nosliw.data.core.domain.HAPExecutableBundle;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.HAPInfoEntityInDomainDefinition;
import com.nosliw.data.core.domain.HAPInfoEntityInDomainExecutable;
import com.nosliw.data.core.domain.HAPInfoParentComplex;
import com.nosliw.data.core.domain.HAPUtilityDomain;
import com.nosliw.data.core.domain.HAPUtilityEntityExecutable;
import com.nosliw.data.core.domain.attachment.HAPUtilityAttachment;
import com.nosliw.data.core.domain.entity.HAPDefinitionEntityInDomainComplex;
import com.nosliw.data.core.domain.entity.HAPExecutableEntity;
import com.nosliw.data.core.domain.entity.HAPExecutableEntityComplex;
import com.nosliw.data.core.domain.entity.HAPInfoAdapter;
import com.nosliw.data.core.domain.entity.HAPProcessorEntityExecutable;
import com.nosliw.data.core.domain.entity.HAPProcessorEntityExecutable1;
import com.nosliw.data.core.domain.entity.attachment.HAPDefinitionEntityContainerAttachment;
import com.nosliw.data.core.domain.entity.valuestructure.HAPDefinitionEntityValueContext;
import com.nosliw.data.core.domain.entity.valuestructure.HAPDefinitionEntityValueStructure;
import com.nosliw.data.core.domain.entity.valuestructure.HAPDefinitionWrapperValueStructure;
import com.nosliw.data.core.structure.reference.HAPCandidatesValueContext;

public class HAPUtilityValueStructureDomain {

	public static void buildValueStructureDomain(HAPIdEntityInDomain rootComplexEntityExecutableId, HAPContextProcessor processContext) {
		
		buildValueStructureComplexTree(rootComplexEntityExecutableId, processContext);
		
		mergeValueStructure(rootComplexEntityExecutableId, processContext);
	}

	//build value structure in complex tree and add to value structure domain
	private static void buildValueStructureComplexTree(HAPIdEntityInDomain rootComplexEntityExecutableId, HAPContextProcessor processContext) {
		HAPUtilityEntityExecutable.traverseExecutableLocalComplexEntityTree(rootComplexEntityExecutableId, new HAPProcessorEntityExecutable() {
			
			private void process(HAPIdEntityInDomain entityIdExe) {
				HAPExecutableBundle bundleExe = processContext.getCurrentBundle();
				HAPDomainEntityDefinitionGlobal definitionGlobalDomain = processContext.getCurrentDefinitionDomain();
				HAPDomainEntityExecutableResourceComplex exeDomain = processContext.getCurrentExecutableDomain();
				HAPDomainValueStructure valueStructureDomain = exeDomain.getValueStructureDomain();

				HAPExecutableEntityComplex complexEntityExe = exeDomain.getEntityInfoExecutable(entityIdExe).getEntity();
				HAPIdEntityInDomain entityIdDef = bundleExe.getDefinitionEntityIdByExecutableEntityId(entityIdExe);
				HAPDefinitionEntityContainerAttachment attachmentContainer = HAPUtilityAttachment.getAttachmentContainerByComplexExeId(entityIdExe, processContext);

				HAPInfoEntityInDomainDefinition complexEntityInfoDef = definitionGlobalDomain.getEntityInfoDefinition(entityIdDef);
				HAPDefinitionEntityInDomainComplex complexEntityDef = (HAPDefinitionEntityInDomainComplex)complexEntityInfoDef.getEntity();
				
				HAPDefinitionEntityValueContext valueContextEntityDef = null;
				HAPIdEntityInDomain valueContextEntityId = complexEntityDef.getValueContextEntity();
				if(valueContextEntityId!=null) {
					valueContextEntityDef = (HAPDefinitionEntityValueContext)definitionGlobalDomain.getEntityInfoDefinition(valueContextEntityId).getEntity();
				}

				//value context
				HAPExecutableEntityValueContext valueContextExe = new HAPExecutableEntityValueContext();
				if(valueContextEntityDef!=null) {
					{
						List<HAPWrapperExecutableValueStructure> wrappers = new ArrayList<HAPWrapperExecutableValueStructure>();
						for(HAPDefinitionWrapperValueStructure part : valueContextEntityDef.getValueStructures()) {
							HAPInfoEntityInDomainDefinition valueStructureDefInfo = definitionGlobalDomain.getEntityInfoDefinition(part.getValueStructureId());
//							String valueStructureExeId = valueStructureDomain.newValueStructure(valueStructureDefInfo, part.getValueStructureId().getEntityId());
							String valueStructureExeId = valueStructureDomain.newValueStructure(valueStructureDefInfo, null);
							HAPWrapperExecutableValueStructure valueStructureWrapperExe = new HAPWrapperExecutableValueStructure(valueStructureExeId);
							valueStructureWrapperExe.cloneFromDefinition(part);
							wrappers.add(valueStructureWrapperExe);
						}
						valueContextExe.addPartSimple(wrappers, HAPUtilityValueContext.createPartInfoDefault());
					}
				}
				complexEntityExe.setValueContext(valueContextExe);
			}
			
			@Override
			public void processComplexRoot(HAPIdEntityInDomain entityId, HAPContextProcessor processContext) {
				process(entityId);
			}

			@Override
			public boolean process(HAPExecutableEntity parentEntity, String attribute, HAPContextProcessor processContext) {
				HAPExecutableEntityComplex parentComplexEntity = (HAPExecutableEntityComplex)parentEntity;
				process(parentComplexEntity.getComplexEntityAttributeValue(attribute));
				return true;
			}
		}, processContext);
	}


	
	//build value structure in complex tree and add to value structure domain
	private static void buildValueStructureComplexTree1(HAPIdEntityInDomain rootComplexEntityExecutableId, HAPContextProcessor processContext) {
		HAPUtilityDomain.traverseExecutableComplexEntityTreeSolidOnly(rootComplexEntityExecutableId, new HAPProcessorEntityExecutable1() {
			@Override
			public void process(HAPInfoEntityInDomainExecutable entityInfo, Set<HAPInfoAdapter> adapters, HAPInfoEntityInDomainExecutable parentEntityInfo, HAPContextProcessor processContext) {
				
				HAPExecutableBundle bundleExe = processContext.getCurrentBundle();
				HAPDomainEntityDefinitionGlobal definitionGlobalDomain = processContext.getCurrentDefinitionDomain();
				HAPDomainEntityExecutableResourceComplex exeDomain = processContext.getCurrentExecutableDomain();
				HAPDomainValueStructure valueStructureDomain = exeDomain.getValueStructureDomain();

				HAPIdEntityInDomain entityIdExe = entityInfo.getEntityId();
				HAPExecutableEntityComplex complexEntityExe = exeDomain.getEntityInfoExecutable(entityIdExe).getEntity();
				HAPIdEntityInDomain entityIdDef = bundleExe.getDefinitionEntityIdByExecutableEntityId(entityIdExe);
				HAPDefinitionEntityContainerAttachment attachmentContainer = HAPUtilityAttachment.getAttachmentContainerByComplexExeId(entityIdExe, processContext);

				HAPInfoEntityInDomainDefinition complexEntityInfoDef = definitionGlobalDomain.getSolidEntityInfoDefinition(entityIdDef, attachmentContainer);
				HAPDefinitionEntityInDomainComplex complexEntityDef = (HAPDefinitionEntityInDomainComplex)complexEntityInfoDef.getEntity();
				
				HAPDefinitionEntityValueContext valueContextEntityDef = null;
				HAPIdEntityInDomain valueContextEntityId = complexEntityDef.getValueContextEntity();
				if(valueContextEntityId!=null) {
					valueContextEntityDef = (HAPDefinitionEntityValueContext)definitionGlobalDomain.getSolidEntityInfoDefinition(valueContextEntityId, attachmentContainer).getEntity();
				}

				//value context
				HAPExecutableEntityValueContext valueContextExe = new HAPExecutableEntityValueContext();
				if(valueContextEntityDef!=null) {
					{
						List<HAPWrapperExecutableValueStructure> wrappers = new ArrayList<HAPWrapperExecutableValueStructure>();
						for(HAPDefinitionWrapperValueStructure part : valueContextEntityDef.getValueStructures()) {
							HAPInfoEntityInDomainDefinition valueStructureDefInfo = definitionGlobalDomain.getSolidEntityInfoDefinition(part.getValueStructureId(), attachmentContainer);
//							String valueStructureExeId = valueStructureDomain.newValueStructure(valueStructureDefInfo, part.getValueStructureId().getEntityId());
							String valueStructureExeId = valueStructureDomain.newValueStructure(valueStructureDefInfo, null);
							HAPWrapperExecutableValueStructure valueStructureWrapperExe = new HAPWrapperExecutableValueStructure(valueStructureExeId);
							valueStructureWrapperExe.cloneFromDefinition(part);
							wrappers.add(valueStructureWrapperExe);
						}
						valueContextExe.addPartSimple(wrappers, HAPUtilityValueContext.createPartInfoDefault());
					}
				}
				complexEntityExe.setValueContext(valueContextExe);
				
			}}, processContext);
	}

	private static void createExtensionPart(HAPExecutableEntityValueContext valueContextExe, HAPDomainValueStructure valueStructureDomain) {
		List<HAPWrapperExecutableValueStructure> wrappers = new ArrayList<HAPWrapperExecutableValueStructure>();
		for(String groupType : HAPUtilityValueStructure.getAllCategaries()) {
			String valueStructureExeId = valueStructureDomain.newValueStructure();
			HAPWrapperExecutableValueStructure valueStructureWrapperExe = new HAPWrapperExecutableValueStructure(valueStructureExeId);
			valueStructureWrapperExe.setGroupType(groupType);
			valueStructureWrapperExe.setGroupName(groupType);
			wrappers.add(valueStructureWrapperExe);
		}
		valueContextExe.addPartSimple(wrappers, HAPUtilityValueContext.createPartInfoExtension());
	}
	
	//merge value structure between paren and child
	private static void mergeValueStructure(HAPIdEntityInDomain rootComplexEntityExecutableId, HAPContextProcessor processContext) {
		HAPUtilityEntityExecutable.traverseExecutableLocalComplexEntityTree(rootComplexEntityExecutableId, new HAPProcessorEntityExecutable() {
			@Override
			public void processComplexRoot(HAPIdEntityInDomain entityId, HAPContextProcessor processContext) {	}

			@Override
			public boolean process(HAPExecutableEntity parentEntity, String attribute, HAPContextProcessor processContext) {
				HAPExecutableEntityComplex parentComplexEntity = (HAPExecutableEntityComplex)parentEntity;

				HAPExecutableBundle bundleExe = processContext.getCurrentBundle();
				HAPDomainEntityDefinitionGlobal definitionGlobalDomain = processContext.getCurrentDefinitionDomain();
				HAPDomainEntityExecutableResourceComplex exeDomain = processContext.getCurrentExecutableDomain();
				HAPDomainValueStructure valueStructureDomain = exeDomain.getValueStructureDomain();

				HAPIdEntityInDomain entityIdExe = parentComplexEntity.getComplexEntityAttributeValue(attribute);
				HAPIdEntityInDomain entityIdDef = bundleExe.getDefinitionEntityIdByExecutableEntityId(entityIdExe);
				HAPDefinitionEntityContainerAttachment attachmentContainer = HAPUtilityAttachment.getAttachmentContainerByComplexExeId(entityIdExe, processContext);

				HAPExecutableEntityComplex entityExe = exeDomain.getEntityInfoExecutable(entityIdExe).getEntity();
				String attachmentContainerId = entityExe.getAttachmentContainerId();
				HAPExecutableEntityValueContext valueContext = entityExe.getValueContext();

				HAPInfoParentComplex parentInfo = definitionGlobalDomain.getComplexEntityParentInfo(entityIdDef);
				HAPConfigureProcessorValueStructure valueStructureConfig = parentInfo==null?null:parentInfo.getParentRelationConfigure().getValueStructureRelationMode();

				//extension value structure
				if(!HAPConstantShared.INHERITMODE_RUNTIME.equals(valueStructureConfig.getInheritProcessorConfigure().getMode())) {
					createExtensionPart(valueContext, valueStructureDomain);
				}
				
				HAPExecutableEntityValueContext parentValueContext = parentComplexEntity.getValueContext();
				
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

				//inheritance
				processInteritance(valueContext, parentValueContext, valueStructureConfig.getInheritProcessorConfigure(), valueStructureDomain);
				return true;
			}}, processContext);
	}

	//merge value structure between paren and child
	private static void mergeValueStructure1(HAPIdEntityInDomain rootComplexEntityExecutableId, HAPContextProcessor processContext) {
		HAPUtilityDomain.traverseExecutableComplexEntityTreeSolidOnly(rootComplexEntityExecutableId, new HAPProcessorEntityExecutable1() {
			@Override
			public void process(HAPInfoEntityInDomainExecutable entityExeInfo, Set<HAPInfoAdapter> adapters, HAPInfoEntityInDomainExecutable parentEntityExeInfo,	HAPContextProcessor processContext) {

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

				HAPInfoParentComplex parentInfo = definitionGlobalDomain.getComplexEntityParentInfo(entityIdDef);
				HAPConfigureProcessorValueStructure valueStructureConfig = parentInfo==null?null:parentInfo.getParentRelationConfigure().getValueStructureRelationMode();

				//extension value structure
				if(parentEntityExeInfo==null || !HAPConstantShared.INHERITMODE_RUNTIME.equals(valueStructureConfig.getInheritProcessorConfigure().getMode())) {
					createExtensionPart(valueContext, valueStructureDomain);
				}
				
				if(parentEntityExeInfo!=null) {
					HAPExecutableEntityComplex parentEntityExe = exeDomain.getEntityInfoExecutable(parentEntityExeInfo.getEntityId()).getEntity();
					HAPExecutableEntityValueContext parentValueContext = parentEntityExe.getValueContext();
					
					
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

					//inheritance
					processInteritance(valueContext, parentValueContext, valueStructureConfig.getInheritProcessorConfigure(), valueStructureDomain);
				}
				
			}}, processContext);
	}

	private static void processInteritance(HAPExecutableEntityValueContext valueContext, HAPExecutableEntityValueContext parentValueContext, HAPConfigureProcessorInherit valueStructureInheritConfig, HAPDomainValueStructure valueStructureDomain) {
		String inheritMode = valueStructureInheritConfig.getMode();
		if(!inheritMode.equals(HAPConstantShared.INHERITMODE_NONE)) {
			List<HAPExecutablePartValueContext> newParts = new ArrayList<HAPExecutablePartValueContext>();
			for(HAPExecutablePartValueContext part : parentValueContext.getParts()) {
				HAPExecutablePartValueContext newPart = part.inheritValueContextPart(valueStructureDomain, inheritMode, valueStructureInheritConfig.getGroupTypes());
				if(!newPart.isEmpty()) newParts.add(newPart);
			}
			valueContext.addPartGroup(newParts, HAPUtilityValueContext.createPartInfoFromParent());
		}
	}

}
