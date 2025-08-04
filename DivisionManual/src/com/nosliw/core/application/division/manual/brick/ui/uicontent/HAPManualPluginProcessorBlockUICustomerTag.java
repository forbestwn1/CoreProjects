package com.nosliw.core.application.division.manual.brick.ui.uicontent;

import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;

import com.nosliw.common.path.HAPPath;
import com.nosliw.core.application.division.manual.core.HAPManualBrick;
import com.nosliw.core.application.division.manual.core.HAPManualManagerBrick;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionBrick;
import com.nosliw.core.application.division.manual.core.process.HAPManualContextProcessBrick;
import com.nosliw.core.application.division.manual.core.process.HAPManualPluginProcessorBlockComplex;
import com.nosliw.core.application.uitag.HAPUITagAttributeDefinition;
import com.nosliw.core.xxx.application1.brick.HAPEnumBrickType;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPManualPluginProcessorBlockUICustomerTag extends HAPManualPluginProcessorBlockComplex{

	public HAPManualPluginProcessorBlockUICustomerTag(HAPRuntimeEnvironment runtimeEnv, HAPManualManagerBrick manualBrickMan) {
		super(HAPEnumBrickType.UICUSTOMERTAG_100, HAPManualBlockComplexUICustomerTag.class, runtimeEnv, manualBrickMan);
	}

	@Override
	public void processInit(HAPPath pathFromRoot, HAPManualContextProcessBrick processContext) {
		Pair<HAPManualDefinitionBrick, HAPManualBrick> blockPair = this.getBrickPair(pathFromRoot, processContext);
		HAPManualDefinitionBlockComplexUICustomerTag uiCustomerTagDef = (HAPManualDefinitionBlockComplexUICustomerTag)blockPair.getLeft();
		HAPManualBlockComplexUICustomerTag uiCustomerTagExe = (HAPManualBlockComplexUICustomerTag)blockPair.getRight();

		uiCustomerTagExe.setUITagDefinition(uiCustomerTagDef.getUITagDefinition());
		
		Map<String, String> attrValues = uiCustomerTagDef.getTagAttributes();
		for(String attrName : attrValues.keySet()) {
			uiCustomerTagExe.addTagAttribute(attrName, attrValues.get(attrName));
		}

		Map<String, HAPUITagAttributeDefinition> attrDefs = uiCustomerTagDef.getTagAttributeDefinitions();
		for(String attrName : attrDefs.keySet()) {
			uiCustomerTagExe.addAttributeDefinition(attrDefs.get(attrName));
		}
		
		uiCustomerTagExe.setUIId(uiCustomerTagDef.getUIId());
		if(uiCustomerTagDef.getBase()!=null) {
			uiCustomerTagExe.setBase(uiCustomerTagDef.getBase());
		}
		if(uiCustomerTagDef.getScriptResourceId()!=null) {
			uiCustomerTagExe.setScriptResourceId(uiCustomerTagDef.getScriptResourceId());
		}
	}
}
