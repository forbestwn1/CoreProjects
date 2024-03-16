package com.nosliw.core.application.division.manual;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.common.interfac.HAPTreeNode;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.HAPBrick;
import com.nosliw.core.application.HAPBrickComplex;
import com.nosliw.core.application.HAPBundleComplex;
import com.nosliw.core.application.HAPHandlerDownwardImpTreeNode;
import com.nosliw.core.application.HAPUtilityEntityExecutableTraverse;
import com.nosliw.core.application.HAPWrapperBrick;
import com.nosliw.core.application.division.manual.brick.valuestructure.HAPDefinitionEntityValueContext;
import com.nosliw.core.application.division.manual.brick.valuestructure.HAPDefinitionEntityValueStructure;
import com.nosliw.core.application.division.manual.brick.valuestructure.HAPDefinitionEntityWrapperValueStructure;
import com.nosliw.data.core.domain.HAPDomainEntityDefinitionGlobal;
import com.nosliw.data.core.domain.HAPDomainEntityExecutableResourceComplex;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.HAPInfoParentComplex;
import com.nosliw.data.core.domain.entity.HAPExecutableEntity;
import com.nosliw.data.core.domain.entity.HAPExecutableEntityComplex;
import com.nosliw.data.core.domain.valuecontext.HAPConfigureProcessorInherit;
import com.nosliw.data.core.domain.valuecontext.HAPConfigureProcessorValueStructure;
import com.nosliw.data.core.domain.valuecontext.HAPUtilityProcessRelativeElement;
import com.nosliw.data.core.domain.valueport.HAPIdValuePort;
import com.nosliw.data.core.domain.valueport.HAPRefValuePort;
import com.nosliw.data.core.domain.valueport.HAPReferenceRootElement;
import com.nosliw.data.core.domain.valueport.HAPUtilityValuePort;
import com.nosliw.data.core.entity.valuestructure.HAPDomainValueStructure;
import com.nosliw.data.core.entity.valuestructure.HAPExecutableEntityValueContext;
import com.nosliw.data.core.entity.valuestructure.HAPUtilityValueContext;
import com.nosliw.data.core.entity.valuestructure.HAPWrapperExecutableValueStructure;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.structure.HAPElementStructure;
import com.nosliw.data.core.structure.HAPElementStructureLeafRelative;
import com.nosliw.data.core.structure.HAPElementStructureLeafRelativeForDefinition;
import com.nosliw.data.core.structure.HAPElementStructureLeafRelativeForValue;
import com.nosliw.data.core.structure.HAPProcessorStructureElement;

public class HAPUtilityValueStructureDomain {

	public static void buildValueStructureDomain(HAPWrapperBrick rootEntityInfo, HAPContextProcess processContext, HAPRuntimeEnvironment runtimeEnv) {
		
		buildValueStructureComplexTree(rootEntityInfo, processContext, runtimeEnv);
		
//		buildExtensionValueStructure(complexEntity, processContext);
		
//		normalizeValuePort(complexEntity, processContext);
		
//		mergeValueStructure(complexEntity, processContext);
	}

	//build value structure in complex tree and add to value structure domain
	private static void buildValueStructureComplexTree(HAPWrapperBrick rootEntityInfo, HAPContextProcess processContext, HAPRuntimeEnvironment runtimeEnv) {
		HAPUtilityEntityExecutableTraverse.traverseExecutableTreeLocalComplexEntity(rootEntityInfo, new HAPHandlerDownwardImpTreeNode() {

			@Override
			protected boolean processTreeNode(HAPTreeNode treeNode, Object data) {
				HAPContextProcess processContext = (HAPContextProcess)data;
				HAPBundleComplex bundle = (HAPBundleComplex)processContext.getCurrentBundle();
				HAPDomainValueStructure valueStructureDomain = bundle.getValueStructureDomain();

				HAPBrickComplex complexEntityExe = (HAPBrickComplex)this.getBrickFromNode(treeNode);
				
				HAPManualInfoEntity rootEntityDefInfo = (HAPManualInfoEntity)bundle.getExtraData(); 
				
				Pair<HAPManualEntity, HAPBrick> entityPair = HAPUtilityDefinitionEntity.getEntityPair(treeNode.getPathFromRoot(), bundle);
				
				HAPManualEntityComplex rootEntityDef = (HAPManualEntityComplex)rootEntityDefInfo.getEntity();
				HAPManualEntityComplex complexEntityDef = (HAPManualEntityComplex)entityPair.getLeft();
				HAPDefinitionEntityValueContext valueContextEntityDef = complexEntityDef.getValueContextEntity();
				
				//value context
				HAPExecutableEntityValueContext valueContextExe = new HAPExecutableEntityValueContext();
				if(valueContextEntityDef!=null) {
					{
						List<HAPWrapperExecutableValueStructure> wrappers = new ArrayList<HAPWrapperExecutableValueStructure>();
						for(HAPDefinitionEntityWrapperValueStructure part : valueContextEntityDef.getValueStructures()) {
							
							String valueStructureExeId = valueStructureDomain.newValueStructure(part.getValueStructure().getAllRoots(), part.getInfo(), part.getName());
							HAPWrapperExecutableValueStructure valueStructureWrapperExe = new HAPWrapperExecutableValueStructure(valueStructureExeId);
							valueStructureWrapperExe.setGroupType(part.getGroupType());
							wrappers.add(valueStructureWrapperExe);

							//solidate plain script expression
//							valueStructureDomain.getValueStructureDefInfoByRuntimeId(valueStructureExeId).getValueStructure().solidateConstantScript(complexEntityExe.getPlainScriptExpressionValues());
						}
						valueContextExe.addPartSimple(wrappers, HAPUtilityValueContext.createPartInfoDefault(), valueStructureDomain);
					}
				}
				complexEntityExe.setValueContext(valueContextExe);
				return true;
			}
		}, runtimeEnv.getEntityManager(), processContext);
	}

	private static void createExtensionPart(HAPExecutableEntityValueContext valueContextExe, HAPDomainValueStructure valueStructureDomain) {
		List<HAPWrapperExecutableValueStructure> wrappers = new ArrayList<HAPWrapperExecutableValueStructure>();
		for(String groupType : HAPUtilityValueStructure.getAllCategaries()) {
			String valueStructureExeId = valueStructureDomain.newValueStructure();
			HAPWrapperExecutableValueStructure valueStructureWrapperExe = new HAPWrapperExecutableValueStructure(valueStructureExeId);
			valueStructureWrapperExe.setGroupType(groupType);
			wrappers.add(valueStructureWrapperExe);
		}
		valueContextExe.addPartSimple(wrappers, HAPUtilityValueContext.createPartInfoExtension(), valueStructureDomain);
	}

	//create extension part
	private static void buildExtensionValueStructure(HAPExecutableEntityComplex complexEntity, HAPContextProcessor processContext) {
		HAPUtilityEntityExecutableTraverse.traverseExecutableLocalComplexEntityTree(complexEntity, new HAPHandlerDownwardImpAttribute() {
			@Override
			public void processRootEntity(HAPExecutableEntity complexEntity, HAPContextProcessor processContext) {
				createExtensionPart(((HAPExecutableEntityComplex)complexEntity).getValueContext(), processContext.getCurrentValueStructureDomain());
			}

			@Override
			public boolean processAttribute(HAPExecutableEntity parentEntity, String attribute, HAPContextProcessor processContext) {
				HAPExecutableEntityComplex parentComplexEntity = (HAPExecutableEntityComplex)parentEntity;

				HAPDomainEntityDefinitionGlobal definitionGlobalDomain = processContext.getCurrentDefinitionDomain();
				HAPDomainEntityExecutableResourceComplex exeDomain = processContext.getCurrentExecutableDomain();
				HAPDomainValueStructure valueStructureDomain = exeDomain.getValueStructureDomain();

				HAPExecutableEntityComplex entityExe = parentComplexEntity.getComplexEntityAttributeValue(attribute);
				HAPIdEntityInDomain entityIdDef = entityExe.getDefinitionEntityId();

				HAPExecutableEntityValueContext valueContext = entityExe.getValueContext();

				HAPInfoParentComplex parentInfo = definitionGlobalDomain.getComplexEntityParentInfo(entityIdDef);
				HAPConfigureProcessorValueStructure valueStructureConfig = parentInfo==null?null:parentInfo.getParentRelationConfigure().getValueStructureRelationMode();

				//extension value structure
				if(valueStructureConfig==null||!HAPConstantShared.INHERITMODE_RUNTIME.equals(valueStructureConfig.getInheritProcessorConfigure().getMode())) {
					createExtensionPart(valueContext, valueStructureDomain);
				}
				return true;
			}}, processContext);
	}

	
	//merge value structure between paren and child
	private static void mergeValueStructure(HAPExecutableEntityComplex complexEntity, HAPContextProcessor processContext) {
		HAPUtilityEntityExecutableTraverse.traverseExecutableLocalComplexEntityTree(complexEntity, new HAPHandlerDownwardImpAttribute() {
			@Override
			public void processRootEntity(HAPExecutableEntity complexEntity, HAPContextProcessor processContext) {	}

			@Override
			public boolean processAttribute(HAPExecutableEntity parentEntity, String attribute, HAPContextProcessor processContext) {
				HAPExecutableEntityComplex parentComplexEntity = (HAPExecutableEntityComplex)parentEntity;

				HAPDomainEntityDefinitionGlobal definitionGlobalDomain = processContext.getCurrentDefinitionDomain();
				HAPDomainEntityExecutableResourceComplex exeDomain = processContext.getCurrentExecutableDomain();
				HAPDomainValueStructure valueStructureDomain = exeDomain.getValueStructureDomain();

				HAPExecutableEntityComplex entityExe = parentComplexEntity.getComplexEntityAttributeValue(attribute);
				HAPIdEntityInDomain entityIdDef = entityExe.getDefinitionEntityId();

				HAPExecutableEntityValueContext valueContext = entityExe.getValueContext();

				HAPInfoParentComplex parentInfo = definitionGlobalDomain.getComplexEntityParentInfo(entityIdDef);
				HAPConfigureProcessorValueStructure valueStructureConfig = parentInfo==null?null:parentInfo.getParentRelationConfigure().getValueStructureRelationMode();

				HAPExecutableEntityValueContext parentValueContext = parentComplexEntity.getValueContext();
				
				//process static
				
				//process relative
				List<HAPInfoValueStructureSorting> valueStructureInfos = HAPUtilityValueContext.getAllValueStructures(valueContext);
				for(HAPInfoValueStructureSorting valueStructureInfo : valueStructureInfos) {
					HAPWrapperExecutableValueStructure valueStructureWrapper = valueStructureInfo.getValueStructure();
					HAPDefinitionEntityValueStructure valueStructure = valueStructureDomain.getValueStructureDefinitionByRuntimeId(valueStructureWrapper.getValueStructureRuntimeId());
					List<HAPServiceData> errors = new ArrayList<HAPServiceData>();
					Set<HAPRefValuePort> dependency = new HashSet<HAPRefValuePort>();
					HAPUtilityProcessRelativeElement.processRelativeInStructure(valueStructure, valueStructureConfig==null?null:valueStructureConfig.getRelativeProcessorConfigure(), dependency, errors, processContext);
				}

				//inheritance
				processInteritance(valueContext, parentValueContext, valueStructureConfig.getInheritProcessorConfigure(), valueStructureDomain);
				return true;
			}}, processContext);
	}

	private static void processInteritance(HAPExecutableEntityValueContext valueContext, HAPExecutableEntityValueContext parentValueContext, HAPConfigureProcessorInherit valueStructureInheritConfig, HAPDomainValueStructure valueStructureDomain) {
		String inheritMode = valueStructureInheritConfig.getMode();
		if(!inheritMode.equals(HAPConstantShared.INHERITMODE_NONE)) {
			List<HAPExecutablePartValueContext> newParts = new ArrayList<HAPExecutablePartValueContext>();
			for(HAPExecutablePartValueContext part : parentValueContext.getParts()) {
				HAPExecutablePartValueContext newPart = part.inheritValueContextPart(valueStructureDomain, inheritMode, valueStructureInheritConfig.getGroupTypes());
				if(!newPart.isEmpty()) {
					newParts.add(newPart);
				}
			}
			valueContext.addPartGroup(newParts, HAPUtilityValueContext.createPartInfoFromParent());
		}
	}
	
	private static void normalizeValuePort(HAPExecutableEntityComplex complexEntity, HAPContextProcessor processContext) {
		HAPUtilityEntityExecutableTraverse.traverseExecutableLocalComplexEntityTree(complexEntity, new HAPHandlerDownwardImpAttribute() {
			@Override
			public void processRootEntity(HAPExecutableEntity complexEntity, HAPContextProcessor processContext) {	}

			@Override
			public boolean processAttribute(HAPExecutableEntity parentEntity, String attribute, HAPContextProcessor processContext) {
				HAPExecutableEntityComplex parentComplexEntity = (HAPExecutableEntityComplex)parentEntity;

				HAPDomainEntityDefinitionGlobal definitionGlobalDomain = processContext.getCurrentDefinitionDomain();
				HAPDomainEntityExecutableResourceComplex exeDomain = processContext.getCurrentExecutableDomain();
				HAPDomainValueStructure valueStructureDomain = exeDomain.getValueStructureDomain();

				HAPExecutableEntityComplex entityExe = parentComplexEntity.getComplexEntityAttributeValue(attribute);
				HAPIdEntityInDomain entityIdDef = entityExe.getDefinitionEntityId();

				HAPExecutableEntityValueContext valueContext = entityExe.getValueContext();

				HAPInfoParentComplex parentInfo = definitionGlobalDomain.getComplexEntityParentInfo(entityIdDef);
				HAPConfigureProcessorValueStructure valueStructureConfig = parentInfo==null?null:parentInfo.getParentRelationConfigure().getValueStructureRelationMode();

				HAPExecutableEntityValueContext parentValueContext = parentComplexEntity.getValueContext();
				
				//process static
				
				//process relative
				List<HAPInfoValueStructureSorting> valueStructureInfos = HAPUtilityValueContext.getAllValueStructures(valueContext);
				for(HAPInfoValueStructureSorting valueStructureInfo : valueStructureInfos) {
					HAPWrapperExecutableValueStructure valueStructureWrapper = valueStructureInfo.getValueStructure();
					HAPDefinitionEntityValueStructure valueStructure = valueStructureDomain.getValueStructureDefinitionByRuntimeId(valueStructureWrapper.getValueStructureRuntimeId());
					HAPUtilityValueStructure.traverseElement(valueStructure, new HAPProcessorStructureElement() {

						private void process(HAPElementStructureLeafRelative relativeEle) {
							HAPReferenceRootElement rootRef = relativeEle.getReference(); 
							HAPRefValuePort valuePortRef = rootRef.getValuePortRef();
							if(valuePortRef==null) {
								String valuePortName = rootRef.getValuePortName();
								HAPIdValuePort valuePortId = null;
								if(valuePortName==null) {
									valuePortId = HAPUtilityValuePort.getDefaultValuePortIdInEntity(parentComplexEntity);
								}
								else if(valuePortName.equals(HAPConstantShared.VALUEPORT_NAME_SELF)) {
									valuePortId = HAPUtilityValueContext.createValuePortIdValueContext(entityExe);
								}
								valuePortRef = new HAPRefValuePort(valuePortId);
								rootRef.setValuePortRef(valuePortRef);
							}
						}
						
						@Override
						public Pair<Boolean, HAPElementStructure> process(HAPInfoElement eleInfo, Object value) {
							HAPElementStructure defStructureElement = eleInfo.getElement();
							HAPElementStructure out = defStructureElement;
							switch(defStructureElement.getType()) {
							case HAPConstantShared.CONTEXT_ELEMENTTYPE_RELATIVE_FOR_DEFINITION:
							{
								process((HAPElementStructureLeafRelativeForDefinition)defStructureElement);
								break;
							}
							case HAPConstantShared.CONTEXT_ELEMENTTYPE_RELATIVE_FOR_VALUE:
							{
								process((HAPElementStructureLeafRelativeForValue)defStructureElement);
								break;
							}
							}
							
							return null;
						}

						@Override
						public void postProcess(HAPInfoElement eleInfo, Object value) {}
					}, null);
					
					List<HAPServiceData> errors = new ArrayList<HAPServiceData>();
					Set<HAPRefValuePort> dependency = new HashSet<HAPRefValuePort>();
					HAPUtilityProcessRelativeElement.processRelativeInStructure(valueStructure, valueStructureConfig==null?null:valueStructureConfig.getRelativeProcessorConfigure(), dependency, errors, processContext);
				}

				//inheritance
				processInteritance(valueContext, parentValueContext, valueStructureConfig.getInheritProcessorConfigure(), valueStructureDomain);
				return true;
			}}, processContext);
	}
}
