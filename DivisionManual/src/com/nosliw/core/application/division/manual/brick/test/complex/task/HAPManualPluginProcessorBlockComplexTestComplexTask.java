package com.nosliw.core.application.division.manual.brick.test.complex.task;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;

import com.nosliw.common.path.HAPPath;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.HAPBundle;
import com.nosliw.core.application.HAPEnumBrickType;
import com.nosliw.core.application.common.interactive.HAPInteractiveExpression;
import com.nosliw.core.application.common.interactive.HAPInteractiveResultTask;
import com.nosliw.core.application.common.interactive.HAPInteractiveTask;
import com.nosliw.core.application.common.interactive.HAPRequestParmInInteractive;
import com.nosliw.core.application.common.interactive.HAPResultElementInInteractiveTask;
import com.nosliw.core.application.common.valueport.HAPConfigureResolveElementReference;
import com.nosliw.core.application.common.valueport.HAPIdElement;
import com.nosliw.core.application.common.valueport.HAPIdValuePortInBrick;
import com.nosliw.core.application.common.valueport.HAPIdValuePortInBundle;
import com.nosliw.core.application.common.valueport.HAPReferenceElement;
import com.nosliw.core.application.common.valueport.HAPUtilityStructureElementReference;
import com.nosliw.core.application.common.valueport.HAPUtilityValuePort;
import com.nosliw.core.application.division.manual.HAPManualContextProcessBrick;
import com.nosliw.core.application.division.manual.HAPManualManagerBrick;
import com.nosliw.core.application.division.manual.HAPManualPluginProcessorBlockComplex;
import com.nosliw.core.application.division.manual.common.task.HAPManualUtilityTask;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionBrick;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionUtilityBrick;
import com.nosliw.core.application.division.manual.executable.HAPManualBrick;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPManualPluginProcessorBlockComplexTestComplexTask extends HAPManualPluginProcessorBlockComplex{

	public HAPManualPluginProcessorBlockComplexTestComplexTask(HAPRuntimeEnvironment runtimeEnv, HAPManualManagerBrick manualBrickMan) {
		super(HAPEnumBrickType.TEST_COMPLEX_TASK_100, HAPManualBlockTestComplexTask.class, runtimeEnv, manualBrickMan);
	}

	@Override
	public void processInit(HAPPath pathFromRoot, HAPManualContextProcessBrick processContext) {
		HAPBundle bundle = processContext.getCurrentBundle();
		Pair<HAPManualDefinitionBrick, HAPManualBrick> blockPair = HAPManualDefinitionUtilityBrick.getBrickPair(pathFromRoot, bundle);
		HAPManualDefinitionBlockTestComplexTask definitionBlock = (HAPManualDefinitionBlockTestComplexTask)blockPair.getLeft();
		HAPManualBlockTestComplexTask executableBlock = (HAPManualBlockTestComplexTask)blockPair.getRight();

		if(definitionBlock.getTaskInteractiveInterface()!=null) {
			String interactiveTaskResult = definitionBlock.getTaskInteractiveResult();
			if(interactiveTaskResult==null) {
				interactiveTaskResult = HAPConstantShared.TASK_RESULT_SUCCESS;
			}
			executableBlock.setTaskInteractiveResult(interactiveTaskResult);
		}
	}

	@Override
	public void processOtherValuePortBuild(HAPPath pathFromRoot, HAPManualContextProcessBrick processContext) {
		HAPBundle bundle = processContext.getCurrentBundle();
		Pair<HAPManualDefinitionBrick, HAPManualBrick> blockPair = HAPManualDefinitionUtilityBrick.getBrickPair(pathFromRoot, bundle);
		HAPManualDefinitionBlockTestComplexTask definitionBlock = (HAPManualDefinitionBlockTestComplexTask)blockPair.getLeft();
		HAPManualBlockTestComplexTask executableBlock = (HAPManualBlockTestComplexTask)blockPair.getRight();

		if(definitionBlock.getTaskInteractiveInterface()!=null) {
			//build value port group
			HAPManualUtilityTask.buildValuePortGroupForInteractiveTask(executableBlock, executableBlock.getTaskInteractive().getValue(), processContext.getCurrentBundle().getValueStructureDomain());
		}
		
		if(definitionBlock.getExpressionInteractiveInterface()!=null) {
			//build value port group
			HAPManualUtilityTask.buildValuePortGroupForInteractiveExpression(executableBlock, executableBlock.getExpressionInteractive().getValue(), processContext.getCurrentBundle().getValueStructureDomain());
		}
	}

	@Override
	public void processBrick(HAPPath pathFromRoot, HAPManualContextProcessBrick processContext) {
		HAPBundle bundle = processContext.getCurrentBundle();
		
		Pair<HAPManualDefinitionBrick, HAPManualBrick> blockPair = HAPManualDefinitionUtilityBrick.getBrickPair(pathFromRoot, bundle);
		HAPManualDefinitionBlockTestComplexTask definitionBlock = (HAPManualDefinitionBlockTestComplexTask)blockPair.getLeft();
		HAPManualBlockTestComplexTask executableBlock = (HAPManualBlockTestComplexTask)blockPair.getRight();
	
		Map<String, HAPReferenceElement> varRefs = definitionBlock.getVariables();
		if(definitionBlock.getTaskInteractiveInterface()!=null) {
			HAPInteractiveTask taskInteractive = executableBlock.getTaskInteractive().getValue();
			//build variable
			buildRquestParmsVars(varRefs, taskInteractive.getRequestParms(), HAPConstantShared.VALUEPORTGROUP_TYPE_INTERACTIVETASK);
			
			for(HAPInteractiveResultTask taskResult : taskInteractive.getResult()) {
				for(HAPResultElementInInteractiveTask output : taskResult.getOutput()) {
					String varName = output.getName();
					HAPReferenceElement varDef = new HAPReferenceElement(varName);
					varDef.setIODirection(HAPConstantShared.IO_DIRECTION_IN);
					varDef.setValuePortId(new HAPIdValuePortInBundle(null, new HAPIdValuePortInBrick(HAPConstantShared.VALUEPORTGROUP_TYPE_INTERACTIVETASK, null)));
					varRefs.put(varName, varDef);
				}
			}
		}

		if(definitionBlock.getExpressionInteractiveInterface()!=null) {
			HAPInteractiveExpression expressionInteractive = executableBlock.getExpressionInteractive().getValue();
			//build variable
			buildRquestParmsVars(varRefs, expressionInteractive.getRequestParms(), HAPConstantShared.VALUEPORTGROUP_TYPE_INTERACTIVEEXPRESSION);
			
			String varName = HAPConstantShared.NAME_ROOT_RESULT;
			HAPReferenceElement varDef = new HAPReferenceElement(varName);
			varDef.setIODirection(HAPConstantShared.IO_DIRECTION_IN);
			varDef.setValuePortId(new HAPIdValuePortInBundle(null, new HAPIdValuePortInBrick(HAPConstantShared.VALUEPORTGROUP_TYPE_INTERACTIVEEXPRESSION, null)));
			varRefs.put(varName, varDef);
		}
		
		for(String varName : varRefs.keySet()) {
			HAPReferenceElement varRef = varRefs.get(varName);
			varRef.setValuePortId(HAPUtilityValuePort.normalizeInternalValuePortId(varRef.getValuePortId(), varRef.getIODirection(), executableBlock));
			HAPIdElement varId = HAPUtilityStructureElementReference.resolveElementReferenceInternal(varRef, executableBlock, new HAPConfigureResolveElementReference(), processContext.getCurrentBundle().getValueStructureDomain()).getElementId();
			executableBlock.getVariables().put(varName, varId);
		}
	}

	private void buildRquestParmsVars(Map<String, HAPReferenceElement> varRefs, List<HAPRequestParmInInteractive> requestParms, String valuePortGroup) {
		for(HAPRequestParmInInteractive requestParm : requestParms) {
			String varName = requestParm.getName();
			HAPReferenceElement varDef = new HAPReferenceElement(varName);
			varDef.setIODirection(HAPConstantShared.IO_DIRECTION_OUT);
			varDef.setValuePortId(new HAPIdValuePortInBundle(null, new HAPIdValuePortInBrick(valuePortGroup, null)));
			varRefs.put(varName, varDef);
		}
	}
}
