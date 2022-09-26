package com.nosliw.data.core.domain.testing.testcomplex1;

import org.json.JSONObject;

import com.nosliw.data.core.domain.HAPContextParser;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.testing.HAPPluginEntityDefinitionInDomainDynamic;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPPluginEntityDefinitionInDomainTestComplex1 extends HAPPluginEntityDefinitionInDomainDynamic{

	public HAPPluginEntityDefinitionInDomainTestComplex1(HAPRuntimeEnvironment runtimeEnv) {
		super(HAPDefinitionEntityTestComplex1.class, true, runtimeEnv);
	}
	
	@Override
	protected void parseDefinitionContent(HAPIdEntityInDomain entityId, Object obj, HAPContextParser parserContext) {
		JSONObject jsonObj = this.convertToJsonObject(obj);
		
		super.parseDefinitionContent(entityId, jsonObj, parserContext);
		HAPDefinitionEntityTestComplex1 entity = (HAPDefinitionEntityTestComplex1)this.getEntity(entityId, parserContext);

		Object varObj = jsonObj.opt(HAPPluginEntityDefinitionInDomainDynamic.PREFIX_IGNORE+"_"+HAPDefinitionEntityTestComplex1.ATTR_VARIABLE);
		if(varObj!=null)  entity.setVariable((String)varObj);
	}
}
