package com.nosliw.data.core.domain.definition;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.domain.HAPContextParser;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.resource.HAPPluginResourceDefinitionImp;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPPluginResourceDefinitionImpEntityThin extends HAPPluginResourceDefinitionImp{

	public HAPPluginResourceDefinitionImpEntityThin(String entityType, HAPRuntimeEnvironment runtimeEnv) {
		super(entityType, runtimeEnv);
	}

	@Override
	protected HAPIdEntityInDomain parseEntity(Object content, HAPSerializationFormat format, HAPContextParser parserContext) {
		HAPIdEntityInDomain out = this.getRuntimeEnvironment().getDomainEntityDefinitionManager().parseDefinition(getResourceType(), content, format, parserContext);
		return out;
	}

}
