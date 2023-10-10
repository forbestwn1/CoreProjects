package com.nosliw.data.core.domain.entity.container;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.domain.entity.HAPPluginEntityProcessorComplexImp;

public class HAPPluginEntityProcessorContainerComplex extends HAPPluginEntityProcessorComplexImp{

	public HAPPluginEntityProcessorContainerComplex() {
		super(HAPConstantShared.RUNTIME_RESOURCE_TYPE_CONTAINERCOMPLEX, HAPExecutableEntityContainerComplex.class);
	}

}
