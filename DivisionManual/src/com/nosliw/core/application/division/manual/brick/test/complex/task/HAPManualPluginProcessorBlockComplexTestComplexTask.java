package com.nosliw.core.application.division.manual.brick.test.complex.task;

import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;

import com.nosliw.common.path.HAPPath;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.HAPBundle;
import com.nosliw.core.application.HAPEnumBrickType;
import com.nosliw.core.application.common.valueport.HAPConfigureResolveElementReference;
import com.nosliw.core.application.common.valueport.HAPReferenceElement;
import com.nosliw.core.application.common.valueport.HAPResultReferenceResolve;
import com.nosliw.core.application.common.valueport.HAPUtilityStructureElementReference;
import com.nosliw.core.application.common.valueport.HAPUtilityValuePort;
import com.nosliw.core.application.division.manual.HAPManualContextProcessBrick;
import com.nosliw.core.application.division.manual.HAPManualManagerBrick;
import com.nosliw.core.application.division.manual.HAPManualPluginProcessorBlockComplex;
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

		executableBlock.setScript(definitionBlock.getScript());
		
	}

	@Override
	public void processOtherValuePortBuild(HAPPath pathFromRoot, HAPManualContextProcessBrick processContext) {
		HAPBundle bundle = processContext.getCurrentBundle();
		Pair<HAPManualDefinitionBrick, HAPManualBrick> blockPair = HAPManualDefinitionUtilityBrick.getBrickPair(pathFromRoot, bundle);
		HAPManualDefinitionBlockTestComplexTask definitionBlock = (HAPManualDefinitionBlockTestComplexTask)blockPair.getLeft();
		HAPManualBlockTestComplexTask executableBlock = (HAPManualBlockTestComplexTask)blockPair.getRight();
		
	}

	@Override
	public void processBrick(HAPPath pathFromRoot, HAPManualContextProcessBrick processContext) {
		HAPBundle bundle = processContext.getCurrentBundle();
		
		Pair<HAPManualDefinitionBrick, HAPManualBrick> blockPair = HAPManualDefinitionUtilityBrick.getBrickPair(pathFromRoot, bundle);
		HAPManualDefinitionBlockTestComplexTask definitionBlock = (HAPManualDefinitionBlockTestComplexTask)blockPair.getLeft();
		HAPManualBlockTestComplexTask executableBlock = (HAPManualBlockTestComplexTask)blockPair.getRight();
	
		Map<String, HAPReferenceElement> varRefs = definitionBlock.getVariables();
		for(String varName : varRefs.keySet()) {
			HAPReferenceElement varRef = varRefs.get(varName);
			varRef.setValuePortId(HAPUtilityValuePort.normalizeInternalValuePortId(varRef.getValuePortId(), HAPConstantShared.IO_DIRECTION_BOTH, executableBlock));
			HAPResultReferenceResolve resolve  = HAPUtilityStructureElementReference.analyzeElementReferenceInternal(varRef, executableBlock, new HAPConfigureResolveElementReference(), processContext.getCurrentBundle().getValueStructureDomain());
			executableBlock.getVariables().put(varName, resolve);
		}
	}
}
