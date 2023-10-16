package com.nosliw.data.core.domain.entity.valuestructure;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.info.HAPInfoImpSimple;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.domain.HAPContextParser;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.HAPPluginEntityDefinitionInDomainImpSimple;
import com.nosliw.data.core.domain.HAPUtilityParserEntity;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPPluginEntityDefinitionInDomainValueContext extends HAPPluginEntityDefinitionInDomainImpSimple{

	public HAPPluginEntityDefinitionInDomainValueContext(HAPRuntimeEnvironment runtimeEnv) {
		super(HAPConstantShared.RUNTIME_RESOURCE_TYPE_VALUECONTEXT, HAPDefinitionEntityValueContext.class, runtimeEnv);
	}

	@Override
	protected void parseDefinitionContent(HAPIdEntityInDomain entityId, Object obj,	HAPContextParser parserContext) {
		HAPDefinitionEntityValueContext valueContext = (HAPDefinitionEntityValueContext)this.getEntity(entityId, parserContext);

		if(obj instanceof JSONArray) {
			JSONArray partJsonArray = (JSONArray)obj;
			for(int i=0; i<partJsonArray.length(); i++) {
				JSONObject partObj = partJsonArray.getJSONObject(i);
				HAPDefinitionWrapperValueStructure valueStructureWrapper = parseValueStructureWrapper(partObj, parserContext);
				valueContext.addValueStructure(valueStructureWrapper);
			}
		}
		else if(obj instanceof JSONObject) {
			HAPDefinitionWrapperValueStructure valueStructureWrapper = parseValueStructureWrapper((JSONObject)obj, parserContext);
			valueContext.addValueStructure(valueStructureWrapper);
		}
	}

	private HAPDefinitionWrapperValueStructure parseValueStructureWrapper(JSONObject wrapperObj, HAPContextParser parserContext) {
		JSONObject valueStructureJsonObj = wrapperObj.optJSONObject(HAPDefinitionWrapperValueStructure.VALUESTRUCTURE);
		if(valueStructureJsonObj==null)   valueStructureJsonObj = wrapperObj;
		HAPIdEntityInDomain valueStructureEntityId = HAPUtilityParserEntity.parseEntity(valueStructureJsonObj, HAPConstantShared.RUNTIME_RESOURCE_TYPE_VALUESTRUCTURE, parserContext, this.getRuntimeEnvironment().getDomainEntityDefinitionManager(), this.getRuntimeEnvironment().getResourceDefinitionManager());

		HAPDefinitionWrapperValueStructure out = new HAPDefinitionWrapperValueStructure(valueStructureEntityId);

		String groupName = (String)wrapperObj.opt(HAPDefinitionWrapperValueStructure.GROUPNAME);
		out.setGroupName(groupName);

		String groupType = (String)wrapperObj.opt(HAPDefinitionWrapperValueStructure.GROUPTYPE);
		if(groupType==null)  groupType = HAPConstantShared.UIRESOURCE_CONTEXTTYPE_PUBLIC;
		out.setGroupType(groupType);

		JSONObject infoJsonObj = wrapperObj.optJSONObject(HAPDefinitionWrapperValueStructure.INFO);
		if(infoJsonObj!=null) {
			HAPInfoImpSimple info = new HAPInfoImpSimple();
			info.buildObject(infoJsonObj, HAPSerializationFormat.JSON);
			out.setInfo(info);
		}

		return out;
	}
	
}
