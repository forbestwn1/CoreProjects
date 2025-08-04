package com.nosliw.core.application.division.manual.brick.ui.uicontent;

import org.apache.commons.lang3.tuple.Pair;

import com.nosliw.common.path.HAPPath;
import com.nosliw.core.application.division.m.HAPManualBrick;
import com.nosliw.core.application.division.manua.HAPManualContextProcessBrick;
import com.nosliw.core.application.division.manua.HAPManualManagerBrick;
import com.nosliw.core.application.division.manua.HAPManualPluginProcessorBlockComplex;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionBrick;
import com.nosliw.core.xxx.application1.brick.HAPEnumBrickType;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPManualPluginProcessorBlockUIPage extends HAPManualPluginProcessorBlockComplex{

	public HAPManualPluginProcessorBlockUIPage(HAPRuntimeEnvironment runtimeEnv, HAPManualManagerBrick manualBrickMan) {
		super(HAPEnumBrickType.UIPAGE_100, HAPManualBlockComplexUIPage.class, runtimeEnv, manualBrickMan);
	}

	@Override
	public void processInit(HAPPath pathFromRoot, HAPManualContextProcessBrick processContext) {
		Pair<HAPManualDefinitionBrick, HAPManualBrick> blockPair = this.getBrickPair(pathFromRoot, processContext);
		HAPManualDefinitionBlockComplexUIPage uiPageDef = (HAPManualDefinitionBlockComplexUIPage)blockPair.getLeft();
		HAPManualBlockComplexUIPage uiPageExe = (HAPManualBlockComplexUIPage)blockPair.getRight();
		
		
	}
}
