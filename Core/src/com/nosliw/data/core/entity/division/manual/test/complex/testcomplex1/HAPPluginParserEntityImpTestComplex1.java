package com.nosliw.data.core.entity.division.manual.test.complex.testcomplex1;

import com.nosliw.data.core.domain.HAPContextParser;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.HAPUtilityEntityDefinitionComplex;
import com.nosliw.data.core.entity.HAPEnumEntityType;
import com.nosliw.data.core.entity.division.manual.HAPManagerEntityDivisionManual;
import com.nosliw.data.core.entity.division.manual.HAPManualEntityComplex;
import com.nosliw.data.core.entity.division.manual.valuestructure.HAPDefinitionEntityValueContext;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPPluginParserEntityImpTestComplex1 extends HAPPluginParserEntityImpDynamic{

	public HAPPluginParserEntityImpTestComplex1(HAPManagerEntityDivisionManual manualDivisionEntityMan, HAPRuntimeEnvironment runtimeEnv) {
		super(HAPEnumEntityType.TEST_COMPLEX_1_100, HAPDefinitionEntityTestComplex1.class, manualDivisionEntityMan, runtimeEnv);
	}
	
	@Override
	protected void postNewInstance(HAPIdEntityInDomain entityId, HAPContextParser parserContext) {
		super.postNewInstance(entityId, parserContext);
		HAPUtilityEntityDefinitionComplex.setupAttributeForComplexEntity(entityId, parserContext, getRuntimeEnvironment());
	}
	
	@Override
	protected void postParseDefinitionContent(HAPIdEntityInDomain entityId, HAPContextParser parserContext) {
		super.postParseDefinitionContent(entityId, parserContext);

		HAPManualEntityComplex complexEntity = (HAPManualEntityComplex)this.getEntity(entityId, parserContext);
		HAPDefinitionEntityValueContext valueContextEntity = complexEntity.getValueContextEntity(parserContext);
		if(valueContextEntity!=null) {
			valueContextEntity.discoverConstantScript(entityId, parserContext, this.getRuntimeEnvironment().getDataExpressionParser());
		}
	}
}
