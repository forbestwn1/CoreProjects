package com.nosliw.data.core.domain.entity.expression;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.complex.HAPPluginEntityProcessorComplexImp;
import com.nosliw.data.core.component.HAPContextProcessor;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;

public class HAPPluginEntityProcessorComplexExpressionSuite extends HAPPluginEntityProcessorComplexImp{

	public HAPPluginEntityProcessorComplexExpressionSuite() {
		super(HAPConstantShared.RUNTIME_RESOURCE_TYPE_DATAEXPRESSIONSUITE, HAPExecutableExpressionSuite.class);
	}

	@Override
	public void process(HAPIdEntityInDomain complexEntityDefinitionId, HAPContextProcessor processContext) {
	}

}
