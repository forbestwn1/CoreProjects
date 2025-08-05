package com.nosliw.core.application.division.manual.brick.test.complex.testcomplex1;

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
public class HAPManualProviderBrickInfoTestComplex1 extends HAPManualProviderBrickInfoImp{

	public HAPManualProviderBrickInfoTestComplex1(HAPManualManagerBrick manualBrickMan, HAPManagerApplicationBrick brickMan) {
		super(manualBrickMan, brickMan);
	}
	
	@Override
	public HAPIdBrickType getBrickTypeId() {  return HAPEnumBrickType.TEST_COMPLEX_1_100;   }

	@Override
	protected HAPManualInfoBrickType newBrickTypeInfo() {   return new HAPManualInfoBrickType(true);  }

	@Override
	protected HAPManualDefinitionPluginParserBrick newBrickParser() {
		return new HAPManualPluginParserBlockTestComplex1(this.getManualBrickManager(), this.getBrickManager());
	}

	@Override
	protected HAPManualPluginProcessorBrick newBrickProcessor() {   return new HAPManualPluginProcessorBlockComplexImpTestComplex1();  }

}
