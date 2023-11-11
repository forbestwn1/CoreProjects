package com.nosliw.data.core.domain.entity.adapter.dataassociation;

import org.json.JSONObject;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.dataassociation.HAPDefinitionDataAssociation;
import com.nosliw.data.core.dataassociation.HAPParserDataAssociation;
import com.nosliw.data.core.domain.HAPContextParser;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.HAPPluginEntityDefinitionInDomainImpSimple;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPPluginEntityDefinitionInDomainDataAssociation  extends HAPPluginEntityDefinitionInDomainImpSimple{

	public HAPPluginEntityDefinitionInDomainDataAssociation(HAPRuntimeEnvironment runtimeEnv) {
		super(HAPConstantShared.RUNTIME_RESOURCE_TYPE_DATAASSOCIATION, HAPDefinitionEntityDataAssciation.class, runtimeEnv);
	}
	
	@Override
	protected void parseDefinitionContentJson(HAPIdEntityInDomain entityId, Object jsonValue, HAPContextParser parserContext) {
		HAPDefinitionEntityDataAssciation entity = (HAPDefinitionEntityDataAssciation)this.getEntity(entityId, parserContext);
		HAPDefinitionDataAssociation da = HAPParserDataAssociation.buildDefinitionByJson((JSONObject)jsonValue);
		entity.setDataAssciation(da);
	}

}
