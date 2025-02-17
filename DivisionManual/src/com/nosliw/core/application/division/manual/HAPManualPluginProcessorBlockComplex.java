package com.nosliw.core.application.division.manual;

import org.apache.commons.lang3.tuple.Pair;

import com.nosliw.common.interfac.HAPEntityOrReference;
import com.nosliw.common.path.HAPPath;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.HAPIdBrickType;
import com.nosliw.core.application.HAPManagerApplicationBrick;
import com.nosliw.core.application.HAPUtilityBrick;
import com.nosliw.core.application.brick.interactive.interfacee.task.HAPBlockInteractiveInterfaceTask;
import com.nosliw.core.application.common.interactive.HAPInteractiveTask;
import com.nosliw.core.application.division.manual.brick.interactive.interfacee.task.HAPManualDefinitionBlockInteractiveInterfaceTask;
import com.nosliw.core.application.division.manual.common.task.HAPManualDefinitionWithTaskInterfaceInteractive;
import com.nosliw.core.application.division.manual.common.task.HAPManualUtilityTask;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionBrick;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionUtilityBrick;
import com.nosliw.core.application.division.manual.executable.HAPManualBrick;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.resource.HAPUtilityResourceId;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public abstract class HAPManualPluginProcessorBlockComplex extends HAPManualPluginProcessorBlock{

	public HAPManualPluginProcessorBlockComplex(HAPIdBrickType brickType, Class<? extends HAPManualBrick> brickClass, HAPRuntimeEnvironment runtimeEnv, HAPManualManagerBrick manualBrickMan) {
		super(brickType, brickClass, runtimeEnv, manualBrickMan);
	}

	//process definition before value context
	public void processInit(HAPPath pathFromRoot, HAPManualContextProcessBrick processContext) {}
	public void postProcessInit(HAPPath pathFromRoot, HAPManualContextProcessBrick processContext) {}

	//build other value port
	public void processOtherValuePortBuild(HAPPath pathFromRoot, HAPManualContextProcessBrick processContext) {
		Pair<HAPManualDefinitionBrick, HAPManualBrick> blockPair = this.getBrickPair(pathFromRoot, processContext);
//		if(blockPair.getRight() instanceof HAPWithBlockInteractiveTask) {
//			//build task interface value port group
//			HAPEntityOrReference taskInterface = ((HAPWithBlockInteractiveTask)blockPair.getRight()).getTaskInterface();
//			if(taskInterface!=null) {
//				HAPBlockInteractiveInterfaceTask taskInterfaceBlock = (HAPBlockInteractiveInterfaceTask)HAPUtilityBrick.getBrick(taskInterface, processContext.getRuntimeEnv().getBrickManager());
//				HAPManualUtilityTask.buildValuePortGroupForInteractiveTask(blockPair.getRight(), taskInterfaceBlock.getValue(), processContext.getCurrentBundle().getValueStructureDomain());
//			}
//		}
		
		if(blockPair.getLeft() instanceof HAPManualDefinitionWithTaskInterfaceInteractive) {
			HAPEntityOrReference taskInterfaceBrickOrRef = ((HAPManualDefinitionWithTaskInterfaceInteractive)blockPair.getLeft()).getTaskInterface();
			if(taskInterfaceBrickOrRef!=null) {
				HAPInteractiveTask taskInterface = getInteractiveTask(taskInterfaceBrickOrRef, processContext.getRuntimeEnv().getBrickManager());
				HAPManualUtilityTask.buildValuePortGroupForInteractiveTask(blockPair.getRight(), taskInterface, processContext.getCurrentBundle().getValueStructureDomain());
			}
		}
	}
	
	private HAPInteractiveTask getInteractiveTask(HAPEntityOrReference brickOrRef, HAPManagerApplicationBrick brickManager) {
		HAPInteractiveTask out = null;
		String type = brickOrRef.getEntityOrReferenceType();
		if(type.equals(HAPConstantShared.BRICK)) {
			HAPManualDefinitionBlockInteractiveInterfaceTask taskInterfaceBrick = (HAPManualDefinitionBlockInteractiveInterfaceTask)brickOrRef;
			out = taskInterfaceBrick.getValue();
		}
		else if(type.equals(HAPConstantShared.RESOURCEID)) {
			HAPBlockInteractiveInterfaceTask taskInterfaceBlock = (HAPBlockInteractiveInterfaceTask)HAPUtilityBrick.getBrickByResource(HAPUtilityResourceId.normalizeResourceId((HAPResourceId)brickOrRef), brickManager);
			out = taskInterfaceBlock.getValue();
		}
		return out;
	}
	
	public void postProcessOtherValuePortBuild(HAPPath pathFromRoot, HAPManualContextProcessBrick processContext) {}
	
	//value context extension, variable resolve
	public void processVariableResolve(HAPPath pathFromRoot, HAPManualContextProcessBrick processContext) {}
	public void postProcessVariableResolve(HAPPath pathFromRoot, HAPManualContextProcessBrick processContext) {}
	
	//matcher
	public void processValueContextDiscovery(HAPPath pathFromRoot, HAPManualContextProcessBrick processContext) {}
	public void postProcessValueContextDiscovery(HAPPath pathFromRoot, HAPManualContextProcessBrick processContext) {}

	//
	public void processBrick(HAPPath pathFromRoot, HAPManualContextProcessBrick processContext) {}
	public void postProcessBrick(HAPPath pathFromRoot, HAPManualContextProcessBrick processContext) {}


	
	

	protected Pair<HAPManualDefinitionBrick, HAPManualBrick> getBrickPair(HAPPath pathFromRoot, HAPManualContextProcessBrick processContext){
		return HAPManualDefinitionUtilityBrick.getBrickPair(pathFromRoot, processContext.getCurrentBundle());
	}
}
