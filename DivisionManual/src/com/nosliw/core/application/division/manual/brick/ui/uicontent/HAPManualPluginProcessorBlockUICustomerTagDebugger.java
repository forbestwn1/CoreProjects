package com.nosliw.core.application.division.manual.brick.ui.uicontent;

import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;

import com.nosliw.common.path.HAPPath;
import com.nosliw.core.application.division.manual.core.HAPManualBrick;
import com.nosliw.core.application.division.manual.core.HAPManualManagerBrick;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionBrick;
import com.nosliw.core.application.division.manual.core.process.HAPManualContextProcessBrick;
import com.nosliw.core.application.division.manual.core.process.HAPManualPluginProcessorBlockComplex;
import com.nosliw.core.xxx.application1.brick.HAPEnumBrickType;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPManualPluginProcessorBlockUICustomerTagDebugger extends HAPManualPluginProcessorBlockComplex{

	public HAPManualPluginProcessorBlockUICustomerTagDebugger(HAPRuntimeEnvironment runtimeEnv, HAPManualManagerBrick manualBrickMan) {
		super(HAPEnumBrickType.UICUSTOMERTAGDEBUGGER_100, HAPManualBlockComplexUICustomerTagDebugger.class, runtimeEnv, manualBrickMan);
	}

	@Override
	public void processInit(HAPPath pathFromRoot, HAPManualContextProcessBrick processContext) {
		Pair<HAPManualDefinitionBrick, HAPManualBrick> blockPair = this.getBrickPair(pathFromRoot, processContext);
		HAPManualDefinitionBlockComplexUICustomerTagDebugger uiCustomerTagDef = (HAPManualDefinitionBlockComplexUICustomerTagDebugger)blockPair.getLeft();
		HAPManualBlockComplexUICustomerTagDebugger uiCustomerTagExe = (HAPManualBlockComplexUICustomerTagDebugger)blockPair.getRight();

		Map<String, String> attrValues = uiCustomerTagDef.getTagAttributes();
		for(String attrName : attrValues.keySet()) {
			uiCustomerTagExe.setTagAttribute(attrName, attrValues.get(attrName));
		}
		
		uiCustomerTagExe.setUITagDefinition(uiCustomerTagDef.getUITagDefinition());
	}
}
