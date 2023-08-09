package com.nosliw.data.core.domain.entity.container;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.domain.entity.HAPPluginEntityProcessorComplexImp;

public class HAPPluginEntityProcessorComplexContainer extends HAPPluginEntityProcessorComplexImp{

	public HAPPluginEntityProcessorComplexContainer() {
		super(HAPConstantShared.RUNTIME_RESOURCE_TYPE_COMPLEXCONTAINER, HAPExecutableEntityComplexContainer.class);
	}

}
