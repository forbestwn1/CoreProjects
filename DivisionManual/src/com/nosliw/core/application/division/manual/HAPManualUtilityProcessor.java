package com.nosliw.core.application.division.manual;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.interfac.HAPTreeNode;
import com.nosliw.common.path.HAPPath;
import com.nosliw.common.path.HAPUtilityPath;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.HAPAttributeInBrick;
import com.nosliw.core.application.HAPBrick;
import com.nosliw.core.application.HAPBundle;
import com.nosliw.core.application.HAPHandlerDownward;
import com.nosliw.core.application.HAPIdBrickInBundle;
import com.nosliw.core.application.HAPIdBrickType;
import com.nosliw.core.application.HAPManagerApplicationBrick;
import com.nosliw.core.application.HAPUtilityBrick;
import com.nosliw.core.application.HAPUtilityBrickTraverse;
import com.nosliw.core.application.HAPUtilityBundle;
import com.nosliw.core.application.HAPWrapperValue;
import com.nosliw.core.application.HAPWrapperValueOfBrick;
import com.nosliw.core.application.HAPWrapperValueOfDynamic;
import com.nosliw.core.application.HAPWrapperValueOfReferenceResource;
import com.nosliw.core.application.common.interactive.HAPInteractiveTask;
import com.nosliw.core.application.common.parentrelation.HAPManualDefinitionBrickRelationAutoProcess;
import com.nosliw.core.application.division.manual.common.task.HAPManualUtilityTask;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionAdapter;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionAttributeInBrick;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionBrick;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionUtilityBrick;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionWrapperBrickRoot;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionWrapperValue;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionWrapperValueBrick;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionWrapperValueDynamic;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionWrapperValueReferenceResource;
import com.nosliw.core.application.division.manual.executable.HAPHandlerDownwardImpTreeNode;
import com.nosliw.core.application.division.manual.executable.HAPManualAdapter;
import com.nosliw.core.application.division.manual.executable.HAPManualAttributeInBrick;
import com.nosliw.core.application.division.manual.executable.HAPManualBrick;
import com.nosliw.core.application.division.manual.executable.HAPManualBrickImp;
import com.nosliw.core.application.division.manual.executable.HAPManualExeUtilityBrick;
import com.nosliw.core.application.division.manual.executable.HAPManualUtilityBrickTraverse;
import com.nosliw.core.application.division.manual.executable.HAPTreeNodeBrick;
import com.nosliw.core.application.dynamic.HAPRefDynamicTask;
import com.nosliw.core.application.dynamic.HAPRefDynamicTaskSingle;
import com.nosliw.core.application.dynamic.HAPUtilityInfoDynamic;
import com.nosliw.core.application.valueport.HAPUtilityValuePortVariable;
import com.nosliw.core.xxx.application1.brick.HAPUtilityBrickPath;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPManualUtilityProcessor {

	public static void processComplexDynamicAttribute(HAPManualContextProcessBrick processContext) {
		HAPManualUtilityBrickTraverse.traverseTreeWithDynamic(processContext, new HAPHandlerDownward() {

			@Override
			public boolean processBrickNode(HAPBundle bundle, HAPPath path, Object data) {
				HAPAttributeInBrick attr = HAPUtilityBrick.getDescendantAttribute(bundle, path);
				
				
				HAPBrick complexBrick = HAPUtilityBrick.getDescdentBrickLocal(bundle, path);
				((HAPManualPluginProcessorBlockComplex)processContext.getManualBrickManager().getBlockProcessPlugin(complexBrick.getBrickType())).processInit(path, processContext);
				return true;
			}

			@Override
			public void postProcessBrickNode(HAPBundle bundle, HAPPath path, Object data) {
				HAPBrick complexBrick = HAPUtilityBrick.getDescdentBrickLocal(bundle, path);
				((HAPManualPluginProcessorBlockComplex)processContext.getManualBrickManager().getBlockProcessPlugin(complexBrick.getBrickType())).postProcessInit(path, processContext);
			}

		}, processContext.getRuntimeEnv().getBrickManager(), null);
	}
	
	public static void cleanupEmptyValueStructure(HAPManualContextProcessBrick processContext) {
		Set<String> vsIds = processContext.getCurrentBundle().getValueStructureDomain().cleanupEmptyValueStructure();

		HAPUtilityBrickTraverse.traverseTreeWithLocalBrick(processContext.getCurrentBundle(), processContext.getRootBrickName(), new HAPHandlerDownward() {

			@Override
			public boolean processBrickNode(HAPBundle bundle, HAPPath path, Object data) {
				HAPManualBrickImp brick = (HAPManualBrickImp)HAPUtilityBrick.getDescdentBrickLocal(bundle, path);
				brick.getManualValueContext().cleanValueStucture(vsIds);
				brick.getOtherExternalValuePortContainer().cleanValueStucture(vsIds);
				brick.getOtherInternalValuePortContainer().cleanValueStucture(vsIds);
				return true;
			}

			@Override
			public void postProcessBrickNode(HAPBundle bundle, HAPPath path, Object data) {
			}

		}, processContext.getRuntimeEnv().getBrickManager(), null);
	}
	
	public static void processComplexBrickInit(HAPManualContextProcessBrick processContext) {
		HAPManualUtilityBrickTraverse.traverseTreeWithLocalBrick(processContext, new HAPHandlerDownward() {

			@Override
			public boolean processBrickNode(HAPBundle bundle, HAPPath path, Object data) {
				HAPBrick complexBrick = HAPUtilityBrick.getDescdentBrickLocal(bundle, path);
				((HAPManualPluginProcessorBlockComplex)processContext.getManualBrickManager().getBlockProcessPlugin(complexBrick.getBrickType())).processInit(path, processContext);
				return true;
			}

			@Override
			public void postProcessBrickNode(HAPBundle bundle, HAPPath path, Object data) {
				HAPBrick complexBrick = HAPUtilityBrick.getDescdentBrickLocal(bundle, path);
				((HAPManualPluginProcessorBlockComplex)processContext.getManualBrickManager().getBlockProcessPlugin(complexBrick.getBrickType())).postProcessInit(path, processContext);
			}

		}, processContext.getRuntimeEnv().getBrickManager(), null);
	}
	
	public static void processComplexBrickNormalizeBrickPath(HAPManualContextProcessBrick processContext) {
		HAPManualUtilityBrickTraverse.traverseTreeWithLocalBrick(processContext, new HAPHandlerDownward() {

			@Override
			public boolean processBrickNode(HAPBundle bundle, HAPPath path, Object data) {
				HAPBrick complexBrick = HAPUtilityBrick.getDescdentBrickLocal(bundle, path);
				((HAPManualPluginProcessorBlockComplex)processContext.getManualBrickManager().getBlockProcessPlugin(complexBrick.getBrickType())).normalizeBrickPath(path, processContext);
				return true;
			}

			@Override
			public void postProcessBrickNode(HAPBundle bundle, HAPPath path, Object data) {
				HAPBrick complexBrick = HAPUtilityBrick.getDescdentBrickLocal(bundle, path);
				((HAPManualPluginProcessorBlockComplex)processContext.getManualBrickManager().getBlockProcessPlugin(complexBrick.getBrickType())).postNormalizeBrickPath(path, processContext);
			}

		}, processContext.getRuntimeEnv().getBrickManager(), null);

		HAPManualUtilityBrickTraverse.traverseTree(processContext, new HAPHandlerDownward() {

			@Override
			public boolean processBrickNode(HAPBundle bundle, HAPPath path, Object data) {
				if(path.isEmpty()) {
					return true;
				}
				
				if(HAPUtilityBundle.getBrickFullPathInfo(path).getPath().isEmpty()) {
					return true;
				}
				
				HAPAttributeInBrick attr = HAPUtilityBrick.getDescendantAttribute(bundle, path);
				if(attr.getValueWrapper().getValueType().equals(HAPConstantShared.ENTITYATTRIBUTE_VALUETYPE_RESOURCEID)) {
					HAPWrapperValueOfReferenceResource resourceIdWrapper = (HAPWrapperValueOfReferenceResource)attr.getValueWrapper();
					for(HAPRefDynamicTask taskRef : resourceIdWrapper.getDynamicTaskInput().getDyanmicTaskReference().values()) {
						switch(taskRef.getType()) {
						case HAPConstantShared.DYNAMICTASK_REF_TYPE_SINGLE:
							HAPRefDynamicTaskSingle simpleDynamicTask = (HAPRefDynamicTaskSingle)taskRef;
							HAPIdBrickInBundle taskIdRef = simpleDynamicTask.getTaskId();
							taskIdRef.setIdPath(HAPUtilityBrickPath.normalizeBrickPath(new HAPPath(taskIdRef.getIdPath()), processContext.getRootBrickName(), false, processContext.getCurrentBundle()).toString());
							taskIdRef.setRelativePath(HAPUtilityPath.fromAbsoluteToRelativePath(taskIdRef.getIdPath(), path.toString()));
							simpleDynamicTask.setTaskId(taskIdRef);
							break;
						}
					}
				}
				return true;
			}

			@Override
			public void postProcessBrickNode(HAPBundle bundle, HAPPath path, Object data) {
			}

		}, processContext.getRuntimeEnv().getBrickManager(), null);

	}
	
	public static void processOtherValuePortBuild(HAPManualContextProcessBrick processContext) {
		HAPManualManagerBrick manualBrickMan = processContext.getManualBrickManager();
		HAPManualUtilityBrickTraverse.traverseTreeWithLocalBrick(processContext, new HAPHandlerDownward() {

			@Override
			public boolean processBrickNode(HAPBundle bundle, HAPPath path, Object data) {
				HAPBrick complexBrick = HAPUtilityBrick.getDescdentBrickLocal(bundle, path);
				((HAPManualPluginProcessorBlockComplex)manualBrickMan.getBlockProcessPlugin(complexBrick.getBrickType())).processOtherValuePortBuild(path, processContext);
				return true;
			}

			@Override
			public void postProcessBrickNode(HAPBundle bundle, HAPPath path, Object data) {
				HAPBrick complexBrick = HAPUtilityBrick.getDescdentBrickLocal(bundle, path);
				((HAPManualPluginProcessorBlockComplex)manualBrickMan.getBlockProcessPlugin(complexBrick.getBrickType())).postProcessOtherValuePortBuild(path, processContext);
			}

		}, processContext.getRuntimeEnv().getBrickManager(), null);
	}

	public static void processComplexVariableResolve(HAPManualContextProcessBrick processContext) {
		HAPManualManagerBrick manualBrickMan = processContext.getManualBrickManager();
		HAPManualUtilityBrickTraverse.traverseTreeWithLocalBrick(processContext, new HAPHandlerDownward() {

			@Override
			public boolean processBrickNode(HAPBundle bundle, HAPPath path, Object data) {
				HAPBrick complexBrick = HAPUtilityBrick.getDescdentBrickLocal(bundle, path);
				((HAPManualPluginProcessorBlockComplex)manualBrickMan.getBlockProcessPlugin(complexBrick.getBrickType())).processVariableResolve(path, processContext);
				return true;
			}

			@Override
			public void postProcessBrickNode(HAPBundle bundle, HAPPath path, Object data) {
				HAPBrick complexBrick = HAPUtilityBrick.getDescdentBrickLocal(bundle, path);
				((HAPManualPluginProcessorBlockComplex)manualBrickMan.getBlockProcessPlugin(complexBrick.getBrickType())).postProcessVariableResolve(path, processContext);
			}

		}, processContext.getRuntimeEnv().getBrickManager(), null);
	}

	public static void processComplexVariableInfoResolve(HAPManualContextProcessBrick processContext) {
		HAPManualUtilityBrickTraverse.traverseTreeWithLocalBrick(processContext, new HAPHandlerDownward() {

			@Override
			public boolean processBrickNode(HAPBundle bundle, HAPPath path, Object data) {
				HAPManualBrick complexBrick = (HAPManualBrick)HAPUtilityBrick.getDescdentBrickLocal(bundle, path);
				HAPUtilityValuePortVariable.buildVariableInfo(complexBrick.getVariableInfoContainer(), processContext.getCurrentBundle().getValueStructureDomain());
				return true;
			}

			@Override
			public void postProcessBrickNode(HAPBundle bundle, HAPPath path, Object data) {	}

		}, processContext.getRuntimeEnv().getBrickManager(), null);
	}

	public static void processComplexValueContextDiscovery(HAPManualContextProcessBrick processContext) {
		HAPManualManagerBrick manualBrickMan = processContext.getManualBrickManager();
		HAPManualUtilityBrickTraverse.traverseTreeWithLocalBrick(processContext, new HAPHandlerDownward() {

			@Override
			public boolean processBrickNode(HAPBundle bundle, HAPPath path, Object data) {
				HAPBrick complexBrick = HAPUtilityBrick.getDescdentBrickLocal(bundle, path);
				((HAPManualPluginProcessorBlockComplex)manualBrickMan.getBlockProcessPlugin(complexBrick.getBrickType())).processValueContextDiscovery(path, processContext);
				return true;
			}

			@Override
			public void postProcessBrickNode(HAPBundle bundle, HAPPath path, Object data) {
				HAPBrick complexBrick = HAPUtilityBrick.getDescdentBrickLocal(bundle, path);
				((HAPManualPluginProcessorBlockComplex)manualBrickMan.getBlockProcessPlugin(complexBrick.getBrickType())).postProcessValueContextDiscovery(path, processContext);
			}

		}, processContext.getRuntimeEnv().getBrickManager(), null);
	}

	public static void processComplexValuePortUpdate(HAPManualContextProcessBrick processContext) {
		HAPManualManagerBrick manualBrickMan = processContext.getManualBrickManager();
		HAPManualUtilityBrickTraverse.traverseTreeWithLocalBrick(processContext, new HAPHandlerDownward() {

			@Override
			public boolean processBrickNode(HAPBundle bundle, HAPPath path, Object data) {
				HAPManualBrick complexBrick = (HAPManualBrick)HAPUtilityBrick.getDescdentBrickLocal(bundle, path);
				HAPUtilityValuePortVariable.updateValuePortElements(complexBrick.getVariableInfoContainer(), processContext.getCurrentBundle().getValueStructureDomain());
				return true;
			}

			@Override
			public void postProcessBrickNode(HAPBundle bundle, HAPPath path, Object data) {	}

		}, processContext.getRuntimeEnv().getBrickManager(), null);
	}

	public static void processAdapterInAttribute(HAPManualContextProcessBrick processContext) {
		HAPManualManagerBrick manualBrickMan = processContext.getManualBrickManager();
		HAPUtilityBrickTraverse.traverseTree(
				processContext.getCurrentBundle(),
				processContext.getRootBrickName(),
			new HAPHandlerDownwardImpTreeNode() {
					
				@Override
				protected boolean processTreeNode(HAPTreeNodeBrick treeNode, Object data) {
					HAPTreeNode treeNodeDef = HAPManualDefinitionUtilityBrick.getDefTreeNodeFromExeTreeNode(treeNode, processContext.getCurrentBundle());
					if(treeNodeDef instanceof HAPManualDefinitionAttributeInBrick) {
						HAPManualDefinitionAttributeInBrick attrDef = (HAPManualDefinitionAttributeInBrick)treeNodeDef;
						HAPManualAttributeInBrick attrExe = (HAPManualAttributeInBrick)treeNode;
						
						if(HAPManualDefinitionUtilityBrick.isAdapterAutoProcess(attrDef, processContext.getRuntimeEnv().getBrickManager())) {
							Set<HAPManualDefinitionAdapter> adapterDefs = attrDef.getAdapters();
							Map<String, HAPManualAdapter> adapterExeByName = new LinkedHashMap<String, HAPManualAdapter>();
							for(HAPManualAdapter adapter : attrExe.getManualAdapters()) {
								adapterExeByName.put(adapter.getName(), adapter);
							}
							
							for(HAPManualDefinitionAdapter adapterDef : adapterDefs) {
								HAPManualAdapter adapterExe = adapterExeByName.get(adapterDef.getName()); 
								HAPManualDefinitionWrapperValueBrick adapterWrapperDef = (HAPManualDefinitionWrapperValueBrick)adapterDef.getValueWrapper();
								HAPManualPluginProcessorAdapter adapterProcessPlugin = manualBrickMan.getAdapterProcessPlugin(adapterWrapperDef.getBrick().getBrickTypeId());
								
								HAPManualBrick brick = (HAPManualBrick)((HAPWrapperValueOfBrick)adapterExe.getValueWrapper()).getBrick();
								adapterProcessPlugin.process(brick, adapterWrapperDef.getBrick(), new HAPManualContextProcessAdapter(processContext.getCurrentBundle(), processContext.getRootBrickName(), treeNode.getTreeNodeInfo().getPathFromRoot(), processContext.getRuntimeEnv(), manualBrickMan));
							}
							
							return true;
						}
						else {
							return false;
						}
					}
					else {
						return true;
					}
				}
			},
			processContext.getRuntimeEnv().getBrickManager(),
			processContext);
	}
	
	public static void processBrick(HAPManualContextProcessBrick processContext, HAPManagerApplicationBrick brickMan) {
		HAPManualManagerBrick manualBrickMan = processContext.getManualBrickManager();
		HAPManualUtilityBrickTraverse.traverseTreeWithLocalBrick(
				processContext, 
			new HAPHandlerDownwardImpTreeNode() {
					
				@Override
				protected boolean processTreeNode(HAPTreeNodeBrick treeNode, Object data) {
					HAPTreeNode treeNodeDef = HAPManualDefinitionUtilityBrick.getDefTreeNodeFromExeTreeNode(treeNode, processContext.getCurrentBundle());
					HAPIdBrickType entityTypeId = null;
					boolean process = true;
					HAPManualBrick block = null;
					if(treeNodeDef instanceof HAPManualDefinitionWrapperBrickRoot) {
						entityTypeId = ((HAPManualDefinitionWrapperBrickRoot)treeNodeDef).getBrickTypeId();
					}
					else {
						HAPManualDefinitionAttributeInBrick attrDef = (HAPManualDefinitionAttributeInBrick)treeNodeDef;
						entityTypeId = ((HAPManualWithBrick)attrDef.getValueWrapper()).getBrickTypeId();
						process = HAPManualUtilityProcessor.isAttributeAutoProcess(attrDef, brickMan);
					}
					if(process) {
						HAPManualPluginProcessorBlockComplex plugin = (HAPManualPluginProcessorBlockComplex)manualBrickMan.getBlockProcessPlugin(entityTypeId);
						plugin.processBrick(treeNode.getTreeNodeInfo().getPathFromRoot(), processContext);
						return true;
					}
					else {
						return false;
					}
				}

				@Override
				public void postProcessTreeNode(HAPTreeNodeBrick treeNode, Object data) {
					HAPTreeNode treeNodeDef = HAPManualDefinitionUtilityBrick.getDefTreeNodeFromExeTreeNode(treeNode, processContext.getCurrentBundle());
					HAPIdBrickType entityTypeId = null;
					boolean process = true;
					HAPManualBrick block = null;
					if(treeNodeDef instanceof HAPManualDefinitionWrapperBrickRoot) {
						entityTypeId = ((HAPManualDefinitionWrapperBrickRoot)treeNodeDef).getBrickTypeId();
					}
					else {
						HAPManualDefinitionAttributeInBrick attrDef = (HAPManualDefinitionAttributeInBrick)treeNodeDef;
						entityTypeId = ((HAPManualWithBrick)attrDef.getValueWrapper()).getBrickTypeId();
						process = HAPManualUtilityProcessor.isAttributeAutoProcess(attrDef, brickMan);
					}
					if(process) {
						HAPManualPluginProcessorBlockComplex plugin = (HAPManualPluginProcessorBlockComplex)manualBrickMan.getBlockProcessPlugin(entityTypeId);
						plugin.postProcessBrick(treeNode.getTreeNodeInfo().getPathFromRoot(), processContext);
					}
				}

			},
			brickMan,
			processContext);
	}

	
	
	public static boolean isAttributeAutoProcess(HAPManualDefinitionAttributeInBrick attr, HAPManagerApplicationBrick entityMan) {
		//check attribute relation configure first
		HAPManualDefinitionBrickRelationAutoProcess relation = (HAPManualDefinitionBrickRelationAutoProcess)HAPManualDefinitionUtilityBrick.getEntityRelation(attr, HAPConstantShared.MANUAL_RELATION_TYPE_AUTOPROCESS);
		if(relation!=null) {
			return relation.isAutoProcess();
		}
		
		HAPManualDefinitionWrapperValue attrValueWrapper = attr.getValueWrapper();
		String valueWrapperType = attrValueWrapper.getValueType();
		if(!valueWrapperType.equals(HAPConstantShared.EMBEDEDVALUE_TYPE_VALUE)) {
			if(valueWrapperType.equals(HAPConstantShared.EMBEDEDVALUE_TYPE_BRICK)) {
				HAPManualDefinitionWrapperValueBrick brickValueWrapper = (HAPManualDefinitionWrapperValueBrick)attrValueWrapper;
				//no value context attribute
				if(brickValueWrapper.getBrickTypeId().getBrickType().equals(HAPConstantShared.RUNTIME_RESOURCE_TYPE_VALUECONTEXT)) {
					return false;
				}
			}
			return true;
		}
		return false;
	}

	public static void initBricks(HAPManualContextProcessBrick processContext, HAPManualManagerBrick manualBrickMan, HAPRuntimeEnvironment runtimeEnv) {
		HAPUtilityBrickTraverse.traverseTreeWithLocalBrick(processContext.getCurrentBundle(), processContext.getRootBrickName(), new HAPHandlerDownwardImpTreeNode() {

			@Override
			protected boolean processTreeNode(HAPTreeNodeBrick treeNode, Object data) {
				HAPManualBrick brick = this.getBrickFromNode(treeNode);
				brick.init();
				return true;
			}
		}, runtimeEnv.getBrickManager(), processContext);
	}
	
	public static HAPManualBrick buildExecutableTree(HAPManualDefinitionBrick brickDef, HAPManualContextProcessBrick processContext, HAPManualManagerBrick manualBrickMan) {
		HAPManualBrick rootBrickExe = HAPManualExeUtilityBrick.newRootBrickInstance(brickDef.getBrickTypeId(), processContext.getRootBrickName(), processContext.getCurrentBundle(), manualBrickMan); 
		buildExecutableTree(brickDef, rootBrickExe, processContext, manualBrickMan);
		return rootBrickExe;
	}

	private static void buildExecutableTree(HAPManualDefinitionBrick brickDef, HAPManualBrick brick, HAPManualContextProcessBrick processContext, HAPManualManagerBrick manualBrickMan) {
		HAPBundle bundle = processContext.getCurrentBundle();
		HAPManagerApplicationBrick brickManager = getBrickManager(processContext);
		
		HAPIdBrickType entityTypeId = brickDef.getBrickTypeId();

		List<HAPManualDefinitionAttributeInBrick> attrsDef = brickDef.getAllAttributes();
		for(HAPManualDefinitionAttributeInBrick attrDef : attrsDef) {
			if(HAPManualUtilityProcessor.isAttributeAutoProcess(attrDef, brickManager)) {
				HAPManualAttributeInBrick attrExe = new HAPManualAttributeInBrick();
				attrExe.setName(attrDef.getName());
				brick.setAttribute(attrExe);

				HAPManualDefinitionWrapperValue attrValueInfo = attrDef.getValueWrapper();
				String attrValueType = attrValueInfo.getValueType();
				if(attrValueType.equals(HAPConstantShared.EMBEDEDVALUE_TYPE_BRICK)) {
					HAPManualDefinitionBrick attrBrickDef = ((HAPManualWithBrick)attrValueInfo).getBrick();
					HAPManualBrick attrBrick = HAPManualExeUtilityBrick.newBrickInstance(attrBrickDef.getBrickTypeId(), processContext.getCurrentBundle(), manualBrickMan);
					attrExe.setValueWrapper(new HAPWrapperValueOfBrick(attrBrick));
					buildExecutableTree(attrBrickDef, attrBrick, processContext, manualBrickMan);
				}
				else if(attrValueType.equals(HAPConstantShared.EMBEDEDVALUE_TYPE_RESOURCEREFERENCE)) {
					//resource reference
					HAPManualDefinitionWrapperValueReferenceResource resourceRefValueDef = (HAPManualDefinitionWrapperValueReferenceResource)attrValueInfo;
					HAPWrapperValueOfReferenceResource resourceRefValue = new HAPWrapperValueOfReferenceResource(resourceRefValueDef.getResourceId());
					for(HAPRefDynamicTask dynamicTask : resourceRefValueDef.getDyanmicInput().getDyanmicTaskReference().values()) {
						resourceRefValue.getDynamicTaskInput().addDynamicTaskReference(dynamicTask);
					}
					attrExe.setValueWrapper(resourceRefValue);
				}
				else if(attrValueType.equals(HAPConstantShared.EMBEDEDVALUE_TYPE_DYNAMIC)) {
					HAPManualDefinitionWrapperValueDynamic dynamicValueDef = (HAPManualDefinitionWrapperValueDynamic)attrValueInfo;
					HAPWrapperValueOfDynamic dynamicValue = new HAPWrapperValueOfDynamic(dynamicValueDef.getDynamicValue());
					attrExe.setValueWrapper(dynamicValue);
					
					HAPInteractiveTask taskInterface = HAPUtilityInfoDynamic.getTaskInterfaceForDynamic(dynamicValueDef.getDynamicValue(), processContext.getCurrentBundle().getDynamicTaskInfo());
					HAPManualUtilityTask.buildValuePortGroupForInteractiveTask(dynamicValue.getDynamicValue(), taskInterface, processContext.getCurrentBundle().getValueStructureDomain());
				}
				
				//adapter
				for(HAPManualDefinitionAdapter defAdapterInfo : attrDef.getAdapters()) {
					HAPManualDefinitionWrapperValue adapterValueWrapper = defAdapterInfo.getValueWrapper();
					String adapterValueType = adapterValueWrapper.getValueType();
					
					HAPWrapperValue adapterValueWrapperExe = null;
					if(adapterValueType.equals(HAPConstantShared.EMBEDEDVALUE_TYPE_BRICK)) {
						//brick
						HAPManualDefinitionWrapperValueBrick adpaterValueDefWrapperBrick = (HAPManualDefinitionWrapperValueBrick)adapterValueWrapper;
						HAPManualBrick adapterBrick = HAPManualExeUtilityBrick.newRootBrickInstance(adpaterValueDefWrapperBrick.getBrick().getBrickTypeId(), null, processContext.getCurrentBundle(), manualBrickMan);
						adapterValueWrapperExe = new HAPWrapperValueOfBrick(adapterBrick);
						buildExecutableTree(adpaterValueDefWrapperBrick.getBrick(), adapterBrick, processContext, manualBrickMan);
					}
					else if(adapterValueType.equals(HAPConstantShared.EMBEDEDVALUE_TYPE_RESOURCEREFERENCE)) {
						//resource reference
						HAPManualDefinitionWrapperValueReferenceResource adpaterValueDefWrapperResourceRef = (HAPManualDefinitionWrapperValueReferenceResource)adapterValueWrapper;
						adapterValueWrapperExe = new HAPWrapperValueOfReferenceResource(adpaterValueDefWrapperResourceRef.getResourceId());
					}
					else if(attrValueType.equals(HAPConstantShared.EMBEDEDVALUE_TYPE_DYNAMIC)) {
						
					}
					HAPManualAdapter adapter = new HAPManualAdapter(adapterValueWrapperExe);
					defAdapterInfo.cloneToEntityInfo(adapter);
					attrExe.addAdapter(adapter);
				}
			}
		}
	}
	

	
	private static HAPManagerApplicationBrick getBrickManager(HAPManualContextProcessBrick processContext) {   return processContext.getRuntimeEnv().getBrickManager(); 	}

}
