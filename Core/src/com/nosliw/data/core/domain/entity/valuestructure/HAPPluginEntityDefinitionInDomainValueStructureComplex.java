package com.nosliw.data.core.domain.entity.valuestructure;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.domain.HAPContextParser;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.HAPPluginEntityDefinitionInDomainImpSimple;
import com.nosliw.data.core.domain.HAPUtilityParserEntity;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPPluginEntityDefinitionInDomainValueStructureComplex extends HAPPluginEntityDefinitionInDomainImpSimple{

	public HAPPluginEntityDefinitionInDomainValueStructureComplex(HAPRuntimeEnvironment runtimeEnv) {
		super(HAPDefinitionEntityComplexValueStructure.class, runtimeEnv);
	}

	@Override
	protected void parseDefinitionContent(HAPIdEntityInDomain entityId, Object obj,	HAPContextParser parserContext) {
		HAPDefinitionEntityComplexValueStructure valueStructureComplex = (HAPDefinitionEntityComplexValueStructure)this.getEntity(entityId, parserContext);

		if(obj instanceof JSONArray) {
			JSONArray partJsonArray = (JSONArray)obj;
			for(int i=0; i<partJsonArray.length(); i++) {
				JSONObject partObj = partJsonArray.getJSONObject(i);
				HAPWrapperValueStructureDefinition valueStructureWrapper = parseValueStructureWrapper(partObj, parserContext);
				valueStructureComplex.addPart(valueStructureWrapper);
			}
		}
		else if(obj instanceof JSONObject) {
			HAPWrapperValueStructureDefinition valueStructureWrapper = parseValueStructureWrapper((JSONObject)obj, parserContext);
			valueStructureComplex.addPart(valueStructureWrapper);
		}
	}

	private HAPWrapperValueStructureDefinition parseValueStructureWrapper(JSONObject wrapperObj, HAPContextParser parserContext) {
		String groupType = (String)wrapperObj.opt(HAPWrapperValueStructureDefinition.GROUPTYPE);
		if(groupType==null)  groupType = HAPConstantShared.UIRESOURCE_CONTEXTTYPE_PUBLIC;

		String groupName = (String)wrapperObj.opt(HAPWrapperValueStructureDefinition.GROUPNAME);

		JSONObject valueStructureJsonObj = wrapperObj.optJSONObject(HAPWrapperValueStructureDefinition.VALUESTRUCTURE);
		if(valueStructureJsonObj==null)   valueStructureJsonObj = wrapperObj;
		
		HAPIdEntityInDomain valueStructureEntityId = HAPUtilityParserEntity.parseEntity(valueStructureJsonObj, HAPConstantShared.RUNTIME_RESOURCE_TYPE_VALUESTRUCTURE, parserContext, this.getRuntimeEnvironment().getDomainEntityManager(), this.getRuntimeEnvironment().getResourceDefinitionManager());

		HAPWrapperValueStructureDefinition out = new HAPWrapperValueStructureDefinition(valueStructureEntityId);
		out.setGroupName(groupName);
		out.setGroupType(groupType);
		return out;
	}
	
}
