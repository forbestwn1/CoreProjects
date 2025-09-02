package com.nosliw.core.application.division.manual.core.process;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.common.info.HAPUtilityEntityInfo;
import com.nosliw.common.path.HAPPath;
import com.nosliw.common.path.HAPUtilityPath;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.HAPBrick;
import com.nosliw.core.application.HAPBundle;
import com.nosliw.core.application.HAPDomainValueStructure;
import com.nosliw.core.application.HAPHandlerDownward;
import com.nosliw.core.application.HAPHandlerDownwardImpAttribute;
import com.nosliw.core.application.HAPInfoValueStructureRuntime;
import com.nosliw.core.application.HAPResultBrickDescentValue;
import com.nosliw.core.application.HAPUtilityBrick;
import com.nosliw.core.application.bricktypefacade.HAPEnumFacadeSingleBrickType;
import com.nosliw.core.application.common.dynamiccriteria.HAPCriteriaDynamic;
import com.nosliw.core.application.common.dynamiccriteria.HAPRestrainBrickTypeFacade;
import com.nosliw.core.application.common.dynamiccriteria.HAPRestrainBrickTypeFacadeTaskInterface;
import com.nosliw.core.application.common.dynamiccriteria.HAPUtilityDynamicCriteria;
import com.nosliw.core.application.common.interactive.HAPUtilityInteractiveTaskValuePort;
import com.nosliw.core.application.common.parentrelation.HAPManualDefinitionBrickRelation;
import com.nosliw.core.application.common.parentrelation.HAPManualDefinitionBrickRelationValueContext;
import com.nosliw.core.application.common.structure.HAPElementStructure;
import com.nosliw.core.application.common.structure.HAPElementStructureLeafRelative;
import com.nosliw.core.application.common.structure.HAPInfoElement;
import com.nosliw.core.application.common.structure.HAPProcessorStructureElement;
import com.nosliw.core.application.common.structure.HAPRootInStructure;
import com.nosliw.core.application.common.structure.HAPUtilityElement;
import com.nosliw.core.application.common.structure.HAPUtilityStructure;
import com.nosliw.core.application.common.structure.reference.HAPUtilityProcessRelativeElementInBundle;
import com.nosliw.core.application.division.manual.brick.valuestructure.HAPManualDefinitionBrickValueContext;
import com.nosliw.core.application.division.manual.brick.valuestructure.HAPManualDefinitionBrickWrapperValueStructure;
import com.nosliw.core.application.division.manual.common.valuecontext.HAPManualPartInValueContext;
import com.nosliw.core.application.division.manual.common.valuecontext.HAPManualPartInValueContextGroupWithEntity;
import com.nosliw.core.application.division.manual.common.valuecontext.HAPManualPartInValueContextSimple;
import com.nosliw.core.application.division.manual.common.valuecontext.HAPManualUtilityValueContext;
import com.nosliw.core.application.division.manual.common.valuecontext.HAPManualValueContext;
import com.nosliw.core.application.division.manual.common.valuecontext.HAPManualWrapperStructure;
import com.nosliw.core.application.division.manual.core.HAPManualBrick;
import com.nosliw.core.application.division.manual.core.HAPManualManagerBrick;
import com.nosliw.core.application.division.manual.core.HAPManualUtilityBrick;
import com.nosliw.core.application.division.manual.core.HAPTreeNodeBrick;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionAttributeInBrick;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionBrick;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionUtilityBrick;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionWrapperValueBrick;
import com.nosliw.core.application.dynamic.HAPInfoDynamicLeaf;
import com.nosliw.core.application.valueport.HAPIdValuePortInBundle;
import com.nosliw.core.application.valueport.HAPUtilityValuePort;

public class HAPManualUtilityProcessorValuePort {

	public static void process(HAPManualContextProcessBrick processContext) {

		//build value context in complex block
		buildValueContext(processContext);
		
		//build other value port
		processOtherValuePortBuild(processContext);
		
		//generate extra value structure for variable extension
		buildExtensionValueStructure(processContext);
		
		//value context inheritage
		processValueContextInheritage(null, processContext);
		
		//relative ele in value context
		processValueContextRelativeElement(processContext);

	}
	
	
	//build value structure in complex tree and add to value structure domain
	private static void buildValueContext(HAPManualContextProcessBrick processContext) {
		HAPManualUtilityBrickTraverse.traverseTreeWithLocalBrickComplex(processContext, new HAPHandlerDownwardImpTreeNode() {

			@Override
			protected boolean processTreeNode(HAPTreeNodeBrick treeNode, Object data) {
				HAPManualContextProcessBrick processContext = (HAPManualContextProcessBrick)data;
				HAPBundle bundle = processContext.getCurrentBundle();
				HAPDomainValueStructure valueStructureDomain = bundle.getValueStructureDomain();

				HAPManualBrick complexBlock = this.getBrickFromNode(treeNode);
				
				Pair<HAPManualDefinitionBrick, HAPManualBrick> blockPair = HAPManualDefinitionUtilityBrick.getBrickPair(treeNode.getTreeNodeInfo(), bundle);
				
				HAPManualDefinitionBrick complexEntityDef = blockPair.getLeft();
				HAPManualDefinitionBrickValueContext valueContextBrickDef = complexEntityDef.getValueContextBrick();
				
				//value context
				HAPManualValueContext valueContextExe = complexBlock.getManualValueContext();
				if(valueContextBrickDef!=null) {
					{
						List<HAPManualWrapperStructure> valueStructureExeWrappers = new ArrayList<HAPManualWrapperStructure>();
						for(HAPManualDefinitionBrickWrapperValueStructure valueStructureDefWrapper : valueContextBrickDef.getManualValueStructures()) {
							if(HAPUtilityEntityInfo.isEnabled(valueStructureDefWrapper)) {
								Set<HAPRootInStructure> roots = new HashSet<HAPRootInStructure>(); 
								for(HAPRootInStructure r : valueStructureDefWrapper.getValueStructureBlock().getValue().getRoots().values()) {
									if(HAPUtilityEntityInfo.isEnabled(r)) {
										HAPRootInStructure root = new HAPRootInStructure();
										root.setDefinition(r.getDefinition());
										r.cloneToEntityInfo(root);
										roots.add(root);
									}
								}
								
								String valueStructureExeId = valueStructureDomain.newValueStructure(roots, valueStructureDefWrapper.getValueStructureBlock().getValue().getInitValue(), valueStructureDefWrapper.getInfo(), valueStructureDefWrapper.getName());
								HAPManualWrapperStructure valueStructureWrapperExe = new HAPManualWrapperStructure(valueStructureExeId);
								valueStructureWrapperExe.setStructureInfo(valueStructureDefWrapper.getStructureInfo());
								valueStructureExeWrappers.add(valueStructureWrapperExe);

								//solidate plain script expression
//								valueStructureDomain.getValueStructureDefInfoByRuntimeId(valueStructureExeId).getValueStructure().solidateConstantScript(complexEntityExe.getPlainScriptExpressionValues());
							}
						}
						valueContextExe.addPartSimple(valueStructureExeWrappers, HAPManualUtilityValueContext.createPartInfoDefault(), valueStructureDomain);
					}
				}
				return true;
			}
		}, processContext);
	}

	private static void processOtherValuePortBuild(HAPManualContextProcessBrick processContext) {
		HAPManualManagerBrick manualBrickMan = processContext.getManualBrickManager();
		HAPManualUtilityBrickTraverse.traverseTree(processContext, new HAPHandlerDownward() {

			@Override
			public boolean processBrickNode(HAPBundle bundle, HAPPath path, Object data) {
				HAPResultBrickDescentValue result = HAPUtilityBrick.getDescdentBrickResult(bundle, path);
				if(result.getBrick()!=null) {
					HAPBrick complexBrick = result.getBrick();
					((HAPManualPluginProcessorBlockImp)manualBrickMan.getBlockProcessPlugin(complexBrick.getBrickType())).processOtherValuePortBuild(path, processContext);
				}
				else if(result.getDyanmicValue()!=null) {
					HAPInfoDynamicLeaf dynamicInfo = (HAPInfoDynamicLeaf)bundle.getDynamicTaskInfo().getDescent(result.getDyanmicValue().getDynamicId());
					HAPCriteriaDynamic dynamicCriteria = dynamicInfo.getCriteria();
					
					HAPRestrainBrickTypeFacadeTaskInterface taskRestrain = (HAPRestrainBrickTypeFacadeTaskInterface)HAPUtilityDynamicCriteria.getSimpleFacadeDynamicCriteriaRestrain(dynamicCriteria, HAPEnumFacadeSingleBrickType.FACADENAME_TASK, HAPRestrainBrickTypeFacade.TYPE_RESTRAIN_TASK_INTERFACE);
					if(taskRestrain!=null) {
						HAPUtilityInteractiveTaskValuePort.buildValuePortGroupForInteractiveTask(result.getDyanmicValue(), taskRestrain.getTaskInteractiveInterface(), processContext.getCurrentBundle().getValueStructureDomain());
						
					}
				}
				return true;
			}

			@Override
			public void postProcessBrickNode(HAPBundle bundle, HAPPath path, Object data) {
				HAPResultBrickDescentValue result = HAPUtilityBrick.getDescdentBrickResult(bundle, path);
				if(result.getBrick()!=null) {
					HAPBrick complexBrick = result.getBrick();
					((HAPManualPluginProcessorBlockImp)manualBrickMan.getBlockProcessPlugin(complexBrick.getBrickType())).postProcessOtherValuePortBuild(path, processContext);
				}
			}

		}, null);
	}

	//create extension part
	private static void buildExtensionValueStructure(HAPManualContextProcessBrick processContext) {
		HAPManualUtilityBrickTraverse.traverseTreeWithLocalBrickComplex(processContext, new HAPHandlerDownwardImpTreeNode() {

			@Override
			protected boolean processTreeNode(HAPTreeNodeBrick treeNode, Object data) {
				HAPManualContextProcessBrick processContext = (HAPManualContextProcessBrick)data;
				HAPBundle bundle = processContext.getCurrentBundle();
				HAPDomainValueStructure valueStructureDomain = bundle.getValueStructureDomain();

				HAPManualBrick complexEntityExe = this.getBrickFromNode(treeNode);
				HAPManualValueContext valueContextExe = complexEntityExe.getManualValueContext();
				
				String valueStructureExeId = valueStructureDomain.newValueStructure();
				HAPManualWrapperStructure valueStructureWrapperExe = new HAPManualWrapperStructure(valueStructureExeId);
				valueStructureWrapperExe.getStructureInfo().setGroupType(HAPConstantShared.UIRESOURCE_CONTEXTTYPE_PUBLIC);
				
				List<HAPManualWrapperStructure> wrappers = new ArrayList<HAPManualWrapperStructure>();
				wrappers.add(valueStructureWrapperExe);
				valueContextExe.addPartSimple(wrappers, HAPManualUtilityValueContext.createPartInfoExtension(), valueStructureDomain);

				return true;
			}
		}, processContext);
	}

	private static void processValueContextInheritage(HAPManualDefinitionBrickRelationValueContext defaultRelation, HAPManualContextProcessBrick processContext) {
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
				HAPManualValueContext valueContextExe = childBrick.getManualValueContext();

				HAPManualDefinitionBrick parentBrickManualDef = HAPManualDefinitionUtilityBrick.getDescendantBrickDefinition(bundle, parentBrickManual.getTreeNodeInfo());
				HAPManualDefinitionBrickRelationValueContext valueContextRelation = resolveValueContextRelation(parentBrickManualDef.getAttribute(attributeName), defaultRelation);
				
				//process value context inheritage
				String inheritMode = valueContextRelation.getMode();
				if(!HAPManualUtilityBrick.isBrickComplex(((HAPManualDefinitionWrapperValueBrick)parentBrickManualDef.getAttribute(attributeName).getValueWrapper()).getBrickTypeId(), processContext.getManualBrickManager())) {
					inheritMode = HAPConstantShared.INHERITMODE_NONE;
				}
				
				if(!HAPConstantShared.INHERITMODE_NONE.equals(inheritMode)) {
					//get part from ancestor to inherit from
					List<HAPManualPartInValueContext> fromParentParts = getValueContextInhertanceDownstream(parentBrickManual, processContext.getManualBrickManager()); 
					List<HAPManualPartInValueContext> inheritParts = new ArrayList<HAPManualPartInValueContext>();
					if(fromParentParts!=null) {
						for(HAPManualPartInValueContext fromParentPart : fromParentParts) {
							//inherit to child
							HAPManualPartInValueContext inheritPart = inheritToChild(fromParentPart, inheritMode, valueStructureDomain);
							if(!inheritPart.isEmptyOfValueStructure(valueStructureDomain)) {
								inheritParts.add(inheritPart);
							}
						}
						childBrick.getManualValueContext().addPartGroup(inheritParts, HAPManualUtilityValueContext.createPartInfoFromParent());
					}
				}
				
				return true;
			}
			
		}, processContext);
		
	}

	private static void processValueContextRelativeElement(HAPManualContextProcessBrick processContext) {
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
				HAPManualValueContext valueContextExe = childBrick.getManualValueContext();

				//normalizeRelativeElement
				for(String valueStructureId : valueContextExe.getValueStructureIds()) {
					for(HAPRootInStructure root: valueStructureDomain.getStructureDefinitionByRuntimeId(valueStructureId).getRoots().values()) {
						HAPInfoValueStructureRuntime valueStructureRuntimeInfo = valueStructureDomain.getValueStructureRuntimeInfo(valueStructureId);
						HAPUtilityElement.traverseElement(root.getDefinition(), null, new HAPProcessorStructureElement() {

							@Override
							public Pair<Boolean, HAPElementStructure> process(HAPInfoElement eleInfo, Object value) {
								if(eleInfo.getElement() instanceof HAPElementStructureLeafRelative) {
									HAPElementStructureLeafRelative relativeEle = (HAPElementStructureLeafRelative)eleInfo.getElement();
									HAPPath defaultParentValueContextPath = findDefaultParentValueContext(childBrick.getTreeNodeInfo().getPathFromRoot(), processContext);
									HAPIdValuePortInBundle normalizedValuePortId = HAPUtilityValuePort.normalizeInBundleValuePortId(relativeEle.getReference().getValuePortId(), HAPConstantShared.VALUEPORTGROUP_SIDE_INTERNAL, valueStructureRuntimeInfo.getIODirection(), defaultParentValueContextPath, defaultParentValueContextPath, processContext.getRootBrickName(), processContext.getCurrentBundle(), null, null);
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
				
				//resolveRelativeElement
				for(String valueStructureId : valueContextExe.getValueStructureIds()) {
					List<HAPServiceData> errors = new ArrayList<HAPServiceData>();
					Set<HAPIdValuePortInBundle> dependency = new HashSet<HAPIdValuePortInBundle>();
					HAPUtilityProcessRelativeElementInBundle.processRelativeInStructure(valueStructureDomain.getStructureDefinitionByRuntimeId(valueStructureId), null, dependency, errors, bundle, processContext.getResourceManager(), processContext.getDataTypeHelper(), processContext.getRuntimeInfo());
				}
				
				return true;
			}
			
		}, processContext);
		
	}

	private static List<HAPManualPartInValueContext> getValueContextInhertanceDownstream(HAPManualBrick brick, HAPManualManagerBrick manualBrickMan) {
		if(HAPManualUtilityBrick.isBrickComplex(brick.getBrickType(), manualBrickMan)) {
			//for complex brick, 
			List<HAPManualPartInValueContext> out = new ArrayList<HAPManualPartInValueContext>();
			for(HAPManualPartInValueContext part : brick.getManualValueContext().getParts()) {
				out.add(inheritFromParent(part, HAPUtilityStructure.getInheritableCategaries()));
			}
			return out;
		}
		else {
			HAPManualBrick parent = brick.getTreeNodeInfo().getParent();
			if(parent!=null) {
				return getValueContextInhertanceDownstream(parent, manualBrickMan);
			}
			return null;
		}
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

			for(HAPManualWrapperStructure valueStructure : simplePart.getValueStructures()) {
				HAPManualWrapperStructure cloned = null;
				if(valueStructure.getStructureInfo().getInheritMode()!=null) {
					inheritMode = valueStructure.getStructureInfo().getInheritMode();
				} 
				
				if(HAPConstantShared.INHERITMODE_RUNTIME.equals(inheritMode)) {
					cloned = new HAPManualWrapperStructure();
					valueStructure.cloneToChildValueStructureWrapper(cloned);
					cloned.setValueStructureRuntimeId(valueStructure.getValueStructureRuntimeId());
				}
				else if(HAPConstantShared.INHERITMODE_DEFINITION.equals(inheritMode)) {
					cloned = new HAPManualWrapperStructure();
					valueStructure.cloneToChildValueStructureWrapper(cloned);
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
	

	private static HAPPath findDefaultParentValueContext(HAPPath path, HAPManualContextProcessBrick processContext) {
		HAPPath parentPath = HAPUtilityPath.getParentPath(path);
		while(parentPath!=null) {
			HAPBrick parentBrick = HAPUtilityBrick.getDescdentBrickLocal(processContext.getCurrentBundle(), parentPath, processContext.getRootBrickName());
			if(!HAPUtilityValuePort.isValuePortContainerEmpty(parentBrick.getInternalValuePorts(), processContext.getCurrentBundle().getValueStructureDomain())) {
				return parentPath;
			}
			
//			HAPManualDefinitionBrick parentBrickDef = HAPManualDefinitionUtilityBrick.getBrick(parentPath, bundle);
//			if(HAPManualUtilityBrick.isBrickComplex(parentBrick.getBrickTypeId(), processContext.getManualBrickManager())) {
//			if(!parentBrickDef.isValueContextEmpty()) {
//				return parentPath;
//			}
			parentPath = HAPUtilityPath.getParentPath(parentPath);
		}
		return new HAPPath();
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
	
	private static HAPManualPartInValueContext inheritFromParent(HAPManualPartInValueContext part, String[] groupTypeCandidates) {
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

			for(HAPManualWrapperStructure valueStructure : simplePart.getValueStructures()) {
				if(groupTypeCandidates==null||groupTypeCandidates.length==0||Arrays.asList(groupTypeCandidates).contains(valueStructure.getStructureInfo().getGroupType())) {
					HAPManualWrapperStructure cloned = valueStructure.cloneValueStructureWrapper();
					out.addValueStructure(cloned);
				}
			}
			return out;
		}
		return null;
	}
}
