package com.nosliw.core.application.division.manual.brick.valuestructure;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.info.HAPInfoImpSimple;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.HAPEnumBrickType;
import com.nosliw.core.application.division.manual.HAPManualContextParse;
import com.nosliw.core.application.division.manual.HAPManualManagerBrick;
import com.nosliw.core.application.division.manual.HAPManualAttribute;
import com.nosliw.core.application.division.manual.HAPManualBrick;
import com.nosliw.core.application.division.manual.HAPPluginParserBrickImpSimple;
import com.nosliw.core.application.division.manual.HAPUtilityParserBrickFormatJson;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPPluginParserEntityImpValueContext extends HAPPluginParserBrickImpSimple{

	public HAPPluginParserEntityImpValueContext(HAPManualManagerBrick manualDivisionEntityMan, HAPRuntimeEnvironment runtimeEnv) {
		super(HAPEnumBrickType.VALUECONTEXT_100, HAPDefinitionEntityValueContext.class, manualDivisionEntityMan, runtimeEnv);
	}
	
	@Override
	protected void parseDefinitionContentJson(HAPManualBrick entityDefinition, Object jsonValue, HAPManualContextParse parseContext) {
		HAPDefinitionEntityValueContext valueContext = (HAPDefinitionEntityValueContext)entityDefinition;

		if(jsonValue instanceof JSONArray) {
			JSONArray partJsonArray = (JSONArray)jsonValue;
			for(int i=0; i<partJsonArray.length(); i++) {
				JSONObject partObj = partJsonArray.getJSONObject(i);
				HAPDefinitionEntityWrapperValueStructure valueStructureWrapper = parseValueStructureWrapper(partObj, parseContext);
				valueContext.addValueStructure(valueStructureWrapper);
			}
		}
		else if(jsonValue instanceof JSONObject) {
			HAPDefinitionEntityWrapperValueStructure valueStructureWrapper = parseValueStructureWrapper((JSONObject)jsonValue, parseContext);
			valueContext.addValueStructure(valueStructureWrapper);
		}
	}

	private HAPDefinitionEntityWrapperValueStructure parseValueStructureWrapper(JSONObject wrapperObj, HAPManualContextParse parseContext) {
		JSONObject valueStructureJsonObj = wrapperObj.optJSONObject(HAPDefinitionEntityWrapperValueStructure.VALUESTRUCTURE);
		if(valueStructureJsonObj==null) {
			valueStructureJsonObj = wrapperObj;
		}

		HAPDefinitionEntityWrapperValueStructure out = new HAPDefinitionEntityWrapperValueStructure();
		HAPManualAttribute valueStructureAttr = HAPUtilityParserBrickFormatJson.parseAttribute(HAPDefinitionEntityWrapperValueStructure.VALUESTRUCTURE, valueStructureJsonObj, HAPEnumBrickType.VALUESTRUCTURE_100, null, parseContext, this.getManualDivisionEntityManager(), this.getEntityManager());
		out.setAttribute(valueStructureAttr);

		String groupName = (String)wrapperObj.opt(HAPDefinitionEntityWrapperValueStructure.NAME);
		out.setName(groupName);

		String groupType = (String)wrapperObj.opt(HAPDefinitionEntityWrapperValueStructure.GROUPTYPE);
		if(groupType==null) {
			groupType = HAPConstantShared.UIRESOURCE_CONTEXTTYPE_PUBLIC;
		}
		out.setGroupType(groupType);

		JSONObject infoJsonObj = wrapperObj.optJSONObject(HAPDefinitionEntityWrapperValueStructure.INFO);
		if(infoJsonObj!=null) {
			HAPInfoImpSimple info = new HAPInfoImpSimple();
			info.buildObject(infoJsonObj, HAPSerializationFormat.JSON);
			out.setInfo(info);
		}

		return out;
	}
	
}
