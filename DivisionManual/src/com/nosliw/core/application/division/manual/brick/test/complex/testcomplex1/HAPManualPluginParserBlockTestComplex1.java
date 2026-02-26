package com.nosliw.core.application.division.manual.brick.test.complex.testcomplex1;

import com.nosliw.core.application.HAPManagerApplicationBrick;
import com.nosliw.core.application.brick.HAPEnumBrickType;
import com.nosliw.core.application.division.manual.core.HAPManualManagerBrick;

public class HAPManualPluginParserBlockTestComplex1 extends HAPManualPluginParserBrickImpDynamic{

	public HAPManualPluginParserBlockTestComplex1(HAPManualManagerBrick manualDivisionEntityMan, HAPManagerApplicationBrick brickMan) {
		super(HAPEnumBrickType.TEST_COMPLEX_1_100, HAPManualDefinitionBlockTestComplex1.class, manualDivisionEntityMan, brickMan);
	}
	
}
