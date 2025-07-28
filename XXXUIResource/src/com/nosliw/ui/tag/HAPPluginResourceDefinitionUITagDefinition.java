package com.nosliw.ui.tag;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.resource.HAPInfoResourceLocation;
import com.nosliw.core.resource.HAPResourceIdSimple;
import com.nosliw.data.core.domain.definition.HAPPluginResourceDefinitionImpEntityThin;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPPluginResourceDefinitionUITagDefinition extends HAPPluginResourceDefinitionImpEntityThin{

	public HAPPluginResourceDefinitionUITagDefinition(HAPRuntimeEnvironment runtimeEnv) {
		super(HAPConstantShared.RUNTIME_RESOURCE_TYPE_UITAGDEFINITION, runtimeEnv);
	}

	@Override
	protected HAPInfoResourceLocation getResourceLocationInfo(HAPResourceIdSimple resourceId) {
		return HAPUtilityUITag.getUITagDefinitionLocationInfo(resourceId.getId());
	}
}
