package com.nosliw.core.application.division.manual;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.common.path.HAPPath;
import com.nosliw.common.path.HAPUtilityPath;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.HAPBrick;
import com.nosliw.core.application.HAPBundle;
import com.nosliw.core.application.HAPHandlerDownwardImpAttribute;
import com.nosliw.core.application.HAPUtilityBrick;
import com.nosliw.core.application.HAPUtilityBrickTraverse;
import com.nosliw.core.application.HAPWrapperBrickRoot;
import com.nosliw.core.application.common.structure.HAPElementStructure;
import com.nosliw.core.application.common.structure.HAPElementStructureLeafRelative;
import com.nosliw.core.application.common.structure.HAPInfoElement;
import com.nosliw.core.application.common.structure.HAPProcessorStructureElement;
import com.nosliw.core.application.common.structure.HAPUtilityStructure;
import com.nosliw.core.application.common.structure.reference.HAPUtilityProcessRelativeElement;
import com.nosliw.core.application.common.valueport.HAPIdValuePortInBundle;
import com.nosliw.core.application.common.valueport.HAPUtilityValuePort;
import com.nosliw.core.application.division.manual.brick.valuestructure.HAPManualDefinitionBrickValueContext;
import com.nosliw.core.application.division.manual.brick.valuestructure.HAPManualDefinitionBrickWrapperValueStructure;
import com.nosliw.core.application.division.manual.common.valuecontext.HAPManualInfoValueStructure;
import com.nosliw.core.application.division.manual.common.valuecontext.HAPManualPartInValueContext;
import com.nosliw.core.application.division.manual.common.valuecontext.HAPManualPartInValueContextGroupWithEntity;
import com.nosliw.core.application.division.manual.common.valuecontext.HAPManualPartInValueContextSimple;
import com.nosliw.core.application.division.manual.common.valuecontext.HAPManualUtilityValueContext;
import com.nosliw.core.application.division.manual.common.valuecontext.HAPManualValueContext;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionAttributeInBrick;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionBrick;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionBrickBlockComplex;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionBrickRelation;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionBrickRelationValueContext;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionUtilityBrick;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionWrapperBrick;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionWrapperValueBrick;
import com.nosliw.core.application.division.manual.executable.HAPHandlerDownwardImpTreeNode;
import com.nosliw.core.application.division.manual.executable.HAPManualBrick;
import com.nosliw.core.application.division.manual.executable.HAPManualUtilityBrickTraverse;
import com.nosliw.core.application.division.manual.executable.HAPTreeNodeBrick;
import com.nosliw.core.application.valuestructure.HAPDomainValueStructure;
import com.nosliw.core.application.valuestructure.HAPInfoValueStructureRuntime;
import com.nosliw.core.application.valuestructure.HAPRootInValueStructure;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPManualUtilityValueStructureDomain {

	public static void buildValueStructureDomain(HAPWrapperBrickRoot rootBrickWrapper, HAPManualContextProcessBrick processContext, HAPManualManagerBrick manualBrickMan, HAPRuntimeEnvironment runtimeEnv) {
		
		buildValueStructureComplexTree(rootBrickWrapper, processContext, manualBrickMan, runtimeEnv);
		
		buildExtensionValueStructure(rootBrickWrapper, processContext, manualBrickMan, runtimeEnv);
		
		//relative element in value context
		normalizeRelativeElement(rootBrickWrapper, processContext);
		resolveRelativeElement(rootBrickWrapper, processContext);

		
		processInheriatage(rootBrickWrapper, null, processContext);
		
//		normalizeValuePort(complexEntity, processContext);
		
//		mergeValueStructure(complexEntity, processContext);
	}

	private static void resolveRelativeElement(HAPWrapperBrickRoot rootBrickWrapper, HAPManualContextProcessBrick processContext) {
		HAPUtilityBrickTraverse.traverseTreeWithLocalBrick(rootBrickWrapper, new HAPHandlerDownwardImpTreeNode() {

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
					HAPUtilityProcessRelativeElement.processRelativeInStructure(valueStructureDomain.getValueStructureDefInfoByRuntimeId(valueStructureId), null, dependency, errors, bundle, processContext.getRuntimeEnv());
				}
				return true;
			}
		}, processContext.getRuntimeEnv().getBrickManager(), processContext);
	}
	
	
	private static void normalizeRelativeElement(HAPWrapperBrickRoot rootBrickWrapper, HAPManualContextProcessBrick processContext) {
		HAPUtilityBrickTraverse.traverseTreeWithLocalBrick(rootBrickWrapper, new HAPHandlerDownwardImpTreeNode() {

			@Override
			protected boolean processTreeNode(HAPTreeNodeBrick treeNode, Object data) {
				HAPManualContextProcessBrick processContext = (HAPManualContextProcessBrick)data;
				HAPBundle bundle = processContext.getCurrentBundle();
				HAPDomainValueStructure valueStructureDomain = bundle.getValueStructureDomain();

				HAPManualBrick complexEntityExe = this.getBrickFromNode(treeNode);
				HAPManualValueContext valueContextExe = complexEntityExe.getManualValueContext();
				
				for(String valueStructureId : valueContextExe.getValueStructureIds()) {
					for(HAPRootInValueStructure root: valueStructureDomain.getValueStructureDefInfoByRuntimeId(valueStructureId).getRoots()) {
						HAPInfoValueStructureRuntime valueStructureRuntimeInfo = valueStructureDomain.getValueStructureRuntimeInfo(valueStructureId);
						HAPUtilityStructure.traverseElement(root.getDefinition(), null, new HAPProcessorStructureElement() {

							@Override
							public Pair<Boolean, HAPElementStructure> process(HAPInfoElement eleInfo, Object value) {
								if(eleInfo.getElement() instanceof HAPElementStructureLeafRelative) {
									HAPElementStructureLeafRelative relativeEle = (HAPElementStructureLeafRelative)eleInfo.getElement();
									HAPPath defaultParentValueContextPath = findDefaultParentValueContext(treeNode.getTreeNodeInfo().getPathFromRoot(), bundle);
									HAPIdValuePortInBundle normalizedValuePortId = HAPUtilityValuePort.normalizeInBundleValuePortId(relativeEle.getReference().getValuePortId(), valueStructureRuntimeInfo.getIODirection(), defaultParentValueContextPath, processContext.getCurrentBundle(), processContext.getRuntimeEnv().getResourceManager(), processContext.getRuntimeEnv().getRuntime().getRuntimeInfo());
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
	
	private static HAPPath findDefaultParentValueContext(HAPPath path, HAPBundle bundle) {
		HAPPath parentPath = HAPUtilityPath.getParentPath(path);
		while(parentPath!=null) {
			HAPManualDefinitionBrick parentBrick = HAPManualDefinitionUtilityBrick.getBrick(parentPath, bundle);
			if(!parentBrick.isValueContextEmpty()) {
				return parentPath;
			}
			parentPath = HAPUtilityPath.getParentPath(parentPath);
		}
		return null;
	}
	
	private static void processInheriatage(HAPWrapperBrickRoot rootBrickWrapper, HAPManualDefinitionBrickRelationValueContext defaultRelation, HAPManualContextProcessBrick processContext) {
		
		HAPUtilityBrickTraverse.traverseTreeWithLocalBrick(rootBrickWrapper, new HAPHandlerDownwardImpAttribute() {

			@Override
			public void processRootEntity(HAPBrick rootEntity, Object data) {}

			@Override
			public boolean processAttribute(HAPBrick parentBrick, String attributeName, Object data) {
				HAPManualBrick parentBrickManual = (HAPManualBrick)parentBrick;
				HAPManualContextProcessBrick processContext = (HAPManualContextProcessBrick)data;
				HAPBundle bundle = processContext.getCurrentBundle();
				HAPDomainValueStructure valueStructureDomain = bundle.getValueStructureDomain();

				HAPManualBrick childBrick = (HAPManualBrick)HAPUtilityBrick.getDescdentBrickLocal(parentBrickManual, new HAPPath(attributeName));
				
				HAPManualDefinitionWrapperBrick rootBrickWrapper = (HAPManualDefinitionWrapperBrick)bundle.getExtraData();
				HAPManualDefinitionBrick parentBrickManualDef = HAPManualDefinitionUtilityBrick.getDescendantBrickDefinition(rootBrickWrapper, parentBrickManual.getTreeNodeInfo().getPathFromRoot());
				HAPManualDefinitionBrickRelationValueContext valueContextRelation = resolveValueContextRelation(parentBrickManualDef.getAttribute(attributeName), defaultRelation);
				
				String inheritMode = valueContextRelation.getMode();
				if(!HAPConstantShared.INHERITMODE_NONE.equals(inheritMode)) {
					List<HAPManualPartInValueContext> fromParentParts = parentBrickManual.getValueContextInhertanceDownstream();
					List<HAPManualPartInValueContext> inheritParts = new ArrayList<HAPManualPartInValueContext>();
					for(HAPManualPartInValueContext fromParentPart : fromParentParts) {
						HAPManualPartInValueContext inheritPart = inheritToChild(fromParentPart, inheritMode, valueStructureDomain);
						if(!inheritPart.isEmpty()) {
							inheritParts.add(inheritPart);
						}
					}
					childBrick.getManualValueContext().addPartGroup(inheritParts, HAPManualUtilityValueContext.createPartInfoFromParent());
				}
				
				return true;
			}
			
		}, null, processContext);
	}
	
	private static HAPManualPartInValueContext inheritToChild(HAPManualPartInValueContext part, String inheritMode, HAPDomainValueStructure valueStructureDomain) {
		String partType = part.getPartType();
		if(partType.equals(HAPConstantShared.VALUESTRUCTUREPART_TYPE_GROUP_WITHENTITY)) {
			HAPManualPartInValueContextGroupWithEntity groupPart = (HAPManualPartInValueContextGroupWithEntity)part;
			HAPManualPartInValueContextGroupWithEntity out = new HAPManualPartInValueContextGroupWithEntity(part.getPartInfo().cloneValueStructurePartInfo());
			part.cloneToPartValueContext(out);
			for(HAPManualPartInValueContext child : groupPart.getChildren()) {
				out.addChild(inheritToChild(child, inheritMode, valueStructureDomain));
			}
			return out;
		}
		else if(partType.equals(HAPConstantShared.VALUESTRUCTUREPART_TYPE_SIMPLE)) {
			HAPManualPartInValueContextSimple simplePart = (HAPManualPartInValueContextSimple)part;
			HAPManualPartInValueContextSimple out = new HAPManualPartInValueContextSimple(part.getPartInfo().cloneValueStructurePartInfo());
			part.cloneToPartValueContext(out);

			for(HAPManualInfoValueStructure valueStructure : simplePart.getValueStructures()) {
				HAPManualInfoValueStructure cloned = null;
				if(HAPConstantShared.INHERITMODE_RUNTIME.equals(inheritMode)) {
					cloned = valueStructure.cloneValueStructureWrapper();
				}
				else if(HAPConstantShared.INHERITMODE_DEFINITION.equals(inheritMode)) {
					cloned = valueStructure.cloneValueStructureWrapper();
					cloned.setValueStructureRuntimeId(valueStructureDomain.cloneRuntime(valueStructure.getValueStructureRuntimeId()));
				}
				else if(HAPConstantShared.INHERITMODE_REFER.equals(inheritMode)) {
//						cloned = valueStructure.cloneValueStructureWrapper();
//						cloned.setValueStructureRuntimeId(valueStructureDomain.createRuntimeByRelativeRef(valueStructure.getValueStructureRuntimeId()));
				}
				out.addValueStructure(cloned);
			}
			return out;
		}
		return null;
	}
	
	public static HAPManualPartInValueContext inheritFromParent(HAPManualPartInValueContext part, String[] groupTypeCandidates) {
		String partType = part.getPartType();
		if(partType.equals(HAPConstantShared.VALUESTRUCTUREPART_TYPE_GROUP_WITHENTITY)) {
			HAPManualPartInValueContextGroupWithEntity groupPart = (HAPManualPartInValueContextGroupWithEntity)part;
			HAPManualPartInValueContextGroupWithEntity out = new HAPManualPartInValueContextGroupWithEntity(part.getPartInfo().cloneValueStructurePartInfo());
			part.cloneToPartValueContext(out);
			for(HAPManualPartInValueContext child : groupPart.getChildren()) {
				out.addChild(inheritFromParent(child, groupTypeCandidates));
			}
			return out;
		}
		else if(partType.equals(HAPConstantShared.VALUESTRUCTUREPART_TYPE_SIMPLE)) {
			HAPManualPartInValueContextSimple simplePart = (HAPManualPartInValueContextSimple)part;
			HAPManualPartInValueContextSimple out = new HAPManualPartInValueContextSimple(part.getPartInfo().cloneValueStructurePartInfo());
			part.cloneToPartValueContext(out);

			for(HAPManualInfoValueStructure valueStructure : simplePart.getValueStructures()) {
				if(groupTypeCandidates==null||groupTypeCandidates.length==0||Arrays.asList(groupTypeCandidates).contains(valueStructure.getGroupType())) {
					HAPManualInfoValueStructure cloned = valueStructure.cloneValueStructureWrapper();
					out.addValueStructure(cloned);
				}
			}
			return out;
		}
		return null;
	}
	
	private static HAPManualDefinitionBrickRelationValueContext resolveValueContextRelation(HAPManualDefinitionAttributeInBrick attrDef, HAPManualDefinitionBrickRelationValueContext defaultRelation) {
		HAPManualDefinitionBrickRelationValueContext out = new HAPManualDefinitionBrickRelationValueContext();
		out.mergeHard(defaultRelation);
		for(HAPManualDefinitionBrickRelation relation : attrDef.getRelations()) {
			if(relation.getType().equals(HAPConstantShared.MANUAL_RELATION_TYPE_VALUECONTEXT)) {
				out.mergeHard((HAPManualDefinitionBrickRelationValueContext)relation);
			}
		}
		
		out.mergeHard(((HAPManualDefinitionWrapperValueBrick)attrDef.getValueWrapper()).getBrick().getValueContextRelationWithParent());
		return out;
	}
	
	//create extension part
	private static void buildExtensionValueStructure(HAPWrapperBrickRoot rootBrickWrapper, HAPManualContextProcessBrick processContext, HAPManualManagerBrick manualBrickMan, HAPRuntimeEnvironment runtimeEnv) {
		HAPManualUtilityBrickTraverse.traverseTreeWithLocalBrickComplex(rootBrickWrapper, new HAPHandlerDownwardImpTreeNode() {

			@Override
			protected boolean processTreeNode(HAPTreeNodeBrick treeNode, Object data) {
				HAPManualContextProcessBrick processContext = (HAPManualContextProcessBrick)data;
				HAPBundle bundle = processContext.getCurrentBundle();
				HAPDomainValueStructure valueStructureDomain = bundle.getValueStructureDomain();

				HAPManualBrick complexEntityExe = this.getBrickFromNode(treeNode);
				HAPManualValueContext valueContextExe = complexEntityExe.getManualValueContext();
				
				String valueStructureExeId = valueStructureDomain.newValueStructure();
				HAPManualInfoValueStructure valueStructureWrapperExe = new HAPManualInfoValueStructure(valueStructureExeId);
				valueStructureWrapperExe.setGroupType(HAPConstantShared.UIRESOURCE_CONTEXTTYPE_PUBLIC);
				
				List<HAPManualInfoValueStructure> wrappers = new ArrayList<HAPManualInfoValueStructure>();
				wrappers.add(valueStructureWrapperExe);
				valueContextExe.addPartSimple(wrappers, HAPManualUtilityValueContext.createPartInfoExtension(), valueStructureDomain);

				return true;
			}
		}, runtimeEnv.getBrickManager(), manualBrickMan, processContext);
	}

	//build value structure in complex tree and add to value structure domain
	private static void buildValueStructureComplexTree(HAPWrapperBrickRoot rootBrickWrapper, HAPManualContextProcessBrick processContext, HAPManualManagerBrick manualBrickMan, HAPRuntimeEnvironment runtimeEnv) {
		HAPManualUtilityBrickTraverse.traverseTreeWithLocalBrickComplex(rootBrickWrapper, new HAPHandlerDownwardImpTreeNode() {

			@Override
			protected boolean processTreeNode(HAPTreeNodeBrick treeNode, Object data) {
				HAPManualContextProcessBrick processContext = (HAPManualContextProcessBrick)data;
				HAPBundle bundle = processContext.getCurrentBundle();
				HAPDomainValueStructure valueStructureDomain = bundle.getValueStructureDomain();

				HAPManualBrick complexEntityExe = this.getBrickFromNode(treeNode);
				
				HAPManualDefinitionWrapperBrick rootEntityDefInfo = (HAPManualDefinitionWrapperBrick)bundle.getExtraData(); 
				
				Pair<HAPManualDefinitionBrick, HAPManualBrick> entityPair = HAPManualDefinitionUtilityBrick.getBrickPair(treeNode.getTreeNodeInfo().getPathFromRoot(), bundle);
				
				HAPManualDefinitionBrickBlockComplex rootEntityDef = (HAPManualDefinitionBrickBlockComplex)rootEntityDefInfo.getBrick();
				HAPManualDefinitionBrickBlockComplex complexEntityDef = (HAPManualDefinitionBrickBlockComplex)entityPair.getLeft();
				HAPManualDefinitionBrickValueContext valueContextEntityDef = complexEntityDef.getValueContextBrick();
				
				//value context
				HAPManualValueContext valueContextExe = complexEntityExe.getManualValueContext();
				if(valueContextEntityDef!=null) {
					{
						List<HAPManualInfoValueStructure> wrappers = new ArrayList<HAPManualInfoValueStructure>();
						for(HAPManualDefinitionBrickWrapperValueStructure part : valueContextEntityDef.getManualValueStructures()) {
							Set<HAPRootInValueStructure> roots = new HashSet<HAPRootInValueStructure>(); 
							for(HAPRootInValueStructure r : part.getValueStructureBlock().getValue().getRoots().values()) {
								HAPRootInValueStructure root = new HAPRootInValueStructure();
								root.setDefinition(r.getDefinition());
								r.cloneToEntityInfo(root);
								roots.add(root);
							}
							
							String valueStructureExeId = valueStructureDomain.newValueStructure(roots, part.getValueStructureBlock().getValue().getInitValue(), part.getInfo(), part.getName());
							HAPManualInfoValueStructure valueStructureWrapperExe = new HAPManualInfoValueStructure(valueStructureExeId);
							valueStructureWrapperExe.setGroupType(part.getGroupType());
							wrappers.add(valueStructureWrapperExe);

							//solidate plain script expression
//							valueStructureDomain.getValueStructureDefInfoByRuntimeId(valueStructureExeId).getValueStructure().solidateConstantScript(complexEntityExe.getPlainScriptExpressionValues());
						}
						valueContextExe.addPartSimple(wrappers, HAPManualUtilityValueContext.createPartInfoDefault(), valueStructureDomain);
					}
				}
				return true;
			}
		}, runtimeEnv.getBrickManager(), manualBrickMan, processContext);
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
					HAPManualInfoValueStructure valueStructureWrapper = valueStructureInfo.getValueStructureBlock();
					HAPManualDefinitionBrickValueStructure valueStructure = valueStructureDomain.getValueStructureDefinitionByRuntimeId(valueStructureWrapper.getValueStructureRuntimeId());
					List<HAPServiceData> errors = new ArrayList<HAPServiceData>();
					Set<HAPIdValuePortInBundle> dependency = new HashSet<HAPIdValuePortInBundle>();
					HAPUtilityProcessRelativeElement.processRelativeInStructure(valueStructure, valueStructureConfig==null?null:valueStructureConfig.getRelativeProcessorConfigure(), dependency, errors, processContext);
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
					HAPManualInfoValueStructure valueStructureWrapper = valueStructureInfo.getValueStructureBlock();
					HAPManualDefinitionBrickValueStructure valueStructure = valueStructureDomain.getValueStructureDefinitionByRuntimeId(valueStructureWrapper.getValueStructureRuntimeId());
					HAPUtilityValueStructure.traverseElement(valueStructure, new HAPProcessorStructureElement() {

						private void process(HAPElementStructureLeafRelative relativeEle) {
							HAPReferenceRootElement rootRef = relativeEle.getReference(); 
							HAPIdValuePortInBundle valuePortRef = rootRef.getValuePortId();
							if(valuePortRef==null) {
								String valuePortName = rootRef.getValuePortName();
								HAPIdValuePortInBrick valuePortId = null;
								if(valuePortName==null) {
									valuePortId = HAPUtilityValuePort.getDefaultValuePortIdInEntity(parentComplexEntity);
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
					HAPUtilityProcessRelativeElement.processRelativeInStructure(valueStructure, valueStructureConfig==null?null:valueStructureConfig.getRelativeProcessorConfigure(), dependency, errors, processContext);
				}

				//inheritance
				processInteritance(valueContext, parentValueContext, valueStructureConfig.getInheritProcessorConfigure(), valueStructureDomain);
				return true;
			}}, processContext);
	}
	

	private static void createExtensionPart(HAPValueContext valueContextExe, HAPDomainValueStructure valueStructureDomain) {
		List<HAPManualInfoValueStructure> wrappers = new ArrayList<HAPManualInfoValueStructure>();
		for(String groupType : HAPUtilityValueStructure.getAllCategaries()) {
			String valueStructureExeId = valueStructureDomain.newValueStructure();
			HAPManualInfoValueStructure valueStructureWrapperExe = new HAPManualInfoValueStructure(valueStructureExeId);
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
