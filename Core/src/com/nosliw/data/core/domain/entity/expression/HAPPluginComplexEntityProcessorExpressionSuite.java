package com.nosliw.data.core.domain.entity.expression;

import com.nosliw.data.core.complex.HAPPluginComplexEntityProcessorImp;
import com.nosliw.data.core.component.HAPContextProcessor;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;

public class HAPPluginComplexEntityProcessorExpressionSuite extends HAPPluginComplexEntityProcessorImp{

	public HAPPluginComplexEntityProcessorExpressionSuite() {
		super(HAPExecutableExpressionSuite.class);
	}

	@Override
	public void process(HAPIdEntityInDomain complexEntityDefinitionId, HAPContextProcessor processContext) {
	}

}
