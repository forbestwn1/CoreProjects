package com.nosliw.ui.entity.uicontent;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.division.manual.HAPPluginProcessorBrickDefinitionComplexImp;

public class HAPPluginEntityProcessorComplexUIPage extends HAPPluginProcessorBrickDefinitionComplexImp{

	public HAPPluginEntityProcessorComplexUIPage() {
		super(HAPConstantShared.RUNTIME_RESOURCE_TYPE_UIPAGE, HAPExecutableEntityComplexUIPage.class);
	}

}
