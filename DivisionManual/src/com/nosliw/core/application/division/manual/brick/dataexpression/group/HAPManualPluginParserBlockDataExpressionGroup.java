package com.nosliw.core.application.division.manual.brick.dataexpression.group;

import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.core.application.HAPEnumBrickType;
import com.nosliw.core.application.brick.dataexpression.group.HAPBlockDataExpressionGroup;
import com.nosliw.core.application.division.manual.HAPManualBrick;
import com.nosliw.core.application.division.manual.HAPManualContextParse;
import com.nosliw.core.application.division.manual.HAPManualManagerBrick;
import com.nosliw.core.application.division.manual.HAPPluginParserBrickImpComplex;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPManualPluginParserBlockDataExpressionGroup extends HAPPluginParserBrickImpComplex{

	public HAPManualPluginParserBlockDataExpressionGroup(HAPManualManagerBrick manualDivisionEntityMan, HAPRuntimeEnvironment runtimeEnv) {
		super(HAPEnumBrickType.DATAEXPRESSIONGROUP_100, HAPManualBlockDataExpressionGroup.class, manualDivisionEntityMan, runtimeEnv);
	}

	@Override
	protected void parseComplexDefinitionContentJson(HAPManualBrick brickDef, JSONObject jsonObj, HAPManualContextParse parseContext) {
		HAPManualBlockDataExpressionGroup groupBlock = (HAPManualBlockDataExpressionGroup)brickDef;
		groupBlock.getValue().buildObject(jsonObj.getJSONObject(HAPBlockDataExpressionGroup.VALUE), HAPSerializationFormat.JSON);
	}
}
