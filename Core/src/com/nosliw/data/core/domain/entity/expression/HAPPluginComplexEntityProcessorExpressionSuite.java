package com.nosliw.data.core.domain.entity.expression;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.complex.HAPPluginComplexEntityProcessorImp;
import com.nosliw.data.core.component.HAPContextProcessor;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;

public class HAPPluginComplexEntityProcessorExpressionSuite extends HAPPluginComplexEntityProcessorImp{

	public HAPPluginComplexEntityProcessorExpressionSuite() {
		super(HAPConstantShared.RUNTIME_RESOURCE_TYPE_DATAEXPRESSIONSUITE, HAPExecutableExpressionSuite.class);
	}

	@Override
	public void process(HAPIdEntityInDomain complexEntityDefinitionId, HAPContextProcessor processContext) {
	}

}
