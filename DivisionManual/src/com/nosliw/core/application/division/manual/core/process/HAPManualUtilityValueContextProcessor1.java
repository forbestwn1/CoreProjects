package com.nosliw.core.application.division.manual.core.process;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.common.path.HAPPath;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.HAPBrick;
import com.nosliw.core.application.HAPBundle;
import com.nosliw.core.application.HAPDomainValueStructure;
import com.nosliw.core.application.HAPHandlerDownwardImpAttribute;
import com.nosliw.core.application.HAPInfoValueStructureRuntime;
import com.nosliw.core.application.common.parentrelation.HAPManualDefinitionBrickRelationValueContext;
import com.nosliw.core.application.common.structure.HAPElementStructure;
import com.nosliw.core.application.common.structure.HAPElementStructureLeafRelative;
import com.nosliw.core.application.common.structure.HAPInfoElement;
import com.nosliw.core.application.common.structure.HAPProcessorStructureElement;
import com.nosliw.core.application.common.structure.HAPRootInStructure;
import com.nosliw.core.application.common.structure.reference.HAPUtilityProcessRelativeElementInBundle;
import com.nosliw.core.application.division.manual.common.valuecontext.HAPManualPartInValueContext;
import com.nosliw.core.application.division.manual.common.valuecontext.HAPManualValueContext;
import com.nosliw.core.application.division.manual.core.HAPManualBrick;
import com.nosliw.core.application.division.manual.core.HAPManualManagerBrick;
import com.nosliw.core.application.division.manual.core.HAPManualUtilityBrick;
import com.nosliw.core.application.division.manual.core.HAPManualWrapperBrickRoot;
import com.nosliw.core.application.division.manual.core.HAPTreeNodeBrick;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionBrick;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionUtilityBrick;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionWrapperValueBrick;
import com.nosliw.core.application.valueport.HAPIdValuePortInBundle;
import com.nosliw.core.runtimeenv.HAPRuntimeEnvironment;
import com.nosliw.core.xxx.application.common.structure.HAPUtilityStructure;
import com.nosliw.core.xxx.application.division.manual.common.valuecontext.HAPManualUtilityValueContext;
import com.nosliw.core.xxx.application.valueport.HAPUtilityValuePort1;

public class HAPManualUtilityValueContextProcessor1 {

	public static void processValueContext(HAPManualWrapperBrickRoot rootBrickWrapper, HAPManualContextProcessBrick processContext, HAPManualManagerBrick manualBrickMan, HAPRuntimeEnvironment runtimeEnv) {
		
		buildValueContext(rootBrickWrapper, processContext, manualBrickMan, runtimeEnv);
		
		buildExtensionValueStructure(rootBrickWrapper, processContext, manualBrickMan, runtimeEnv);
		
		//relative element in value context
		normalizeRelativeElement(rootBrickWrapper, processContext);
		resolveRelativeElement(rootBrickWrapper, processContext);

		
		processInheriatage(rootBrickWrapper, null, processContext);
		
//		normalizeValuePort(complexEntity, processContext);
		
//		mergeValueStructure(complexEntity, processContext);
	}

	
	
	public static void resolveRelativeElement(HAPManualContextProcessBrick processContext) {
		HAPManualUtilityBrickTraverse.traverseTreeWithLocalBrick(processContext, new HAPHandlerDownwardImpTreeNode() {

			@Override
			protected boolean processTreeNode(HAPTreeNodeBrick treeNode, Object data) {
				HAPManualContextProcessBrick processContext = (HAPManualContextProcessBrick)data;
				HAPBundle bundle = processContext.getCurrentBundle();
				HAPDomainValueStructure valueStructureDomain = bundle.getValueStructureDomain();

				HAPManualBrick complexEntityExe = this.getBrickFromNode(treeNode);
				HAPManualValueContext valueContextExe = complexEntityExe.getManualValueContext();
				
				for(String valueStructureId : valueContextExe.getValueStructureIds()) {
				
					List<HAPServiceData> errors = new ArrayList<HAPServiceData>();
					Set<HAPIdValuePortInBundle> dependency = new HashSet<HAPIdValuePortInBundle>();
					HAPUtilityProcessRelativeElementInBundle.processRelativeInStructure(valueStructureDomain.getStructureDefinitionByRuntimeId(valueStructureId), null, dependency, errors, bundle, processContext.getRuntimeEnv());
				}
				return true;
			}
		}, processContext.getRuntimeEnv().getBrickManager(), processContext);
	}
	
	
	public static void normalizeRelativeElement(HAPManualContextProcessBrick processContext) {
		HAPManualUtilityBrickTraverse.traverseTreeWithLocalBrick(processContext, new HAPHandlerDownwardImpTreeNode() {

			@Override
			protected boolean processTreeNode(HAPTreeNodeBrick treeNode, Object data) {
				HAPManualContextProcessBrick processContext = (HAPManualContextProcessBrick)data;
				HAPBundle bundle = processContext.getCurrentBundle();
				HAPDomainValueStructure valueStructureDomain = bundle.getValueStructureDomain();

				HAPManualBrick complexEntityExe = this.getBrickFromNode(treeNode);
				HAPManualValueContext valueContextExe = complexEntityExe.getManualValueContext();
				
				for(String valueStructureId : valueContextExe.getValueStructureIds()) {
					for(HAPRootInStructure root: valueStructureDomain.getStructureDefinitionByRuntimeId(valueStructureId).getRoots().values()) {
						HAPInfoValueStructureRuntime valueStructureRuntimeInfo = valueStructureDomain.getValueStructureRuntimeInfo(valueStructureId);
						HAPUtilityStructure.traverseElement(root.getDefinition(), null, new HAPProcessorStructureElement() {

							@Override
							public Pair<Boolean, HAPElementStructure> process(HAPInfoElement eleInfo, Object value) {
								if(eleInfo.getElement() instanceof HAPElementStructureLeafRelative) {
									HAPElementStructureLeafRelative relativeEle = (HAPElementStructureLeafRelative)eleInfo.getElement();
									HAPPath defaultParentValueContextPath = findDefaultParentValueContext(treeNode.getTreeNodeInfo().getPathFromRoot(), processContext);
									HAPIdValuePortInBundle normalizedValuePortId = HAPUtilityValuePort1.normalizeInBundleValuePortId(relativeEle.getReference().getValuePortId(), HAPConstantShared.VALUEPORTGROUP_SIDE_INTERNAL, valueStructureRuntimeInfo.getIODirection(), defaultParentValueContextPath, defaultParentValueContextPath, processContext.getRootBrickName(), processContext.getCurrentBundle(), processContext.getRuntimeEnv().getResourceManager(), processContext.getRuntimeEnv().getRuntime().getRuntimeInfo());
									relativeEle.getReference().setValuePortId(normalizedValuePortId);
									return Pair.of(false, null);
								}
								return Pair.of(true, null);
							}

							@Override
							public void postProcess(HAPInfoElement eleInfo, Object value) {
							}}, valueContextExe);
					}
				}
				
				return true;
			}
		}, processContext.getRuntimeEnv().getBrickManager(), processContext);
	}
	
	private static void processInheriatage(HAPManualDefinitionBrickRelationValueContext defaultRelation, HAPManualContextProcessBrick processContext) {
		
		HAPManualUtilityBrickTraverse.traverseTreeWithLocalBrick(processContext, new HAPHandlerDownwardImpAttribute() {

			@Override
			public void processRootEntity(HAPBrick rootEntity, Object data) {}

			@Override
			public boolean processAttribute(HAPBrick parentBrick, String attributeName, Object data) {
				HAPManualBrick parentBrickManual = (HAPManualBrick)parentBrick;
				HAPManualContextProcessBrick processContext = (HAPManualContextProcessBrick)data;
				HAPBundle bundle = processContext.getCurrentBundle();
				HAPDomainValueStructure valueStructureDomain = bundle.getValueStructureDomain();

				HAPManualBrick childBrick = (HAPManualBrick)this.getChildBrick(parentBrickManual, attributeName);
				
				HAPManualDefinitionBrick parentBrickManualDef = HAPManualDefinitionUtilityBrick.getDescendantBrickDefinition(bundle, parentBrickManual.getTreeNodeInfo());
				HAPManualDefinitionBrickRelationValueContext valueContextRelation = resolveValueContextRelation(parentBrickManualDef.getAttribute(attributeName), defaultRelation);
				
				String inheritMode = valueContextRelation.getMode();
				if(!HAPManualUtilityBrick.isBrickComplex(((HAPManualDefinitionWrapperValueBrick)parentBrickManualDef.getAttribute(attributeName).getValueWrapper()).getBrickTypeId(), processContext.getManualBrickManager())) {
					inheritMode = HAPConstantShared.INHERITMODE_NONE;
				}
				
				if(!HAPConstantShared.INHERITMODE_NONE.equals(inheritMode)) {
					List<HAPManualPartInValueContext> fromParentParts = getValueContextInhertanceDownstream(parentBrickManual, processContext.getManualBrickManager()); 
					List<HAPManualPartInValueContext> inheritParts = new ArrayList<HAPManualPartInValueContext>();
					for(HAPManualPartInValueContext fromParentPart : fromParentParts) {
						HAPManualPartInValueContext inheritPart = inheritToChild(fromParentPart, inheritMode, valueStructureDomain);
						if(!inheritPart.isEmptyOfValueStructure(valueStructureDomain)) {
							inheritParts.add(inheritPart);
						}
					}
					childBrick.getManualValueContext().addPartGroup(inheritParts, HAPManualUtilityValueContext.createPartInfoFromParent());
				}
				
				return true;
			}
			
		}, null, processContext);
	}
	
	
	
	
	
	

	
	
/*	
	//merge value structure between paren and child
	private static void mergeValueStructure1(HAPExecutableEntityComplex complexEntity, HAPContextProcessor processContext) {
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
				List<HAPManualInfoValueStructureSorting> valueStructureInfos = HAPManualUtilityValueContext.getAllValueStructures(valueContext);
				for(HAPManualInfoValueStructureSorting valueStructureInfo : valueStructureInfos) {
					HAPManualWrapperStructure valueStructureWrapper = valueStructureInfo.getValueStructureBlock();
					HAPManualDefinitionBrickValueStructure valueStructure = valueStructureDomain.getValueStructureDefinitionByRuntimeId(valueStructureWrapper.getValueStructureRuntimeId());
					List<HAPServiceData> errors = new ArrayList<HAPServiceData>();
					Set<HAPIdValuePortInBundle> dependency = new HashSet<HAPIdValuePortInBundle>();
					HAPUtilityProcessRelativeElementInBundle.processRelativeInStructure(valueStructure, valueStructureConfig==null?null:valueStructureConfig.getRelativeProcessorConfigure(), dependency, errors, processContext);
				}

				//inheritance
				processInteritance(valueContext, parentValueContext, valueStructureConfig.getInheritProcessorConfigure(), valueStructureDomain);
				return true;
			}}, processContext);
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
				List<HAPManualInfoValueStructureSorting> valueStructureInfos = HAPManualUtilityValueContext.getAllValueStructures(valueContext);
				for(HAPManualInfoValueStructureSorting valueStructureInfo : valueStructureInfos) {
					HAPManualWrapperStructure valueStructureWrapper = valueStructureInfo.getValueStructureBlock();
					HAPManualDefinitionBrickValueStructure valueStructure = valueStructureDomain.getValueStructureDefinitionByRuntimeId(valueStructureWrapper.getValueStructureRuntimeId());
					HAPUtilityValueStructure.traverseElement(valueStructure, new HAPProcessorStructureElement() {

						private void process(HAPElementStructureLeafRelative relativeEle) {
							HAPReferenceRootElement rootRef = relativeEle.getReference(); 
							HAPIdValuePortInBundle valuePortRef = rootRef.getValuePortId();
							if(valuePortRef==null) {
								String valuePortName = rootRef.getValuePortName();
								HAPIdValuePortInBrick valuePortId = null;
								if(valuePortName==null) {
									valuePortId = HAPUtilityBrickValuePort.getDefaultValuePortIdInEntity(parentComplexEntity);
								}
								else if(valuePortName.equals(HAPConstantShared.VALUEPORT_NAME_SELF)) {
									valuePortId = HAPManualUtilityValueContext.createValuePortIdValueContext(entityExe);
								}
								valuePortRef = new HAPIdValuePortInBundle(valuePortId);
								rootRef.setValuePortId(valuePortRef);
							}
						}
						
						@Override
						public Pair<Boolean, HAPElementStructure> process(HAPInfoElementResolve eleInfo, Object value) {
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
						public void postProcess(HAPInfoElementResolve eleInfo, Object value) {}
					}, null);
					
					List<HAPServiceData> errors = new ArrayList<HAPServiceData>();
					Set<HAPIdValuePortInBundle> dependency = new HashSet<HAPIdValuePortInBundle>();
					HAPUtilityProcessRelativeElementInBundle.processRelativeInStructure(valueStructure, valueStructureConfig==null?null:valueStructureConfig.getRelativeProcessorConfigure(), dependency, errors, processContext);
				}

				//inheritance
				processInteritance(valueContext, parentValueContext, valueStructureConfig.getInheritProcessorConfigure(), valueStructureDomain);
				return true;
			}}, processContext);
	}
	

	private static void createExtensionPart(HAPValueContext valueContextExe, HAPDomainValueStructure valueStructureDomain) {
		List<HAPManualWrapperStructure> wrappers = new ArrayList<HAPManualWrapperStructure>();
		for(String groupType : HAPUtilityValueStructure.getAllCategaries()) {
			String valueStructureExeId = valueStructureDomain.newValueStructure();
			HAPManualWrapperStructure valueStructureWrapperExe = new HAPManualWrapperStructure(valueStructureExeId);
			valueStructureWrapperExe.setGroupType(groupType);
			wrappers.add(valueStructureWrapperExe);
		}
		valueContextExe.addPartSimple(wrappers, HAPManualUtilityValueContext.createPartInfoExtension(), valueStructureDomain);
	}

	//create extension part
	private static void buildExtensionValueStructure1(HAPExecutableEntityComplex complexEntity, HAPContextProcessor processContext) {
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

	
	private static void processInteritance1(HAPValueContext valueContext, HAPValueContext parentValueContext, HAPConfigureProcessorInherit valueStructureInheritConfig, HAPDomainValueStructure valueStructureDomain) {
		String inheritMode = valueStructureInheritConfig.getMode();
		if(!inheritMode.equals(HAPConstantShared.INHERITMODE_NONE)) {
			List<HAPManualPartInValueContext> newParts = new ArrayList<HAPManualPartInValueContext>();
			for(HAPManualPartInValueContext part : parentValueContext.getParts()) {
				HAPManualPartInValueContext newPart = part.inheritValueContextPart(valueStructureDomain, inheritMode, valueStructureInheritConfig.getGroupTypes());
				if(!newPart.isEmpty()) {
					newParts.add(newPart);
				}
			}
			valueContext.addPartGroup(newParts, HAPManualUtilityValueContext.createPartInfoFromParent());
		}
	}
*/	
}
