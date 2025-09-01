package com.nosliw.core.application.division.manual.core.process;

import java.util.List;

import com.nosliw.common.path.HAPPath;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPUtilityBasic;
import com.nosliw.core.application.HAPBrick;
import com.nosliw.core.application.HAPBundle;
import com.nosliw.core.application.HAPHandlerDownward;
import com.nosliw.core.application.HAPIdBrickType;
import com.nosliw.core.application.HAPUtilityBrick;
import com.nosliw.core.application.HAPUtilityBrickTraverse;
import com.nosliw.core.application.HAPUtilityBundle;
import com.nosliw.core.application.HAPWrapperValue;
import com.nosliw.core.application.HAPWrapperValueOfBrick;
import com.nosliw.core.application.HAPWrapperValueOfDynamic;
import com.nosliw.core.application.HAPWrapperValueOfReferenceResource;
import com.nosliw.core.application.common.interactive.HAPInteractiveTask;
import com.nosliw.core.application.common.interactive.HAPUtilityInteractiveTaskValuePort;
import com.nosliw.core.application.division.manual.core.HAPInfoTreeNode;
import com.nosliw.core.application.division.manual.core.HAPManualAdapter;
import com.nosliw.core.application.division.manual.core.HAPManualAttributeInBrick;
import com.nosliw.core.application.division.manual.core.HAPManualBrick;
import com.nosliw.core.application.division.manual.core.HAPManualManagerBrick;
import com.nosliw.core.application.division.manual.core.HAPManualWrapperBrickRoot;
import com.nosliw.core.application.division.manual.core.HAPTreeNodeBrick;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionAdapter;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionAttributeInBrick;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionBrick;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionWithBrick;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionWrapperBrickRoot;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionWrapperValue;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionWrapperValueBrick;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionWrapperValueReferenceResource;
import com.nosliw.core.application.dynamic.HAPInputDynamic;
import com.nosliw.core.application.dynamic.HAPUtilityInfoDynamic;
import com.nosliw.core.xxx.application.division.manual.core.definition1.HAPManualDefinitionWrapperValueDynamic;

public class HAPManualProcessProcessorInit {

	public static HAPManualWrapperBrickRoot process(HAPManualDefinitionWrapperBrickRoot rootBrickDefWrapper, HAPManualContextProcessBrick processContext) {
		HAPManualDefinitionBrick brickDef = rootBrickDefWrapper.getBrick();
		//build executable tree
		HAPManualWrapperBrickRoot out = new HAPManualWrapperBrickRoot(HAPManualProcessProcessorInit.buildExecutableTree(brickDef, processContext));
		out.setName(processContext.getRootBrickName());
		out.setDefinition(rootBrickDefWrapper);
		processContext.getCurrentBundle().addRootBrickWrapper(out);
		
		//brick init
		HAPManualProcessProcessorInit.initBricks(processContext);

		//init
		HAPManualProcessProcessorInit.processComplexBrickInit(processContext);
		return out;
	}
	
	private static HAPManualBrick buildExecutableTree(HAPManualDefinitionBrick brickDef, HAPManualContextProcessBrick processContext) {
		HAPManualBrick rootBrickExe = newRootBrickInstance(brickDef.getBrickTypeId(), processContext.getRootBrickName(), processContext.getCurrentBundle(), processContext.getManualBrickManager()); 
		buildExecutableTree(brickDef, rootBrickExe, processContext);
		return rootBrickExe;
	}

	private static void buildExecutableTree(HAPManualDefinitionBrick brickDef, HAPManualBrick brick, HAPManualContextProcessBrick processContext) {
		HAPBundle bundle = processContext.getCurrentBundle();
		
		HAPIdBrickType entityTypeId = brickDef.getBrickTypeId();

		List<HAPManualDefinitionAttributeInBrick> attrsDef = brickDef.getAllAttributes();
		for(HAPManualDefinitionAttributeInBrick attrDef : attrsDef) {
			if(HAPManualUtilityProcess.isAttributeAutoProcess(attrDef, processContext.getBrickManager())) {
				HAPManualAttributeInBrick attrExe = new HAPManualAttributeInBrick();
				attrExe.setName(attrDef.getName());
				brick.setAttribute(attrExe);

				HAPManualDefinitionWrapperValue attrValueInfo = attrDef.getValueWrapper();
				String attrValueType = attrValueInfo.getValueType();
				if(attrValueType.equals(HAPConstantShared.EMBEDEDVALUE_TYPE_BRICK)) {
					HAPManualDefinitionBrick attrBrickDef = ((HAPManualDefinitionWithBrick)attrValueInfo).getBrick();
					HAPManualBrick attrBrick = newBrickInstance(attrBrickDef.getBrickTypeId(), processContext.getCurrentBundle(), processContext.getManualBrickManager());
					attrExe.setValueWrapper(new HAPWrapperValueOfBrick(attrBrick));
					buildExecutableTree(attrBrickDef, attrBrick, processContext);
				}
				else if(attrValueType.equals(HAPConstantShared.EMBEDEDVALUE_TYPE_RESOURCEREFERENCE)) {
					//resource reference
					HAPManualDefinitionWrapperValueReferenceResource resourceRefValueDef = (HAPManualDefinitionWrapperValueReferenceResource)attrValueInfo;
					HAPWrapperValueOfReferenceResource resourceRefValue = new HAPWrapperValueOfReferenceResource(resourceRefValueDef.getResourceId());
					for(HAPInputDynamic dynamicTask : resourceRefValueDef.getDyanmicInput().getDyanmicTaskReference().values()) {
						resourceRefValue.getDynamicTaskInput().addDynamicTaskReference(dynamicTask);
					}
					attrExe.setValueWrapper(resourceRefValue);
				}
				else if(attrValueType.equals(HAPConstantShared.EMBEDEDVALUE_TYPE_DYNAMIC)) {
					HAPManualDefinitionWrapperValueDynamic dynamicValueDef = (HAPManualDefinitionWrapperValueDynamic)attrValueInfo;
					HAPWrapperValueOfDynamic dynamicValue = new HAPWrapperValueOfDynamic(dynamicValueDef.getDynamicValue());
					attrExe.setValueWrapper(dynamicValue);
					
					HAPInteractiveTask taskInterface = HAPUtilityInfoDynamic.getTaskInterfaceForDynamic(dynamicValueDef.getDynamicValue(), processContext.getCurrentBundle().getDynamicTaskInfo());
					HAPUtilityInteractiveTaskValuePort.buildValuePortGroupForInteractiveTask(dynamicValue.getDynamicValue(), taskInterface, processContext.getCurrentBundle().getValueStructureDomain());
				}
				
				//adapter
				for(HAPManualDefinitionAdapter defAdapterInfo : attrDef.getAdapters()) {
					HAPManualDefinitionWrapperValue adapterValueWrapper = defAdapterInfo.getValueWrapper();
					String adapterValueType = adapterValueWrapper.getValueType();
					
					HAPWrapperValue adapterValueWrapperExe = null;
					if(adapterValueType.equals(HAPConstantShared.EMBEDEDVALUE_TYPE_BRICK)) {
						//brick
						HAPManualDefinitionWrapperValueBrick adpaterValueDefWrapperBrick = (HAPManualDefinitionWrapperValueBrick)adapterValueWrapper;
						HAPManualBrick adapterBrick = newRootBrickInstance(adpaterValueDefWrapperBrick.getBrick().getBrickTypeId(), null, processContext.getCurrentBundle(), processContext.getManualBrickManager());
						adapterValueWrapperExe = new HAPWrapperValueOfBrick(adapterBrick);
						buildExecutableTree(adpaterValueDefWrapperBrick.getBrick(), adapterBrick, processContext);
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
	
	private static void initBricks(HAPManualContextProcessBrick processContext) {
		HAPUtilityBrickTraverse.traverseTreeWithLocalBrick(processContext.getCurrentBundle(), processContext.getRootBrickName(), new HAPHandlerDownwardImpTreeNode() {

			@Override
			protected boolean processTreeNode(HAPTreeNodeBrick treeNode, Object data) {
				HAPManualBrick brick = this.getBrickFromNode(treeNode);
				brick.init();
				return true;
			}
		}, processContext.getBrickManager(), processContext);
	}
	
	private static void processComplexBrickInit(HAPManualContextProcessBrick processContext) {
		HAPManualUtilityBrickTraverse.traverseTreeWithLocalBrick(processContext, new HAPHandlerDownward() {

			@Override
			public boolean processBrickNode(HAPBundle bundle, HAPPath path, Object data) {
				HAPBrick complexBrick = HAPUtilityBrick.getDescdentBrickLocal(bundle, path);
				((HAPManualPluginProcessorBlockImp)processContext.getManualBrickManager().getBlockProcessPlugin(complexBrick.getBrickType())).processInit(path, processContext);
				return true;
			}

			@Override
			public void postProcessBrickNode(HAPBundle bundle, HAPPath path, Object data) {
				HAPBrick complexBrick = HAPUtilityBrick.getDescdentBrickLocal(bundle, path);
				((HAPManualPluginProcessorBlockImp)processContext.getManualBrickManager().getBlockProcessPlugin(complexBrick.getBrickType())).postProcessInit(path, processContext);
			}

		}, null);
	}
	
	private static HAPManualBrick newRootBrickInstance(HAPIdBrickType brickTypeId, String rootName, HAPBundle bundle, HAPManualManagerBrick manualBrickMan) {
		HAPManualBrick brick = manualBrickMan.newBrick(brickTypeId, bundle);
		
		if(HAPUtilityBasic.isStringEmpty(rootName)) {
			brick.setTreeNodeInfo(new HAPInfoTreeNode());
		}
		else {
			brick.setTreeNodeInfo(new HAPInfoTreeNode(new HAPPath(HAPUtilityBundle.buildBranchPathSegment(rootName)), null));
		}
		return brick;
	}

	private static HAPManualBrick newBrickInstance(HAPIdBrickType brickTypeId, HAPBundle bundle, HAPManualManagerBrick manualBrickMan) {
		return manualBrickMan.newBrick(brickTypeId, bundle);
	}

}
