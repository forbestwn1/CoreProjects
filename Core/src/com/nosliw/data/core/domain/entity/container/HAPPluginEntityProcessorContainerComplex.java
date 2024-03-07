package com.nosliw.data.core.domain.entity.container;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.entity.division.manual.HAPPluginProcessorEntityDefinitionComplexImp;

public class HAPPluginEntityProcessorContainerComplex extends HAPPluginProcessorEntityDefinitionComplexImp{

	public HAPPluginEntityProcessorContainerComplex() {
		super(HAPConstantShared.RUNTIME_RESOURCE_TYPE_CONTAINERCOMPLEX, HAPExecutableEntityContainerComplex.class);
	}

}
