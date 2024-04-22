package com.nosliw.core.application.division.manual;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.HAPBrick;
import com.nosliw.core.application.HAPBrickComplex;
import com.nosliw.core.application.HAPBundleComplex;
import com.nosliw.core.application.HAPHandlerDownwardImpAttribute;
import com.nosliw.core.application.HAPHandlerDownwardImpTreeNode;
import com.nosliw.core.application.HAPTreeNodeBrick;
import com.nosliw.core.application.HAPUtilityBrickTraverse;
import com.nosliw.core.application.HAPWrapperBrick;
import com.nosliw.core.application.common.structure.HAPElementStructure;
import com.nosliw.core.application.common.structure.HAPElementStructureLeafRelative;
import com.nosliw.core.application.common.structure.HAPElementStructureLeafRelativeForDefinition;
import com.nosliw.core.application.common.structure.HAPElementStructureLeafRelativeForValue;
import com.nosliw.core.application.common.structure.HAPProcessorStructureElement;
import com.nosliw.core.application.common.structure.reference.HAPUtilityProcessRelativeElement;
import com.nosliw.core.application.common.valueport.HAPIdValuePort;
import com.nosliw.core.application.common.valueport.HAPReferenceRootElement;
import com.nosliw.core.application.common.valueport.HAPReferenceValuePort;
import com.nosliw.core.application.common.valueport.HAPUtilityValuePort;
import com.nosliw.core.application.division.manual.brick.valuestructure.HAPManualBrickValueContext;
import com.nosliw.core.application.division.manual.brick.valuestructure.HAPManualBrickValueStructure;
import com.nosliw.core.application.division.manual.brick.valuestructure.HAPManualBrickWrapperValueStructure;
import com.nosliw.core.application.division.manual.brick.valuestructure.HAPManualRootInValueStructure;
import com.nosliw.core.application.valuecontext.HAPInfoValueStructure;
import com.nosliw.core.application.valuecontext.HAPPartInValueContext;
import com.nosliw.core.application.valuecontext.HAPUtilityValueContext;
import com.nosliw.core.application.valuecontext.HAPValueContext;
import com.nosliw.core.application.valuestructure.HAPDomainValueStructure;
import com.nosliw.core.application.valuestructure.HAPRootInValueStructure;
import com.nosliw.data.core.domain.HAPDomainEntityDefinitionGlobal;
import com.nosliw.data.core.domain.HAPDomainEntityExecutableResourceComplex;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.HAPInfoParentComplex;
import com.nosliw.data.core.domain.entity.HAPExecutableEntity;
import com.nosliw.data.core.domain.entity.HAPExecutableEntityComplex;
import com.nosliw.data.core.domain.valuecontext.HAPConfigureProcessorInherit;
import com.nosliw.data.core.domain.valuecontext.HAPConfigureProcessorValueStructure;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPUtilityValueStructureDomain {

	public static void buildValueStructureDomain(HAPWrapperBrick rootEntityInfo, HAPManualContextProcess processContext, HAPRuntimeEnvironment runtimeEnv) {
		
		buildValueStructureComplexTree(rootEntityInfo, processContext, runtimeEnv);
		
//		buildExtensionValueStructure(complexEntity, processContext);
		
//		normalizeValuePort(complexEntity, processContext);
		
//		mergeValueStructure(complexEntity, processContext);
	}

	//build value structure in complex tree and add to value structure domain
	private static void buildValueStructureComplexTree(HAPWrapperBrick rootEntityInfo, HAPManualContextProcess processContext, HAPRuntimeEnvironment runtimeEnv) {
		HAPUtilityBrickTraverse.traverseTreeLocalComplexBrick(rootEntityInfo, new HAPHandlerDownwardImpTreeNode() {

			@Override
			protected boolean processTreeNode(HAPTreeNodeBrick treeNode, Object data) {
				HAPManualContextProcess processContext = (HAPManualContextProcess)data;
				HAPBundleComplex bundle = (HAPBundleComplex)processContext.getCurrentBundle();
				HAPDomainValueStructure valueStructureDomain = bundle.getValueStructureDomain();

				HAPBrickComplex complexEntityExe = (HAPBrickComplex)this.getBrickFromNode(treeNode);
				
				HAPManualWrapperBrick rootEntityDefInfo = (HAPManualWrapperBrick)bundle.getExtraData(); 
				
				Pair<HAPManualBrick, HAPBrick> entityPair = HAPManualUtilityBrick.getEntityPair(treeNode.getTreeNodeInfo().getPathFromRoot(), bundle, runtimeEnv.getBrickManager());
				
				HAPManualBrickComplex rootEntityDef = (HAPManualBrickComplex)rootEntityDefInfo.getBrick();
				HAPManualBrickComplex complexEntityDef = (HAPManualBrickComplex)entityPair.getLeft();
				HAPManualBrickValueContext valueContextEntityDef = complexEntityDef.getValueContextBrick();
				
				//value context
				HAPValueContext valueContextExe = new HAPValueContext();
				if(valueContextEntityDef!=null) {
					{
						List<HAPInfoValueStructure> wrappers = new ArrayList<HAPInfoValueStructure>();
						for(HAPManualBrickWrapperValueStructure part : valueContextEntityDef.getValueStructures()) {
							Set<HAPRootInValueStructure> roots = new HashSet<HAPRootInValueStructure>(); 
							for(HAPManualRootInValueStructure r : part.getValueStructure().getAllRoots()) {
								HAPRootInValueStructure root = new HAPRootInValueStructure();
								root.setDefinition(r.getDefinition());
								r.cloneToEntityInfo(root);
								roots.add(root);
							}
							
							String valueStructureExeId = valueStructureDomain.newValueStructure(roots, part.getValueStructure().getInitValue(), part.getInfo(), part.getName());
							HAPInfoValueStructure valueStructureWrapperExe = new HAPInfoValueStructure(valueStructureExeId);
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
		}, runtimeEnv.getBrickManager(), processContext);
	}

	private static void createExtensionPart(HAPValueContext valueContextExe, HAPDomainValueStructure valueStructureDomain) {
		List<HAPInfoValueStructure> wrappers = new ArrayList<HAPInfoValueStructure>();
		for(String groupType : HAPUtilityValueStructure.getAllCategaries()) {
			String valueStructureExeId = valueStructureDomain.newValueStructure();
			HAPInfoValueStructure valueStructureWrapperExe = new HAPInfoValueStructure(valueStructureExeId);
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

				HAPValueContext valueContext = entityExe.getValueContext();

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

				HAPValueContext valueContext = entityExe.getValueContext();

				HAPInfoParentComplex parentInfo = definitionGlobalDomain.getComplexEntityParentInfo(entityIdDef);
				HAPConfigureProcessorValueStructure valueStructureConfig = parentInfo==null?null:parentInfo.getParentRelationConfigure().getValueStructureRelationMode();

				HAPValueContext parentValueContext = parentComplexEntity.getValueContext();
				
				//process static
				
				//process relative
				List<HAPInfoValueStructureSorting> valueStructureInfos = HAPUtilityValueContext.getAllValueStructures(valueContext);
				for(HAPInfoValueStructureSorting valueStructureInfo : valueStructureInfos) {
					HAPInfoValueStructure valueStructureWrapper = valueStructureInfo.getValueStructure();
					HAPManualBrickValueStructure valueStructure = valueStructureDomain.getValueStructureDefinitionByRuntimeId(valueStructureWrapper.getValueStructureRuntimeId());
					List<HAPServiceData> errors = new ArrayList<HAPServiceData>();
					Set<HAPReferenceValuePort> dependency = new HashSet<HAPReferenceValuePort>();
					HAPUtilityProcessRelativeElement.processRelativeInStructure(valueStructure, valueStructureConfig==null?null:valueStructureConfig.getRelativeProcessorConfigure(), dependency, errors, processContext);
				}

				//inheritance
				processInteritance(valueContext, parentValueContext, valueStructureConfig.getInheritProcessorConfigure(), valueStructureDomain);
				return true;
			}}, processContext);
	}

	private static void processInteritance(HAPValueContext valueContext, HAPValueContext parentValueContext, HAPConfigureProcessorInherit valueStructureInheritConfig, HAPDomainValueStructure valueStructureDomain) {
		String inheritMode = valueStructureInheritConfig.getMode();
		if(!inheritMode.equals(HAPConstantShared.INHERITMODE_NONE)) {
			List<HAPPartInValueContext> newParts = new ArrayList<HAPPartInValueContext>();
			for(HAPPartInValueContext part : parentValueContext.getParts()) {
				HAPPartInValueContext newPart = part.inheritValueContextPart(valueStructureDomain, inheritMode, valueStructureInheritConfig.getGroupTypes());
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

				HAPValueContext valueContext = entityExe.getValueContext();

				HAPInfoParentComplex parentInfo = definitionGlobalDomain.getComplexEntityParentInfo(entityIdDef);
				HAPConfigureProcessorValueStructure valueStructureConfig = parentInfo==null?null:parentInfo.getParentRelationConfigure().getValueStructureRelationMode();

				HAPValueContext parentValueContext = parentComplexEntity.getValueContext();
				
				//process static
				
				//process relative
				List<HAPInfoValueStructureSorting> valueStructureInfos = HAPUtilityValueContext.getAllValueStructures(valueContext);
				for(HAPInfoValueStructureSorting valueStructureInfo : valueStructureInfos) {
					HAPInfoValueStructure valueStructureWrapper = valueStructureInfo.getValueStructure();
					HAPManualBrickValueStructure valueStructure = valueStructureDomain.getValueStructureDefinitionByRuntimeId(valueStructureWrapper.getValueStructureRuntimeId());
					HAPUtilityValueStructure.traverseElement(valueStructure, new HAPProcessorStructureElement() {

						private void process(HAPElementStructureLeafRelative relativeEle) {
							HAPReferenceRootElement rootRef = relativeEle.getReference(); 
							HAPReferenceValuePort valuePortRef = rootRef.getValuePortRef();
							if(valuePortRef==null) {
								String valuePortName = rootRef.getValuePortName();
								HAPIdValuePort valuePortId = null;
								if(valuePortName==null) {
									valuePortId = HAPUtilityValuePort.getDefaultValuePortIdInEntity(parentComplexEntity);
								}
								else if(valuePortName.equals(HAPConstantShared.VALUEPORT_NAME_SELF)) {
									valuePortId = HAPUtilityValueContext.createValuePortIdValueContext(entityExe);
								}
								valuePortRef = new HAPReferenceValuePort(valuePortId);
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
					Set<HAPReferenceValuePort> dependency = new HashSet<HAPReferenceValuePort>();
					HAPUtilityProcessRelativeElement.processRelativeInStructure(valueStructure, valueStructureConfig==null?null:valueStructureConfig.getRelativeProcessorConfigure(), dependency, errors, processContext);
				}

				//inheritance
				processInteritance(valueContext, parentValueContext, valueStructureConfig.getInheritProcessorConfigure(), valueStructureDomain);
				return true;
			}}, processContext);
	}
}
