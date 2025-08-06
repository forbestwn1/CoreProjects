package com.nosliw.core.application.division.manual.brick.test.complex.script;

import org.springframework.stereotype.Component;

import com.nosliw.core.application.HAPIdBrickType;
import com.nosliw.core.application.HAPManagerApplicationBrick;
import com.nosliw.core.application.brick.HAPEnumBrickType;
import com.nosliw.core.application.division.manual.core.HAPManualManagerBrick;
import com.nosliw.core.application.division.manual.core.HAPManualProviderBrickInfoImp;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionPluginParserBrick;
import com.nosliw.core.application.division.manual.core.process.HAPManualInfoBrickType;
import com.nosliw.core.application.division.manual.core.process.HAPManualPluginProcessorBrick;

@Component
public class HAPManualProviderBrickInfoTestScript extends HAPManualProviderBrickInfoImp{

	public HAPManualProviderBrickInfoTestScript(HAPManualManagerBrick manualBrickMan,
			HAPManagerApplicationBrick brickMan) {
		super(manualBrickMan, brickMan);
	}

	@Override
	public HAPIdBrickType getBrickTypeId() {   return HAPEnumBrickType.TEST_COMPLEX_SCRIPT_100;   }

	@Override
	protected HAPManualDefinitionPluginParserBrick newBrickParser() {
		return new HAPManualPluginParserBlockTestComplexScript(this.getManualBrickManager(), this.getBrickManager());
	}

	@Override
	protected HAPManualPluginProcessorBrick newBrickProcessor() {
		return new HAPManualPluginProcessorBlockComplexTestComplexScript();
	}

	@Override
	protected HAPManualInfoBrickType newBrickTypeInfo() {  return new HAPManualInfoBrickType(true);  }

}
