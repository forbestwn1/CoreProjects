package com.nosliw.data.core.domain.entity.valuestructure;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.domain.HAPContextParser;
import com.nosliw.data.core.domain.HAPDomainEntityDefinition;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.HAPPluginEntityDefinitionInDomainImpSimple;
import com.nosliw.data.core.domain.HAPUtilityDomain;
import com.nosliw.data.core.domain.HAPUtilityParserEntity;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPPluginEntityDefinitionInDomainValueStructureComplex extends HAPPluginEntityDefinitionInDomainImpSimple{

	public HAPPluginEntityDefinitionInDomainValueStructureComplex(HAPRuntimeEnvironment runtimeEnv) {
		super(HAPDefinitionEntityComplexValueStructure.class, runtimeEnv);
	}

	@Override
	protected void parseDefinitionContent(HAPIdEntityInDomain entityId, Object obj,	HAPDomainEntityDefinition definitionDomain) {
		HAPDefinitionEntityComplexValueStructure valueStructureComplex = (HAPDefinitionEntityComplexValueStructure)this.getEntity(entityId, definitionDomain);

		if(obj instanceof JSONArray) {
			JSONArray partJsonArray = (JSONArray)obj;
			for(int i=0; i<partJsonArray.length(); i++) {
				JSONObject partObj = partJsonArray.getJSONObject(i);
				HAPValueStructureWrapper valueStructureWrapper = parseValueStructureWrapper(partObj, definitionDomain, HAPUtilityDomain.getContextParse(entityId, definitionDomain));
				valueStructureComplex.addPart(valueStructureWrapper);
			}
		}
		else if(obj instanceof JSONObject) {
			HAPValueStructureWrapper valueStructureWrapper = parseValueStructureWrapper((JSONObject)obj, definitionDomain, HAPUtilityDomain.getContextParse(entityId, definitionDomain));
			valueStructureComplex.addPart(valueStructureWrapper);
		}
	}

	private HAPValueStructureWrapper parseValueStructureWrapper(JSONObject wrapperObj, HAPDomainEntityDefinition definitionDomain, HAPContextParser parserContext) {
		String groupType = (String)wrapperObj.opt(HAPValueStructureWrapper.GROUPTYPE);
		if(groupType==null)  groupType = HAPConstantShared.UIRESOURCE_CONTEXTTYPE_PUBLIC;

		String groupName = (String)wrapperObj.opt(HAPValueStructureWrapper.GROUPNAME);

		JSONObject valueStructureJsonObj = wrapperObj.optJSONObject(HAPValueStructureWrapper.VALUESTRUCTURE);
		if(valueStructureJsonObj==null)   valueStructureJsonObj = wrapperObj;
		
		HAPIdEntityInDomain valueStructureEntityId = HAPUtilityParserEntity.parseEntity(valueStructureJsonObj, HAPConstantShared.RUNTIME_RESOURCE_TYPE_VALUESTRUCTURE, parserContext, this.getRuntimeEnvironment().getDomainEntityManager(), this.getRuntimeEnvironment().getResourceDefinitionManager());

		HAPValueStructureWrapper out = new HAPValueStructureWrapper(valueStructureEntityId);
		out.setGroupName(groupName);
		out.setGroupType(groupType);
		return out;
	}
	
}
