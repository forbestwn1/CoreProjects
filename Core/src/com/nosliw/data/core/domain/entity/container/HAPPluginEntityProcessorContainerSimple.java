package com.nosliw.data.core.domain.entity.container;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.domain.entity.HAPPluginEntityProcessorComplexImp;

public class HAPPluginEntityProcessorContainerSimple extends HAPPluginEntityProcessorComplexImp{

	public HAPPluginEntityProcessorContainerSimple() {
		super(HAPConstantShared.RUNTIME_RESOURCE_TYPE_CONTAINERSIMPLE, HAPExecutableEntityContainerSimple.class);
	}

}
