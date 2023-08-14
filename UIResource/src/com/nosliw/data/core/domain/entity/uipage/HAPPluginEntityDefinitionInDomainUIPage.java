package com.nosliw.data.core.domain.entity.uipage;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPPluginEntityDefinitionInDomainUIPage extends HAPPluginEntityDefinitionInDomainUIUnit{

	public HAPPluginEntityDefinitionInDomainUIPage(HAPRuntimeEnvironment runtimeEnv) {
		super(HAPConstantShared.RUNTIME_RESOURCE_TYPE_UIPAGE, HAPDefinitionEntityComplexUIPage.class, runtimeEnv);
	}


}
