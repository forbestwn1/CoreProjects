package com.nosliw.data.core.domain.entity.task;

import org.json.JSONObject;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.domain.HAPContextParser;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.HAPPluginEntityDefinitionInDomainImpComplexJson;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPPluginEntityDefinitionInDomainTask extends HAPPluginEntityDefinitionInDomainImpComplexJson{

	public HAPPluginEntityDefinitionInDomainTask(HAPRuntimeEnvironment runtimeEnv) {
		super(HAPConstantShared.RUNTIME_RESOURCE_TYPE_TASK, HAPDefinitionEntityTask.class, runtimeEnv);
	}

	@Override
	protected void parseComplexDefinitionContentJson(HAPIdEntityInDomain entityId, JSONObject jsonObj, HAPContextParser parserContext) {
		HAPDefinitionEntityTask taskEntityDef = (HAPDefinitionEntityTask)parserContext.getCurrentDomain().getEntityInfoDefinition(entityId).getEntity();
	
		String impEntityType = jsonObj.getString(HAPExecutableEntityTask.TYPE);
		taskEntityDef.setImpEntityType(impEntityType);

		JSONObject impEntityJsonObj = jsonObj.getJSONObject(HAPExecutableEntityTask.IMPLEMENTATION);
		parseComplexEntityAttributeSelf(impEntityJsonObj, entityId, HAPExecutableEntityTask.IMPLEMENTATION, impEntityType, null, null, parserContext);
	}
}
