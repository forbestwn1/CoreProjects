package com.nosliw.core.application.division.manual.brick.ui.uicontent;

import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;

import com.nosliw.common.path.HAPPath;
import com.nosliw.core.application.brick.HAPEnumBrickType;
import com.nosliw.core.application.division.manual.core.HAPManualBrick;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionBrick;
import com.nosliw.core.application.division.manual.core.process.HAPManualContextProcessBrick;
import com.nosliw.core.application.division.manual.core.process.HAPManualPluginProcessorBlockImp;

public class HAPManualPluginProcessorBlockUICustomerTagDebugger extends HAPManualPluginProcessorBlockImp{

	public HAPManualPluginProcessorBlockUICustomerTagDebugger() {
		super(HAPEnumBrickType.UICUSTOMERTAGDEBUGGER_100, HAPManualBlockComplexUICustomerTagDebugger.class);
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
