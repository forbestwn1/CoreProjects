package com.nosliw.core.application.division.manual;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.interfac.HAPTreeNode;
import com.nosliw.common.path.HAPPath;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.HAPBrick;
import com.nosliw.core.application.HAPBundle;
import com.nosliw.core.application.HAPHandlerDownward;
import com.nosliw.core.application.HAPIdBrickType;
import com.nosliw.core.application.HAPManagerApplicationBrick;
import com.nosliw.core.application.HAPUtilityBrick;
import com.nosliw.core.application.HAPUtilityBrickTraverse;
import com.nosliw.core.application.HAPWrapperBrickRoot;
import com.nosliw.core.application.HAPWrapperValue;
import com.nosliw.core.application.HAPWrapperValueOfBrick;
import com.nosliw.core.application.HAPWrapperValueOfReferenceResource;
import com.nosliw.core.application.common.parentrelation.HAPManualDefinitionBrickRelationAutoProcess;
import com.nosliw.core.application.common.valueport.HAPUtilityValuePortVariable;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionAdapter;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionAttributeInBrick;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionBrick;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionUtilityBrick;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionWrapperBrickRoot;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionWrapperValue;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionWrapperValueBrick;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionWrapperValueReferenceResource;
import com.nosliw.core.application.division.manual.executable.HAPHandlerDownwardImpTreeNode;
import com.nosliw.core.application.division.manual.executable.HAPManualAdapter;
import com.nosliw.core.application.division.manual.executable.HAPManualAttributeInBrick;
import com.nosliw.core.application.division.manual.executable.HAPManualBrick;
import com.nosliw.core.application.division.manual.executable.HAPManualBrickImp;
import com.nosliw.core.application.division.manual.executable.HAPManualExeUtilityBrick;
import com.nosliw.core.application.division.manual.executable.HAPTreeNodeBrick;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPManualUtilityProcessor {

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
	
	public static void processComplexBrickInit(HAPWrapperBrickRoot rootBrickWrapper, HAPManualContextProcessBrick processContext) {
		HAPUtilityBrickTraverse.traverseTreeWithLocalBrick(rootBrickWrapper, new HAPHandlerDownward() {

			@Override
			public boolean processBrickNode(HAPWrapperBrickRoot rootBrickWrapper, HAPPath path, Object data) {
				HAPBrick complexBrick = HAPUtilityBrick.getDescdentBrickLocal(rootBrickWrapper, path);
				((HAPManualPluginProcessorBlockComplex)processContext.getManualBrickManager().getBlockProcessPlugin(complexBrick.getBrickType())).processInit(path, processContext);
				return true;
			}

			@Override
			public void postProcessBrickNode(HAPWrapperBrickRoot rootBrickWrapper, HAPPath path, Object data) {
				HAPBrick complexBrick = HAPUtilityBrick.getDescdentBrickLocal(rootBrickWrapper, path);
				((HAPManualPluginProcessorBlockComplex)processContext.getManualBrickManager().getBlockProcessPlugin(complexBrick.getBrickType())).postProcessInit(path, processContext);
			}

		}, processContext.getRuntimeEnv().getBrickManager(), null);
	}
	
	public static void processOtherValuePortBuild(HAPWrapperBrickRoot rootBrickWrapper, HAPManualContextProcessBrick processContext) {
		HAPManualManagerBrick manualBrickMan = processContext.getManualBrickManager();
		HAPUtilityBrickTraverse.traverseTreeWithLocalBrick(rootBrickWrapper, new HAPHandlerDownward() {

			@Override
			public boolean processBrickNode(HAPWrapperBrickRoot brickWrapper, HAPPath path, Object data) {
				HAPBrick complexBrick = HAPUtilityBrick.getDescdentBrickLocal(rootBrickWrapper, path);
				((HAPManualPluginProcessorBlockComplex)manualBrickMan.getBlockProcessPlugin(complexBrick.getBrickType())).processOtherValuePortBuild(path, processContext);
				return true;
			}

			@Override
			public void postProcessBrickNode(HAPWrapperBrickRoot brickWrapper, HAPPath path, Object data) {
				HAPBrick complexBrick = HAPUtilityBrick.getDescdentBrickLocal(rootBrickWrapper, path);
				((HAPManualPluginProcessorBlockComplex)manualBrickMan.getBlockProcessPlugin(complexBrick.getBrickType())).postProcessOtherValuePortBuild(path, processContext);
			}

		}, processContext.getRuntimeEnv().getBrickManager(), null);
	}

	public static void processComplexVariableResolve(HAPWrapperBrickRoot rootBrickWrapper, HAPManualContextProcessBrick processContext) {
		HAPManualManagerBrick manualBrickMan = processContext.getManualBrickManager();
		HAPUtilityBrickTraverse.traverseTreeWithLocalBrick(rootBrickWrapper, new HAPHandlerDownward() {

			@Override
			public boolean processBrickNode(HAPWrapperBrickRoot brickWrapper, HAPPath path, Object data) {
				HAPBrick complexBrick = HAPUtilityBrick.getDescdentBrickLocal(rootBrickWrapper, path);
				((HAPManualPluginProcessorBlockComplex)manualBrickMan.getBlockProcessPlugin(complexBrick.getBrickType())).processVariableResolve(path, processContext);
				return true;
			}

			@Override
			public void postProcessBrickNode(HAPWrapperBrickRoot brickWrapper, HAPPath path, Object data) {
				HAPBrick complexBrick = HAPUtilityBrick.getDescdentBrickLocal(rootBrickWrapper, path);
				((HAPManualPluginProcessorBlockComplex)manualBrickMan.getBlockProcessPlugin(complexBrick.getBrickType())).postProcessVariableResolve(path, processContext);
			}

		}, processContext.getRuntimeEnv().getBrickManager(), null);
	}

	public static void processComplexVariableInfoResolve(HAPWrapperBrickRoot rootBrickWrapper, HAPManualContextProcessBrick processContext) {
		HAPManualManagerBrick manualBrickMan = processContext.getManualBrickManager();
		HAPUtilityBrickTraverse.traverseTreeWithLocalBrick(rootBrickWrapper, new HAPHandlerDownward() {

			@Override
			public boolean processBrickNode(HAPWrapperBrickRoot brickWrapper, HAPPath path, Object data) {
				HAPManualBrick complexBrick = (HAPManualBrick)HAPUtilityBrick.getDescdentBrickLocal(rootBrickWrapper, path);
				HAPUtilityValuePortVariable.buildVariableInfo(complexBrick.getVariableInfoContainer(), processContext.getCurrentBundle().getValueStructureDomain());
				return true;
			}

			@Override
			public void postProcessBrickNode(HAPWrapperBrickRoot brickWrapper, HAPPath path, Object data) {	}

		}, processContext.getRuntimeEnv().getBrickManager(), null);
	}

	public static void processComplexValueContextDiscovery(HAPWrapperBrickRoot rootBrickWrapper, HAPManualContextProcessBrick processContext) {
		HAPManualManagerBrick manualBrickMan = processContext.getManualBrickManager();
		HAPUtilityBrickTraverse.traverseTreeWithLocalBrick(rootBrickWrapper, new HAPHandlerDownward() {

			@Override
			public boolean processBrickNode(HAPWrapperBrickRoot brickWrapper, HAPPath path, Object data) {
				HAPBrick complexBrick = HAPUtilityBrick.getDescdentBrickLocal(rootBrickWrapper, path);
				((HAPManualPluginProcessorBlockComplex)manualBrickMan.getBlockProcessPlugin(complexBrick.getBrickType())).processValueContextDiscovery(path, processContext);
				return true;
			}

			@Override
			public void postProcessBrickNode(HAPWrapperBrickRoot brickWrapper, HAPPath path, Object data) {
				HAPBrick complexBrick = HAPUtilityBrick.getDescdentBrickLocal(rootBrickWrapper, path);
				((HAPManualPluginProcessorBlockComplex)manualBrickMan.getBlockProcessPlugin(complexBrick.getBrickType())).postProcessValueContextDiscovery(path, processContext);
			}

		}, processContext.getRuntimeEnv().getBrickManager(), null);
	}

	public static void processComplexValuePortUpdate(HAPWrapperBrickRoot rootBrickWrapper, HAPManualContextProcessBrick processContext) {
		HAPManualManagerBrick manualBrickMan = processContext.getManualBrickManager();
		HAPUtilityBrickTraverse.traverseTreeWithLocalBrick(rootBrickWrapper, new HAPHandlerDownward() {

			@Override
			public boolean processBrickNode(HAPWrapperBrickRoot brickWrapper, HAPPath path, Object data) {
				HAPManualBrick complexBrick = (HAPManualBrick)HAPUtilityBrick.getDescdentBrickLocal(rootBrickWrapper, path);
				HAPUtilityValuePortVariable.updateValuePortElements(complexBrick.getVariableInfoContainer(), complexBrick);
				return true;
			}

			@Override
			public void postProcessBrickNode(HAPWrapperBrickRoot brickWrapper, HAPPath path, Object data) {	}

		}, processContext.getRuntimeEnv().getBrickManager(), null);
	}

	public static void processAdapter(HAPManualContextProcessBrick processContext) {
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
								adapterProcessPlugin.process(brick, adapterWrapperDef.getBrick(), new HAPManualContextProcessAdapter(processContext.getCurrentBundle(), processContext.getRootBrickName(), treeNode.getTreeNodeInfo().getPathFromRoot(), processContext.getRuntimeEnv()));
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
	
	public static void processBrick(HAPManualWrapperBrickRoot rootBrickWrapper, HAPManualContextProcessBrick processContext, HAPManagerApplicationBrick brickMan) {
		HAPManualManagerBrick manualBrickMan = processContext.getManualBrickManager();
		HAPUtilityBrickTraverse.traverseTreeWithLocalBrick(
				rootBrickWrapper, 
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
		HAPManualBrick rootBrickExe = HAPManualExeUtilityBrick.newRootBrickInstance(brickDef.getBrickTypeId(), processContext.getCurrentBundle(), manualBrickMan); 
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
					attrExe.setValueWrapper(resourceRefValue);
				}
				
				//adapter
				for(HAPManualDefinitionAdapter defAdapterInfo : attrDef.getAdapters()) {
					HAPManualDefinitionWrapperValue adapterValueWrapper = defAdapterInfo.getValueWrapper();
					String adapterValueType = adapterValueWrapper.getValueType();
					
					HAPWrapperValue adapterValueWrapperExe = null;
					if(adapterValueType.equals(HAPConstantShared.EMBEDEDVALUE_TYPE_BRICK)) {
						//brick
						HAPManualDefinitionWrapperValueBrick adpaterValueDefWrapperBrick = (HAPManualDefinitionWrapperValueBrick)adapterValueWrapper;
						HAPManualBrick adapterBrick = HAPManualExeUtilityBrick.newRootBrickInstance(adpaterValueDefWrapperBrick.getBrick().getBrickTypeId(), processContext.getCurrentBundle(), manualBrickMan);
						adapterValueWrapperExe = new HAPWrapperValueOfBrick(adapterBrick);
						buildExecutableTree(adpaterValueDefWrapperBrick.getBrick(), adapterBrick, processContext, manualBrickMan);
					}
					else if(adapterValueType.equals(HAPConstantShared.EMBEDEDVALUE_TYPE_RESOURCEREFERENCE)) {
						//resource reference
						HAPManualDefinitionWrapperValueReferenceResource adpaterValueDefWrapperResourceRef = (HAPManualDefinitionWrapperValueReferenceResource)adapterValueWrapper;
						adapterValueWrapperExe = new HAPWrapperValueOfReferenceResource(adpaterValueDefWrapperResourceRef.getResourceId());
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
