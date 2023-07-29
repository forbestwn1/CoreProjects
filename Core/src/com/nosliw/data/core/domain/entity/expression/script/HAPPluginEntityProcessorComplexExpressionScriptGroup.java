package com.nosliw.data.core.domain.entity.expression.script;

import org.json.JSONObject;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.domain.HAPContextParser;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.entity.expression.data.HAPPluginEntityDefinitionInDomainImpComplexWithDataExpressionDataGroup;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPPluginEntityProcessorComplexExpressionScriptGroup extends HAPPluginEntityDefinitionInDomainImpComplexWithDataExpressionDataGroup{

	public HAPPluginEntityProcessorComplexExpressionScriptGroup(HAPRuntimeEnvironment runtimeEnv) {
		super(HAPConstantShared.RUNTIME_RESOURCE_TYPE_SCRIPTEXPRESSIONGROUP, HAPDefinitionEntityExpressionScriptGroup.class, runtimeEnv);
	}

	@Override
	protected void parseComplexDefinitionContent(HAPIdEntityInDomain entityId, JSONObject jsonObj,
			HAPContextParser parserContext) {
		// TODO Auto-generated method stub
		
	}

}
