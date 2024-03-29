package com.nosliw.core.application.division.manual.brick.test.complex.testcomplex1;

import com.nosliw.core.application.HAPEnumBrickType;
import com.nosliw.core.application.division.manual.HAPManualManagerBrick;
import com.nosliw.core.application.division.manual.HAPManualBrickComplex;
import com.nosliw.core.application.division.manual.brick.valuestructure.HAPManualBrickValueContext;
import com.nosliw.data.core.domain.HAPContextParser;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.HAPUtilityEntityDefinitionComplex;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPPluginParserBrickImpTestComplex1 extends HAPPluginParserBrickImpDynamic{

	public HAPPluginParserBrickImpTestComplex1(HAPManualManagerBrick manualDivisionEntityMan, HAPRuntimeEnvironment runtimeEnv) {
		super(HAPEnumBrickType.TEST_COMPLEX_1_100, HAPManualTestComplex1.class, manualDivisionEntityMan, runtimeEnv);
	}
	
	@Override
	protected void postNewInstance(HAPIdEntityInDomain entityId, HAPContextParser parserContext) {
		super.postNewInstance(entityId, parserContext);
		HAPUtilityEntityDefinitionComplex.setupAttributeForComplexEntity(entityId, parserContext, getRuntimeEnvironment());
	}
	
	@Override
	protected void postParseDefinitionContent(HAPIdEntityInDomain entityId, HAPContextParser parserContext) {
		super.postParseDefinitionContent(entityId, parserContext);

		HAPManualBrickComplex complexEntity = (HAPManualBrickComplex)this.getEntity(entityId, parserContext);
		HAPManualBrickValueContext valueContextEntity = complexEntity.getValueContextEntity(parserContext);
		if(valueContextEntity!=null) {
			valueContextEntity.discoverConstantScript(entityId, parserContext, this.getRuntimeEnvironment().getDataExpressionParser());
		}
	}
}
