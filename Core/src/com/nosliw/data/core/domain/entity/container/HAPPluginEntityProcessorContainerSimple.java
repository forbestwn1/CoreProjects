package com.nosliw.data.core.domain.entity.container;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.division.manual.HAPPluginProcessorEntityDefinitionComplexImp;

public class HAPPluginEntityProcessorContainerSimple extends HAPPluginProcessorEntityDefinitionComplexImp{

	public HAPPluginEntityProcessorContainerSimple() {
		super(HAPConstantShared.RUNTIME_RESOURCE_TYPE_CONTAINERSIMPLE, HAPExecutableEntityContainerSimple.class);
	}

}
