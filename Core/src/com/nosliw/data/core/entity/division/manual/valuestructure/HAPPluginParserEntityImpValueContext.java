package com.nosliw.data.core.entity.division.manual.valuestructure;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.info.HAPInfoImpSimple;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.entity.HAPEnumEntityType;
import com.nosliw.data.core.entity.HAPUtilityEntity;
import com.nosliw.data.core.entity.division.manual.HAPContextParse;
import com.nosliw.data.core.entity.division.manual.HAPManagerEntityDivisionManual;
import com.nosliw.data.core.entity.division.manual.HAPManualEntity;
import com.nosliw.data.core.entity.division.manual.HAPManualInfoAttributeValue;
import com.nosliw.data.core.entity.division.manual.HAPPluginParserEntityImpSimple;
import com.nosliw.data.core.entity.division.manual.HAPUtilityParserEntityFormatJson;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPPluginParserEntityImpValueContext extends HAPPluginParserEntityImpSimple{

	public HAPPluginParserEntityImpValueContext(HAPManagerEntityDivisionManual manualDivisionEntityMan, HAPRuntimeEnvironment runtimeEnv) {
		super(HAPEnumEntityType.VALUECONTEXT_100, HAPDefinitionEntityValueContext.class, manualDivisionEntityMan, runtimeEnv);
	}
	
	@Override
	protected void parseDefinitionContentJson(HAPManualEntity entityDefinition, Object jsonValue, HAPContextParse parseContext) {
		HAPDefinitionEntityValueContext valueContext = (HAPDefinitionEntityValueContext)entityDefinition;

		if(jsonValue instanceof JSONArray) {
			JSONArray partJsonArray = (JSONArray)jsonValue;
			for(int i=0; i<partJsonArray.length(); i++) {
				JSONObject partObj = partJsonArray.getJSONObject(i);
				HAPDefinitionWrapperValueStructure valueStructureWrapper = parseValueStructureWrapper(partObj, parseContext);
				valueContext.addValueStructure(valueStructureWrapper);
			}
		}
		else if(jsonValue instanceof JSONObject) {
			HAPDefinitionWrapperValueStructure valueStructureWrapper = parseValueStructureWrapper((JSONObject)jsonValue, parseContext);
			valueContext.addValueStructure(valueStructureWrapper);
		}
	}

	private HAPDefinitionWrapperValueStructure parseValueStructureWrapper(JSONObject wrapperObj, HAPContextParse parseContext) {
		JSONObject valueStructureJsonObj = wrapperObj.optJSONObject(HAPDefinitionWrapperValueStructure.VALUESTRUCTURE);
		if(valueStructureJsonObj==null) {
			valueStructureJsonObj = wrapperObj;
		}
		HAPManualInfoAttributeValue valueStructureEntityValueInfo = HAPUtilityParserEntityFormatJson.parseEntity(valueStructureJsonObj, HAPUtilityEntity.parseEntityTypeId(HAPConstantShared.RUNTIME_RESOURCE_TYPE_VALUESTRUCTURE), parseContext, this.getManualDivisionEntityManager(), this.getEntityManager());

		HAPDefinitionWrapperValueStructure out = new HAPDefinitionWrapperValueStructure(valueStructureEntityValueInfo);

		String groupName = (String)wrapperObj.opt(HAPDefinitionWrapperValueStructure.NAME);
		out.setName(groupName);

		String groupType = (String)wrapperObj.opt(HAPDefinitionWrapperValueStructure.GROUPTYPE);
		if(groupType==null) {
			groupType = HAPConstantShared.UIRESOURCE_CONTEXTTYPE_PUBLIC;
		}
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
