package com.nosliw.data.core.domain.entity.container;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.division.manual.HAPPluginProcessorBrickDefinitionComplexImp;

public class HAPPluginEntityProcessorContainerComplex extends HAPPluginProcessorBrickDefinitionComplexImp{

	public HAPPluginEntityProcessorContainerComplex() {
		super(HAPConstantShared.RUNTIME_RESOURCE_TYPE_CONTAINERCOMPLEX, HAPExecutableEntityContainerComplex.class);
	}

}
