package com.nosliw.data.core.domain.entity.container;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.division.manual.HAPPluginProcessorBrickDefinitionComplexImp;

public class HAPPluginEntityProcessorContainerSimple extends HAPPluginProcessorBrickDefinitionComplexImp{

	public HAPPluginEntityProcessorContainerSimple() {
		super(HAPConstantShared.RUNTIME_RESOURCE_TYPE_CONTAINERSIMPLE, HAPExecutableEntityContainerSimple.class);
	}

}
