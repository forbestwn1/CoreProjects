package com.nosliw.data.core.domain.entity.valuestructure;

import java.util.List;

import org.json.JSONObject;

import com.nosliw.data.core.domain.HAPDomainEntityDefinition;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.HAPPluginEntityDefinitionInDomainImpSimple;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.structure.HAPParserStructure;
import com.nosliw.data.core.structure.HAPRootStructure;

public class HAPPluginEntityDefinitionInDomainValueStructure extends HAPPluginEntityDefinitionInDomainImpSimple{

	public HAPPluginEntityDefinitionInDomainValueStructure(HAPRuntimeEnvironment runtimeEnv) {
		super(HAPDefinitionEntityValueStructure.class, runtimeEnv);
	}

	@Override
	protected void parseDefinitionContent(HAPIdEntityInDomain entityId, Object obj,	HAPDomainEntityDefinition definitionDomain) {
		JSONObject structureJson = (JSONObject)obj;
		if(structureJson!=null) {
			HAPDefinitionEntityValueStructure valueStructure = (HAPDefinitionEntityValueStructure)this.getEntity(entityId, definitionDomain);
			Object elementsObj = structureJson.opt(HAPDefinitionEntityValueStructure.VALUE);
			if(elementsObj==null)  elementsObj = structureJson;
			List<HAPRootStructure> roots = HAPParserStructure.parseRoots(elementsObj);
			for(HAPRootStructure root : roots)  valueStructure.addRoot(root);
		}
	}
}
