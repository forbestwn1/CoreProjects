package com.nosliw.core.application.division.manual.brick.test.complex.testcomplex1;

import com.nosliw.core.application.division.manual.HAPManualManagerBrick;
import com.nosliw.core.application.division.manual.brick.valuestructure.HAPManualDefinitionBrickValueContext;
import com.nosliw.core.xxx.application1.brick.HAPEnumBrickType;
import com.nosliw.core.xxx.application1.division.manual.definition.HAPManualDefinitionBrickBlockComplex;
import com.nosliw.data.core.domain.HAPContextParser;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.HAPUtilityEntityDefinitionComplex;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPManualPluginParserBlockTestComplex1 extends HAPManualPluginParserBrickImpDynamic{

	public HAPManualPluginParserBlockTestComplex1(HAPManualManagerBrick manualDivisionEntityMan, HAPRuntimeEnvironment runtimeEnv) {
		super(HAPEnumBrickType.TEST_COMPLEX_1_100, HAPManualDefinitionBlockTestComplex1.class, manualDivisionEntityMan, runtimeEnv);
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
