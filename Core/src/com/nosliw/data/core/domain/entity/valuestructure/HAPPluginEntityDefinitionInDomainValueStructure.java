package com.nosliw.data.core.domain.entity.valuestructure;

import java.util.List;

import org.json.JSONObject;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.domain.HAPContextParser;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.HAPPluginEntityDefinitionInDomainImpSimple;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPPluginEntityDefinitionInDomainValueStructure extends HAPPluginEntityDefinitionInDomainImpSimple{

	public HAPPluginEntityDefinitionInDomainValueStructure(HAPRuntimeEnvironment runtimeEnv) {
		super(HAPConstantShared.RUNTIME_RESOURCE_TYPE_VALUESTRUCTURE, HAPDefinitionEntityValueStructure.class, runtimeEnv);
	}

	@Override
	protected void parseDefinitionContentJson(HAPIdEntityInDomain entityId, Object jsonValue,	HAPContextParser parserContext) {
		JSONObject structureJson = (JSONObject)jsonValue;
		if(structureJson!=null) {
			HAPDefinitionEntityValueStructure valueStructure = (HAPDefinitionEntityValueStructure)this.getEntity(entityId, parserContext);
			Object elementsObj = structureJson.opt(HAPDefinitionEntityValueStructure.VALUE);
			if(elementsObj==null)  elementsObj = structureJson;
			List<HAPRootStructure> roots = HAPParserValueStructure.parseStructureRoots(elementsObj);
			for(HAPRootStructure root : roots)  valueStructure.addRoot(root);
		}
	}
}
