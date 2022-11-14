package com.nosliw.data.core.domain.entity.test.complex.testcomplex1;

import org.json.JSONObject;

import com.nosliw.data.core.domain.HAPContextParser;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.entity.test.HAPPluginEntityDefinitionInDomainDynamic;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPPluginEntityDefinitionInDomainTestComplex1 extends HAPPluginEntityDefinitionInDomainDynamic{

	public HAPPluginEntityDefinitionInDomainTestComplex1(HAPRuntimeEnvironment runtimeEnv) {
		super(HAPDefinitionEntityTestComplex1.class, true, runtimeEnv);
	}
	
	@Override
	protected void parseDefinitionContent(HAPIdEntityInDomain entityId, Object obj, HAPContextParser parserContext) {
		JSONObject jsonObj = this.convertToJsonObject(obj);
		super.parseDefinitionContent(entityId, jsonObj, parserContext);
	}
}
