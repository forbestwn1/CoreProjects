package com.nosliw.core.application.division.manual.core.process;

import com.nosliw.common.path.HAPPath;
import com.nosliw.core.application.HAPBrick;
import com.nosliw.core.application.HAPBundle;
import com.nosliw.core.application.HAPHandlerDownward;
import com.nosliw.core.application.HAPUtilityBrick;
import com.nosliw.core.application.division.manual.core.HAPManualBrick;
import com.nosliw.core.application.division.manual.core.HAPManualManagerBrick;
import com.nosliw.core.application.valueport.HAPUtilityValuePortVariable;

public class HAPManualUtilityProcessorVariable {

	public static void process(HAPManualContextProcessBrick processContext) {

		//variable resolve (variable extension)-----impact data container
		processComplexVariableResolve(processContext);
	
		//build var criteria infor in var info container according to value port def
		processComplexVariableInfoResolve(processContext);
		
		//variable criteria discovery ---- impact data container and value structure in context domain
		processComplexValueContextDiscovery(processContext);
		
		//update value port element according to var info container after discovery
//		HAPManualUtilityProcess.processComplexValuePortUpdate(processContext);
	}
	
	private static void processComplexVariableResolve(HAPManualContextProcessBrick processContext) {
		HAPManualManagerBrick manualBrickMan = processContext.getManualBrickManager();
		HAPManualUtilityBrickTraverse.traverseTreeWithLocalBrick(processContext, new HAPHandlerDownward() {

			@Override
			public boolean processBrickNode(HAPBundle bundle, HAPPath path, Object data) {
				HAPBrick complexBrick = HAPUtilityBrick.getDescdentBrickLocal(bundle, path);
				((HAPManualPluginProcessorBlockImp)manualBrickMan.getBlockProcessPlugin(complexBrick.getBrickType())).processVariableResolve(path, processContext);
				return true;
			}

			@Override
			public void postProcessBrickNode(HAPBundle bundle, HAPPath path, Object data) {
				HAPBrick complexBrick = HAPUtilityBrick.getDescdentBrickLocal(bundle, path);
				((HAPManualPluginProcessorBlockImp)manualBrickMan.getBlockProcessPlugin(complexBrick.getBrickType())).postProcessVariableResolve(path, processContext);
			}

		}, null);
	}

	private static void processComplexVariableInfoResolve(HAPManualContextProcessBrick processContext) {
		HAPManualUtilityBrickTraverse.traverseTreeWithLocalBrick(processContext, new HAPHandlerDownward() {

			@Override
			public boolean processBrickNode(HAPBundle bundle, HAPPath path, Object data) {
				HAPManualBrick complexBrick = (HAPManualBrick)HAPUtilityBrick.getDescdentBrickLocal(bundle, path);
				HAPUtilityValuePortVariable.buildVariableInfo(complexBrick.getVariableInfoContainer(), processContext.getCurrentBundle().getValueStructureDomain());
				return true;
			}

			@Override
			public void postProcessBrickNode(HAPBundle bundle, HAPPath path, Object data) {	}

		}, null);
	}

	private static void processComplexValueContextDiscovery(HAPManualContextProcessBrick processContext) {
		HAPManualManagerBrick manualBrickMan = processContext.getManualBrickManager();
		HAPManualUtilityBrickTraverse.traverseTreeWithLocalBrick(processContext, new HAPHandlerDownward() {

			@Override
			public boolean processBrickNode(HAPBundle bundle, HAPPath path, Object data) {
				HAPBrick complexBrick = HAPUtilityBrick.getDescdentBrickLocal(bundle, path);
				((HAPManualPluginProcessorBlockImp)manualBrickMan.getBlockProcessPlugin(complexBrick.getBrickType())).processValueContextDiscovery(path, processContext);
				return true;
			}

			@Override
			public void postProcessBrickNode(HAPBundle bundle, HAPPath path, Object data) {
				HAPBrick complexBrick = HAPUtilityBrick.getDescdentBrickLocal(bundle, path);
				((HAPManualPluginProcessorBlockImp)manualBrickMan.getBlockProcessPlugin(complexBrick.getBrickType())).postProcessValueContextDiscovery(path, processContext);
			}

		}, null);
	}

	private static void processComplexValuePortUpdate(HAPManualContextProcessBrick processContext) {
		HAPManualUtilityBrickTraverse.traverseTreeWithLocalBrick(processContext, new HAPHandlerDownward() {

			@Override
			public boolean processBrickNode(HAPBundle bundle, HAPPath path, Object data) {
				HAPManualBrick complexBrick = (HAPManualBrick)HAPUtilityBrick.getDescdentBrickLocal(bundle, path);
				HAPUtilityValuePortVariable.updateValuePortElements(complexBrick.getVariableInfoContainer(), processContext.getCurrentBundle().getValueStructureDomain());
				return true;
			}

			@Override
			public void postProcessBrickNode(HAPBundle bundle, HAPPath path, Object data) {	}

		}, null);
	}


}
