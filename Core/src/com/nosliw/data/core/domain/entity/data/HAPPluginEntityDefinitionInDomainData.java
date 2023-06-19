package com.nosliw.data.core.domain.entity.data;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.data.HAPData;
import com.nosliw.data.core.data.HAPUtilityData;
import com.nosliw.data.core.domain.HAPContextParser;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.HAPPluginEntityDefinitionInDomainImp;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPPluginEntityDefinitionInDomainData extends HAPPluginEntityDefinitionInDomainImp{

	public HAPPluginEntityDefinitionInDomainData(HAPRuntimeEnvironment runtimeEnv) {
		super(HAPConstantShared.RUNTIME_RESOURCE_TYPE_DATA, HAPDefinitionEntityData.class, runtimeEnv);
	}
	
	@Override
	protected void parseDefinitionContent(HAPIdEntityInDomain entityId, Object obj, HAPContextParser parserContext) {
		HAPDefinitionEntityData entity = (HAPDefinitionEntityData)this.getEntity(entityId, parserContext);
		HAPData data = HAPUtilityData.buildDataWrapperFromObject(obj);
		entity.setData(data);
	}

	@Override
	public boolean isComplexEntity() {   return false;  }
}
