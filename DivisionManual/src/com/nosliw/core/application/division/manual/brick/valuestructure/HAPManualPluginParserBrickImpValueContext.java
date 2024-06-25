package com.nosliw.core.application.division.manual.brick.valuestructure;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.info.HAPInfoImpSimple;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.division.manual.HAPManualAttribute;
import com.nosliw.core.application.division.manual.HAPManualBrick;
import com.nosliw.core.application.division.manual.HAPManualContextParse;
import com.nosliw.core.application.division.manual.HAPManualEnumBrickType;
import com.nosliw.core.application.division.manual.HAPManualManagerBrick;
import com.nosliw.core.application.division.manual.HAPManualUtilityParserBrickFormatJson;
import com.nosliw.core.application.division.manual.HAPPluginParserBrickImpSimple;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPManualPluginParserBrickImpValueContext extends HAPPluginParserBrickImpSimple{

	public HAPManualPluginParserBrickImpValueContext(HAPManualManagerBrick manualDivisionEntityMan, HAPRuntimeEnvironment runtimeEnv) {
		super(HAPManualEnumBrickType.VALUECONTEXT_100, HAPManualBrickValueContext.class, manualDivisionEntityMan, runtimeEnv);
	}
	
	@Override
	protected void parseDefinitionContentJson(HAPManualBrick entityDefinition, Object jsonValue, HAPManualContextParse parseContext) {
		HAPManualBrickValueContext valueContext = (HAPManualBrickValueContext)entityDefinition;

		if(jsonValue instanceof JSONArray) {
			JSONArray partJsonArray = (JSONArray)jsonValue;
			for(int i=0; i<partJsonArray.length(); i++) {
				JSONObject partObj = partJsonArray.getJSONObject(i);
				HAPManualBrickWrapperValueStructure valueStructureWrapper = parseValueStructureWrapper(partObj, parseContext);
				valueContext.addValueStructure(valueStructureWrapper);
			}
		}
		else if(jsonValue instanceof JSONObject) {
			HAPManualBrickWrapperValueStructure valueStructureWrapper = parseValueStructureWrapper((JSONObject)jsonValue, parseContext);
			valueContext.addValueStructure(valueStructureWrapper);
		}
	}

	private HAPManualBrickWrapperValueStructure parseValueStructureWrapper(JSONObject wrapperObj, HAPManualContextParse parseContext) {
		JSONObject valueStructureJsonObj = wrapperObj.optJSONObject(HAPManualBrickWrapperValueStructure.VALUESTRUCTURE);
		if(valueStructureJsonObj==null) {
			valueStructureJsonObj = wrapperObj;
		}

		HAPManualBrickWrapperValueStructure out = (HAPManualBrickWrapperValueStructure)this.getManualDivisionEntityManager().newBrick(HAPManualEnumBrickType.VALUESTRUCTUREWRAPPER_100); 
		HAPManualAttribute valueStructureAttr = HAPManualUtilityParserBrickFormatJson.parseAttribute(HAPManualBrickWrapperValueStructure.VALUESTRUCTURE, valueStructureJsonObj, HAPManualEnumBrickType.VALUESTRUCTURE_100, null, parseContext, this.getManualDivisionEntityManager(), this.getBrickManager());
		out.setAttribute(valueStructureAttr);

		String groupName = (String)wrapperObj.opt(HAPManualBrickWrapperValueStructure.NAME);
		out.setName(groupName);

		String groupType = (String)wrapperObj.opt(HAPManualBrickWrapperValueStructure.GROUPTYPE);
		if(groupType==null) {
			groupType = HAPConstantShared.UIRESOURCE_CONTEXTTYPE_PUBLIC;
		}
		out.setGroupType(groupType);

		JSONObject infoJsonObj = wrapperObj.optJSONObject(HAPManualBrickWrapperValueStructure.INFO);
		if(infoJsonObj!=null) {
			HAPInfoImpSimple info = new HAPInfoImpSimple();
			info.buildObject(infoJsonObj, HAPSerializationFormat.JSON);
			out.setInfo(info);
		}

		return out;
	}
	
}
