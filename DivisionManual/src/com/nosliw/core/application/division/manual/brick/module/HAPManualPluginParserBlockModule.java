package com.nosliw.core.application.division.manual.brick.module;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.core.application.HAPEnumBrickType;
import com.nosliw.core.application.brick.module.HAPBlockModule;
import com.nosliw.core.application.division.manual.HAPManualManagerBrick;
import com.nosliw.core.application.division.manual.brick.wrapperbrick.HAPManualDefinitionBrickWrapperBrick;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionBrick;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionContextParse;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionPluginParserBrickImpComplex;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPManualPluginParserBlockModule extends HAPManualDefinitionPluginParserBrickImpComplex{

	public HAPManualPluginParserBlockModule(HAPManualManagerBrick manualDivisionEntityMan, HAPRuntimeEnvironment runtimeEnv) {
		super(HAPEnumBrickType.MODULE_100, HAPManualDefinitionBlockModule.class, manualDivisionEntityMan, runtimeEnv);
	}

	@Override
	protected void parseComplexDefinitionContentJson(HAPManualDefinitionBrick brickDefinition, JSONObject jsonObj, HAPManualDefinitionContextParse parseContext) {
		HAPManualDefinitionBlockModule moduleBrick = (HAPManualDefinitionBlockModule)brickDefinition; 
		
		JSONArray brickArrayJson = jsonObj.optJSONArray(HAPBlockModule.BRICK);
		for(int i=0; i<brickArrayJson.length(); i++) {
			HAPManualDefinitionBrickWrapperBrick brickEle = (HAPManualDefinitionBrickWrapperBrick)this.getManualDivisionEntityManager().parseBrickDefinition(brickArrayJson.getJSONObject(i), HAPEnumBrickType.WRAPPERBRICK_100, HAPSerializationFormat.JSON, parseContext);
			moduleBrick.getBricks().addElementWithBrick(brickEle);
		}
		
		JSONArray lifecycleArrayJson = jsonObj.optJSONArray(HAPBlockModule.LIFECYCLE);
		if(lifecycleArrayJson!=null) {
			for(int i=0; i<lifecycleArrayJson.length(); i++) {
				HAPManualDefinitionBrickWrapperBrick lifecycleEle = (HAPManualDefinitionBrickWrapperBrick)this.getManualDivisionEntityManager().parseBrickDefinition(lifecycleArrayJson.getJSONObject(i), HAPEnumBrickType.WRAPPERBRICK_100, HAPSerializationFormat.JSON, parseContext);
				moduleBrick.getLifecycles().addElementWithBrick(lifecycleEle);
			}
		}
		
		
	}
}
