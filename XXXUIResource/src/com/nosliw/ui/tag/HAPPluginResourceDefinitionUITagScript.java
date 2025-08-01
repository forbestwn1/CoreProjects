package com.nosliw.ui.tag;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.resource.HAPResourceIdSimple;
import com.nosliw.core.xxx.resource.HAPInfoResourceLocation;
import com.nosliw.data.core.domain.definition.HAPPluginResourceDefinitionImpEntityThin;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPPluginResourceDefinitionUITagScript extends HAPPluginResourceDefinitionImpEntityThin{

	public HAPPluginResourceDefinitionUITagScript(HAPRuntimeEnvironment runtimeEnv) {
		super(HAPConstantShared.RUNTIME_RESOURCE_TYPE_UITAGSCRIPT, runtimeEnv);
	}

	@Override
	protected HAPInfoResourceLocation getResourceLocationInfo(HAPResourceIdSimple resourceId) {
		return HAPUtilityUITag.getUITagScriptLocationInfo(resourceId.getId());
	}
}
