package com.nosliw.core.application.division.manual.brick.test.complex.testcomplex1;

import com.nosliw.core.application.HAPManagerApplicationBrick;
import com.nosliw.core.application.brick.HAPEnumBrickType;
import com.nosliw.core.application.division.manual.brick.valuestructure.HAPManualDefinitionBrickValueContext;
import com.nosliw.core.application.division.manual.core.HAPManualManagerBrick;

public class HAPManualPluginParserBlockTestComplex1 extends HAPManualPluginParserBrickImpDynamic{

	public HAPManualPluginParserBlockTestComplex1(HAPManualManagerBrick manualDivisionEntityMan, HAPManagerApplicationBrick brickMan) {
		super(HAPEnumBrickType.TEST_COMPLEX_1_100, HAPManualDefinitionBlockTestComplex1.class, manualDivisionEntityMan, brickMan);
	}
	
	@Override
	protected void postNewInstance(HAPIdEntityInDomain entityId, HAPContextParser parserContext) {
		super.postNewInstance(entityId, parserContext);
		HAPUtilityEntityDefinitionComplex.setupAttributeForComplexEntity(entityId, parserContext, getRuntimeEnvironment());
	}
	
	@Override
	protected void postParseDefinitionContent(HAPIdEntityInDomain entityId, HAPContextParser parserContext) {
		super.postParseDefinitionContent(entityId, parserContext);

		HAPManualDefinitionBrickBlockComplex complexEntity = (HAPManualDefinitionBrickBlockComplex)this.getEntity(entityId, parserContext);
		HAPManualDefinitionBrickValueContext valueContextEntity = complexEntity.getValueContextEntity(parserContext);
		if(valueContextEntity!=null) {
			valueContextEntity.discoverConstantScript(entityId, parserContext, this.getRuntimeEnvironment().getDataExpressionParser());
		}
	}
}
