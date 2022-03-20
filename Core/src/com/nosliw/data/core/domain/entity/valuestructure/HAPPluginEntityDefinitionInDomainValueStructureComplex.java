package com.nosliw.data.core.domain.entity.valuestructure;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.domain.HAPContextParser;
import com.nosliw.data.core.domain.HAPDomainDefinitionEntity;
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
	protected void parseDefinitionContent(HAPIdEntityInDomain entityId, Object obj,	HAPDomainDefinitionEntity definitionDomain) {
		HAPDefinitionEntityComplexValueStructure valueStructureComplex = (HAPDefinitionEntityComplexValueStructure)this.getEntity(entityId, definitionDomain);

		if(obj instanceof JSONArray) {
			JSONArray partJsonArray = (JSONArray)obj;
			for(int i=0; i<partJsonArray.length(); i++) {
				Object partObj = partJsonArray.get(i);
				
				HAPPartComplexValueStructureSimple simplePart = parseSimpleValueStructurePart(partObj, definitionDomain, HAPUtilityDomain.getContextParse(entityId, definitionDomain));
				valueStructureComplex.addPart(simplePart);
			}
		}
		else if(obj instanceof JSONObject) {
			HAPPartComplexValueStructureSimple simplePart = parseSimpleValueStructurePart(obj, definitionDomain, HAPUtilityDomain.getContextParse(entityId, definitionDomain));
			valueStructureComplex.addPart(simplePart);
		}
	}

	private HAPPartComplexValueStructureSimple parseSimpleValueStructurePart(Object partObj, HAPDomainDefinitionEntity definitionDomain, HAPContextParser parserContext) {
		
		HAPPartComplexValueStructureSimple out = new HAPPartComplexValueStructureSimple();
		
		if(partObj instanceof JSONArray) {
			JSONArray wrapperArrayObj = (JSONArray)partObj;
			for(int i=0; i<wrapperArrayObj.length(); i++) {
				HAPValueStructureGrouped wrapper = parseValueStructureWrapper(wrapperArrayObj.getJSONObject(i), definitionDomain, parserContext);
				out.addValueStructure(wrapper);
			}
		}
		else if(partObj instanceof JSONObject) {
			HAPValueStructureGrouped wrapper = parseValueStructureWrapper((JSONObject)partObj, definitionDomain, parserContext);
			out.addValueStructure(wrapper);
		}
		
		return out;
	}
	
	private HAPValueStructureGrouped parseValueStructureWrapper(JSONObject wrapperObj, HAPDomainDefinitionEntity definitionDomain, HAPContextParser parserContext) {
		String groupName = (String)wrapperObj.opt(HAPValueStructureGrouped.GROUPNAME);
		if(groupName==null)  groupName = HAPConstantShared.UIRESOURCE_CONTEXTTYPE_PUBLIC;
		
		JSONObject valueStructureJsonObj = wrapperObj.optJSONObject(HAPValueStructureGrouped.VALUESTRUCTURE);
		if(valueStructureJsonObj==null)   valueStructureJsonObj = wrapperObj;
		
		HAPIdEntityInDomain valueStructureEntityId = HAPUtilityParserEntity.parseEntity(valueStructureJsonObj, HAPConstantShared.RUNTIME_RESOURCE_TYPE_VALUESTRUCTURE, parserContext, this.getRuntimeEnvironment().getDomainEntityManager());

		HAPValueStructureGrouped out = new HAPValueStructureGrouped(valueStructureEntityId);
		out.setGroupName(groupName);
		return out;
	}
	
}
