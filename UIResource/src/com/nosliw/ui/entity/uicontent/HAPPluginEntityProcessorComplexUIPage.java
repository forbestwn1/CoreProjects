package com.nosliw.ui.entity.uicontent;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.entity.division.manual.HAPPluginProcessorEntityDefinitionComplexImp;

public class HAPPluginEntityProcessorComplexUIPage extends HAPPluginProcessorEntityDefinitionComplexImp{

	public HAPPluginEntityProcessorComplexUIPage() {
		super(HAPConstantShared.RUNTIME_RESOURCE_TYPE_UIPAGE, HAPExecutableEntityComplexUIPage.class);
	}

}
